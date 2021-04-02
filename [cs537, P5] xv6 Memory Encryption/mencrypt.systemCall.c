
int mencrypt(char *, int);

/**
 * @brief Encrypt ranges of virtual pages
 *
 * Example: 4KB page size
 * - mencrypt(0x3000, 2) = mencrypt(0x3050, 2) ⇒ encrypts [0x3000, 0x5000]
 *
 * @param virtual_addr virtual address indicating the starting virtual page
 * (page associated with the address)
 * @param len # of pages to encrypt
 * @return int 0 on success, otherwise -1 on failure.
 */
int mencrypt(char *virtual_addr, int len) {
    if (len == 0)
        return 0;  // do nothing (short-circuit before any error checking)

    // negative value or a very large value that will let the page range exceed
    // the upper bound of the user virtual space
    if (len < 0 /*|| out of range */) goto fail;

    // assumption virtual_addr is not necessarily page-aligned

    // case part or all pages already encypted: Encrypted pages and their
    // corresponding page table entries should remain unchanged. All the
    // unencrypted pages should be encrypted

    // case virtual address is an invalid address (e.g., out-of-range value)
    goto fail;

    // case calling process does not have permission or privilege to access or
    // modify some pages in the range (either all the pages in the range are
    // successfully encrypted or none of them is encrypted)
    goto fail;

fail:

    return -1;

success:
    /* encrypt virtual addresses ranging from
        [PGROUNDDOWN(virtual_addr), PGROUNDDOWN(virtual_addr) + len * PGSIZE)
    */
    return 0;
}
