# inside UML kernel running 
FS=fs_ext2
mkdir mount_$FS; 

# create ext2 filesystem
dd if=/dev/zero of=$FS bs=1M count=1024
mkfs.ext2 $FS
mount $FS mount_$FS -o loop
df -hT

