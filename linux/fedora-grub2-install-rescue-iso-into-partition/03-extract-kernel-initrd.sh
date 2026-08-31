#!/usr/bin/env bash
# CONTINGENCY helper — only needed if the loopback entry still fails with
# "can't allocate kernel" on a clean single boot attempt after 01 passed.
# Extracts the kernel and initrd from the ISO onto sda6 at
# /live/boot/x86_64/loader/ so GRUB can load them straight from ext4.
# Afterwards, install temporary/custom-direct-boot.cfg the same way
# 02-install-custom-cfg.sh installs custom.cfg.
set -euo pipefail

ISO_PATH='/live/Fedora-Workstation-Live-44-1.7.x86_64.iso'
MNT=/mnt/iso-rw

echo ">>> mounting /dev/sda6 read-write at $MNT"
sudo mkdir -p "$MNT"
sudo mount /dev/sda6 "$MNT"
trap 'sudo umount "$MNT"' EXIT

echo ">>> extracting kernel + initrd from ISO (7z)"
sudo 7z x -y -o"$MNT/live" "$MNT$ISO_PATH" \
    boot/x86_64/loader/linux boot/x86_64/loader/initrd

echo ">>> extracted files:"
ls -l "$MNT/live/boot/x86_64/loader/"
