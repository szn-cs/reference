### VM: 
- OVMF + QEMU: passthrough GPU/CPU 
    - Requires support for (1) VT-d virtualiztion (2) IOMMU motherboard/BIOS option. 
    - detailed requirements of CPU and Motherboard https://www.youtube.com/watch?v=fFz44XivxWI
- IOMMU details https://github.com/pavolelsig/IOMMU-viewer
- https://www.kernel.org/doc/html/latest/driver-api/vfio.html 
- https://www.reddit.com/r/VFIO/wiki/index/
- Enable Kernel IOMMU: ```
    sudo grubby --update-kernel=ALL --args=“intel_iommu=on”
    sudo grubby --update-kernel=ALL --remove-args=“intel_iommu”
```
- check iommu https://gist.github.com/Misairu-G/616f7b2756c488148b7309addc940b28?permalink_comment_id=3096989
- https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/virtualization_deployment_and_administration_guide/app-iommu
- https://wiki.gentoo.org/wiki/GPU_passthrough_with_libvirt_qemu_kvm
-  `lspci | grep VGA`
- `for a in /sys/kernel/iommu_groups/*; do find $a -type l; done | sort --version-sort`
- Fedora complete guide https://gist.github.com/firelightning13/e530aec3e3a4e15885a10f6c4b7ae021

- Nobara is a Fedora which includes many thirdparty installs and kernel patches.
- ACS patch for Fedora https://copr.fedorainfracloud.org/coprs/sentry/kernel-fsync/
- Grub editting Fedora https://www.baeldung.com/linux/grub-menu-management



Stages:
1. UEFI settings: VT-d
2. separate PCI devices into individual IOMMU groups using ACS Kernel patch. 
3. Kernel parameters: intel_iommu=on pcie_acs_override=downstream
