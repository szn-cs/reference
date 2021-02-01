#include <stdio.h>

// messages defined in specification
#define MESSAGE_UTILITY_INFO "my-look from CS537 Spring 2021"
#define MESSAGE_ARG_ERROR "my-look: invalid command line"
#define MESSAGE_FILE_ERROR "my-look: cannot open file"

/* Note: assuming input lines are a subset of those in /usr/share/dict/words (longest line ~30 chars) */
const int BUFFER_SIZE = 128; // maximum line length

/**
 * @brief find all lines that contain the input string as prefix
 *
 * functionality:
 * - search comparisons are case-insensitive
 * - results are printed to standard output
 *
 * limitations:
 * - input words starting with "-" are not supported
 *
 * @param argc
 * @param argv
 * @return int program exit code
 */
int main(int argc, char const* argv[]) {

    // argument options. if multiple arguments provided it will process the first argument only.
    getopt();
    // -V : prints information about this utility
    printf("%s\n", MESSAGE_UTILITY_INFO);
    exit(0);
    // -h : prints help information about this utility
    printf("%s", ""); // todo display information as you please
    exit(0);
    // -f <filename> : uses <filename> as the input dictionary 
    if (filename)
        ; // read from file
    else
        // read from stdin
        ;
    // encounters any other arguments or has any error parsing the command line arguments, 
    exit(1);
    printf("%s\n", MESSAGE_ARG_ERROR)


        // one required argument
        char* searchPrefix; //  the string to search for




    FILE* f = readInputFile(); // retrieve dictionary (list of words)
    char* buffer; // read-in buffer 
    fgets(); // read input line by line 

    if (strncasecmp())
        printf("%s", buffer)


        return 0;
}

/**
 * @brief print usage documentation
 *
 */
void documentation() {
    // usage: `my-look -f <filename> <string input>

}

FILE* readInputFile() {
    FILE* fp = fopen("main.c", "r");
    if (fp == NULL) {
        printf("%s\n", MESSAGE_FILE_ERROR);
        exit(1);
    }

    // pause function 

    fclose();
}

FILE* readStandardInput() {
    stdin
}