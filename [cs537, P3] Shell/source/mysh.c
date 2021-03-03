/**
 * @file mysh.c
 * @brief basic shell implementation.
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <getopt.h>
#include <string.h>
#include <stdbool.h>
#include <sys/wait.h>
#include "./mysh.h"
#include "./message.h"

/**
 *  Shell / Command-line interpreter
 *
 * *functionality:*
 * [core]
 * → provides a prompt to receive & execute commands
 *
 * *feature:*
 * → output redirection
 * → aliasing
 * [execution mode]
 * → interactive: reads from standard input
 * → batch mode: reads commands from a file
 *
 * *supported behavior:*
 * → full paths are required to be specified for all commands (no relative
 * searches )
 * → long commands exceeding 512 characters are ignored
 *
 * *user interface:*
 * → command-line
 *      no argument: interactive mode
 *      `<batch file>` argument: batch mode
 *
 * @param argc command-line argument count
 * @param argv command-line argument value
 * @return int program exit code
 */
int main(int argc, char *argv[]) {
    // set default variables and program configuration parameters
    struct Config config = {INTERACTIVE, {.input = stdin}};

    // parse CLI arguments: update configuration of variables & execution
    // function
    cliAdapter(argc, argv, &config);

    // execute requested functionality
    switch (config.functionality) {
        case BATCH: {
            batch(config.variable.input);
        } break;
        case INTERACTIVE:
            prompt(config.variable.input);
            break;
        default:
            break;
    }

    // clean up
    if (fclose(config.variable.input) != 0) {
        perror("Error: while closing file. ");
        fflush(stderr);
        exit(1);
    }

    return 0;
}

/**
 * parse CLI arguments: handle associated input variables and update program
 * config
 *
 * @param argc cli count
 * @param argv pointer to cli value vector
 * @param config program's behavior configuration
 */
static void cliAdapter(int argc, char **argv, struct Config *config) {
    opterr = 0;      // suppress printing errors
    int nextOption;  // option key
    // parse option & non-option arguments:
    while ((nextOption = getopt(argc, argv, "")) != -1) {
        switch (nextOption) {
            case '\1':  // parse non-option arguments
            case '?':
            default:
                // error parsing, unknown character
                goto error_argument;
        }
    }

    if (argc == 1)  // single argument
        return;
    else if (argc == 2 && optind == 1)  // check for single input argument
        goto case_filename;

error_argument:  // incorrect command line arguments number
    fprintf(stderr, "%s", ERROR_ARGUMENTS);
    fflush(stderr);
    exit(1);

case_filename:
    config->functionality = BATCH;
    config->variable.input = createFileDescriptor(argv[1]);
    return;
}

/**
 * parse commands
 *
 * handle following scenarios which are not errors :
 *  - An empty command line.
 *  - White spaces include tabs and spaces.
 *  - Multiple white spaces on an otherwise empty command line.
 *  - Multiple white spaces between command-line arguments, including before
 *  the first command on a line and after the last command.
 *
 */
static int parse(char ***externalToken, char line[]) {
    char **token;                 // tokens array of input command
    char delimiters[] = " \n\t";  // token delimiters
    char *state = NULL;           // strtok_r reserve state

    // handles up to 99 tokens for each shell command, 100 including the
    // necessary NULL terminator.
    if ((token = malloc(sizeof(char *) * TOKEN_SIZE + 1)) == NULL) {
        perror("Error: allocating memory. ");
        fflush(stderr);
        exit(1);
    }
    for (int i = 0; i < TOKEN_SIZE; i++) token[i] = NULL;

    int i;  // token index
    char *t;
    t = strtok_r(line, delimiters, &state);
    for (i = 0; t != NULL && i < TOKEN_SIZE; i++) {
        token[i] = strdup(t);
        t = strtok_r(NULL, delimiters, &state);
    }

    // shrink allocated size instead of using up all TOKEN_SIZE of memory
    int e = i + 1;  // # of elements in token array
    if ((token = realloc(token, sizeof(char *) * e)) == NULL) {
        perror("Error: allocating memory. ");
        fflush(stderr);
        exit(1);
    }

    token[i] = NULL;  // set last token to null, marking the end of array

    *externalToken = token;  // modify external input
    return i;                // return number of valid tokens excluding NULL
}

