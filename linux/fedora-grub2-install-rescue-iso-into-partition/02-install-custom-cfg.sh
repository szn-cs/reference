#!/usr/bin/env bash
# Install temporary/custom.cfg as /boot/grub2/custom.cfg and verify the
# installed copy is byte-identical (guards against line-wrap mangling).
# Run 2/3, after 01-verify-iso.sh passed.
set -euo pipefail

SELF_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="${1:-$SELF_DIR/custom.cfg}"
DST=/boot/grub2/custom.cfg

echo ">>> pre-install checks on $SRC"
# the linux/initrd lines are long; any wrap means corruption
if awk 'length > 200 { found=1 } END { exit found ? 1 : 0 }' "$SRC"; then
    echo "    ok: no over-long lines"
else
    echo "    ERROR: line over 200 chars — file is wrapped/corrupted" >&2
    exit 1
fi

echo ">>> backing up current $DST"
sudo cp -a "$DST" "${DST}.bak.$(date +%Y%m%d-%H%M%S)"

echo ">>> installing"
sudo cp "$SRC" "$DST"

echo ">>> verifying byte-identical install"
sudo cmp "$SRC" "$DST" && echo "    ok: installed copy matches source exactly"
sudo grub2-script-check "$DST" && echo "    ok: grub2-script-check passed"
