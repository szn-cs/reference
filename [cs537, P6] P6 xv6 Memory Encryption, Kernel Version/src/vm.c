#include "param.h"
#include "types.h"
#include "defs.h"
#include "x86.h"
#include "memlayout.h"
#include "mmu.h"
#include "proc.h"
#include "elf.h"

extern char data[];  // defined by kernel.ld
pde_t *kpgdir;       // for use in scheduler()

// Set up CPU's kernel segment descriptors.
// Run once on entry on each CPU.
void seginit(void) {
    struct cpu *c;

    // Map "logical" addresses to virtual addresses using identity map.
    // Cannot share a CODE descriptor for both kernel and user
    // because it would have to have DPL_USR, but the CPU forbids
    // an interrupt from CPL=0 to DPL=3.
    c = &cpus[cpuid()];
    c->gdt[SEG_KCODE] = SEG(STA_X | STA_R, 0, 0xffffffff, 0);
    c->gdt[SEG_KDATA] = SEG(STA_W, 0, 0xffffffff, 0);
    c->gdt[SEG_UCODE] = SEG(STA_X | STA_R, 0, 0xffffffff, DPL_USER);
    c->gdt[SEG_UDATA] = SEG(STA_W, 0, 0xffffffff, DPL_USER);
    lgdt(c->gdt, sizeof(c->gdt));
}

// Return the address of the PTE in page table pgdir
// that corresponds to virtual address va.  If alloc!=0,
// create any required page table pages.
static pte_t *walkpgdir(pde_t *pgdir, const void *va, int alloc) {
    pde_t *pde;
    pte_t *pgtab;

    pde = &pgdir[PDX(va)];
    if (*pde & PTE_P) {
        pgtab = (pte_t *)P2V(PTE_ADDR(*pde));
    } else {
        if (!alloc || (pgtab = (pte_t *)kalloc()) == 0) return 0;
        // Make sure all those PTE_P bits are zero.
        memset(pgtab, 0, PGSIZE);
        // The permissions here are overly generous, but they can
        // be further restricted by the permissions in the page table
        // entries, if necessary.
        *pde = V2P(pgtab) | PTE_P | PTE_W | PTE_U;
    }
    return &pgtab[PTX(va)];
}

// Create PTEs for virtual addresses starting at va that refer to
// physical addresses starting at pa. va and size might not
// be page-aligned.
static int mappages(pde_t *pgdir, void *va, uint size, uint pa, int perm) {
    char *a, *last;
    pte_t *pte;

    a = (char *)PGROUNDDOWN((uint)va);
    last = (char *)PGROUNDDOWN(((uint)va) + size - 1);
    for (;;) {
        if ((pte = walkpgdir(pgdir, a, 1)) == 0) return -1;
        if (*pte & (PTE_P | PTE_E)) panic("remap");
        if (perm & PTE_E) {
            *pte = pa | perm | PTE_E;
        } else {
            *pte = pa | perm | PTE_P;
        }
        if (a == last) break;
        a += PGSIZE;
        pa += PGSIZE;
    }
    return 0;
}

// There is one page table per process, plus one that's used when
// a CPU is not running any process (kpgdir). The kernel uses the
// current process's page table during system calls and interrupts;
// page protection bits prevent user code from using the kernel's
// mappings.
//
// setupkvm() and exec() set up every page table like this:
//
//   0..KERNBASE: user memory (text+data+stack+heap), mapped to
//                phys memory allocated by the kernel
//   KERNBASE..KERNBASE+EXTMEM: mapped to 0..EXTMEM (for I/O space)
//   KERNBASE+EXTMEM..data: mapped to EXTMEM..V2P(data)
//                for the kernel's instructions and r/o data
//   data..KERNBASE+PHYSTOP: mapped to V2P(data)..PHYSTOP,
//                                  rw data + free physical memory
//   0xfe000000..0: mapped direct (devices such as ioapic)
//
// The kernel allocates physical memory for its heap and for user memory
// between V2P(end) and the end of physical memory (PHYSTOP)
// (directly addressable from end..P2V(PHYSTOP)).

