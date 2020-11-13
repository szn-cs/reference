
////////////////////////////////////////////////////////////////////////////////
//
// Copyright 2013,2019-2020, Jim Skrentny, (skrentny@cs.wisc.edu)
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission, CS354-Fall 2020, Deb Deppeler (deppeler@cs.wisc.edu)
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        csim.c
// This File:        csim.c
// Other Files:
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
//
////////////////////////////////////////////////////////////////////////////////

/**
 * csim.c:
 * A cache simulator that can replay traces (from Valgrind) and
 * output statistics for the number of hits, misses, and evictions.
 * The replacement policy is LRU.
 *
 * Implementation and assumptions:
 *  1. Each load/store can cause at most 1 cache miss plus a possible eviction.
 *  2. Instruction loads (I) are ignored.
 *  3. Data modify (M) is treated as a load followed by a store to the same
 *  address. Hence, an M operation can result in two cache hits, or a miss and a
 *  hit plus a possible eviction.
 */

#include <getopt.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include <limits.h>
#include <string.h>
#include <errno.h>
#include <stdbool.h>

/*****************************************************************************/
/* DO NOT MODIFY THESE VARIABLES *********************************************/

// Globals set by command line args.
int b = 0;  // number of block (b) bits
int s = 0;  // number of set (s) bits
int E = 0;  // number of lines per set

// Globals derived from command line args.
int B;  // block size in bytes: B = 2^b
int S;  // number of sets: S = 2^s

// Global counters to track cache statistics in access_data().
int hit_cnt = 0;
int miss_cnt = 0;
int evict_cnt = 0;

// Global to control trace output
int verbosity = 0;  // print trace if set
/*****************************************************************************/

const int ADDRESS_BITS = 64;  // m: # of bits in a memory address
int t;  // # bits required to represent all addresses that map to same set

// Type mem_addr_t: Use when dealing with addresses or address masks.
typedef unsigned long long int mem_addr_t;

// Type cache_line_t: Use when dealing with cache lines.
typedef struct cache_line {
  char valid;
  mem_addr_t tag;
  // tracking LRU policy:
  // Using the set of lines as an ordered list (in LRU order)
} cache_line_t;

// Type cache_set_t: Use when dealing with cache sets
// Note: Each set is a pointer to a heap array of one or more cache lines.
typedef cache_line_t* cache_set_t;
// Type cache_t: Use when dealing with the cache.
// Note: A cache is a pointer to a heap array of one or more sets.
typedef cache_set_t* cache_t;

// Create the cache (i.e., pointer var) we're simulating.
cache_t cache;

/**
 * Allocates the data structure for a cache with S sets and E lines per set.
 * Initializes all valid bits and tags with 0s.
 */
void init_cache() {
  // S: # of sets in cache, E: # of lines per set, B: # of bytes per block.
  S = pow(2, s);  // calculate sets
  B = pow(2, b);  // calculate bytes per block

  /* allocate the data structures using malloc() to hold information about the
   * sets and cache lines. */
  // allocate cache size of sets
  if ((cache = malloc(S * sizeof(cache_set_t))) == NULL) {
    fprintf(stderr, "Error: Allocation failed - %s\n", strerror(errno));
    exit(1);
  }
  // allocate sets lines
  for (int i = 0; i < S; i++) {
    if ((cache[i] = malloc(E * sizeof(cache_line_t))) == NULL) {
      fprintf(stderr, "Error: Allocation failed - %s\n", strerror(errno));
      exit(1);
    }
  }

  /* clear memory locations */
  for (int set = 0; set < S; set++) {
    for (int line = 0; line < E; line++) {
      cache[set][line].valid = 0;
      cache[set][line].tag = 0;
    }
  }
}

/**
 * Frees all heap allocated memory used by the cache (mainly allocation created
 * in init_cache() function)
 */
void free_cache() {
  // free nested arrays in cache data structures
  for (int set = 0; set < S; set++) {
    free(cache[set]);
    cache[set] = NULL;
  }
  free(cache);
  cache = NULL;
}

