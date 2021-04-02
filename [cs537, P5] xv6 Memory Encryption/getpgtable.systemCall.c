int getpgtable(struct pt_entry*, int);

/**
 * @brief retreive statistics about the state of the page table
 *
 * pt_entry uses bitfields to conserve space, fields that have a ': 1' next to
 them have a size of 1 bit. Attempting to set a value greater than 1 will cause
 an overflow error.
 *
 * @param entries array of pt_entries
 * @param num num elements that should be allocated by the user application and
 * filled up by the kernel
 * @return int
 */
int getpgtable(struct pt_entry* entries, int num) {
    // kernel should fill up the entries array using the information from the
    // page table of the currently running process. Only valid (allocated)
    // virtual pages belong to the user will be considered.

    // When the actual number of valid virtual pages is greater than the num,
    // filling up the array starts from the allocated virtual page with the
    // highest page numbers and returns num in this case.

    // You might find sz field in the proc structure of each process is useful
    // to identify the most top user page.

    /*
     For instance, if one process has
     allocated 10 virtual pages with page numbers ranging from 0x0 - 0x9 and
     page 0x9 is encrypted,  then page 0x9 - 0x7 should be used to fill up the
     array when num is 3. The array should look as follows (ppage might be
     different):
    0: pdx: 0x0, ptx: 0x9, ppage: 0xC3, present: 0, writable: 1, encrypted: 1
    1: pdx: 0x0, ptx: 0x8, ppage: 0xC2, present: 1, writable: 1, encrypted: 0
    2: pdx: 0x0, ptx: 0x7, ppage: 0xC1, present: 1, writable: 1, encrypted: 0
    */

    // When the actual number of valid virtual pages is less than or equals to
    // the num, then only fill up the array using those valid virtual pages.
    // Return the actual number of elements that are filled in entries.  The
    // only error defined for this function is if entries is a null pointer, in
    // which case you should return -1. Return -1 if you encounter any other
    // error, too.
}

/**
 * @brief
 *
 * @param physical_addr physical_addr is now a uint instead of a char*. You must
 use argint to parse it, and you cannot dereference it until you translate it to
 a kernel virtual memory address. How do you do that?
 * @param buffer
 * @return int
 */
int dump_rawphymem(uint physical_addr, char* buffer) {
    /*
    allows the user to dump the raw content of one physical page where
    physical_addr resides (This is very dangerous! We're implementing this
    syscall only for testing purposes.). The kernel should fill up the buffer
    with the current content of the page where physical_addr resides -- it
    should not affect any of the page table entries that might point to this
    physical page (i.e., it shouldn't modify PTE_P or PTE_E) and it shouldn't do
    any decryption or encryption.  Note that physical_addr may not be the first
    address of a page (i.e., may not be page aligned).  buffer will be allocated
    by the user and have the size of PGSIZE. You are not required to do any
    error handling here. Note that argptr() will do a boundary check, which
    would cause an error for the pointer physical_addr. Therefore, when you grab
    the value of physical_addr from the stack, use argint() instead of argptr().

    dump_rawphymem should return 0 on success and -1 on any error.
    If you do this function right, it will only be a couple of lines of code
    (see copyout).
    */
}
