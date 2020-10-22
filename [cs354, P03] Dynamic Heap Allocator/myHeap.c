/*
contributions: 
- iterators https://www.cs.yale.edu/homes/aspnes/pinewiki/C(2f)Iterators.html
*/

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
#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>
#include "myHeap.h"

// Debug flag
#define DEBUG if(1)
// general flags indicating operation status
#define FAILURE -1
#define SUCCESS 0
// Processor's unit of data in bytes
#define WORD 4 
#define DOUBLE_WORD (2 * WORD)
// heap space end mark
#define END_MARK 1

/*
 * Severs as: 
 * - The header for each allocated/free memory block.
 * or
 * - The footer for each free block (only containing size).
 * 
 * Adjust scale factor to perform pointer arithmetic with automatic scaling as expected.
        - void* or char* = to set the scale factor to 1. 
        - int* = would increment the address by 4 bytes. 
        - blockHeader* = 4 bytes
   
 */
typedef struct blockHeader {    
    /*
     * Using headers with size and status information (p-bit & a-bit).
     * 
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
     * Footer of free blocks - storing block size.
     * 
     * End Mark - The end of the available memory is indicated using a size_status of 1.
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
} blockHeader, blockFooter;         

/* Global variable - DO NOT CHANGE. It should always point to the first block (block at the lowest address) */
blockHeader *heapStart = NULL;     

/* Size of heap allocation padded to round to nearest page size. */
int allocsize;

/* most recently allocated block */
blockHeader* recentBlock = NULL; 

// check bit at particular position if set, otherwise false for 0.
bool isBitAtPositionSet(int *i, int p) { return (*i & (1 << (p-1))) ? true : false; }
// toggle at bit particular position.
void toggleBitAtPosition(int *i, int p) { *i = *i ^ (1 << (p-1)); }
// unset all bits to particular position
int unsetBitsTillPosition(int *i, int p) { return *i >> p << p; }

/**
 * Implicit free list & heap information functions: 
 */
// check a-bit flag. if a block is allocated or not.
bool isCurrentAllocated(blockHeader* b) { return isBitAtPositionSet(&b->size_status, 1); }
// check p-bit flag. if preceding block is allocated or not.
bool isPreviousAllocated(blockHeader* b) { return isBitAtPositionSet(&b->size_status, 2); }
// toggle a-bit flag - current block allocation status
void toggleABit(blockHeader* b) { toggleBitAtPosition(&b->size_status, 1); }
// toggle p-bit flag - previous block allocation status
void togglePBit(blockHeader* b) { toggleBitAtPosition(&b->size_status, 2); }
// extract size of block from size_status block information
int getSize(blockHeader *b) { return unsetBitsTillPosition(&b->size_status, 2); }
// Calculate padding if needed from block payload size - total size of the allocated block including header must be a multiple of 8
int getBlockPadding(int payloadSize, int multiple) {
    return multiple - (payloadSize + sizeof(blockHeader)) % multiple;
}

/**
 * Block iterator functions: 
 */
bool blockDone(blockHeader* b) { return b->size_status == END_MARK; }
blockHeader* blockNext(blockHeader* b) { 
    if(blockDone(b)) return NULL; 
    return (void *) b + getSize(b); 
}
blockHeader* blockNextWrapAround(blockHeader* b) {
    if(blockDone(b)) 
        return (void *) heapStart;
    else {
        blockHeader *next = blockNext(b); // next block
        return (void *) next; // wrap-around if required
    } 
}
blockHeader* blockFirst(blockHeader* recentBlock) {
    // if no previously allocated block
    return (recentBlock == NULL) ? heapStart : blockNext(recentBlock);
}
blockHeader* blockPrevFree(blockHeader* b) { 
    if(b == heapStart || isPreviousAllocated(b)) return NULL;
    blockFooter* footer = b - sizeof(blockFooter); 
    return b - footer->size_status;
}

/**
 * Heap allocator policies' functions: 
 */
/*
 * verify 8 byte aligned pointer: last hexadecimal digit should be multiple of 8 (i.e. 0 or 8)
 * alignment: double-word (8 bytes) aligned (to improve performance)
 */
bool isDoubleWordAligned(void* ptr) { return ((unsigned int)(ptr) % DOUBLE_WORD) == 0; }

/**
 * next-fit placement policy to chose a free block.
 */
int nextFitPlacementPolicy(int blockSize, blockHeader** ptr) {
    for(blockHeader* b = blockFirst(recentBlock); b != recentBlock; b = blockNextWrapAround(b)) {
        // loop through free blocks only, that have enough space
        if(!isCurrentAllocated(b) && getSize(b) >= blockSize) {
            *ptr = b;
            return SUCCESS;
        }
    }
    return FAILURE; 
}

/*
 * split block policy to divide too large chosen free block (to minimize internal fragmentation). 
 * splinting - remainder of free block should be at least 8 bytes in size.
 */
void splitBlockPolicy(int *blockSize, blockHeader* freeBlock) {
    // validate block size: checking if split is possible
    if(*blockSize >= getSize(freeBlock)) return; // skip if no free block remainder

    // validate free block remainder: should be at least 8 bytes in size.
    int remainderSize = getSize(freeBlock) - *blockSize;
    if(remainderSize < 8) {
        *blockSize = getSize(freeBlock); // take up entire free block 
        return;
    }

    // remainder free block
    blockHeader *remainderBlock = (void *) freeBlock + *blockSize; 
    // add header to remaining free block
    remainderBlock->size_status = remainderSize;
    togglePBit(remainderBlock); // update p-bit
    // add footer 
    blockHeader *footer = (void *) remainderBlock + (remainderSize - sizeof(blockHeader)); 
    footer->size_status = remainderSize;   
}

// immediate coalescing with adjacent free memory blocks, if one or both of the adjacent neighbors are free
blockHeader* immediateCoalescingPolicy(int* blockSize, void* ptr) {
    // get adjacent blocks
    blockHeader *prev = NULL, *next = NULL;
    if((next = blockNext(ptr)) != NULL) *blockSize += getSize(next);
    if((prev = blockPrevFree(ptr)) != NULL) {
        *blockSize += getSize(prev);
        return prev;
    };
    return ptr;
}

/**
 * myAlloc Tests: 
    ✔   test_alloc1: a simple 8-byte allocation
    ✔   test_alloc1_nospace: allocation is too big to fit in available space3 
    ✔   test_writeable: write to a chunk from Mem_Alloc and check the value
    ✔   test_align1: the first pointer returned is 8-byte aligned
    ✔   test_alloc2: a few allocations in multiples of 4 bytes
    ✔   test_alloc2_nospace: the second allocation is too big to fit 
    ✔   test_align2: a few allocations in multiples of 4 bytes checked for alignment 
    ✔   test_alloc3: many odd sized allocations 
    ✔   test_align3: many odd sized allocations checked for alignment
*/
/* 
 * Function for allocating 'size' bytes from process's heap memory.
 * 
 * Allocator design choices: 
 *    - implicit free list structure: a-bit, p-bit. headers and footers.
 *    - Block allocation: next-fit, splitting, double-word alignment, padding (double-word multiples).
 *    - No additional heap memory should be requested from the OS.
 * 
 * Argument size: requested size for the payload
 * Returns address/pointer of allocated block (allocated payload of 'size' bytes) on success.
 * Returns NULL on failure
 */
void* myAlloc(int size) {
    blockHeader* b = NULL; // allocated block

    // validate size: if non-positive or larger than heap space short-circuit
    if(size < 1 || size > allocsize)
        return NULL; 
    
    // Determine block size rounding up to a multiple of 8 and possibly adding padding as a result.
    assert(sizeof(blockHeader) == WORD); // make sure header size matches a single WORD
    int blockSize = sizeof(blockHeader) + size + getBlockPadding(size, DOUBLE_WORD); 
    // validate block size: if larger than heap allocation short-circuit
    if(blockSize > allocsize) 
        return NULL;

    // choose matching block if any
    if(nextFitPlacementPolicy(blockSize, &b) == FAILURE) {
        // if there isn't a free block large enough to satisfy the request.
        return NULL;
    }; 
    DEBUG printf("chosen free block: %08x\n", (unsigned int)(b)); 
    
    // double word alignment of payload
    if(!isDoubleWordAligned(b + 1)) {
        fprintf(stderr, "Issue with double word alignment");
        exit(1);
    }; 

    // split block if possible
    splitBlockPolicy(&blockSize, b);
    
    // update allocated header
    bool prevAllocated = isPreviousAllocated(b);
    b->size_status = blockSize; // override with modified block size
    toggleABit(b); // a-bit toggle
    if(prevAllocated) togglePBit(b); // p-bit toggle

    recentBlock = b; // set most recent allocated

    DEBUG printf("Allocated block: %08x, %i\n", (unsigned int)(b), getSize(b)); 
    return (void*) (b + 1); // return address/pointer to payload
} 

/**
 *  Tests: 
        test_free1: a few allocations in multiples of 4 bytes followed by frees
        test_free2: many odd sized allocations and interspersed frees
        test_coalesce1: check for coalescing free space
        test_coalesce2: check for coalescing free space
        test_coalesce3: check for coalescing free space
        test_coalesce4: check for coalescing free space
        test_coalesce5: check for coalescing free space (first chunk)
        test_coalesce6: check for coalescing free space (last chunk)
 */
/* 
 * Function for freeing up a previously allocated block.
 * 
 * Freeing memory policies: 
 *     - immediate coalescing.
 *     - requires only one header and one footer in the coalesced free block, without clearing up older data
 * 
 * Argument ptr: address of the block to be freed up.
 * Returns 0 on success.
 * Returns -1 on failure.
 */                    
int myFree(void *ptr) {
    // validate input pointer
    if(ptr == NULL) return FAILURE;
    // validate alignment - not a multiple of 8 (not 8 byte aligned)
    if(!isDoubleWordAligned(ptr)) return FAILURE;
    // validate range: outside of the heap space (not within the range of memory allocated by myInit())
    if(ptr < (void*) heapStart || ptr >= (void*) heapStart + allocsize) return FAILURE;
    // validate status: block is already freed (points to a free block)
    if(!isCurrentAllocated(ptr)) return FAILURE;

    int blockSize = getSize(ptr); // size of free block
    // coalesce adjacent blocks
    ptr = immediateCoalescingPolicy(&blockSize, ptr);
    // update header
    bool prevAllocated = isPreviousAllocated(ptr); // p-bit
    ((blockHeader *) ptr)->size_status = blockSize; 
    if(prevAllocated) togglePBit(ptr);
    // add footer  
    blockHeader *footer = (void *) ptr + (blockSize - sizeof(blockHeader)); 
    footer->size_status = blockSize;   

    return SUCCESS;
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

