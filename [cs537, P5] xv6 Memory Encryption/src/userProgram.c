#include "types.h"
#include "user.h"
#include "ptentry.h"

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

    char *virtualAddress = (char *)0x2;  // 0x0, 0x1, 0x2
    mencrypt(virtualAddress, 2);

    exit();
}
