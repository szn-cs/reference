# gcc environment
FROM gcc:latest

# dependencies
RUN apt update && \
    PKG="gcc clang make bash id flex bison pahole fdformat depmod e2fsck fsck.jfs reiserfsck xfs_db mksquashfs btrfsck pccardctl quota pppd showmount ps udevd grub mcelog iptables openssl bc sphinx-build epio build-essential dwarves python3 libncurses-dev flex bison libssl-dev bc libelf-dev gcc clang make bash ld flex bison pahole fdformat depmod e2fsck fsck.jfs reiserfsck xfs_db mksquashfs btrfsck pccardctl quota pppd showmount ps udevd grub mcelog iptables openssl bc sphinx cpio python2 g++ make nano vim" && \
    for i in $PKG; do apt install -y $i; done && \
    apt update && apt upgrade && apt autoremove
  
#---------- Download
RUN mkdir -p ~/vm && mkdir -p ~/vm/download && cd ~/vm && \
    wget https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.19.9.tar.xz && \
    wget http://fs.devloop.org.uk/filesystems/Gentoo/Gentoo-AMD64-root_fs.bz2 && \
    cd ~/vm && ls -al; cp ./Gentoo-AMD64-root_fs.bz2 ~/vm/download && cp ./linux-5.19.9.tar.xz ~/vm/download && \
    cd ~/vm && tar xvf linux-5.19.9.tar.xz && mv linux-5.19.9 kernel && \
    bunzip2 Gentoo-AMD64-root_fs.bz2 && mv Gentoo-AMD64-root_fs root_fs &&\ 
    rm -rf ~/vm/*.tar.xz ~/vm/*.bz2
  

CMD ["/bin/bash"]
