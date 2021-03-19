#include "types.h"
#include "stat.h"
#include "user.h"

/**
 * @brief runs large workload after sleeping for x ticks
 *
 * @param argc argument count
 * @param argv argument vector
 */
int main(int argc, char **argv) {
    int sleepT = 0;  // sleep ticks

    // verify commandline arguments
    if (argc != 2) {
        printf(1, "Usage: loop <# of sleep ticks>\n");
        exit();
    }

    sleepT = atoi(argv[1]);  // parse argument

    sleep(sleepT);  // sleep before doing work

    // loop on large workload
    int i = 0, j = 0;
    while (i < 800000000) {
        j += i * j + 1;
        i++;
    }

    exit();
}
