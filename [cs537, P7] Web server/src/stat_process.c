#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/shm.h>
#include "./helper.h"
#include "./stat_process.h"
#include "./definition.h"

static long PAGESIZE;  // memory page size

/**
 * @brief display statistics on specific intervals from shared memory segement
 *
 * usage: `stat_process [shm_name] [sleeptime_ms] [num_threads]`
 *
 * @param argc args count
 * @param argv args values
 * @arg @param shm_name: shared memory object name to read from
 * @arg @param sleeptime_ms: sleep interval (milliseconds) between stat updates
 * @arg @param num_threads: # of worker threads on the server
 * @return int 0 on successful exit, otherwise 1;
 */
int main(int argc, char *argv[]) {
    // NOTE: platform specific call (4096)
    PAGESIZE = getpagesize();

    /** argumenrs handling **/
    int sleeptime_ms, num_threads;
    char *shm_name;  // shared memory name
    getargs(&sleeptime_ms, &num_threads, &shm_name, argc, argv);  // get args
    if (sleeptime_ms < 0 || num_threads < 1) goto fail;  // validate argument
    // validate parameters
    if (num_threads > 32) {  // exceeds supported worker number
        fprintf(stderr, "Error: # of threads argument exceeds max supported");
        goto fail;
    }

    static FileDescriptor shm_fd;  // shared memory segment file descriptor
    static slot_t *shm;            // pointer to shared memory segement
    int iterationCount = 0;

    printf("stat process started\n");

    printf("%s", shm_name);

    // map shared memory
    if ((shm_fd = shm_open(shm_name, O_RDONLY, 0660)) == -1) goto fail;
    void *shm_ptr = mmap(NULL, PAGESIZE, PROT_READ, MAP_SHARED, shm_fd, 0);
    if (shm_ptr == MAP_FAILED) goto fail;
    // cast into an array of slots
    shm = (slot_t *)shm_ptr;

read:

    if (msleep(sleeptime_ms) == -1) goto fail;  // sleep interval

    printf("\n\n%i", ++iterationCount);

    // read the shared statistics for every thread and print to stdout
    for (int i = 0; i < num_threads; i++) {
        slot_t s = shm[i];  // stat info for current thread
        printf("\n%li : %i %i %i", s.threadID, s.totalRequest, s.staticRequest,
               s.dynamicRequest);
        fflush(stdout);
    }

    goto read;

fail:
    printf("Error: occurred in stat process; %s", strerror(errno));
    return 1;
}

// millisecond wrapper for nanosleep
// taken from: https://qnaplus.com/c-program-to-sleep-in-milliseconds/
static int msleep(long tms) {
    struct timespec ts;
    int ret;

    if (tms < 0) {
        errno = EINVAL;
        return -1;
    }

    ts.tv_sec = tms / 1000;
    ts.tv_nsec = (tms % 1000) * 1000000;

    do {
        ret = nanosleep(&ts, &ts);
    } while (ret && errno == EINTR);

    return ret;
}

// Parse arguments
static void getargs(int *sleeptime_ms, int *num_threads, char **shm_name,
                    int argc, char *argv[]) {
    if (argc != 4) {
        fprintf(stderr, "Usage: %s <shm_name> <sleeptime_ms> <num_threads>\n",
                argv[0]);
        exit(1);
    }
    *shm_name = argv[1];
    *sleeptime_ms = atoi(argv[2]);
    *num_threads = atoi(argv[3]);
}
