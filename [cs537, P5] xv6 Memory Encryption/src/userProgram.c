#include "types.h"
#include "user.h"
#include "ptentry.h"

#define PGSIZE 4096

/**
 * @brief user program to test the encryption implementation of page addresses
 *
 * @param argc argument count
 * @param argv argument vector
 */
int main(int argc, char **argv) {
    // int y, x;

    // // verify commandline arguments
    // if (argc != 3) {
    //     printf(1, "Usage: userProgram <x> <y> ");
    //     exit();
    // }

    // // parse arguments
    // x = atoi(argv[1]);
    // y = atoi(argv[3]);
    char *ptr = sbrk(4000 * PGSIZE);

    mencrypt(ptr, 1);

    *ptr = 5;

    printf(1, "pte value: %d\n", *ptr);

    *ptr += 3;

    exit();
}