// This table defines the kernel's mappings, which are present in
// every process's page table.
static struct kmap {
    void *virt;
    uint phys_start;
    uint phys_end;
    int perm;
} kmap[] = {
    {(void *)KERNBASE, 0, EXTMEM, PTE_W},             // I/O space
    {(void *)KERNLINK, V2P(KERNLINK), V2P(data), 0},  // kern text+rodata
    {(void *)data, V2P(data), PHYSTOP, PTE_W},        // kern data+memory
    {(void *)DEVSPACE, DEVSPACE, 0, PTE_W},           // more devices
};

// Set up kernel part of a page table.
pde_t *setupkvm(void) {
    pde_t *pgdir;
    struct kmap *k;

    if ((pgdir = (pde_t *)kalloc()) == 0) return 0;
    memset(pgdir, 0, PGSIZE);
    if (P2V(PHYSTOP) > (void *)DEVSPACE) panic("PHYSTOP too high");
    for (k = kmap; k < &kmap[NELEM(kmap)]; k++)
        if (mappages(pgdir, k->virt, k->phys_end - k->phys_start,
                     (uint)k->phys_start, k->perm) < 0) {
            freevm(pgdir);
            return 0;
        }
    return pgdir;
}

// Allocate one page table for the machine for the kernel address
// space for scheduler processes.
void kvmalloc(void) {
    kpgdir = setupkvm();
    switchkvm();
}

// Switch h/w page table register to the kernel-only page table,
// for when no process is running.
void switchkvm(void) {
    lcr3(V2P(kpgdir));  // switch to the kernel page table
}

// Switch TSS and h/w page table to correspond to process p.
void switchuvm(struct proc *p) {
    if (p == 0) panic("switchuvm: no process");
    if (p->kstack == 0) panic("switchuvm: no kstack");
    if (p->pgdir == 0) panic("switchuvm: no pgdir");

    pushcli();
    mycpu()->gdt[SEG_TSS] =
        SEG16(STS_T32A, &mycpu()->ts, sizeof(mycpu()->ts) - 1, 0);
    mycpu()->gdt[SEG_TSS].s = 0;
    mycpu()->ts.ss0 = SEG_KDATA << 3;
    mycpu()->ts.esp0 = (uint)p->kstack + KSTACKSIZE;
    // setting IOPL=0 in eflags *and* iomb beyond the tss segment limit
    // forbids I/O instructions (e.g., inb and outb) from user space
    mycpu()->ts.iomb = (ushort)0xFFFF;
    ltr(SEG_TSS << 3);
    lcr3(V2P(p->pgdir));  // switch to process's address space
    popcli();
}

// Load the initcode into address 0 of pgdir.
// sz must be less than a page.
void inituvm(pde_t *pgdir, char *init, uint sz) {
    char *mem;

    if (sz >= PGSIZE) panic("inituvm: more than a page");
    mem = kalloc();
    memset(mem, 0, PGSIZE);
    mappages(pgdir, 0, PGSIZE, V2P(mem), PTE_W | PTE_U);
    memmove(mem, init, sz);
}

// Load a program segment into pgdir.  addr must be page-aligned
// and the pages from addr to addr+sz must already be mapped.
int loaduvm(pde_t *pgdir, char *addr, struct inode *ip, uint offset, uint sz) {
    uint i, pa, n;
    pte_t *pte;

    if ((uint)addr % PGSIZE != 0) panic("loaduvm: addr must be page aligned");
    for (i = 0; i < sz; i += PGSIZE) {
        if ((pte = walkpgdir(pgdir, addr + i, 0)) == 0)
            panic("loaduvm: address should exist");
        pa = PTE_ADDR(*pte);
        if (sz - i < PGSIZE)
            n = sz - i;
        else
            n = PGSIZE;
        if (readi(ip, P2V(pa), offset + i, n) != n) return -1;
    }
    return 0;
}

// Allocate page tables and physical memory to grow process from oldsz to
// newsz, which need not be page aligned.  Returns new size or 0 on error.
int allocuvm(pde_t *pgdir, uint oldsz, uint newsz) {
    char *mem;
    uint a;

    if (newsz >= KERNBASE) return 0;
    if (newsz < oldsz) return oldsz;

    a = PGROUNDUP(oldsz);
    for (; a < newsz; a += PGSIZE) {
        mem = kalloc();
        if (mem == 0) {
            cprintf("allocuvm out of memory\n");
            deallocuvm(pgdir, newsz, oldsz);
            return 0;
        }
        memset(mem, 0, PGSIZE);
        if (mappages(pgdir, (char *)a, PGSIZE, V2P(mem), PTE_W | PTE_U) < 0) {
            cprintf("allocuvm out of memory (2)\n");
            deallocuvm(pgdir, newsz, oldsz);
            kfree(mem);
            return 0;
        }
    }
    return newsz;
}

