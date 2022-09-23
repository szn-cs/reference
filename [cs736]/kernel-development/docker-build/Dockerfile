# FROM gcc:latest 
FROM debian:stable 

ARG kernel_url="https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.19.9.tar.xz"
ARG root_fs_url="http://fs.devloop.org.uk/filesystems/Debian-Jessie/Debian-Jessie-AMD64-root_fs.bz2"
ARG dependencies="gcc clang make bash ld flex bison pahole fdformat depmod e2fsck fsck.jfs reiserfsck xfs_db mksquashfs btrfsck pccardctl quota pppd showmount ps udevd grub mcelog iptables openssl bc sphinx-build epio build-essential dwarves python3 libncurses-dev flex bison libssl-dev bc libelf-dev gcc cpio linux-headers-generic vi nano wget file curl"
ENV kernel_url=${kernel_url}
ENV root_fs_url=${root_fs_url}
ENV dependencies=${dependencies}

# dependencies
RUN   apt update && apt upgrade \
  &&  for i in $dependencies; do apt install -y $i; done \
  &&  apt update && apt upgrade \
  &&  apt autoremove; exit 0

WORKDIR /root/

#---------- Download
RUN   wget $kernel_url -O kernel.tar.xz && tar xf kernel.tar.xz && mv ./linux-* ./kernel \
  &&  wget $root_fs_url -O root_fs.bz2 && bunzip2 -d root_fs.bz2 \
  &&  rm -rf ./*.tar.xz ./*.bz2;

CMD ["/bin/bash", "-c", "echo \"\n1. kernel \n2. root_fs\""]
