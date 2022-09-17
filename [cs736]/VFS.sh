MountRootFS=mounted_new_root_fs
RootFS=new_root_fs
MountRootFS_original=mounted_root_fs
RootFS_original=root_fs

# create a virtual file system
mkdir -p ~/vm && cd ~/vm; mkdir $MountRootFS; mkdir $MountRootFS_original;


df -h; 
dd if=/dev/zero of=$RootFS bs=1M count=5120
mkfs.ext4 $RootFS
sudo mount ~/vm/$RootFS ~/vm/$MountRootFS -o loop
sudo mount ~/vm/$RootFS_original ~/vm/$MountRootFS_original -o loop
df -hT
# sudo rsync -ax --exclude={$RootFS,'/home/unixuser/vm/*','/home/unixuser/shared-drives'} / ~/vm/$MountRootFS
sudo rsync -ax ~/vm/$MountRootFS_original/* ~/vm/$MountRootFS


# make it work with UML
sudo nano ~/vm/$MountRootFS/etc/HOSTNAME
# type "umlnode1.localnet"
# NOTE: other steps are already done, if copied fs from a UML based FS.
# Create virtual disks http://howto.gumph.org/content/get-started-with-user-mode-linux/



# ------------------------------
# resize filesystem size
e2fsck -f ./root_fs_1
resize2fs ./root_fs_1 2500000
ls -alh 
