# xv6 Memory Encryption
- Page encryption by flipping all bits in a page (i.e., xor every bit with 1 or just use ~).
- User-level memory encryption: system call called by the user to toggle encryption for a range of virtual pages.
- Page decryption happens whenever it is accessed, after a page has been decrypted, it will stay decrypted.. Kernel must catch user's access to any encrypted page by managing a page table entry bit PTE_E for encryption.
- When a child process is created, its initial memory state (including whether a page is encrypted or not), should match that of its parent.

# xv6 specifics: 
- xv6 uses a two level page table
- should understand virtual memory layout: index & manipulate page table, grab certain entry from page table, change certain bit in page entry, access physical memory from kernel.
- xv6 places the kernel in the virtual address space of each process from KERNBASE to KERNBASE + PHYSTOP; which are mapped in physical memory from 0 to PHYSTOP; e.g. virtual address KERNBASE + pa is mapped to physical address pa
- xv6 is running on emulated x86 hardware: on user VA access, the hardware walks the page tables to find the PTE and grab the corresponding physical address translation. The OS is only involved when there is a page fault (PTE_P bit isn't set).
  - To allow OS to handle the page access, clear the PTE_P bit when the PTE_E bit is set.
  - change original code `if (*pte & PTE_P) // Check whether this pde is valid or in-used.` to match the additional meaning of an unset PTE_P, that now could possibly be valid but encrypted. To do this check uva2ka(), copyuvm(), deallocuvm(), freevm() and mappages() in vm.c (e.g. copyuvm() is called when fork() creates a child process. That child process should inherit the encrypted pages, so you'll want to setup the child's page table appropriately.)
- Files related: 
  - memlayout.h: helps in translation between virtual to physical address.
  - mmu.h: 
    - [x] PGROUNDUP, PGROUNDDOWN used to calculate a particular virtual address's page (first address in the page).
    - format for 32-bit virtual addresses is defined: [10 bits page directory index, 10 bits inner page table index, 12 bits offset within a page]
    - format of PTE: 
      - PTE_ADDR macro -> upper 20 bits address (physical page) stored in PTE; 
      - PTE_FLAGS -> lower 12 bits designate flags in PTE.
      - PTE_P, PTE_W, PTE_U -> 3 least-significant bits indicate if page is present/valid, writable, & if part of user space.
  - vm.c: walkpgdir(), uva2ka(), and copyout(): either use or modify those routines, or implement similar functionality. 
    - uva2ka() can help with error checking validating a virtual address. Modify to handle the PTE_P bit being clear for encrypted pages.
    - mappages() or clearpteu(): to see how to set & clear certain bits in the PTE.
  - trap.c & traps.h: allow page fault to be triggered. 
    - if the faulting address occurred for a page where PTE_P was cleared and PTE_E was set, you need to decrypt the page, reset the appropriate bits in the PTE, and return from the trap.
    - if a non related page fault occured, silently exit() from trap.c;
    - Use rcr2() to get the virtual address, and pass it to a decryption function, that would determine whether the page was valid and encrypted or simply invalid.

- [Control timer clock frequency](https://stackoverflow.com/questions/59276602/how-to-modify-process-preemption-policies-like-rr-time-slices-in-xv6) `lapicw(TICR, 10000000)` @ lapic.c
- xv6 help command: `ctrl+a h`
- user program defined through makefile UPROGS entry
- run programs in xv6 concurrently in background: `<user program> &; <user program> &;`

# Todo list:
- [x]  set CPUS = 1 and change the compilation flag from O2 to Og
- [x]  Download ptentry.h file
- [ ]  [xv6 book](https://pdos.csail.mit.edu/6.828/2018/xv6/book-rev11.pdf): page tables, traps, and interrupts of xv6. 
  - Chapters 2 and 3
  - xv6 memory layout is Figure 2-2 (Page 31)
  - Figure 2-1 (Page 30) to pick a bit from flags to designate PTE_E. 
- [ ] Flush the TLB after modifying the page table: e.g. by overwriting the CR3 register (page table base register) to the same value by calling switchuvm() to the same process.


## submission: 
- [ ] ~cs537-1/handin/<login>/p5/ontime/src/<xv6 files>
- [ ] partners.txt: `cslogin1 wisclogin1 Lastname1 Firstname1`
- [ ] if slip day: put code in corresponding slip directory.
- [ ] 