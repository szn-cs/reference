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


tmux

git log
alias glods="git log --graph --pretty='%Cred%h%Creset -%C(auto)%d%Creset %s %Cgreen(%ad) %C(bold blue)<%an>%Creset' --date=short"
glods # alias to git log with specific options


# Here document (Heredoc)  https://www.tecmint.com/use-heredoc-in-shell-scripting/
x() {
  cat << EOF > file.txt
  something
  EOF

}



## navigate directories using stack method
pushd
popd

