
// When an encrypted page is accessed by the user, a page fault should be
// triggered.
void handlePageFault() {
    if (calling process`s #decrepted pages < N) {
        // decrypt virtual page and push to tail of clock queue
    } else {
        // find victim page to replace
        victimPage = clockAlgorithm();
        evict(victimPage);  // evict page
    }
}

// victim policy - Clock algorithm (FIFO with second-chance) for picking victim
// page.
void clockAlgorithm() {
    // (statically) allocate a clock queue for each process - storing all the
    // virtual pages that are currently decrypted

    // PTE_A (0x020 i.e. sixth bit): reference bit that gets set by x86
    // hardware to 1 every time a page is accessed.
    // - This hardware-managed access bit should be cleared by the kernel (in
    // software) at the appropriate time, while set automatically by hardware on
    // next access (there is no harm in setting it manually).

    // victim selection: examine the page at the head of the queue.
    while (victimFound) {
        // head page has been accessed since it was last enqueued
        if (head.PTE_A == 1) {
            //  clear the reference bit and move the node to the tail of the
            //  queue

            continue;  // victim selection should proceed to the next page in
                       // the queue
        }
    }

    return victimPage;
}

void evictPage() {
    // page should be encrypted and the appropriate bits in the PTE should be
    // reset
}

/** statistics **/

/**
 * @brief
 *
 * @param entries array of pt_entries to be filled
 * @param num number elements that should be filled up by the kernel
 * @param wsetOnly whether should filter the results and only output the page
 * table entries for the pages in the working set.
 * @return int
 */
int getpgtable(struct pt_entry* entries, int num, int wsetOnly) {
    if (wsetOnly != 0 && wsetOnly != 1) goto fail;

    // fill entries array using information from the page table of the currently
    // running process, considering only valid virtual  pages.
    // NOTE: filling up the array should start from the valid virtual page with
    // the highest page numbers

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
 * @return int
 */
int dump_rawphymem(uint physical_addr, char* buffer) {
    // Note: no need to validate buffer parameter.

    // The buffer might be encrypted, in which case you should decrypt that
    // page. Either using the buffer's uva for memmove (which copyout does not
    // do) or touching the buffer using *buffer = *buffer before copyout
}