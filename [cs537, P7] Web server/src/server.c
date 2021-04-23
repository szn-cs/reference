#include <stdio.h>
#include <pthread.h>
#include "helper.h"
#include "request.h"
#include "server.h"

int listenfd, clientlen;  // webserver accept variables

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
// TODO: any passed arg must be freed/deallocated by thread when parent exits
static void *workerConsumer(void *arg) {
    struct ProducerConsumer *work = (struct ProducerConsumer *)arg;
    pthread_t t = pthread_self();
    printf("Consumer thread created: %lu\n", t);

consume:
    pthread_mutex_lock(&work->mutex);
    while (work->size == 0)  // case: full
        pthread_cond_wait(&work->fill_cv, &work->mutex);

    printf("Handling request by worker: %lu\n", t);
    FileDescriptor fd = get(work);

    pthread_cond_signal(&work->empty_cv);
    pthread_mutex_unlock(&work->mutex);

    requestHandle(fd);
    Close(fd);

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
 * @param argc args count
 * @param argv args values
 * @arg @param port port number that the web server should listen on
 * @arg @param threads # of worker threads that should be created
 * @arg @param buffers # of request connections can be accepted at one time
 * @arg @param shm_name name of shared memory object
 * @return int 0 on success, otherwise 1
 */
int main(int argc, char *argv[]) {
    struct ThreadPool worker;      // theads buffer
    struct ProducerConsumer work;  // connections file descriptors buffer

    /** argumenrs handling **/
    int port, threads, buffers;
    char *shm_name;
    getargs(&port, &threads, &buffers, &shm_name, argc, argv);  // get args
    if (threads < 1 || buffers < 1) goto fail;  // validate argument

    // initialize structures
    initializeWorker(&worker, threads);
    initializeWork(&work, buffers);

    // TODO: (Part B): Create & initialize the shared memory region...
    // will create <shm_name> at /dev/shm/  in CSL machines

    // create thread pool workers
    for (int i = 0; i < worker.capacity; ++i) {
        pthread_create(&worker.list[i], NULL, &workerConsumer, &work);
        worker.size++;
    }

    // start server on port
    listenfd = Open_listenfd(port);
    serverProducer(&work);  // recieve connections

    free(worker.list);  // TODO: should be handled in threads as main thread
                        // receives sigint

    return 0;
fail:
    return 1;
}

// initialize worker buffer data structure
void initializeWorker(struct ThreadPool *w, int capacity) {
    if ((w->list = calloc(capacity, sizeof(FileDescriptor))) == NULL) {
        fprintf(stderr, "Error: memory allocation failed");
        exit(1);
    }
    w->capacity = capacity;
    w->size = 0;
}

// initialize work buffer data structure
void initializeWork(struct ProducerConsumer *w, int capacityLimit) {
    if (capacityLimit > CONNECTION_MAX) {  // must not exceed maximum number
        fprintf(stderr, "Error: buffer argument exceeds max connections");
        exit(1);
    }

    w->capacity = capacityLimit;  // limit statically allocated buffer
    w->size = 0;
    w->fill_i = 0;
    w->use_i = 0;

    pthread_mutex_init(&w->mutex, NULL);
    pthread_cond_init(&w->fill_cv, NULL);
    pthread_cond_init(&w->empty_cv, NULL);
}

// Parse arguments
void getargs(int *port, int *threads, int *buffers, char **shm_name, int argc,
             char *argv[]) {
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
