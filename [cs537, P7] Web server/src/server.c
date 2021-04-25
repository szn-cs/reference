#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/shm.h>
#include "./helper.h"
#include "./request.h"
#include "./server.h"

static long PAGESIZE;  // memory page size

static int listenfd, clientlen;       // webserver accept variables
static struct ThreadPool worker;      // theads buffer
static struct ProducerConsumer work;  // connections file descriptors buffer
static slot_t *shm;                   // pointer to shared memory segement
static char *shm_name;                // shared memory name

// put work
void put(struct ProducerConsumer *buffer, FileDescriptor value) {
    buffer->list[buffer->fill_i] = value;
    buffer->fill_i = (buffer->fill_i + 1) % buffer->capacity;
    buffer->size++;
}

// get work
FileDescriptor get(struct ProducerConsumer *buffer) {
    FileDescriptor fd = buffer->list[buffer->use_i];
    buffer->use_i = (buffer->use_i + 1) % buffer->capacity;
    buffer->size--;
    return fd;
}

// consumer function
static void *workerConsumer(void *arg) {
    // extract arguments
    struct ProducerConsumer *work = ((struct ThreadArgument *)arg)->work;
    int index = ((struct ThreadArgument *)arg)->threadIndex;  // thread index
    free(arg);  // free allocated temporary argument structure memory

    pthread_t threadID = pthread_self();  // thread identifier
    // total number of HTTP requests, static count, dynamic count
    int count_totalRequest = 0, count_staticRequest = 0,
        count_dynamicRequest = 0;

    shm[index].threadID = threadID;  // write id to memory slot
    printf("Consumer thread created: %lu  slot index: %i\n", threadID, index);

consume : {
    pthread_mutex_lock(&work->mutex);
    while (work->size == 0)  // case: full
        pthread_cond_wait(&work->fill_cv, &work->mutex);

    printf("Handling request by worker: %lu\n", threadID);
    FileDescriptor fd = get(work);

    pthread_cond_signal(&work->empty_cv);
    pthread_mutex_unlock(&work->mutex);

    // handle request
    (requestHandle(fd) == 1) ? count_staticRequest++ : count_dynamicRequest++;
    count_totalRequest++;
    Close(fd);

    // update statistics on shared memory
    shm[index].totalRequest = count_totalRequest;
    shm[index].staticRequest = count_staticRequest;
    shm[index].dynamicRequest = count_dynamicRequest;
}

    goto consume;

    return NULL;  // no information needed to be passed to main thread.
}

// producer function
static void serverProducer(struct ProducerConsumer *work) {
    FileDescriptor fd;
    struct sockaddr_in clientaddr;

// accept connections (blocking call) and fill buffer
produce:
    clientlen = sizeof(clientaddr);

    // accept connection and get file descriptor
    fd = Accept(listenfd, (SA *)&clientaddr, (socklen_t *)&clientlen);
    printf("Accepted request\n");

    pthread_mutex_lock(&work->mutex);
    while (work->size == work->capacity)  // case: full
        pthread_cond_wait(&work->empty_cv, &work->mutex);

    put(work, fd);

    pthread_cond_signal(&work->fill_cv);
    pthread_mutex_unlock(&work->mutex);

    goto produce;
}

/**
 * @brief  A very, very simple web server. Repeatedly handles HTTP requests sent
 * to this port number. Most of the work is done within routines written in
 * request.c
 *
 * usage: `server [port] [threads] [buffers] [shm_name]`
 *      client requests:
 *      - `client localhost 5003 /home.html`
 *      - `client localhost 5003 /output.cgi?2`
 *
 * Terminating server should be done using SIGINT signal (with Ctrl-C)
 *
 * @param argc args count
 * @param argv args values
 * @arg @param port port number that the web server should listen on
 * @arg @param threads # of worker threads that should be created
 * @arg @param buffers # of request connections can be accepted at one time
 * @arg @param shm_name name of shared memory object
 * @return int 0 on success, otherwise 1
 */
