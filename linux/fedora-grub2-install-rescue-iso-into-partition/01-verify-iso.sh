#!/usr/bin/env bash
# Verify the Fedora live ISO on sda6 (ext4) against its embedded isomd5sum checksum.
# Run 1/3, BEFORE installing the new custom.cfg. If this fails, re-download
# the ISO — do not continue with the other scripts.
set -euo pipefail

ISO_PATH='/live/Fedora-Workstation-Live-44-1.7.x86_64.iso'
MNT=/mnt/iso-ro

if ! command -v checkisomd5 >/dev/null 2>&1; then
    echo ">>> installing isomd5sum"
    sudo dnf install -y isomd5sum
fi

echo ">>> mounting /dev/sda6 read-only at $MNT"
sudo mkdir -p "$MNT"
sudo mount -o ro /dev/sda6 "$MNT"
trap 'sudo umount "$MNT"' EXIT

echo ">>> verifying (reads all ~2.8 GB, takes a minute or two)"
checkisomd5 --verbose "$MNT$ISO_PATH"