/**
 * execute single command
 *
 */
static void executeCommand(char **token, FILE *sharedFile) {
    int forkResult;
    if ((forkResult = fork()) == -1)  // check fork status
        goto error_generic;
    else if (forkResult == 0)
        goto process_child;

    int childPid, statusPtr;
    childPid = forkResult;
    // wait for child process
    if (waitpid(childPid, &statusPtr, 0) == -1) goto error_generic;
    // if error exit code (e.g.command does not exist)
    if (WEXITSTATUS(statusPtr) != 0) goto error_noCommand;
    return;

process_child:
    // execute command with token arguments
    if (execv(token[0], token) == -1)  // if error occured in child process
        goto error_childProcess;
    else
        return;  // contiue processing

error_noCommand : {
    // command does not exist and cannot be executed
    fprintf(stderr, ERROR_COMMAND(token[0]));
    fflush(stderr);
    return;  // continue processing
}
error_generic : {
    perror("Error: ");
    fflush(stderr);
    exit(1);
}
error_childProcess : {
    /*
    solve infinite loop problem in parent caused by rewinding streams
    when calling exit() in child process: use _exit() OR fclose() to
    close the shared file with the parent (input commands file) in the
    child process before calling exit().
    */
    fclose(sharedFile);
    exit(1);
}
}

/**
 * read stream, parst lines, and execute each as a separate command
 *
 */
static void executeStream(FILE *input, void (*f)(char *), int action) {
    // extrabytes: 1 for null terminator + 1 for determining exceeding length
    const int BUFFER_SIZE = LINE_SIZE + 2;
    char line[BUFFER_SIZE];  // command string input line
    char **token;            // array of tokens
    int length;              // tokens array length

    if (action == 1) f(line);

    // read stream until it ends (EOF or reading error occurs)
    /* Note: a better function to use is "getline" which allows to
       distinguish between EOF and errors. */
    while (fgets(line, BUFFER_SIZE, input) != NULL) {
        // A very long command line (over 512 characters)
        if (strlen(line) > LINE_SIZE) {
            fprintf(stdout, "%s",
                    "warning: ignoring long command exceeding 512 characters");
            fflush(stdout);
            continue;
        }

        if (action == 2) f(line);

        length = parse(&token, line);

        if (length == 0) continue;  // no tokens parsed - ignoring line.
        // handle special commands:
        if (strcmp(token[0], "exit") == 0)
            goto end;  // terminate on exit command

        // execute current input command and wait for it to finish
        executeCommand(token, input);

        if (action == 1) f(line);
    }

end:
    // shell terminates when it sees the exit command on areaches the end of
    // the input stream (i.e., the end of the batch file or the user types
    // 'Ctrl-D')
    free(token);  // cleanup
    // handle non-error - Batch file ends without exit command
    return;
}

/**
 * prompt the shell creates a child process that executes the command you
 * entered and then prompts for more user input when it has finished.
 */
static void prompt(FILE *input) { executeStream(input, &promptPrint, 1); }
static void promptPrint(char *line) {
    // display PROMPT to stdout
    fprintf(stdout, PROMPT);
    fflush(stdout);
}

/**
 * reads each line of the batch-file for commands and executes them
 *
 * batch file: contains the list of commands (each on its own line)
 */
static void batch(FILE *input) { executeStream(input, &batchPrint, 2); }
static void batchPrint(char *line) {
    // echo line to be exected (If the line is empty or only composed of
    // whitespace, you should still echo it; if it is over the 512-character
    // limit then echo at least the first 512 characters.)
    fprintf(stdout, "%s", line);
    fflush(stdout);
}

/**
 * open file for reading with error handling
 *
 * @param filename name of the file to open
 * @return FILE* file descriptor / input stream
 */
static FILE *createFileDescriptor(char *filename) {
    FILE *s = NULL;
    // create requested file stream
    if ((s = fopen(filename, "r")) == NULL) goto fileError;
    return s;

fileError:
    // - batch file does not exist or cannot be opened
    fprintf(stderr, ERROR_FILE(filename));
    fflush(stderr);
    exit(1);
}