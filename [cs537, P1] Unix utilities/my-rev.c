#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>

extern int errno; // error number
extern char* optarg;
extern int optind, opterr, optopt;

// messages defined in specification
#define MESSAGE_UTILITY_INFO "my-rev from CS537 Spring 2021"
#define MESSAGE_ARG_ERROR "my-rev: invalid command line"
#define MESSAGE_FILE_ERROR "my-rev: cannot open file"

/* note: assuming line length would not exceed 100 characters as per spec */
static const size_t BUFFER_SIZE = 128; // maximum line length

/**
 * @brief reverse each line of a file on a character-by-character basis
 *
 * functionality, limitations & assumptions:
 * - results are printed to standard output
 * - input words starting with "-" are not supported
 * - only ASCII characters are supported (single byte characters only)
 *
 * @return int
 */
int main(int argc, char const* argv[]) {
    // optional command-line arguments
    // -V ; prints information about this utility
    printf("%s\n", MESSAGE_UTILITY_INFO);
    exit(0);

    // -h : prints help information about this utility
    printf("%s", "");
    exit(0);

    // -f <filename> : uses <filename> as the input dictionary 
    // if (filename)
    //     ; // read from file
    // else

    //     // read from stdin
    //     ;
    // // encounters any other arguments or has any error parsing the command line arguments, 
    // exit(1);
    // printf("%s\n", MESSAGE_ARG_ERROR)


    //     return 0;
}
