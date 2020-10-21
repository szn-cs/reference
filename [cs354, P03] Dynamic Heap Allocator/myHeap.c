///////////////////////////////////////////////////////////////////////////////
//
// Copyright 2019-2020 Jim Skrentny
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission Fall 2020, CS354-deppeler
//
///////////////////////////////////////////////////////////////////////////////
 
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <stdio.h>
#include <string.h>
#include "myHeap.h"
 
// general flags indicating operation status
#define FAILURE -1
#define SUCCESS 0

/*
 * Severs as: 
 * - The header for each allocated/free memory block.
 * or
 * - The footer for each free block (only containing size).
 */
typedef struct blockHeader {    
    /*
     * size of block - stored in all block headers and free block footers: 
     *     - always a multiple of 8
     * 
     * status of block - stored only in headers using the two least significant bits: 
     *     - Bit0 (least significant bit, last bit): status of current block
     *          0 => free block
     *          1 => allocated block
     *     - Bit1 (second last bit): status of previous block
     *          0 => free block
     *          1 => allocated block
     * 
     * Special case: 
     * - End Mark: The end of the available memory is indicated using a size_status of 1.
     * 
     * 
     * Examples:
     * 
     * 1. Allocated block of size 24 bytes:
     *    Header:
     *      If the previous block is allocated, size_status should be 27
     *      If the previous block is free, size_status should be 25
     * 
     * 2. Free block of size 24 bytes:
     *    Header:
     *      If the previous block is allocated, size_status should be 26
     *      If the previous block is free, size_status should be 24
     *    Footer:
     *      size_status should be 24
     */
    int size_status;
} blockHeader;         

/* Global variable - DO NOT CHANGE. It should always point to the first block (header),
 * i.e., the block at the lowest address.
 */
blockHeader *heapStart = NULL;     

/* Size of heap allocation padded to round to nearest page size.
 */
int allocsize;


 
/* 
 * Function for allocating 'size' bytes from process's heap memory.
 * Allocator design choices: 
 *    - implicit free list structure
 *        - Using headers with size and status information (p-bit & a-bit)
 *        - free block footers for heap structure, storing block size
 *    - Block allocation: 
 *        - next-fit placement policy to chose a free block
 *        - split block policy to divide too large chosen free block (to minimize internal fragmentation)
 *            - splinting: remainder of free block should be at least 8 bytes in size.
 *        - double-word (8 bytes) aligned (to improve performance)
 *        - padding: total size of the allocated block including header must be a multiple of 8
 *    - No additional heap memory should be requested from the OS.
 * 
 * Argument size: requested size for the payload
 * Returns address/pointer of allocated block (allocated payload of 'size' bytes) on success.
 * Returns NULL on failure
 * 
 * Tests: 
    test_alloc1: a simple 8-byte allocation
    test_alloc1_nospace: allocation is too big to fit in available space3 
    test_writeable: write to a chunk from Mem_Alloc and check the value
    test_align1: the first pointer returned is 8-byte aligned
    test_alloc2: a few allocations in multiples of 4 bytes
    test_alloc2_nospace: the second allocation is too big to fit 
    test_align2: a few allocations in multiples of 4 bytes checked for alignment 
    test_alloc3: many odd sized allocations 
    test_align3: many odd sized allocations checked for alignment
 */
void* myAlloc(int size) {     
    // Adjust scale factor to perform pointer arithmetic as expected
    // Double check your pointer arithmetic's automatic scaling. int* would increment the address by 4 bytes. Cast your heap block pointers to void* or char* to set the scale factor to 1. What scale factor is used for blockHeader*?
    (blockHeader*) ptr;

    // case: Check size - Return NULL if not positive or if larger than heap space.
    // case: if there isn't a free block large enough to satisfy the request.

    // Determine block size rounding up to a multiple of 8 and possibly adding padding as a result.

    // to get that size rather than using 4 bytes.
    sizeof(blockHeader);


    // verify 8 byte aligned pointer: last hexadecimal digit should be multiple of 8 (i.e. 0 or 8)
    printf("%08x", (unsigned int)(ptr)); 

    // check if a block is allocated or not.
    if((size_status & 1) == 0);

    // Update header(s) and footer as needed.

    return NULL;
} 
 
/* 
 * Function for freeing up a previously allocated block.
 * Freeing memory: 
 *     - immediate coalescing with adjacent free memory blocks, 
 *       if one or both of the adjacent neighbors are free.
 *     - requires only one header and one footer in the coalesced free block, 
 *       and you should not waste time clearing old headers, footers, or data.
 * 
 * Argument ptr: address of the block to be freed up.
 * Returns 0 on success.
 * Returns -1 on failure.
 *      - if ptr is .
 *      - if ptr is .
 *      - if ptr .
 * 
 
 Tests: 
    test_free1: a few allocations in multiples of 4 bytes followed by frees
    test_free2: many odd sized allocations and interspersed frees
    test_coalesce1: check for coalescing free space
    test_coalesce2: check for coalescing free space
    test_coalesce3: check for coalescing free space
    test_coalesce4: check for coalescing free space
    test_coalesce5: check for coalescing free space (first chunk)
    test_coalesce6: check for coalescing free space (last chunk)
 */                    
