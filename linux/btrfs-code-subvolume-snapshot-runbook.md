# Btrfs Code Subvolume Migration Runbook
## Snapshot Method - Fast & Direct Migration

**Source**: `/home/unixuser/code` (ID 263, 41GB, 619,825 files)
**Target**: `/dev/sda3` (145.5GB btrfs partition)
**Method**: Btrfs snapshot + send/receive
**Timeline**: 30-45 minutes total
**Safety**: Source becomes read-only, target mounted directly

---

## PRE-MIGRATION CHECKLIST

### Current State
- **Source**: `/home/unixuser/code` subvolume on `/` (nvme0n1p4, btrfs)
- **Size**: 41GB with 619,825 files, 101,025 directories
- **Target**: `/dev/sda3` (145.5GB btrfs, currently unmounted)
- **Available space**: ~104.5GB on target (ample for 41GB source)

### Verification
```bash
# Check source subvolume
sudo btrfs subvolume show /home/unixuser/code
du -sh /home/unixuser/code  # Should show 41G

# Check target partition
sudo blkid /dev/sda3  # Get UUID for fstab
lsblk -f /dev/sda3    # Confirm btrfs filesystem

# Verify both are btrfs
sudo btrfs filesystem show / | grep nvme0n1p4
```

---

## MIGRATION PROCEDURE

### Step 1: Make Source Read-Only (2 minutes)

```bash
# Make code subvolume read-only to prevent changes during migration
sudo btrfs property set /home/unixuser/code ro true

# Verify read-only status
sudo btrfs property get /home/unixuser/code
# Should show: ro=true

# Test that it's actually read-only
touch /home/unixuser/code/test-write 2>/dev/null || echo "Confirmed read-only"
```

**Why this works**: Making the source read-only ensures no data changes during the migration, guaranteeing an exact copy.

### Step 2: Mount Target Partition (2 minutes)

```bash
# Create mount point
sudo mkdir -p /mnt/code-target

# Mount target partition
sudo mount /dev/sda3 /mnt/code-target

# Verify mount and filesystem
df -h /mnt/code-target  # Should show ~145GB available
sudo btrfs filesystem show /mnt/code-target
```

### Step 3: Create Snapshot and Send to Target (20-30 minutes)

```bash
# Create read-only snapshot of source
sudo btrfs subvolume snapshot -r /home/unixuser/code /home/unixuser/code_snapshot

# Verify snapshot created
sudo btrfs subvolume list / | grep code_snapshot

# Send snapshot to target partition
echo "Starting btrfs send/receive (20-30 minutes for 41GB)..."
sudo btrfs send /home/unixuser/code_snapshot | sudo btrfs receive /mnt/code-target/

# This creates: /mnt/code-target/code_snapshot/
```

**Why snapshot method**: 
- Faster than send/receive of live subvolume
- Read-only snapshot ensures data consistency
- Can be interrupted and resumed if needed
- Source remains untouched until cleanup

### Step 4: Verify Copy Integrity (5 minutes)

```bash
# Compare file counts
SOURCE_FILES=$(find /home/unixuser/code -type f | wc -l)
TARGET_FILES=$(find /mnt/code-target/code_snapshot -type f | wc -l)

echo "Source files: $SOURCE_FILES"
echo "Target files: $TARGET_FILES"

if [ "$SOURCE_FILES" -eq "$TARGET_FILES" ]; then
    echo "✅ File count matches"
else
    echo "❌ File count mismatch - investigate"
fi

# Verify directory structure
ls -la /mnt/code-target/code_snapshot/
sudo btrfs subvolume show /mnt/code-target/code_snapshot

# Check size matches
du -sh /home/unixuser/code
du -sh /mnt/code-target/code_snapshot
```

### Step 5: Update Mount Configuration (5 minutes)

```bash
# Get target UUID
SDA3_UUID=$(sudo blkid -s UUID -o value /dev/sda3)
echo "Target UUID: $SDA3_UUID"

# Backup fstab
sudo cp /etc/fstab /etc/fstab.backup.$(date +%Y%m%d)

# Update fstab to mount sda3 as /home/unixuser/code
sudo sed -i '/home\/unixuser\/code/d' /etc/fstab  # Remove existing code entry if any
echo "UUID=$SDA3_UUID /home/unixuser/code btrfs subvol=code_snapshot,defaults 0 0" | sudo tee -a /etc/fstab

# Verify fstab syntax
sudo grep code /etc/fstab
```

**Key change**: Mounting `subvol=code_snapshot` instead of the default subvolume.

### Step 6: Remount and Test (5 minutes)

```bash
# Unmount both source and target
sudo umount /mnt/code-target

# Test new mount from fstab
sudo mount /home/unixuser/code

# Verify it's mounted from sda3
mount | grep code
df -h | grep code
sudo btrfs filesystem show /home/unixuser/code

# Verify it's the snapshot on sda3
sudo btrfs subvolume show /home/unixuser/code

# Test functionality
cd /home/unixuser/code
ls -la
touch test-write-$$ && rm test-write-$$ && echo "✅ Write test passed"
```