/**
 * Simulates data access at given "addr" memory address in the cache.
 * Uses data structures that were allocated in init_cache() function and
 * simulates and tracks the cache hits, misses and evictions. Implementing the
 * Least-Recently-Used (LRU) cache replacement policy.
 *
 * Approach used to tracking LRU caches - List: Move the most recently used
 * cache line to the head of the list. Thus, each set will point to the most
 * recently used cache line. Evict the tail of the list as the least recently
 * used.
 *
 * addr: target memory address to access (64-bit hexadecimal)
 *       addr composed of tag-bits, set-bits, block offset bits.
 */
void access_data(mem_addr_t addr) {
  mem_addr_t tag, set;  // tag and set portions of the address.

  // addr composed of: tag, set, block
  tag = addr >> (s + b);  // retrieve tag number
  // extract set portion using shifting
  set = addr << t >> (t + b);
  // extract set portion using address masking
  // set = (addr >> b) & (S - 1);

  if (verbosity) {
    printf("set: %llx ", set);
    printf("tag: %llx ", tag);
    // printf("m: %i ", t + s + b);
    for (int i = 0; i < E; i++) printf("v:%llx ", cache[set][i].valid);
  }

  // get occupied starting cache position
  int mostRecent = -1;  // first occupied line (most recently accessed line)
  while (++mostRecent < E && cache[set][mostRecent].valid == 0)
    ;

  // [CASE 1] If already in cache, increment hit_cnt
  // iterate over the cache
  for (int line = mostRecent; line < E; line++) {
    if (cache[set][line].valid && cache[set][line].tag == tag) {
      if (verbosity) printf("hit ");
      ++hit_cnt;

      // shift & move to the head (most-recently used) of list
      int shiftIndex = line - 1;
      while (cache[set][shiftIndex].valid == 1 && shiftIndex >= 0) {
        // move one spot right
        cache[set][shiftIndex + 1] = cache[set][shiftIndex];
        --shiftIndex;
      }
      // set head to accessed memory
      cache[set][mostRecent].tag = tag;

      if (verbosity) {
        printf("\e[33m [");
        for (int i = 0; i < E; i++)
          printf("%i|%llx, ", cache[set][i].valid, cache[set][i].tag);
        printf("] \e[0m");
      }

      return;
    }
  }

  /*
      NOTE: Unrequired for the traces provided
      // validate address set mapping
      if(!(set >= 0 && set < S)) // do not map to the current cache sets
          return;
          // printf("\n%i \n\n", set);
  */

  // [CASE 2] If not in cache, cache it (set tag), increment miss_cnt
  if (verbosity) printf("miss ");
  ++miss_cnt;
  // case there is a free line available in the cache
  if (mostRecent != 0) {  // if free line
    // printf("\n%i\n", mostRecent);
    int freeLine = mostRecent - 1;  // next free line in order
    cache[set][freeLine].tag = tag;
    cache[set][freeLine].valid = 1;
    return;
  }

  // [CASE 3] If a line is evicted, increment evict_cnt
  if (verbosity) printf("eviction ");
  ++evict_cnt;
  // shift & move to the head (most-recently used) of list
  int shiftIndex = E - 1;  // shift all elements to the right
  while (shiftIndex-- > 0)
    // move one spot right
    cache[set][shiftIndex + 1] = cache[set][shiftIndex];
  // set head to accessed memory
  cache[set][0].tag = tag;  // head at index 0 (most recent occupied)
  cache[set][0].valid = 1;
  for (int i = 0; i < E; i++) {
    printf("V:%llx ", cache[set][i].valid);
    printf("Tag:%llx ", cache[set][i].tag);
  }
}

/**
 * Replays the given trace file against the cache.
 *
 * Parses the input trace file, reading the input trace file line by line.
 * Extracts the type of each memory access : L/S/M
 * TRANSLATE each "L" as a load i.e. 1 memory access
 * TRANSLATE each "S" as a store i.e. 1 memory access
 * TRANSLATE each "M" as a load followed by a store i.e. 2 memory accesses
 *
 * assumption - the block size as determined by b will be greater than or equal
 * to that maximum size of the bytes accessed by operations in a trace.
 *
 * trace_fn: trace filename to parse as input insturctions
 */
