# References: 
- grub options https://askubuntu.com/questions/871896/where-do-i-find-all-grub-options
- command-line linux options https://askubuntu.com/questions/716957/what-do-the-nomodeset-quiet-and-splash-kernel-parameters-mean

# edit `/etc/default/grub`

GRUB_CMDLINE_LINUX_DEFAULT="pcie_acs_override=downstream,multifunction"

GRUB_CMDLINE_LINUX_DEFAULT="intel_iommu=on"

## remove option for hidding output messages (remove `quiet`)
GRUB_CMDLINE_LINUX_DEFAULT="quiet"

## [Debian] update grub
$ update-grub

