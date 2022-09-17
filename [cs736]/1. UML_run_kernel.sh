host$: 

  # docker pull gcc
  (cd ./docker-kernel-dev && chmod 755 ./build.sh && ./build.sh)
  docker run --privileged --name kernel --storage-opt size=120G -it kernel:init /bin/bash
  # docker exec -it kernel /bin/bash 


#############################################################################


container$: 

  #---------- UML kernel build
  # choose x64, ext2 (uncheck use ext4 for ext2), ext3, ext4
  make mrproper ARCH=um && make defconfig ARCH=um && make menuconfig ARCH=um 
  # build kernel (UML binary)
  make -j$(nproc) ARCH=um && ls -l linux

  # (optional) docker commit <container> kernel:built

  # Run (username: root)
  ./kernel/linux ubda=root_fs rw mem=5000m init=/bin/sh rootfstype=ext4 


#############################################################################


uml$: 

  #---------- Mount virtual filesystem
  FS=fs
  MNT=mount_$FS
  mkdir -p ~/$MNT

  df -h
  dd if=/dev/zero of=$FS bs=1M count=5120
  mkfs.ext4 -L $FS $FS 
  mount -o loop $FS $MNT 
  df -hT
  lsblk | grep loop0 
  mount | grep uml

  # exit gracefully
  halt -f