int myFree(void *ptr) {
    /* Validate input pointer */    
    if(ptr == NULL) // case: NULL
        return FAILURE; 
    // case: not a multiple of 8 (not 8 byte aligned)
    // case: outside of the heap space (not within the range of memory allocated by myInit())
    // case: block is already freed (points to a free block)

    // Update header(s) and footer as needed.

    return FAILURE;
} 
 
/*
 * Function used to initialize the memory allocator (memory-mapped segment being used to simulate the heap segment).
 * Intended to be called ONLY once by a program.
 * Argument sizeOfRegion: the size of the heap space to be allocated.
 * Returns 0 on success.
 * Returns -1 on failure.
 */                    
int myInit(int sizeOfRegion) {    
 
    static int allocated_once = 0; //prevent multiple myInit calls
 
    int pagesize;  // page size
    int padsize;   // size of p\adding when heap size not a multiple of page size
    void* mmap_ptr; // pointer to memory mapped area
    int fd;

    blockHeader* endMark;
  
    if (0 != allocated_once) {
        fprintf(stderr, 
        "Error:mem.c: InitHeap has allocated space during a previous call\n");
        return -1;\
    }
    if (sizeOfRegion <= 0) {
        fprintf(stderr, "Error:mem.c: Requested block size is not positive\n");
        return -1;
    }

    // Get the pagesize
    pagesize = getpagesize();

    // Calculate padsize as the padding required to round up sizeOfRegion 
    // to a multiple of pagesize
    padsize = sizeOfRegion % pagesize;
    padsize = (pagesize - padsize) % pagesize;

    allocsize = sizeOfRegion + padsize;

    // Using mmap to allocate memory
    fd = open("/dev/zero", O_RDWR);
    if (-1 == fd) {
        fprintf(stderr, "Error:mem.c: Cannot open /dev/zero\n");
        return -1;
    }
    mmap_ptr = mmap(NULL, allocsize, PROT_READ | PROT_WRITE, MAP_PRIVATE, fd, 0);
    if (MAP_FAILED == mmap_ptr) {
        fprintf(stderr, "Error:mem.c: mmap cannot allocate space\n");
        allocated_once = 0;
        return -1;
    }
  
    allocated_once = 1;

    // for double word alignment and end mark
    allocsize -= 8;

    // Initially there is only one big free block in the heap.
    // Skip first 4 bytes for double word alignment requirement.
    heapStart = (blockHeader*) mmap_ptr + 1;

    // Set the end mark
    endMark = (blockHeader*)((void*)heapStart + allocsize);
    endMark->size_status = 1;

    // Set size in header
    heapStart->size_status = allocsize;

    // Set p-bit as allocated in header
    // note a-bit left at 0 for free
    heapStart->size_status += 2;

    // Set the footer
    blockHeader *footer = (blockHeader*) ((void*)heapStart + allocsize - 4);
    footer->size_status = allocsize;
  
    return 0;
} 
                  
/* 
 * Function to be used for DEBUGGING to help you visualize your heap structure.
 * Prints out a list of all the blocks including this information:
 * No.      : serial number of the block 
 * Status   : free/used (allocated)
 * Prev     : status of previous block free/used (allocated)
 * t_Begin  : address of the first byte in the block (where the header starts) 
 * t_End    : address of the last byte in the block 
 * t_Size   : size of the block as stored in the block header
 * 
 * TODO: It is recommended that you extend the implementation to display the information stored in the footers of the free blocks too.
 */                     
void dispMem() {     
 
    int counter;
    char status[5];
    char p_status[5];
    char *t_begin = NULL;
    char *t_end   = NULL;
    int t_size;

    blockHeader *current = heapStart;
    counter = 1;

    int used_size = 0;
    int free_size = 0;
    int is_used   = -1;

    fprintf(stdout, "************************************Block list***\
                    ********************************\n");
    fprintf(stdout, "No.\tStatus\tPrev\tt_Begin\t\tt_End\t\tt_Size\n");
    fprintf(stdout, "-------------------------------------------------\
                    --------------------------------\n");
  
    while (current->size_status != 1) {
        t_begin = (char*)current;
        t_size = current->size_status;
    
        if (t_size & 1) {
            // LSB = 1 => used block
            strcpy(status, "used");
            is_used = 1;
            t_size = t_size - 1;
        } else {
            strcpy(status, "Free");
            is_used = 0;
        }

        if (t_size & 2) {
            strcpy(p_status, "used");
            t_size = t_size - 2;
        } else {
            strcpy(p_status, "Free");
        }

        if (is_used) 
            used_size += t_size;
        else 
            free_size += t_size;

        t_end = t_begin + t_size - 1;
    
        fprintf(stdout, "%d\t%s\t%s\t0x%08lx\t0x%08lx\t%d\n", counter, status, 
        p_status, (unsigned long int)t_begin, (unsigned long int)t_end, t_size);
    
        current = (blockHeader*)((char*)current + t_size);
        counter = counter + 1;
    }

    fprintf(stdout, "---------------------------------------------------\
                    ------------------------------\n");
    fprintf(stdout, "***************************************************\
                    ******************************\n");
    fprintf(stdout, "Total used size = %d\n", used_size);
    fprintf(stdout, "Total free size = %d\n", free_size);
    fprintf(stdout, "Total size = %d\n", used_size + free_size);
    fprintf(stdout, "***************************************************\
                    ******************************\n");
    fflush(stdout);

    return;  
} 


// end of myHeap.c (fall 2020)

