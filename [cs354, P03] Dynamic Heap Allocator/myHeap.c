///////////////////////////////////////////////////////////////////////////////
//
// Copyright 2019-2020 Jim Skrentny
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission Fall 2020, CS354-deppeler
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        ./tests/*.c
// This File:        myHeap.c
// Other Files:      myHeap.h
// Semester:         CS 354 Fall 2020
//
// Author:           Safi Nassar
// Email:            nassar2@wisc.edu
// CS Login:         safi@cs.wisc.edu
//
/////////////////////////// OTHER SOURCES OF HELP //////////////////////////////
//
// Persons:
//
// Online sources:
//  • iterators
//  https://www.cs.yale.edu/homes/aspnes/pinewiki/C(2f)Iterators.html
////////////////////////////////////////////////////////////////////////////////

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
#define DEBUG 0
// general flags indicating operation status
#define FAILURE -1
#define SUCCESS 0
// heap space end mark
#define END_MARK 1
// processor's unit of data in bytes
#define WORD 4
#define DOUBLE_WORD (2 * WORD)

/*
 * Severs as:
 * - The header for each allocated/free memory block.
 * - The footer for each free block (only containing size).
 *
 * Adjust scale factor to perform pointer arithmetic with automatic scaling as
 * expected:
 *   - void* or char* = to set the scale factor to 1.
 *   - blockHeader* or int* = would increment the address by 4 bytes.
 */
typedef struct blockHeader {
  /*
   * serves as:
   *
   * • headers with size and status information (p-bit & a-bit):
   *
   *      size of block - stored in all block headers and free block footers:
   *          - always a multiple of 8 (double-word)
   *
   *      status of block - stored only in headers using the two least
   *      significant bits:
   *          - Bit0 (least significant bit, last bit): status of current block
   *              0 => free block, 1 => allocated block
   *          - Bit1 (second last bit): status of previous block
   *              0 => free block, 1 => allocated block
   *
   * • Footer - storing block size of free blocks.
   *
   * • End Mark - end of process's available memory, indicated by value of 1.
   */
  int size_status;
} blockHeader, blockFooter;

/* Global variable - DO NOT CHANGE. It should always point to the first block
(block at the lowest address) */
blockHeader* heapStart = NULL;

/* Size of heap allocation padded to round to nearest page size. */
int allocsize;

/* next-fit pointer - last searched block (most recently allocated block) */
blockHeader* lastSearched = NULL;

/**
 * General function utilities
 */
bool isBitAtPositionSet(int*, int);
void toggleBitAtPosition(int*, int);
int unsetBitsTillPosition(int*, int);

/**
 * Implicit free list & heap information functions:
 */
bool isAllocated(blockHeader*);
bool isPreviousAllocated(blockHeader*);
void toggleABit(blockHeader*);
void togglePBit(blockHeader*);
int getSize(blockHeader*);
int getBlockPadding(int payloadSize, int multiple);

/**
 * Block iterator functions - implemented to match used policies:
 */
bool blockDone(blockHeader*);
blockHeader* blockFirst(blockHeader* lastSearched);
blockHeader* blockNext(blockHeader*);
blockHeader* blockPrevFree(blockHeader*);
blockHeader* blockNextWrapAround(blockHeader*);

/**
 * Heap allocator policies' functions:
 */
bool isDoubleWordAligned(void* ptr);
int nextFitPlacementPolicy(int blockSize, blockHeader** ptr);
int splitBlockPolicy(int* blockSize, blockHeader* freeBlock);
int immediateCoalescingPolicy(int* blockSize, blockHeader** ptr);

/*
 * Allocating memory block of 'size' bytes from process's heap memory.
 *
 * Allocator design choices:
 *    - implicit free list structure: a-bit, p-bit. headers and footers.
 *    - Block allocation: next-fit, splitting, double-word alignment, padding.
 *    - No additional heap memory would be requested from the OS.
 *
 * size: requested size for the payload
 * address of allocated block on success (payload of 'size' bytes), NULL on
 * failure.
 */