// Deallocate user pages to bring the process size from oldsz to
// newsz.  oldsz and newsz need not be page-aligned, nor does newsz
// need to be less than oldsz.  oldsz can be larger than the actual
// process size.  Returns the new process size.
int deallocuvm(pde_t *pgdir, uint oldsz, uint newsz) {
    pte_t *pte;
    uint a, pa;

    if (newsz >= oldsz) return oldsz;

    a = PGROUNDUP(newsz);
    for (; a < oldsz; a += PGSIZE) {
        pte = walkpgdir(pgdir, (char *)a, 0);
        if (!pte)
            a = PGADDR(PDX(a) + 1, 0, 0) - PGSIZE;
        else if ((*pte & (PTE_P | PTE_E)) != 0) {
            pa = PTE_ADDR(*pte);
            if (pa == 0) panic("kfree");
            char *v = P2V(pa);
            kfree(v);
            *pte = 0;
        }
    }
    return newsz;
}

// Free a page table and all the physical memory pages
// in the user part.
void freevm(pde_t *pgdir) {
    uint i;

    if (pgdir == 0) panic("freevm: no pgdir");
    deallocuvm(pgdir, KERNBASE, 0);
    for (i = 0; i < NPDENTRIES; i++) {
        if (pgdir[i] & (PTE_P | PTE_E)) {
            char *v = P2V(PTE_ADDR(pgdir[i]));
            kfree(v);
        }
    }
    kfree((char *)pgdir);
}

// Clear PTE_U on a page. Used to create an inaccessible
// page beneath the user stack.
void clearpteu(pde_t *pgdir, char *uva) {
    pte_t *pte;

    pte = walkpgdir(pgdir, uva, 0);
    if (pte == 0) panic("clearpteu");
    *pte &= ~PTE_U;
}

// Given a parent process's page table, create a copy
// of it for a child.
// check & ensure the behavior of: expect the child to maintain the exact
// same working set and pgdir flags
pde_t *copyuvm(pde_t *pgdir, uint sz) {
    pde_t *d;
    pte_t *pte;
    uint pa, i, flags;
    char *mem;

    if ((d = setupkvm()) == 0) return 0;
    for (i = 0; i < sz; i += PGSIZE) {
        if ((pte = walkpgdir(pgdir, (void *)i, 0)) == 0)
            panic("copyuvm: pte should exist");
        if (!(*pte & (PTE_P | PTE_E))) panic("copyuvm: page not present");
        pa = PTE_ADDR(*pte);
        flags = PTE_FLAGS(*pte);
        if ((mem = kalloc()) == 0) goto bad;
        memmove(mem, (char *)P2V(pa), PGSIZE);
        if (mappages(d, (void *)i, PGSIZE, V2P(mem), flags) < 0) {
            kfree(mem);
            goto bad;
        }
    }
    return d;

bad:
    freevm(d);
    return 0;
}

// PAGEBREAK!
// Map user virtual address to kernel address.
char *uva2ka(pde_t *pgdir, char *uva) {
    pte_t *pte;

    pte = walkpgdir(pgdir, uva, 0);
    if ((*pte & (PTE_P | PTE_E)) == 0) return 0;
    if ((*pte & PTE_U) == 0) return 0;
    return (char *)P2V(PTE_ADDR(*pte));
}

// Copy len bytes from p to user address va in page table pgdir.
// Most useful when pgdir is not the current page table.
// uva2ka ensures this only works for PTE_U pages.
int copyout(pde_t *pgdir, uint va, void *p, uint len) {
    char *buf, *pa0;
    uint n, va0;

    buf = (char *)p;
    while (len > 0) {
        va0 = (uint)PGROUNDDOWN(va);
        pa0 = uva2ka(pgdir, (char *)va0);
        if (pa0 == 0) return -1;
        n = PGSIZE - (va - va0);
        if (n > len) n = len;
        memmove(pa0 + (va - va0), buf, n);
        len -= n;
        buf += n;
        va = va0 + PGSIZE;
    }
    return 0;
}