### Step 7: Cleanup Source (Optional, Immediate)

```bash
# Remove old source subvolume (now on root filesystem)
# Since we mounted the snapshot as /home/unixuser/code, the old path
# /home/unixuser/code is now inaccessible via the mounted path

# The original subvolume is still at: / (root fs) -> subvol ID 263
# We can delete it since the snapshot is now mounted as /home/unixuser/code

# To delete the original subvolume (BE VERY CAREFUL):
# First, verify we're using the snapshot on sda3:
mount | grep code  # Should show /dev/sda3

# If confirmed, delete the original subvolume from root filesystem
sudo btrfs subvolume delete /home/unixuser/code_original_263

# OR simply leave it - it's not taking up much additional space due to btrfs COW
```

**Alternative**: Keep the original as backup - btrfs copy-on-write means minimal space overhead.

---

## POST-MIGRATION VERIFICATION

### System Verification
```bash
# Verify system boots correctly
sudo systemctl status  # Should show no failed services

# Verify code directory works
cd /home/unixuser/code
ls -la components/ donation-app/ claude\ code\ repositories/

# Verify disk space
df -h /home/unixuser/code  # Should show ~145GB total, ~41GB used
df -h /                    # Should show reduced root filesystem usage

# Verify btrfs features
sudo btrfs filesystem df /home/unixuser/code
sudo btrfs subvolume show /home/unixuser/code
```

### Application Verification
```bash
# Test any applications that use code directory
# Verify git repos work, build processes work, etc.

# If you have any services using code:
sudo systemctl status <any-code-related-services>
```

---

## ROLLBACK PROCEDURE (If Needed)

### If New Mount Fails
```bash
# Revert fstab changes
sudo cp /etc/fstab.backup.YYYYMMDD /etc/fstab

# Remount original configuration
sudo mount -a

# Verify old setup is back
mount | grep code
```

### If Copy Was Incomplete
```bash
# The snapshot still exists, so you can retry the send/receive:
sudo btrfs send /home/unixuser/code_snapshot | sudo btrfs receive /mnt/code-target/

# Or fall back to original setup and troubleshoot
```

---

## ADVANTAGES OF SNAPSHOT METHOD

### Speed
- **Faster**: 20-30 minutes vs 1-2 hours for live send/receive
- **Clean**: Read-only snapshot ensures consistency
- **Verifiable**: Can compare before/after easily

### Safety  
- **Source intact**: Original subvolume untouched until deletion
- **Atomic**: Snapshot is point-in-time consistent
- **Reversible**: Can rollback by reverting fstab changes

### Simplicity
- **Direct**: No complex incremental transfers
- **Clean**: One-shot copy operation
- **Minimal steps**: Fewer phases than other methods

---

## TIMELINE BREAKDOWN

| Step | Operation | Time |
|------|-----------|------|
| 1 | Make source read-only | 2 min |
| 2 | Mount target partition | 2 min |
| 3 | Snapshot + send/receive | 20-30 min |
| 4 | Verify copy integrity | 5 min |
| 5 | Update mount configuration | 5 min |
| 6 | Remount and test | 5 min |
| 7 | Cleanup source | Optional |
| **Total** | **Migration complete** | **39-49 min** |

---

## FINAL STATE

### Before Migration
```
/: /dev/nvme0n1p4 (375.4G btrfs)
└── home/unixuser/code/ (41G subvolume) <- ROOT FILESYSTEM
```

### After Migration
```
/dev/sda3 (145.5G btrfs)
└── code_snapshot/ (41G) <- MOUNTED AS /home/unixuser/code

/: /dev/nvme0n1p4 (reduced usage)
```

### Benefits Achieved
- ✅ **Code separated** from root filesystem
- ✅ **Dedicated 145.5GB partition** for code growth
- ✅ **Same btrfs features** preserved (compression, snapshots, etc.)
- ✅ **Better performance** with separate disk
- ✅ **Simpler backups** for code-specific needs
- ✅ **Clean filesystem structure**

---

## CRITICAL NOTES

### DO NOT SKIP
- **Read-only source**: Essential for data consistency during transfer
- **File count verification**: Ensures complete copy
- **Mount testing**: Prevents boot failures

### COMMON PITFALLS
- **Wrong subvol path**: Verify `subvol=code_snapshot` in fstab
- **UUID errors**: Double-check UUID from `blkid /dev/sda3`
- **Mount order**: Ensure new mount works before removing source

### SAFETY MEASURES
- **Backup fstab**: Always before editing
- **Test mount**: Verify before finalizing
- **Keep snapshot**: Don't delete original immediately

---

**This runbook provides a complete, fast, and safe method for migrating the btrfs code subvolume using snapshot technology. Total time: 30-45 minutes with verification.**