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
ps aux
pstree 1 # display all processes hierarchy in system

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
tar
bunzip2
lsblk
apt list --installed
ssh-keygen -t ed25519 -C "some_comment" # generate ssh keys
ssh-copy-id username@euler.wacc.wisc.edu # adds ssh to remote machine 
chattr +i ~/somefile 
### Setup ssh key login https://unix.stackexchange.com/questions/4484/ssh-prompts-for-password-despite-ssh-copy-id
netstat -nlt 
ss
systemd-analyze blame

temporary_directory=$(mktemp -d) # useful for automation scripts with ephemeral files or directories. 

{
  test 2 == \
  $(kustomize build $DEMO_HOME | grep -A 2 ".*Ref" | grep "test-" | wc -l); \
  echo $?    
}

echo "123" | tee ./new_file.txt


less somefile.txt

scp
rsync


# change owner of all files recuresively from root to specific user.
USER=<user>
sudo find ~ -user root -exec sudo chown $USER: {} +

# remove all commit messages while keeping the changes (no changed files will be deleted)
git reset --soft HEAD^

# finds files that are larger than 10M
find ./ -type f -size +10M
find ~/<directory> -name <filename> 

ls -a **/file.txt | sort

#### TOOLS 
# neovim



# Compression 

tar -cf /path/to/dest.tar /path/to/src-directory # Creating a tar file
tar -cf /path/to/dest.tar /path/to/src-directory1 /path/to/src-directory2 # Creating a tar file of multiple directories
tar -czf /path/to/dest.tar.gz /path/to/src-directory # Compressing a directory with gZip
tar -xf /path/to/src.tar # Extracting a tar file


# Other 
# linux version
lsb_release -a
cat /etc/debian_version

# Update packages
sudo apt update
sudo apt upgrade
sudo apt full-upgrade
sudo apt --purge autoremove

#package sources
/etc/apt/sources.list

# apt installed packages
ls /usr/bin

# user info
uname -mrs


alias ll="ls -alh"
tree .
## search for pattern inside files 
grep -rnw '/path/to/somewhere/' -e 'pattern'

watch -n 12 --differences=cumulative “cat /folder/file.txt | grep status”

tmux

git log
alias glods="git log --graph --pretty='%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%ad) %C(bold blue)<%an>%Creset' --date=short"
glods # alias to git log with specific options
git submodule update --remote

man -k strcmp
man -w strcmp
man 3 strcmp

## navigate directories using stack method
pushd
popd



ldd <executable> # lists binary dependencies

telnet

# Here document (Heredoc)  https://www.tecmint.com/use-heredoc-in-shell-scripting/
x() {
  cat << EOF > file.txt
  something
  EOF

}

# show bridges/switches including virtual
brctrl show 

# control current swap usage
cat /proc/swaps
swapon --show
sudo swapoff -a # moves all swaps to memory
sudo swapon -a # re-enable swap

# increase swap (zram)
zramctl
sudo nano /etc/systemd/zram-generator.conf
# ```
# [zram0]
# zram-size = 16384
# ```
# refresh zram allocation
sudo swapoff -a
sudo swapon -a

# system resources
htop 