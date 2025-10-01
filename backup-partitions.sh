sudo dd if=/dev/nvme1n1p1 of=./boot-efi.img bs=4M status=progress
sudo dd if=/dev/nvme1n1p2 of=./boot.img bs=4M status=progress