int main(int argc, char *argv[]) {
    // NOTE: platform specific call (4096)
    PAGESIZE = getpagesize();

    // register signal handler
    if (signal(SIGINT, &sigintHandler) == SIG_ERR) {
        fprintf(stderr, "Error: failed to register SIGINT handler");
        exit(1);
    }

    /** argumenrs handling **/
    int port, threads, buffers;
    getargs(&port, &threads, &buffers, &shm_name, argc, argv);  // get args
    if (threads < 1 || buffers < 1 || port < 1) goto fail;  // validate argument

    // initialize shared memory segment
    initializeSHM(&shm, shm_name);
    // initialize structures
    initializeWorker(&worker, threads);
    initializeWork(&work, buffers);

    // create thread pool workers
    for (int i = 0; i < worker.capacity; ++i) {
        // NOTE: temporary allocated structure is freed in the thread
        struct ThreadArgument *arg = calloc(1, sizeof(struct ThreadArgument));
        arg->threadIndex = i;
        arg->work = &work;
        pthread_create(&worker.list[i], NULL, &workerConsumer, arg);
        worker.size++;
    }

    // start server on port
    listenfd = Open_listenfd(port);
    serverProducer(&work);  // recieve connections

    return 0;
fail:
    return 1;
}

// clean up structures & shared memory
static void sigintHandler(int signum) {
    free(work.list);

    if (munmap((void *)shm, PAGESIZE) == -1) goto fail;

    if (shm_unlink(shm_name)) goto fail;

    exit(0);
fail:
    fprintf(stderr, "Error: failed to cleanup shared memory");
    exit(1);
}

// initialize shared memory segement
static void initializeSHM(slot_t **shm, char *shm_name) {
    static FileDescriptor shm_fd;  // shared memory segment file descriptor

    // create & initialize the shared memory region
    // (NOTE: will create <shm_name> at /dev/shm/  in CSL machines)
    // O_RDWR | O_CREAT - readable & writable and will be created if not
    // exists
    if ((shm_fd = shm_open(shm_name, O_RDWR | O_CREAT, 0660)) == -1) goto fail;

    // set size of shared segment and clear it
    if (ftruncate(shm_fd, (int)PAGESIZE) == -1) goto fail;

    // map shared memory into user address space [shm_ptr, shm_ptr + PAGESIZE)
    // (MAP_SHARED: allows sharing with other processes)
    void *shm_ptr = mmap(NULL, (int)PAGESIZE, PROT_READ | PROT_WRITE,
                         MAP_SHARED, shm_fd, 0);
    if (shm_ptr == MAP_FAILED) goto fail;

    // cast into an array of slots
    *shm = (slot_t *)shm_ptr;

    return;
fail:
    fprintf(stderr, "Error: shared memory page cannot be exclusively created");
    exit(1);
}

// initialize work buffer data structure
static void initializeWork(struct ProducerConsumer *w, int capacity) {
    // if (capacity > CONNECTION_MAX) {  // exceeds supported worker number
    //     fprintf(stderr, "Error: # of threads argument exceeds max
    //     supported"); exit(1);
    // }

    if ((w->list = calloc(capacity, sizeof(FileDescriptor))) == NULL) {
        fprintf(stderr, "Error: memory allocation failed");
        exit(1);
    }
    w->capacity = capacity;
    w->size = 0;
    w->fill_i = 0;
    w->use_i = 0;

    pthread_mutex_init(&w->mutex, NULL);
    pthread_cond_init(&w->fill_cv, NULL);
    pthread_cond_init(&w->empty_cv, NULL);
}

// initialize worker buffer data structure
static void initializeWorker(struct ThreadPool *w, int capacityLimit) {
    if (capacityLimit > THREADS_MAX) {  // must not exceed maximum number
        fprintf(stderr, "Error: buffer argument exceeds max connections");
        exit(1);
    }

    w->capacity = capacityLimit;  // limit statically allocated buffer
    w->size = 0;
}

// Parse arguments
static void getargs(int *port, int *threads, int *buffers, char **shm_name,
                    int argc, char *argv[]) {
    if (argc != 5) {
        fprintf(stderr, "Usage: %s <port> <threads> <buffers> <shm_name>\n",
                argv[0]);
        exit(1);
    }
    *port = atoi(argv[1]);
    *threads = atoi(argv[2]);
    *buffers = atoi(argv[3]);
    *shm_name = argv[4];
}