// PAGEBREAK!
// Blank page.
// PAGEBREAK!
// Blank page.
// PAGEBREAK!
// Blank page.

/** 📝 statistics **/

/**
 * @brief retreive stats about the state of the page table - filling entries
 array using information from the page table of the currently running process,
 considering only valid virtual  pages.
 *
 * NOTE: filling up the array should start from the valid virtual page with the
 highest page numbers.
 *
 * @param entries array of pt_entries to be filled
 * @param num number elements that should be filled up by the kernel
 * @param wsetOnly whether should filter the results and only output the page
 * table entries for the pages in the working set.
 * @return int actual number of elements that are filled in entries, otherwise
 if error encountered return -1
 */
int getpgtable(struct pt_entry *entries, int num, int wsetOnly) {
    // validate params
    if (wsetOnly != 0 && wsetOnly != 1) goto fail;
    if (entries == 0 || num < 0) goto fail;
    if (num == 0) return 0;  // short-circuit for 0 elements

    struct proc *proc = myproc();  // current process info
    struct MultipageIndex page_i;  // index in the multipage table
    pte_t *pte;                    // page table entry

    // identify top most user page
    char *topVA = (char *)(proc->sz - 1);
    page_i = getPageIndex(topVA);  // starting page table entry

    // CASE: When the actual number of valid virtual pages is greater than
    // the num, filling up the array starts from the allocated virtual page
    // with the highest page numbers and returns num in this case. CASE:
    // When the actual number of valid virtual pages is less than or equals
    // to the num, then only fill up the array using those valid virtual
    // pages.
    int count = 0;  // number of elements filled
    for (int i = 0; i < num; ++i) {
        struct MultipageIndex currentPage_i = pteIterator(page_i, -i);
        // get current page table entry
        pte = getPTE(proc->pgdir, currentPage_i);
        if (pte == 0) break;  // invalid page encountered

        // filter pages in working set
        if (wsetOnly && clock_getIndex(&proc->workingSet, pte) == -1)
            continue;
        else
            count++;

        entries[i].pdx = currentPage_i.pd;
        entries[i].ptx = currentPage_i.pt;
        entries[i].ppage = (*pte) >> PTXSHIFT;  // as per spec
        entries[i].present = IS_BIT(pte, PTE_P) ? 1 : 0;
        entries[i].writable = IS_BIT(pte, PTE_W) ? 1 : 0;
        entries[i].encrypted = IS_BIT(pte, PTE_E) ? 1 : 0;
        entries[i].user = IS_BIT(pte, PTE_U) ? 1 : 0;
        entries[i].ref = IS_BIT(pte, PTE_A) ? 1 : 0;
    }

    return count;

fail:
    return -1;
}

/**
 * @brief dump the raw content of one physical page.
 *
 * @param physical_addr address where physical page resides
 * @param buffer to be filled by kernel with the content of the page where
 * physical_addr resides
 * - Assuming buffer will be allocated by the user and have the size of PGSIZE
 * @return int 0 on success, otherwise -1 on any error.
 */
int dump_rawphymem(uint physical_addr, char *buffer) {
    const int length = PGSIZE;

    // Note: no need to validate buffer parameter.
    if (physical_addr >= PHYSTOP) goto fail;  // validate param

    // The buffer might be encrypted, in which case you should decrypt
    // that page. Either using the buffer's uva for memmove (which copyout does
    // not do) or touching the buffer before copyout
    *buffer = *buffer;  // as per spec

    char *ka;           // kernel virtual address
    struct proc *proc;  // current process info

    proc = myproc();
    // translate to a kernel virtual memory address
    ka = (char *)P2V((char *)physical_addr);
    ka = (char *)PGROUNDDOWN((uint)ka);  // kernel address at page index 0

    /*
        The kernel should fill up the buffer with the current content of the
        page where physical_addr resides
        - it should not affect any of the page table entries that might point
        to this physical page (i.e., it shouldn't modify PTE_P or PTE_E)
        - it shouldn't do any decryption or encryption.
    */
    if (copyout(proc->pgdir, (uint)buffer, ka, length) == -1) goto fail;

    return 0;
fail:
    return -1;
}