void* myAlloc(int size) {
  blockHeader* b = NULL;  // allocated block

  // validate size: if non-positive or larger than heap space short-circuit
  if (size < 1 || size > allocsize) return NULL;

  // Determine block size rounding up to a multiple of 8 and possibly adding
  // padding as a result.
  assert(sizeof(blockHeader) ==
         WORD);  // make sure header size matches a single WORD
  int blockSize =
      sizeof(blockHeader) + size + getBlockPadding(size, DOUBLE_WORD);
  // validate block size: if larger than heap allocation short-circuit
  if (blockSize > allocsize) return NULL;

  // choose matching block if any
  if (nextFitPlacementPolicy(blockSize, &b) == FAILURE) {
    // if there isn't a free block large enough to satisfy the request.
    return NULL;
  };
  if (DEBUG) printf("chosen free block: %08x\n", (unsigned int)(b));

  // double word alignment of payload
  if (!isDoubleWordAligned(b + 1)) {
    fprintf(stderr, "Issue with double word alignment");
    exit(1);
  };

  // split block if possible
  if (splitBlockPolicy(&blockSize, b) == FAILURE) {
    // update next block only if no split has happened.
    blockFooter* next = blockNext(b);  // initial next block
    // update next block's p-bit
    if (next != NULL && !blockDone(next)) togglePBit(next);
  };
  // update allocated header
  bool prevAllocated = isPreviousAllocated(b);
  b->size_status = blockSize;        // override with modified block size
  toggleABit(b);                     // a-bit toggle
  if (prevAllocated) togglePBit(b);  // p-bit toggle

  lastSearched = b;  // set most recent allocated

  if (DEBUG)
    printf("Allocated block: %08x, %i\n", (unsigned int)(b), getSize(b));
  return (void*)(b + 1);  // return address/pointer to payload
}

/*
 * Freeing up previously allocated block. Using freeing memory policies:
 * - immediate coalescing.
 * - modifies a single header & footer in coalesced free block, without clearing
 * up older data
 *
 * ptr: address of the block to be freed up.
 * 0 on success, -1 on failure.
 */
int myFree(void* ptr) {
  blockHeader* b;       // block header section
  blockFooter* footer;  // block footer section
  int blockSize;        // size of the block

  // validate input pointer
  if (ptr == NULL) return FAILURE;
  // validate alignment: not a multiple of 8
  if (!isDoubleWordAligned(ptr)) return FAILURE;

  // get block header from payload pointer
  b = (void*)ptr - sizeof(blockHeader);
  ptr = NULL;              // prevent usage of ptr after this line
  blockSize = getSize(b);  // size of free block
  // current block is where prev search finished
  bool isLastSeached = lastSearched == b;

  // validate range block: outside of the heap space allocated for the process
  // between first possible header and last, taking into account alignment
  // requirements
  void* endBlock = (void*)heapStart + allocsize;  // End Mark block header
  if (b < heapStart || (void*)b > endBlock - DOUBLE_WORD - sizeof(blockHeader))
    return FAILURE;
  // validate status: block is already freed (points to a free block)
  if (!isAllocated(b)) return FAILURE;

  // coalesce adjacent blocks
  bool isPointerUpdated = immediateCoalescingPolicy(&blockSize, &b) == 1;

  // update search starting block, if it is the one freed
  if (isLastSeached && isPointerUpdated) lastSearched = b;

  // update block information
  bool prevAllocated = isPreviousAllocated(b);
  b->size_status = blockSize;        // with status flags unset.
  if (prevAllocated) togglePBit(b);  // set p-bit to initial state
  // add footer
  footer = (void*)b + (blockSize - sizeof(blockFooter));
  footer->size_status = blockSize;

  return SUCCESS;
}

/*
 * Function used to initialize the memory allocator (memory-mapped segment being
 * used to simulate the heap segment). Intended to be called ONLY once by a
 * program. 
 * 
 * sizeOfRegion: the size of the heap space to be allocated.
 * 0 on success.
 * -1 on failure.
 */
