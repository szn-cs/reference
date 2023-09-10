## current systems config file
zcat /proc/config.gz

compgen # output all possible bash commands
man compgen
CTRL+D # END-OF-FILE message terminate input stream
CTRL+C # terminate regardless of input/output
cat; 
echo $? # print exit code of previous command
/usr/share/doc # additional tool documentations regardless of man or info documentation sources.
pwd
whoami
cd -
file
chmod
chown
ls -al | grep x
ll
mount
umount
"x" >file.txt
echo "append-x" >>file.txt
vi file.txt
nano file.txt
jobs
ps -al
kill $(jobs -p)  # remove all background jobs
kill -l # print list of kernel -> process signal names
kill -9 <PID>
uname -a
df -hT
sudo /bin/sh
sudo /bin/bash
su x-user
wget
curl
rsync
tar
bunzip2
lsblk
apt list --installed
ssh-keygen -t ed25519 -C "some_comment" # generate ssh keys
chattr +i ~/somefile # 



# change owner of all files recuresively from root to specific user.
USER=<user>
sudo find ~ -user root -exec sudo chown $USER: {} +

# remove all commit messages while keeping the changes (no changed files will be deleted)
git reset --soft HEAD^

# finds files that are larger than 10M
find ./ -type f -size +10M


#### TOOLS 
# neovim

