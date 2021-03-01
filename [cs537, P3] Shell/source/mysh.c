#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>

extern int errno;
extern char *optarg;
extern int optind, opterr, optopt;

#define PROMPT "mysh> "
#define ERROR_REDIRECTION "Redirection misformatted.\n"
#define ERROR_IO(FILENAME) "Cannot write to file " FILENAME ".\n"
#define ERROR_UNALIAS "unalias: Incorrect number of arguments.\n"
#define ERROR_ALIAS "alias: Too dangerous to alias that.\n"
#define ERROR_ARGUMENTS "Usage: mysh [batch-file]\n"
#define ERROR_FILE(FILENAME) "Error: Cannot open file " FILENAME ".\n"
#define ERROR_COMMAND(JOB) JOB ": Command not found.\n"

// program configuration parameters controlling input/output and behavior
struct Config {
    // program subroutines
    enum { INTERACTIVE, BATCH } functionality;
    // input & output params
    struct {
        char *filename;  // input filename
    } variable;
};

/* function definitions */
static void cliAdapter(int argc, char ***argv, struct Config *param);

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
    struct Config config = {INTERACTIVE, {}};

    // parse CLI arguments: update configuration of variables & execution
    // function
    cliAdapter(argc, &argv, &config);

    // execute requested functionality
    switch (config.functionality) {
        case BATCH:
            printf("batch");
            // batch();
            break;
        case INTERACTIVE:
            printf("prompt");
            // prompt();
            break;
        default:
            break;
    }

    /* IMPORTANT:
     - use write() to prevent intermingled output (instead of fprintf or
     pritnf). or use printf/fprintf calling fflush() immediately after each
     time.

     - fgets() reading lines of input, fopen(), perror() if errors occure with
     builtin routines,strtok() parsing commandline or strdup() for preserving
     input string (careful about memory leaks),
    */

    // for both modes: shell terminates when it sees the exit command on a line
    // or reaches the end of the input stream (i.e., the end of the batch file
    // or the user types 'Ctrl-D').

    // NOTE: We will not test the exit command with extra arguments.

    // program failures:

    // - batch file does not exist or cannot be opened
    char *batchName = "";
    // write(STDERR_FILENO, ERROR_FILE(batchName));
    exit(1);

    // - command does not exist and cannot be executed
    char *jobName = "";
    // write(STDERR_FILENO, ERROR_COMMAND(jobName));
    // continue processing

    // - A very long command line (over 512 characters)
    printf("warning: ignoring long command exceeding 512 characters");
    // continue processing

    // handle following scenarios which are not errors :
    // - An empty command line.
    // - White spaces include tabs and spaces.
    // - Multiple white spaces on an otherwise empty command line.
    // - Multiple white spaces between command-line arguments, including before
    // the first command on a line and after the last command.
    // - Batch file ends without exit command or user types 'Ctrl-D' as a
    // command in interactive mode.

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
static void cliAdapter(int argc, char ***argv, struct Config *config) {
    // short-circuit when arguments don't match required
    if (argc < 1 || argc > 2) goto error_argument;

    opterr = 0;      // suppress printing errors
    int nextOption;  // option key
    // parse option & non-option arguments:
    while ((nextOption = getopt(argc, *argv, "")) != -1) {
        switch (nextOption) {
            case '\1':  // parse non-option arguments
                config->functionality = BATCH;
                config->variable.filename = optarg;
                return;
            case '?':
            default:
                // error parsing, unknown character
                goto error_argument;
        }
    }

    if (optind == argc + 1) return;

error_argument:
    // - incorrect command line arguments number
    // write(STDERR_FILENO, ERROR_ARGUMENTS);
    exit(1);
}

/**
 *
 *
 */
void executeCommand() {
    /* IMPORTANT:
         - solve infinite loop problem in parent caused by rewinding streams
       when calling exit() in child process:
            1. use _exit().
            or
            2. use fclose() to close the shared file with the parent (input
       commands file) in the child process before calling exit().

        - You should be able to handle up to 99 tokens for each shell command,
       100 including the necessary NULL terminator.

        - executing commands: fork(), execv() must be used, and waitpid()

        - if execv() is successful, it will not return; if it does return, there
        was an error (e.g., the command does not exist). In this case, the child
        process should call _exit() to terminate itself. Contruct the argv array
        correctly with argv[3] = NULL;

     */
}

