host$:

  # build
  {
    # docker pull gcc
    (cd ./docker-build/ && chmod 755 ./build.sh && ./build.sh)
    # update: /etc/default/grub with: GRUB_CMDLINE_LINUX_DEFAULT="rootflags=uquota,pquota"
    # docker volume rm $(docker volume ls -q)
  }

  #--------------- RUN:
  {
    docker_tag=myuserindocker/kernel-development:init-v2
    docker run --privileged --name kernel -it $docker_tag /bin/bash # (required specific changes to apply): --storage-opt size=120G
    # docker start kernel; docker exec -it kernel /bin/bash
  }
  # OR
  { 
    docker compose run kernel-development
    # docker rm -f $(docker ps -a -q) # clean all docker containers command

    # Run the modified kernel from volume inside the container... 
  }


#############################################################################


  installDependencies() {
    dependencies="gcc clang make bash ld flex bison pahole fdformat depmod e2fsck fsck.jfs reiserfsck xfs_db mksquashfs btrfsck pccardctl quota pppd showmount ps udevd grub mcelog iptables openssl bc sphinx-build epio build-essential dwarves python3 libncurses-dev flex bison libssl-dev bc libelf-dev gcc cpio linux-headers-generic vi nano wget file curl"
    
    apt update && apt upgrade \
      &&  for i in $dependencies; do apt install -y $i; done \
      &&  apt update && apt upgrade \
      &&  apt autoremove;
  }

  downloadFiles() {
    kernel_url="https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.19.9.tar.xz"
    root_fs_url="http://fs.devloop.org.uk/filesystems/Debian-Jessie/Debian-Jessie-AMD64-root_fs.bz2" 

    wget $kernel_url -O kernel.tar.xz && tar xf kernel.tar.xz && mv ./linux-* ./kernel \
      &&  wget $root_fs_url -O root_fs.bz2 && bunzip2 -d root_fs.bz2 \
      &&  rm -rf ./*.tar.xz ./*.bz2; 
  }

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

  createFS.ext2() {
    rm -rf ./root_fs
    dd if=/dev/zero of=root_fs bs=1M count=5120
    mkfs.ext2 -L ROOT_FS root_fs
    mkdir -p /mnt/uml; mount -t ext2 -o loop root_fs /mnt/uml
    df -h; lsblk | grep loop0; mount | grep uml;
    ls -lah /mnt/uml; 
  }

  setupFS1() { # [STATUS: WORKS]
    # Alpine FS http://dl-cdn.alpinelinux.org/alpine/latest-stable/main/x86_64/
    curl -LO http://dl-cdn.alpinelinux.org/alpine/latest-stable/main/x86_64/apk-tools-static-2.12.9-r3.apk \
    && tar -xvf apk-tools-static-2.12.9-r3.apk -C /mnt/uml
    
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

  setupFS3() { # setup from downloaded file [STATUS: WORKS]
    mkdir -p /mnt/uml; mount -t ext4 -o loop root_fs /mnt/uml
    df -h; lsblk | grep loop0; mount | grep uml;
    echo "LABEL=ROOT_FS / ext4 defaults 0 0" > /mnt/uml/etc/fstab
  }

  startKernel() {
    umount /mnt/uml*;
    
    # Run (username: root)
    # ./linux root=/dev/root rootfstype=hostfs ubda=root_fs rw mem=128M init=/bin/sh TERM=linux umid=kernel # quiet  
    ./linux rootfstype=ext4 ubda=root_fs rw mem=128M init=/bin/sh TERM=linux umid=kernel # quiet 
  }

  startKernel2() {
    umount /mnt/uml*;
  
    # Run (username: root)
    ./linux rootfstype=ext4 ubda=root_fs ubdb=fs_device_1 rw mem=128M init=/bin/sh TERM=linux umid=kernel # quiet
  }

  tagDocker() {
    # (optional) 
    docker_tag=myuserindocker/kernel-development:built-v1
    docker commit <container-id> $docker_tag
    # docker run --privileged --name kernel -it myuserindocker/kernel-development:built /bin/bash
  }  


# USE HOST OR CONTAINER:

host$: # [STATUS: WORKS]
  installDependencies;
  downloadFiles;

  buildKernel;

  # setup donwloaded FS 
  setupFS3;

  startKernel; jobs;


container$: # [STATUS: WORKS]
  buildKernel;

  {
    createFS.ext4;
    setupFS1; 
    startKernel; jobs;
  }
  # OR 
  {
    setupFS2;
    startKernel; jobs;
  }
  # OR
  {
    # move root_fs to kernel folder
    setupFS3; 
    startKernel; jobs;
  }
  # OR 
  {
    createFS.ext4;
    mv root_fs fs_device_1;
    cp ../root_fs ./;
    startKernel2;
  }

#############################################################################

uml$:
  
  PS1="[\u@uml:\w ] $ " # make it more friendly 
  echo "root_fs inside UML" > /info.txt; uname -a >> /info.txt

  # mount vFS from attached device in UML VM [STATUS: WORKS]
  {
    mkdir -p /mnt/fs_device_1
    mount -t ext4 /dev/ubdb /mnt/fs_device_1 # "-o loop" is used only for non virtualized environment.
    df -hT; lsblk | grep loop0; mount | grep uml
  }

  # Mount host FS to the UML 
  {
    # If you need to get files out of the image onto the CSL host, you can mount the host inside the UML with 
    mount none <mount_point_inside_UML> -t hostfs -o <CSL_host_directory_to_mount>.
  } 

  halt -f # exit gracefully