/** 📝 custom **/

/**
 * @brief insert page into the working set usign eviction policy - Clock
algorithm (FIFO with second-chance) for picking victim page.
 *
 * NOTE: page should be guaranteed not to be already in queue
 *
 * @param workingSet
 * @param pte
 */
void pageReplacement(struct clock *workingSet, pte_t *pte, char *uva) {
    decryptPage(pte);  // decrypt inserted virtual page

    pte_t *evicted;  // evicted page from clock queue if any
    if ((evicted = clock_insert(workingSet, pte, (uint)uva)) != 0)
        // page should be encrypted and the appropriate bits in the PTE should
        // be reset
        encryptPage(evicted);  // encrypt evicted page
}

/* Clock queue manipulation functions */

// initialize clock structure to an empty state
void clock_initialize(struct clock *c) {
    memset((void *)c, 0, sizeof(c));
    c->capacity = CLOCKSIZE;
    c->size = 0;
    c->hand = -1;
    for (int i = 0; i < c->capacity; i++) c->queue[i].valid = INVALID;
}

// Insert a page into the clock queue.
pte_t *clock_insert(struct clock *c, pte_t *pte, uint uva) {
    // victim selection: examine the page at the head of the queue for candidate
    for (;;) {
        // First advance the hand.
        c->hand = (c->hand + 1) % c->capacity;

        // TODO: check if this is needed with current implementation
        // if (c->size < c->capacity) {  // workingSet not full
        //     // push page to tail of clock queue
        // }

        // case: empty slot
        if (c->queue[c->hand].valid == INVALID) {
            c->queue[c->hand].pte = pte;
            c->queue[c->hand].uva = uva;
            c->queue[c->hand].valid = VALID;
            c->size++;
            return 0;
        }

        // case: referenced bit is not set, then evict (Page is unused so
        // replace it)
        if (!IS_BIT(c->queue[c->hand].pte, PTE_A)) {
            pte_t *evictedPage = c->queue[c->hand].pte;
            // Put in the new page.
            c->queue[c->hand].pte = pte;
            c->queue[c->hand].uva = uva;
            c->queue[c->hand].valid = VALID;
            return evictedPage;
        }

        // clear referenced bit (PTE_A) of the page in slot and move the
        // node to the tail of the queue
        *(c->queue[c->hand].pte) = CLEAR_BIT(c->queue[c->hand].pte, PTE_A);
    }
}

// find page table entry
int clock_getIndex(struct clock *c, pte_t *pte) {
    for (int i = 0; i < c->size; ++i) {
        int idx = (c->hand + i) % c->capacity;
        if (c->queue[idx].pte == pte) return idx;
    }

    return -1;  // not found
}

// Removing a page forcefully is tricky because you need to
// shift things around.
// This happens at page deallocation.
pte_t *clock_remove(struct clock *c, pte_t *pte) {
    int prev_tail = c->hand;
    int match_idx = -1;

    // Search for the matching element.
    if ((match_idx = clock_getIndex(c, pte)) == -1) return 0;

    // compact - shift everything from match_idx+1 to prev_tail to
    // one slot to the left.
    for (int idx = match_idx; idx != prev_tail; idx = (idx + 1) % c->capacity) {
        int next_idx = (idx + 1) % c->capacity;
        c->queue[idx].pte = c->queue[next_idx].pte;
        c->queue[idx].uva = c->queue[next_idx].uva;
        c->queue[idx].valid = c->queue[next_idx].valid;
    }

    // Clear the element at prev_tail. Set hand to one entry to the left.
    c->queue[prev_tail].valid = INVALID;
    c->hand = c->hand == 0 ? c->capacity - 1 : c->hand - 1;
    return pte;
}

/**
 * @brief Encrypt ranges of virtual pages
 *
 * encrypt virtual addresses ranging
 *       [PGROUNDDOWN(va), PGROUNDDOWN(va)+len*PGSIZE)
 * Example: 4KB page size
 *      mencrypt(0x3000, 2) = mencrypt(0x3050, 2) ⇒ encrypts [0x3000, 0x5000]
 *
 * @param va virtual address indicating the starting virtual page
 * (page associated with the address)
 * @param len # of pages to encrypt
 * @return int 0 on success, otherwise -1 on failure.
 */