void replay_trace(char* trace_fn) {
  char buf[1000];       // buf[1] has type of acccess(S/L/M)
  mem_addr_t addr = 0;  // addr has the address to be accessed
  unsigned int len = 0;
  FILE* trace_fp = fopen(trace_fn, "r");

  if (!trace_fp) {
    fprintf(stderr, "%s: %s\n", trace_fn, strerror(errno));
    exit(1);
  }

  while (fgets(buf, 1000, trace_fp) != NULL) {
    if (buf[1] == 'S' || buf[1] == 'L' || buf[1] == 'M') {
      sscanf(buf + 3, "%llx,%u", &addr, &len);

      if (verbosity) printf("%c %llx,%u ", buf[1], addr, len);

      // simulate cache access number of times depending on type of access
      switch (buf[1]) {
        // twice memory access
        case 'M':
          access_data(addr);
        // single memory access
        case 'L':
        case 'S':
          access_data(addr);
          break;
      }

      if (verbosity) printf("\n");
    }
  }

  fclose(trace_fp);
}

/**
 * Print information on how to use csim to standard output.
 * 
 * argv: command-line arguments
 */
void print_usage(char* argv[]) {
  printf("Usage: %s [-hv] -s <num> -E <num> -b <num> -t <file>\n", argv[0]);
  printf("Options:\n");
  printf("  -h         Print this help message.\n");
  printf("  -v         Optional verbose flag.\n");
  printf("  -s <num>   Number of s bits for set index.\n");
  printf("  -E <num>   Number of lines per set.\n");
  printf("  -b <num>   Number of b bits for block offsets.\n");
  printf("  -t <file>  Trace file.\n");
  printf("\nExamples:\n");
  printf("  linux>  %s -s 4 -E 1 -b 4 -t traces/yi.trace\n", argv[0]);
  printf("  linux>  %s -v -s 8 -E 2 -b 4 -t traces/yi.trace\n", argv[0]);
  exit(0);
}

/**
 * Prints a summary of the cache simulation statistics to a file.
 * 
 * hits: number of hits
 * misses: number of misses
 * evictions: number of evictions
 */
void print_summary(int hits, int misses, int evictions) {
  printf("hits:%d misses:%d evictions:%d\n", hits, misses, evictions);
  FILE* output_fp = fopen(".csim_results", "w");
  assert(output_fp);
  fprintf(output_fp, "%d %d %d\n", hits, misses, evictions);
  fclose(output_fp);
}

/**
 * Main parses command line args, makes the cache, replays the memory accesses,
 * free the cache and print the summary statistics.
 * 
 * argc: command-line argument count
 * argv: command-line argument value
 * program exit status
 */
int main(int argc, char* argv[]) {
  char* trace_file = NULL;
  char c;

  // Parse the command line arguments: -h, -v, -s, -E, -b, -t
  while ((c = getopt(argc, argv, "s:E:b:t:vh")) != -1) {
    switch (c) {
      case 'b':
        b = atoi(optarg);
        break;
      case 'E':
        E = atoi(optarg);
        break;
      case 'h':
        print_usage(argv);
        exit(0);
      case 's':
        s = atoi(optarg);
        break;
      case 't':
        trace_file = optarg;
        break;
      case 'v':
        verbosity = 1;
        break;
      default:
        print_usage(argv);
        exit(1);
    }
  }

  t = ADDRESS_BITS - s - b;  // caculate the t bits

  // Make sure that all required command line args were specified.
  if (s == 0 || E == 0 || b == 0 || trace_file == NULL) {
    printf("%s: Missing required command line argument\n", argv[0]);
    print_usage(argv);
    exit(1);
  }

  // Initialize cache.
  init_cache();

  // Replay the memory access trace.
  replay_trace(trace_file);

  // Free memory allocated for cache.
  free_cache();

  // Print the statistics to a file.
  // DO NOT REMOVE: This function must be called for test_csim to work.
  print_summary(hit_cnt, miss_cnt, evict_cnt);
  return 0;
}

// end csim.c
