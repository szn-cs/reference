#include <pthread.h>

#ifndef __SERVER_H__

typedef int FileDescriptor;

// maximum number of supported simultaneous connections
#define CONNECTION_MAX 32

// work producer-consumer buffer holding file descriptors
struct ProducerConsumer {
  FileDescriptor list[CONNECTION_MAX];
  int capacity;  // actual requested capacity
  int size;      // elements count
  int fill_i;    // fill pointer index
  int use_i;     // pointer to use index
  // synchronization primitives:
  pthread_mutex_t mutex;
  // for buf_not_full and buf_not_empty cases
  pthread_cond_t fill_cv, empty_cv;
};

// worker threads
struct ThreadPool {
  pthread_t *list;  // dynamically allocated list of threads
  int capacity;
  int size;
};

void initializeWorker(struct ThreadPool *w, int capacity);
void initializeWork(struct ProducerConsumer *w, int capacityLimit);
void getargs(int *port, int *threads, int *buffers, char **shm_name, int argc,
             char *argv[]);
#endif
