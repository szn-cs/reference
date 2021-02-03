#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>


extern int errno; // error number
extern char* optarg;
extern int optind, opterr, optopt;


#define DEBUG false // debug flag
// messages defined in specification
#define MESSAGE_UTILITY_INFO "my-look from CS537 Spring 2021"
#define MESSAGE_ARG_ERROR "my-look: invalid command line"
#define MESSAGE_FILE_ERROR "my-look: cannot open file"

/* note: assuming input lines are a subset of those in /usr/share/dict/words (longest line ~30 chars) */
static const int BUFFER_SIZE = 128; // maximum line length
static struct Config {
    enum { COMPARE, USAGE, INFO } functionality;
    struct {
        char* prefix; // input string to search for
        FILE* target; // input target stream
        FILE* output; // result output stream
    } variable;
};


/**
 * pick mode & associated variables
 *
 */
static void cliAdapter(int argc, char const* argv[], struct Config* config) {
    int nextOption;
    char* filename = NULL;
    opterr = 0; // suppress printing errors 

    // parse option arguments:
    // if multiple arguments provided it will process the first argument only.
    while ((nextOption = getopt(argc, argv, "-Vhf:")) != -1) {
        switch (nextOption) {
            case 'V':
                config->functionality = USAGE;
                return;
            case 'h':
                config->functionality = INFO;
                return;
            case 'f':
                config->functionality = COMPARE;
                if (config->variable.target = fopen(optarg, "r") == NULL)
                    goto fileError;
                break;
            case '\1':
                // parse non-option argument:
                config->variable.prefix = optarg;
                goto verifyArgumentCount;
            case '?':
            default:
                // error parsing, unknown character, missing required argument
                goto argumentError;
        }
    }

fileError:
    printf("%s\n", MESSAGE_FILE_ERROR);
    exit(1);
verifyArgumentCount:
    if (argc - optind == 0) // must be last argument
        return;
argumentError:
    printf("%s\n", MESSAGE_ARG_ERROR);
    exit(1);
}

/**
 * input data values:
 * - prefix string
 * - target string
 *
 * result value:
 * - result string
 *
 * limitations:
 * - search comparisons are case-insensitive
 *
 * @param string to search for
 * @param target dictionary (list of words)
 */
void findPrefixedLine(char* prefix, FILE* target) {
    char* buffer[BUFFER_SIZE]; // read-in buffer 
    size_t n = 0;

    // read input line by line 
    while (fgets(&buffer, BUFFER_SIZE, target) != NULL) {
        fprintf(stdout, "%s", buffer);
    }

    // fclose(target);
}

void compareStringPrefix(char* prefix, char* line) {
    // if (strncasecmp())
    //     printf("%s", buffer)

}


/**
 * @brief print utility information
 *
 */
void printInformation() {
    printf("%s\n", MESSAGE_UTILITY_INFO);
}

/**
 * @brief print usage documentation
 *
 */
void printDocumentation() {
    printf("%s\n%s\n%s\n", "usage: `my-look [-f <filename>] <string to search>`", "info: `my-look -h`", "doc: `my-look -V`");
}

/**
 * program objective: find all lines beginning with a prefix
 *
 * functionality:
 * [core]
 *  → compare target lines with prefix string
 *      [input variables]
 *          - search target string lines
 *          - prefix string
 *      [output result]
 *          - matching string lines
 * [auxiliary]
 *  → provide information about program
 *  → provide usage documentation
 *
 * supported behavior:
 * [comparison]
 *  → case insensitive only
 * [input data type/format]
 *  → ASCII characters only
 *  → words starting with "-" unsupported
 * [input source for search target variable]
 *  → standard input stream
 *  → file stream
 * [output result target]
 *  → standard output stream
 *
 * user interface:
 *  → command-line
 *      [option flags & values]
 *          -V prints utility information
 *          -h prints help usage documentation
 *          -f reads <filename> as the input dictionary stream
 *
 * @param argc
 * @param argv
 * @return int program exit code
 */
int main(int argc, char const* argv[]) {
    // set default variables and program configurations
    struct Config config = {
        COMPARE,
        {
            NULL,
            stdin,
            stdout
        }
    };

    // parse command-line arguments: set variables and choose mode
    cliAdapter(argc, argv, &config);

    // choose function to execute
    switch (config.functionality) {
        case COMPARE:
            findPrefixedLine(config.variable.prefix, config.variable.target);
            // TODO: check if closing stdin breaks program
            break;
        case USAGE:
            printDocumentation();
            break;
        case INFO:
            printInformation();
            break;
    }

    return 0;
}



