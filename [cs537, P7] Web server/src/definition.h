
#ifndef __DEFINITION_H__

typedef int FileDescriptor;

// maximum number of supported simultaneous connections
#define CONNECTION_MAX 32

// shared memory slots
// (NOTE: structure must fit within a single page & support
// CONNECTION_MAX connections e.g. 4096 / 32 = 128 byte per slot)
typedef struct {
  long threadID;      // thread id
  int totalRequest;   // total number of HTTP requests it has completed thus far
  int staticRequest;  // number of static requests
  int dynamicRequest;  // # of dynamic requests
} slot_t;

#endif