/**
 * prompt the shell creates a child process that executes the command you
 * entered and then prompts for more user input when it has finished.
 */
void prompt() {
    // no argument => interactive mode
    // 1. display PROMPT to stdout
    // 2. recieve typed in command (parse the input)
    //    & execute it and wait for it to finish.
    // repeated until user types exit or ends their input.

    // simple loop that waits for input
    // while () {
    // fork(), exec(), wait()
    // }
}

/**
 *
 *
 */
void batch() {
    // file name arguemnt => batch mode (read each line of the batch-file for
    // commands to be executed)
    // - batch file: contains the list of commands (each on its own line)
    // 1. echo line to be exected (If the line is empty or only composed of
    // whitespace, you should still echo it). (if it is over the 512-character
    // limit then echo at least the first 512 characters (but you may echo more
    // if you want)).
    // 2. execute command in the current line.
}

/**
 * send the output of a program to a file rather than to the screen
 *
 */
void redirection() {
    // redirect standard output to a file with ">" character
    // - example `/bin/ls -la /tmp > output` should not print anything to the
    // screen, instead output rereouted to file output.
    // - Note that standard error output should not be changed
    // -  If the output file exists before you run your program, you should
    // simply overwrite it (after truncating it, which sets the file's size to
    // zero bytes).
    // fork(), setup file descriptors, execv()
    // approach 1- close stdout, then open new file (which will take its place)
    // approach 2- use dup2(): 2 different file descriptors can point to the
    // same open file.

    // FOrmat of redirection:  a command (along with its arguments, if present),
    // followed by any number of white spaces (including none), the redirection
    // symbol >,  again any number of white space (including none), followed by
    // a filename.

    // errors: Multiple redirection operators (e.g.
    // /bin/ls > > file.txt ), starting with a redirection sign (e.g. > file.txt
    // ), multiple files to the right of the redirection sign (e.g. /bin/ls >
    // file1.txt file2.txt ), or not specifying an output file (e.g. /bin/ls >
    // )are all errors.
    printf("%s", ERROR_REDIRECTION);
    // do not execute command and continue to the next line.

    // errors: If if the output file cannot be opened for some reason (e.g.,
    // the user doesn't have write permission or the name is an existing
    // directory)
    char *filename = "";
    // printf("%s", ERROR_IO(filename));
    // do not execute command and continue to the next line.

    // Note: Do not worry about redirection for built-in commands (alias,
    // unalias, and exit); we will not test these cases.
}

/**
 * alias shortcuts for commands
 *
 * example: `mysh> alias ls /bin/ls`
 */
void alias() {
    // A built-in command means that the shell interprets this command directly;
    // the shell does not exec() the built-in command and run it as a separate
    // process; instead, the built-in command impacts how the shell itself runs.
    // data structure to store aliases: doubly-linked list

    // 3 ways to invoke:
    // 1. `alias <alias-name> <... replacement strigns>` if alias name already
    // exist, replace its value.
    // 2. `alias` should display all the aliases that have been set up (one per
    // line).
    char *aliasName = "", *replacementValue = "";
    printf("%s %s", aliasName,
           replacementValue /* each token separated by one space*/);
    // 3. `alias <alias name>` if word matches alias name, print it (alias and
    // replacement value). if word doesn't match ignore and continue.
}

void unalias() {
    // `unalias <alias name>` should remove alias from the list datastructure.
    // if alias dones't exist ignore and continue.

    // errors: if no argument or too many arguments to unalias, print error and
    // continue
    printf("%s", ERROR_UNALIAS);

    // Note: You do not need to worry about aliases to other aliases, aliases
    // that involve redirection, or redirection of aliases.

    // error: cannot be used as alias-names: alias, unalias, and exit. Should
    // print error and continue
    fprintf(stderr, "%s", ERROR_ALIAS);

    // Note:  additional arguments (e.g. ll -a where ll is an alias-name) is
    // undefined behavior. We will not test this. All alias calls will consist
    // of only the alias-name.
}