int myInit(int sizeOfRegion) {
  static int allocated_once = 0;  // prevent multiple myInit calls

  int pagesize;  // page size
  int padsize;   // size of p\adding when heap size not a multiple of page size
  void* mmap_ptr;  // pointer to memory mapped area
  int fd;

  blockHeader* endMark;

  if (0 != allocated_once) {
    fprintf(
        stderr,
        "Error:mem.c: InitHeap has allocated space during a previous call\n");
    return -1;
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
  heapStart = (blockHeader*)mmap_ptr + 1;

  // Set the end mark
  endMark = (blockHeader*)((void*)heapStart + allocsize);
  endMark->size_status = 1;

  // Set size in header
  heapStart->size_status = allocsize;

  // Set p-bit as allocated in header
  // note a-bit left at 0 for free
  heapStart->size_status += 2;

  // Set the footer
  blockHeader* footer = (blockHeader*)((void*)heapStart + allocsize - 4);
  footer->size_status = allocsize;

  return 0;
}

/*
 * Function to be used for if(DEBUG)GING to help you visualize your heap
 * structure. Prints out a list of all the blocks including this information:
 * No.      : serial number of the block
 * Status   : free/used (allocated)
 * Prev     : status of previous block free/used (allocated)
 * t_Begin  : address of the first byte in the block (where the header starts)
 * t_End    : address of the last byte in the block
 * t_Size   : size of the block as stored in the block header
 */
void dispMem() {
  int counter;
  char status[5];
  char p_status[5];
  char* t_begin = NULL;
  char* t_end = NULL;
  int t_size;
  int footer = -1;

  blockHeader* current = heapStart;
  counter = 1;

  int used_size = 0;
  int free_size = 0;
  int is_used = -1;

  fprintf(stdout,
          "************************************Block list*****************\n");
  fprintf(stdout, "No.\tStatus\tPrev\tt_Begin\t\tt_End\t\tfooter\t\tt_Size\n");
  fprintf(stdout,
          "---------------------------------------------------------------\n");

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
      blockHeader* footerBlock =
          (void*)current + getSize(current) - sizeof(blockHeader);
      footer = ((blockHeader*)footerBlock)->size_status;
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

    fprintf(stdout, "%d\t%s\t%s\t0x%08lx\t0x%08lx\t%d\t\t%d\n", counter, status,
            p_status, (unsigned long int)t_begin, (unsigned long int)t_end,
            footer, t_size);

    current = (blockHeader*)((char*)current + t_size);
    counter = counter + 1;
  }

  fprintf(stdout,
          "---------------------------------------------------------------\n");
  fprintf(stdout,
          "***************************************************************\n");
  fprintf(stdout, "Total used size = %d\n", used_size);
  fprintf(stdout, "Total free size = %d\n", free_size);
  fprintf(stdout, "Total size = %d\n", used_size + free_size);
  fprintf(stdout,
          "***************************************************************\n");
  fflush(stdout);

  return;
}

/*
 * verify double-word aligned pointer (for improving performance):
 * last hexadecimal digit should be 0 or 8 (multiple of 8)
 *
 * ptr: payload pointer to check alignment of
 * true if double-word aligned address, otherwise false.
 */
bool isDoubleWordAligned(void* ptr) {
  return ((unsigned int)(ptr) % DOUBLE_WORD) == 0;
}

/*
 * "next-fit" placement policy to chose a free block.
 *
 * blockSize: block size requested in allocation
 * ptr: pointer to free block found satisfying request block size
 * SUCCESS on found block, FAILURE when no block was found to satisfy request.
 */
int nextFitPlacementPolicy(int blockSize, blockHeader** ptr) {
  blockHeader* b = blockFirst(lastSearched);
  do {
    // loop through free blocks only, that have enough space
    if (!isAllocated(b) && getSize(b) >= blockSize) {
      *ptr = b;  // update pointer to chosen free block
      return SUCCESS;
    }
    b = blockNextWrapAround(b);
  } while (b != lastSearched);
  return FAILURE;  // no enough space found
}

/*
 * split block policy to divide too large chosen free block (to minimize
 * internal fragmentation). dealing with splinting - remainder of free block
 * should be at least 8 bytes in size.
 *
 * blockSize: block size requested for allocation
 * freeBlock: free block to split if possible and satisfies splinting
 * requirement SUCCESS on successful split of free block, otherwise FAILURE.
 */
int splitBlockPolicy(int* blockSize, blockHeader* freeBlock) {
  // validate block size: checking if split is possible
  if (*blockSize >= getSize(freeBlock))
    return FAILURE;  // skip if no free block remainder

  // validate free block remainder: should be at least 8 bytes in size.
  int remainderSize = getSize(freeBlock) - *blockSize;
  if (remainderSize < 8) {
    *blockSize = getSize(freeBlock);  // take up entire free block
    return FAILURE;
  }

  // remainder free block
  blockHeader* remainderBlock = (void*)freeBlock + *blockSize;
  // add header to remaining free block
  remainderBlock->size_status = remainderSize;
  togglePBit(remainderBlock);  // update p-bit
  // add footer
  blockHeader* footer =
      (void*)remainderBlock + (remainderSize - sizeof(blockHeader));
  footer->size_status = remainderSize;
  return SUCCESS;
}

/*
 * Immediate coalescing with adjacent free memory blocks, if one or both of the
 * adjacent neighbors are free.
 *
 * blockSize: size of the block, updated in case coalesced.
 * ptr: free block to be coalesced with neighboring free blocks
 * 1 if ptr pointer header changed (i.e. coalesced with previous free block)
 * 0 if ptr not changed after possible coalescing
 */