int mencrypt(pde_t *pageDirectory, uint sz, char *va, int len) {
    if (len == 0)  // ignore - short-circuit before any error checking
        return 0;
    else if (len < 0)  // case: negative value
        goto fail;

    char *va0;  // first virtual address in the corresponding va's page
    struct MultipageIndex page_i;

    va0 = (char *)PGROUNDDOWN((uint)va);  // assuming va could be page-aligned
    /*🐞 assert correctly rounded (NOTE: unnecessary step; only for debugging)
    if ((uint)va >> PTXSHIFT != (uint)va0 >> PTXSHIFT) goto fail; */

    // check if virtual address is out of range in current user virtual space:
    // case: virtual address is an invalid address (e.g., out-of-range
    // value)
    // case: a very large value that will let the page range  exceed the upper
    // bound of the user virtual space
    if (va < 0 || (uint)va >= sz || (uint)NEXT_PAGE(va0, len - 1) >= sz)
        goto fail;

    // get virtual address's page entry
    page_i = getPageIndex(va0);
    /*🐞 assert pages equal (NOTE: unnecessary step; only for debugging)
// pte_t *pte;  // page table entry (matching input va's page)
// pte = getPTE(pageDirectory, page_i);
struct MultipageIndex page_i0 = {PDX(va0), PTX(va0)};
if (pte != getPTE(pageDirectory, page_i0)) goto fail; */

    // NOTE: un-necessary check as pages handled by kernel before refreshing
    // user process case: calling process does not have permission or privilege
    // to  access or modify some pages in the range (either all the pages in the
    // range are successfully encrypted or none of them is encrypted)
    // for (int i = 0; i < len; ++i) {
    //     struct MultipageIndex currentPage_i = pteIterator(page_i, i);
    //     pte_t *e = getPTE(pageDirectory, currentPage_i);  // current pte
    //     //  if (!e || !IS_BIT(e, PTE_P) && !IS_BIT(e, PTE_E)) ||
    //     //     !IS_BIT(e, PTE_W) || !IS_BIT(e, PTE_U))
    //     //     goto fail;
    // }

    // case part or all pages already encypted: Encrypted pages and their
    // corresponding page table entries should remain unchanged. All the
    // unencrypted pages should be encrypted
    for (int i = 0; i < len; ++i) {
        struct MultipageIndex currentPage_i = pteIterator(page_i, i);
        pte_t *e = getPTE(pageDirectory, currentPage_i);  // current pte

        if (!e) goto fail;
        // already encrypted
        if (!IS_BIT(e, PTE_W) || !IS_BIT(e, PTE_U))
            ;  // ignore as kernel handles encryption before refreshing
               // process

        encryptPage(e);
    }

    return 0;

fail:
    return -1;
}

/**
 * @brief get index multipagetable of page corresponding to the virtual address
 *
 * @param va virtual address
 * @return page table index including directory and table indecies.
 */
struct MultipageIndex getPageIndex(char *va) {
    struct MultipageIndex i;

    char *va0 = (char *)PGROUNDDOWN((uint)va);
    i.pd = PDX(va0);
    i.pt = PTX(va0);

    return i;
}

/**
 * @brief get next page entry index in the multilevel page table using page
 * index arithmetic
 *
 * @param page_i page directory & table index of current state of iterator
 * @param nextPageIndex # of page table entry beyond the current position
 * @return struct MultipageIndex next page entry index. NOTE: can return an
 * invalid index
 */
struct MultipageIndex pteIterator(struct MultipageIndex page_i,
                                  int nextPageIndex) {
    if (nextPageIndex < 0) goto reverse;  // support reverse order

    // calculate indecies of outer & inner page tables using current page #
    // outer page table
    page_i.pd = page_i.pd + (page_i.pt + nextPageIndex) / NPTENTRIES;
    // inner page table
    page_i.pt = (page_i.pt + nextPageIndex) % NPTENTRIES;
    return page_i;

reverse : {               // iterate to a lower index page
    nextPageIndex *= -1;  // remove negative sign
    // offset from table start
    int offsetEntry = page_i.pt - (nextPageIndex % NPTENTRIES);

    page_i.pt = (NPTENTRIES + offsetEntry) % NPTENTRIES;
    // calculate offset of the page directory index and decrease accordingly
    page_i.pd -= nextPageIndex / NPTENTRIES;  // # of overlaps to previous pages
    // negative offset the extends to the previous page
    if (offsetEntry != 0) page_i.pd -= (NPTENTRIES + -offsetEntry) / NPTENTRIES;

    return page_i;
}
}

