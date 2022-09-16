# Tools: 
-	Linux Kernel https://kernel.org/
- Disk image to boot kernel from http://fs.devloop.org.uk/   Gentoo
- required system tools: https://docs.kernel.org/process/changes.html#changes 
- User-Mode Linux (UML): run Linux as process http://user-mode-linux.sourceforge.net/source.html
- Create virtual disks http://howto.gumph.org/content/get-started-with-user-mode-linux/

## current systems config file
zcat /proc/config.gz

## kernel
wget https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-5.19.9.tar.xz
tar xvf linux-5.19.9.tar.xz

# tools
# sudo apt install gcc clang make bash ld flex bison pahole fdformat depmod e2fsck fsck.jfs reiserfsck xfs_db mksquashfs btrfsck pccardctl quota pppd showmount ps udevd grub mcelog iptables openssl bc sphinx cpio 
sudo apt install build-essential dwarves python3 libncurses-dev flex bison libssl-dev bc libelf-dev


## configure & run kernel (for UML)
make defconfig ARCH=um
make menuconfig ARCH=um # choose x64, ext2 (uncheck use ext4 for ext2), ext3, ext4
make ARCH=um # generates a UML binary
ls -l linux

## download Gentoo 
wget http://fs.devloop.org.uk/filesystems/Gentoo/Gentoo-AMD64-root_fs.bz2
mv Gentoo-AMD64-root_fs.bz2 root_fs
sudo ./linux mem=256m
# log in root with no password

# if issues arise clear up build 
make mrproper