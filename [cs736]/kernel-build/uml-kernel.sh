host$:

  # docker pull gcc
  (cd ./kernel-build/docker && chmod 755 ./build.sh && ./build.sh)
  # update: /etc/default/grub with: GRUB_CMDLINE_LINUX_DEFAULT="rootflags=uquota,pquota"
  docker_tag=myuserindocker/kernel-development:init-v1
  docker run --privileged --name kernel -it $docker_tag /bin/bash # (required specific changes to apply): --storage-opt size=120G
  # docker exec -it kernel /bin/bash


#############################################################################

container$:

  buildKernel() {
    #---------- UML kernel build
    # choose x64, check ext2, ext3, & ext4
    make mrproper ARCH=um && make defconfig ARCH=um && make menuconfig ARCH=um
    rm -rf ./root_fs

    # fix error: "PROT_EXEC mmap in /dev/shm/...failed: Operation not permitted"
    mkdir /tmp/uml; chown root.root /tmp/uml; chmod 777 /tmp/uml; 
    export TMPDIR=/tmp/uml

    # build kernel (UML binary)
    make -j$(nproc) ARCH=um && ls -l linux
  }

  createFS.ext4() {
    rm -rf ./root_fs
    dd if=/dev/zero of=root_fs bs=1M count=5120
    mkfs.ext4 -L ROOT_FS root_fs
    mkdir -p /mnt/uml; mount -t ext4 -o loop root_fs /mnt/uml
    df -h; lsblk | grep loop0; mount | grep uml;
    ls -lah /mnt/uml; 
  }

  createFS.ext3() {
    rm -rf ./root_fs
    dd if=/dev/zero of=root_fs bs=1M count=5120
    mkfs.ext3 -L ROOT_FS root_fs
    mkdir -p /mnt/uml; mount -t ext3 -o loop root_fs /mnt/uml
    df -h; lsblk | grep loop0; mount | grep uml;
    ls -lah /mnt/uml; 
  }

  setupFS1() {
    # Alpine FS http://dl-cdn.alpinelinux.org/alpine/latest-stable/main/x86_64/
    curl -LO http://dl-cdn.alpinelinux.org/alpine/latest-stable/main/x86_64/apk-tools-static-2.12.9-r3.apk && tar -xvf apk-tools-static-2.12.9-r3.apk -C /mnt/uml
    /mnt/uml/sbin/apk.static --repository http://dl-cdn.alpinelinux.org/alpine/v3.13/main/ --update-cache --allow-untrusted --root /mnt/uml --initdb add alpine-base 

    echo "LABEL=ROOT_FS / ext4 defaults 0 0" > /mnt/uml/etc/fstab
  }

  setupFS2() {
    rm -rf ./root_fs
    root_fs_url="http://fs.devloop.org.uk/filesystems/Gentoo/Gentoo-x86-root_fs.bz2"
    wget $root_fs_url -O root_fs.bz2 && bunzip2 -d root_fs.bz2
    mkdir -p /mnt/uml; mount -t ext3 -o loop root_fs /mnt/uml
    df -h; lsblk | grep loop0; mount | grep uml;
    echo "LABEL=ROOT_FS / ext3 defaults 0 0" > /mnt/uml/etc/fstab
    # rm -rf ./*.tar.xz ./*.bz2;
  }

  startKernel() {
    umount /mnt/*;

    # Run (username: root)
    ./linux root=/dev/root rootfstype=hostfs ubd0=root_fs rw mem=128M init=/bin/sh TERM=linux umid=kernel # quiet # ubd1=fs  
  }

  tagDocker() {
    # (optional) 
    docker_tag=myuserindocker/kernel-development:built-v1
    docker commit <container-id> $docker_tag
    # docker run --privileged --name kernel -it myuserindocker/kernel-development:built /bin/bash
  }  

  buildKernel;

  createFS.ext4; 
  setupFS1; 

  # setupFS2; 

  startKernel;

  jobs; 


#############################################################################

uml$:
  
  PS1="[\u@uml:\w ] $ " # make it more friendly 

  mkdir -p $MNT
  #---------- Mount virtual filesystem
  mount -t ext2 /dev/ubdb $MNT # "-o loop" is used only for non virtualized environment.
  df -hT
  lsblk | grep loop0; mount | grep uml

  # exit gracefully
  halt -f


#############################################################################
# If you need to get files out of the image onto the CSL host, you can mount the host inside the UML with mount none <mount_point_inside_UML> -t hostfs -o <CSL_host_directory_to_mount>.