/**
 * @brief retrieve page table entry using indexes
 *
 * @param pageDirectory target/current process page directory base kva
 * @param page_i page directory & table entry index
 * @return pte_t* pointer to page table entry, or 0 if page dir not present
 */
pte_t *getPTE(pde_t *pageDirectory, struct MultipageIndex page_i) {
    if (page_i.pd < 0 || page_i.pt < 0) goto fail;  // validate param

    pde_t *pde;
    pte_t *pgtab;

    // check other flags for directory page table enteries like permission
    pde = &pageDirectory[page_i.pd];
    if (!IS_BIT(pde, PTE_P) || !IS_BIT(pde, PTE_W) || !IS_BIT(pde, PTE_U))
        goto fail;

    pgtab = (pte_t *)P2V(PTE_ADDR(*pde));
    return &pgtab[page_i.pt];

fail:
    return 0;
}

/**
 * @brief check if the thrown page fault is a legit/genuine encryption fault
 *
 * @param faultVA virtual address causing the fault
 * @return pte_t* page table entry corresponding to the address, or 0 if a non
 * encryption related fault.
 */
pte_t *validateFaultPage(pde_t *pageDirectory, char *faultVA) {
    struct MultipageIndex page_i = {PDX(faultVA), PTX(faultVA)};
    pte_t *pte =
        getPTE(pageDirectory, page_i);  // get suspected page table entry

    /* validate legit page fault */
    if (pte == 0 || IS_BIT(pte, PTE_P) || !IS_BIT(pte, PTE_E) ||
        !IS_BIT(pte, PTE_W))
        return 0;

    return pte;
}

/**
 * @brief encrypt a single page table entry
 *
 * @param pte page table entry associated with the target page
 * - NOTE: assumes page entry is both valid for current user with permission
 */
void encryptPage(pte_t *pte) {
    if ((!IS_BIT(pte, PTE_P) && IS_BIT(pte, PTE_E)))
        return;  // skip encrypted pages

    char *pagePhysicalAddr = (char *)PTE_ADDR(*pte);
    toggleEncryptPageSize(pagePhysicalAddr);
    // update flags
    *pte = CLEAR_BIT(pte, PTE_P);
    *pte = SET_BIT(pte, PTE_E);
    flushTLB();  // flush cache after a page entry update
}

/**
 * @brief decrypt page addresses and update page entry
 *
 * @param pte page table entry associated with the target page
 * - NOTE: assumes page entry is both valid for current user with permission
 */
void decryptPage(pte_t *pte) {
    if (!IS_BIT(pte, PTE_E)) return;  // skip non-encrypted pages

    char *pagePhysicalAddr = (char *)PTE_ADDR(*pte);
    toggleEncryptPageSize(pagePhysicalAddr);  // encrypt all addresses in page

    // update flags
    *pte = CLEAR_BIT(pte, PTE_E);
    *pte = SET_BIT(pte, PTE_P);
    flushTLB();
}

/**
 * @brief encrypt/decrypt all bytes in a page starting from the beginning
 *
 * @param pagePhysicalAddress first physical address of the page
 */
void toggleEncryptPageSize(char *pagePhysicalAddress) {
    // obtain kernel virtual address to modify the bytes contents
    char *pageKA = (char *)P2V(pagePhysicalAddress);
    // encrypt/decrypt contents of all page bytes
    for (int i = 0; i < PGSIZE; ++i, ++pageKA) *pageKA = FLIP_BITS(*pageKA);
}

/**
 * @brief flush current process TLB after change table entry values
 */
void flushTLB() { switchuvm(myproc()); }

/**
 * @brief  get absolute mathematical value
 * source:
 * https://stackoverflow.com/questions/9772348/get-absolute-value-without-using-abs-function-nor-if-statement
 *
 * @param n integer number
 * @return int absolute value of the integer number
 */
int absolute(int n) {
    const int ret[2] = {n, -n};
    return ret[n < 0];
}