int immediateCoalescingPolicy(int* blockSize, blockHeader** ptr) {
  blockHeader *prev = NULL, *next = NULL;  // adjacent blocks
  // if next block is free
  next = blockNext(*ptr);
  if (next != NULL && !blockDone(next)) {
    if (!isAllocated(next))
      *blockSize += getSize(next);  // coalesce free next
    else  // update p-bit of block following coalesced part only if needed
      togglePBit(next);
  }
  // if prev block is free
  if ((prev = blockPrevFree(*ptr)) != NULL) {
    *blockSize += getSize(prev);
    *ptr = prev;
    return 1;
  };
  return 0;
}

/*
 * Iterator condition check
 *
 * b: block header as iterator's element
 * true if reached heap end, otherwise false.
 */
bool blockDone(blockHeader* b) { return b->size_status == END_MARK; }

/*
 * Initial iterator's element - get starting block to iterator/search list from
 *
 * lastSearched: previous block where searched finished from if any
 * first block to begin iteration from
 */
blockHeader* blockFirst(blockHeader* lastSearched) {
  // if no previously allocated block
  return lastSearched == NULL ? heapStart : lastSearched;
}

/*
 * Iterator's next block till heap's end - get the next block in the memory heap
 *
 * b: current block
 * next block pointer, or NULL if non.
 */
blockHeader* blockNext(blockHeader* b) {
  if (b == NULL || blockDone(b)) return NULL;  // if NULL or end mark
  return (void*)b + getSize(b);                // next block
}

/*
 * Iterator's next block with wrap around - get next block wrapping around the
 * end of heap
 *
 * b: current block
 * next block in iteration
 */
blockHeader* blockNextWrapAround(blockHeader* b) {
  blockHeader* next;
  // iterator codition check of current block and next block
  if (!blockDone(b) && !blockDone(next = blockNext(b))) return next;

  return (void*)heapStart;  // wrap-around if required
}

/*
 * Iterator's previous block - get the preceding block in the memory heap
 *
 * b: current block
 * previous block pointer, or NULL if non (reached heap start).
 */
blockHeader* blockPrevFree(blockHeader* b) {
  if (b == heapStart || isPreviousAllocated(b)) return NULL;
  blockFooter* footer = (void*)b - sizeof(blockFooter);  // prev footer block
  return (void*)b - footer->size_status;  // pointer to the preceding block
}

/*
 * Check if bit set at a particular position
 *
 * i: binary number to examine
 * p: position of bit to check (starting from 1)
 * true if bit is set, false if unset.
 */
bool isBitAtPositionSet(int* i, int p) {
  return (*i & (1 << (p - 1))) ? true : false;
}

/*
 * Toggle bit at a particular position.
 *
 * i: binary number to manipulate
 * p: position of bit to set/unset (starting from 1)
 */
void toggleBitAtPosition(int* i, int p) { *i = *i ^ (1 << (p - 1)); }

/*
 * Unset all bits to particular position
 *
 * i: binary number
 * p: position of bit to unset (starting from 1)
 * updated number with updated bits
 */
int unsetBitsTillPosition(int* i, int p) { return *i >> p << p; }

/*
 * Check if a block is allocated (using a-bit flag of header).
 *
 * b: block header to check
 * true if block allocated, otherwise false.
 */
bool isAllocated(blockHeader* b) {
  return isBitAtPositionSet(&b->size_status, 1);
}

/*
 * Check if preceding block is allocated or not (using p-bit flag).
 *
 * b: block header to check
 */
bool isPreviousAllocated(blockHeader* b) {
  return isBitAtPositionSet(&b->size_status, 2);
}

/*
 * Toggle current block allocation status (a-bit flag).
 *
 * b: block header to manipulate
 */
void toggleABit(blockHeader* b) { toggleBitAtPosition(&b->size_status, 1); }

/*
 * Toggle previous block allocation status (p-bit flag).
 *
 * b: block header to manipulate
 */
void togglePBit(blockHeader* b) { toggleBitAtPosition(&b->size_status, 2); }

/*
 * Extract size of block from size_status header block information
 *
 * b: block header to examine
 * size of the memory block
 */
int getSize(blockHeader* b) {
  return unsetBitsTillPosition(&b->size_status, 2);
}

/*
 * Calculate padding if needed from block payload size
 * fullfilling requirement - total size of the allocated block must be a
 * multiple of 8
 *
 * payloadSize: size of payload block section
 * multiple: alignment size (corresponding to size processor's unit of data)
 * padding memory block size
 */
int getBlockPadding(int payloadSize, int multiple) {
  return multiple - (payloadSize + sizeof(blockHeader)) % multiple;
}

// end of myHeap.c (fall 2020)