/**
 * @file server.c
 * @brief runs a multithreaded simple server
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#ifndef __SERVER_H__

#include <pthread.h>
#include "./definition.h"

// work producer-consumer buffer holding file descriptors
struct ProducerConsumer {
  FileDescriptor *list;
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
  pthread_t list[THREADS_MAX];  // dynamically allocated list of threads
  int capacity;
  int size;
};

// user for argument passing
struct ThreadArgument {
  int threadIndex;
  struct ProducerConsumer *work;
};

static void sigintHandler(int signum);
static void initializeSHM(slot_t **shm, char *shm_name);
static void initializeWorker(struct ThreadPool *w, int capacity);
static void initializeWork(struct ProducerConsumer *w, int capacityLimit);
static void getargs(int *port, int *threads, int *buffers, char **shm_name,
                    int argc, char *argv[]);

#endif
