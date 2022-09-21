docker_tag=myuserindocker/kernel-development:init-v1

host$:

  # docker pull gcc
  (cd ./kernel-build/docker && chmod 755 ./build.sh && ./build.sh)
  # update: /etc/default/grub with: GRUB_CMDLINE_LINUX_DEFAULT="rootflags=uquota,pquota"
  docker run --privileged --name kernel -it $docker_tag  /bin/bash # (required specific changes to apply): --storage-opt size=120G
  # docker exec -it kernel /bin/bash


#############################################################################

container$:

  #---------- UML kernel build
  # choose x64, check ext2, ext3, & ext4
  make mrproper ARCH=um && make defconfig ARCH=um && make menuconfig ARCH=um
  # build kernel (UML binary)
  make -j$(nproc) ARCH=um && ls -l linux

  # (optional) 
  docker commit <container-id> myuserindocker/kernel-development:built
  # docker run --privileged --name kernel -it myuserindocker/kernel-development:built /bin/bash

  # fix error: "PROT_EXEC mmap in /dev/shm/...failed: Operation not permitted"
  mkdir /tmp/uml; chown root.root /tmp/uml; chmod 777 /tmp/uml; 
  export TMPDIR=/tmp/uml

  # create additional FS
  FS=fs; MNT=mount_$FS; 
  mkdir -p ~/$MNT
  df -h
  dd if=/dev/zero of=$FS bs=1M count=5120
  mkfs.ext2 -L $FS $FS

  # Run (username: root)
  ROOT_DISK=root_fs; FS_DISK=fs; 
  ./kernel/linux root=/dev/root rootfstype=hostfs ubd0=$ROOT_DISK rw mem=128M init=/bin/sh ubd1=$FS_DISK umid=kernel 

#############################################################################

uml$:

  mkdir -p $MNT
  #---------- Mount virtual filesystem
  mount -t ext2 /dev/ubdb $MNT # "-o loop" is used only for non virtualized environment.
  df -hT
  lsblk | grep loop0
  mount | grep uml

  # exit gracefully
  halt -f


#############################################################################
# If you need to get files out of the image onto the CSL host, you can mount the host inside the UML with mount none <mount_point_inside_UML> -t hostfs -o <CSL_host_directory_to_mount>.
