- setup Codium https://github.com/VSCodium/vscodium/blob/master/DOCS.md#extensions-marketplace
- https://vscode.dev/
- fix wsl in Codium https://github.com/VSCodium/vscodium/issues/1265
  


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
```
    lspci
    dmesg | grep iommu
```


