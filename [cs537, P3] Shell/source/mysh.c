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

    // NOTE: could have implemented doubly-linked list, but decided to use a
    // simple list.
    aliasList.pointer = NULL;  // initialize alias array
    aliasList.length = 0;

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
    for (int i = 0; i < aliasList.length; i++) {
        for (int j = 0; j < aliasList.pointer[i]->length; j++) {
            free(aliasList.pointer[i]->token[j]);
            aliasList.pointer[i]->token[j] = NULL;
        }
        free(aliasList.pointer[i]);
        aliasList.pointer[i] = NULL;
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
static void executeCommand(char *command, char **argument, FILE *sharedFile,
                           char *redirectFilename) {
    int forkResult;
    if ((forkResult = fork()) == -1)  // check fork status
        goto error_generic;
    else if (forkResult == 0)
        goto process_child;

    int childPid, statusPtr;
    childPid = forkResult;
    // wait for child process
    if (waitpid(childPid, &statusPtr, 0) == -1) goto error_generic;

    // if error exit code related to command (e.g. command does not exist)
    if (WIFEXITED(statusPtr) == 1 && WEXITSTATUS(statusPtr) == 1)
        goto error_noCommand;
    return;

process_child:
    // redirect output (standard error output is not changed)
    if (redirectFilename != NULL && redirection(stdout, redirectFilename) == -1)
        // do not execute command and continue to the next line.
        return;

    // execute command with token arguments
    if (execv(command, argument) == -1)  // if error occured in child process
        goto error_childProcess;
    else
        return;  // contiue processing other command

error_noCommand : {
    // command does not exist and cannot be executed
    fprintf(stderr, ERROR_COMMAND(command));
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
    char *command;           // command token
    char **argument;         // argument tokens
    // Note LINE_ZISE for filename exceeds the minimum required for the token
    char filename[LINE_SIZE];  // redirection filename
    char *redirectFilename =
        filename;            // used as modifiable pointer (instead using heap)
    AliasStruct *aliasData;  // alias corresponding to current command

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

        // parse redirection part delimited by '>', if it failed skip commmand
        if (parseRedirection(line, &redirectFilename) == -1) goto skip;

        // parse command line
        length = parse(&token, line);
        if (length == 0) goto skip;  // no tokens parsed

        command = token[0];
        argument = token;
        /* handle special commands: */
        // 'exit' command
        if (strcmp(command, "exit") == 0)
            goto end;  // terminate on exit command
        // 'alias' or 'unalias' commands
        if (strcmp(command, "alias") == 0) {
            alias(token, length);
            goto skip;
        }
        if (strcmp(command, "unalias") == 0) {
            unalias(token, length);
            goto skip;
        }
        if ((findAlias(command, &aliasData)) != -1) {
            command = aliasData->token[0];
            argument = aliasData->token;
        }

        // execute current input command and wait for it to finish
        executeCommand(command, argument, input, redirectFilename);
    skip:  // skip current command to last action
        if (action == 1) f(line);
    }

end:
    // shell terminates when it sees the exit command on areaches the end of
    // the input stream (i.e., the end of the batch file or the user types
    // 'Ctrl-D')
    free(token);  // cleanup
    token = NULL;
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

/**
 * redirect standard output to a file with ">" character, sending the output of
 * a program to a file rather than to the screen.
 *
 *
 * supported behavior:
 * - existing file gets truncated and overwritten
 */
static int redirection(FILE *current, char *filename) {
    // setup file descriptors approaches:
    // - close stdout, then open new file (which will take its place)
    // - use dup2(): 2 different file descriptors can point to the same open
    // file.
    if (fclose(current) == EOF) goto error_fileOpen;
    if (fopen(filename, "w") == NULL) goto error_fileClose;
    return 0;

error_fileOpen:
    perror("Error: ");
    fflush(stderr);
    return -1;  // signal failure

error_fileClose:
    // errors: output file cannot be opened for some reason (e.g., the user
    // doesn't have write permission or the name is an existing directory)
    fprintf(stdout, ERROR_IO(filename));
    fflush(stdout);
    return -1;  // signal failure
}

/**
 * Format of redirection:
 * command (with its arguments) → white spaces (including none) → redirection
 * symbol `>` → white space (including none) → filename.
 *
 * supported behavior
 * - unsupported redirection for built-in commands (alias, unalias, and exit)
 * - NOTE: extenal variable should be located on the same line variable stack
 */
static int parseRedirection(char *line, char **externalFilename) {
    char delimiter[] = ">";  // token delimiters
    char *state = NULL;      // strtok_r reserve state
    char *token = NULL;      // token part

    // check for consectutive delimiter
    // NOTE: strsep instead of strtok can be used to detect repeated delimiters
    for (int i = 1; i < strlen(line); i++)
        if (line[i] == '>' && line[i - 1] == line[i]) goto error_parsing;
    // line composed of only whitespace chars
    if (isWhitespaceString(line)) return -1;

    // part before '>'
    // NOTE: strtok modifies the original string to the first token
    token = strtok_r(line, delimiter, &state);  // part before '>' character
    trim(token);
    // check if beginning with '>' character or preceded by whitespace chars
    if (strlen(token) == 0 || line[0] == '>') goto error_parsing;

    // part after '>' character
    if ((token = strtok_r(NULL, delimiter, &state)) == NULL)
        goto no_redirection;
    trim(token);  // (ignoring start & end whitespace)
    // check if '>' character at the end
    if (strlen(token) == 0) goto error_parsing;
    // validate filename token (no whitespace in-between)
    for (int i = 0; i < strlen(token); i++)
        if (isWhitespace(token[i])) goto error_parsing;

    // if another part exists (more than one '>' character)
    if (strtok_r(NULL, delimiter, &state) != NULL) goto error_parsing;

    *externalFilename = token;
    return 0;

no_redirection:
    *externalFilename = NULL;
    return 0;

error_parsing:
    // errors: Multiple redirection operators (e.g. /bin/ls > > file.txt ),
    // starting with a redirection sign (e.g. > file.txt ), multiple files
    // to the right of the redirection sign (e.g. /bin/ls > file1.txt
    // file2.txt ), or not specifying an output file (e.g.     /bin/ls >
    // )are all errors.
    fprintf(stderr, "%s", ERROR_REDIRECTION);
    fflush(stderr);
    return -1;
    // do not execute command and continue to the next line.
}

static inline bool isWhitespaceString(char *s) {
    for (int i = 0; i < strlen(s); i++)
        if (!isWhitespace(s[i])) return false;
    return true;
}

static inline bool isWhitespace(char c) {
    char whitespace[] = " \n\t";
    for (int i = 0; i < strlen(whitespace); i++)
        if (c == whitespace[i]) return true;
    return false;
}

/**
 * trim string of whitespace
 * (modified from
 * https://stackoverflow.com/questions/122616/how-do-i-trim-leading-trailing-whitespace-in-a-standard-way)
 *
 * @param s string to be trimmed
 */
static inline void trim(char *s) {
    if (s == NULL) return;  // shortcircuit
    char *p = s;
    int l = strlen(p);
    while (isWhitespace(p[l - 1])) p[--l] = 0;
    while (*p && isWhitespace(*p)) ++p, --l;
    memmove(s, p, l + 1);
}

/**
 * alias shortcuts for commands (built-in command means that the shell
 * interprets this command directly)
 * usage: `mysh> alias ls /bin/ls`
 *
 * supported behavior:
 * → unsupported - aliases to other aliases, aliases that involve redirection,
 * or redirection of aliases
 * → additional arguments (e.g. ll -a where ll is an alias-name) is undefined
 * behavior. All alias calls must consist of only the alias-name.
 * - stores alias in a global data structure.
 */
static void alias(char **token, int tokenLength) {
    AliasStruct *a = NULL;  // target alias to handle
    char *label = token[1];
    int aliasArgument = tokenLength - 2;  // # of tokens after 'alias' & label

    switch (tokenLength) {
        case 1:
            goto case_displayAll;
        case 2:
            goto case_displaySingle;
        default:
            goto case_alias;
    }

case_alias : {
    // handle special labels
    if (strcmp(label, "alias") == 0 || strcmp(label, "unalias") == 0 ||
        strcmp(label, "exit") == 0)
        goto error_specialAlias;

    // `alias <alias-name> <... replacement strigns>` if alias name already
    // exist, replace its value.
    if (findAlias(label, &a) == -1) {  // if not found
        // create alias structure
        a = calloc(1, sizeof(AliasStruct));
        a->label = strdup(label);  // set label
        addAlias(a);
    } else {
        // free alias tokens
        for (int i = 0; i < a->length; i++) {
            free(a->token[i]);
            a->token[i] = NULL;
        }
        a->length = 0;
    }

    // allocate memory for tokens including NULL terminator
    a->token = calloc(aliasArgument + 1, sizeof(char *));
    // copy argument tokens over the alias structure
    for (int i = 0; i < aliasArgument; i++) a->token[i] = strdup(token[i + 2]);
    a->length = aliasArgument + 1;  // arguments + Null terminator

    return;
}

case_displayAll : {
    // `alias` should display all the aliases that have been set up (one per
    // line).
    for (int i = 0; i < aliasList.length; i++) printAlias(aliasList.pointer[i]);
    return;
}

case_displaySingle:
    // `alias <alias name>` if word matches alias name, print it (alias and
    // replacement value).
    if ((findAlias(label, &a) != -1)) printAlias(a);
    return;  // continue.

error_specialAlias:
    // error: cannot be used as alias-names: alias, unalias, and exit.
    fprintf(stderr, "%s", ERROR_ALIAS);
    fflush(stderr);
    return;  // continue
}

static void printAlias(AliasStruct *a) {
    fprintf(stdout, "%s ", a->label);
    /* print each token separated by one space, and excluding null terminator */
    for (int j = 0; j < a->length - 1; j++)
        fprintf(stdout, "%s%s", a->token[j], (j < a->length - 2) ? " " : "");
    fprintf(stdout, "\n");
    fflush(stdout);
}

static int findAlias(char *label, AliasStruct **external_alias) {
    for (int i = 0; i < aliasList.length; i++)
        if (strcmp(aliasList.pointer[i]->label, label) == 0) {
            *external_alias = aliasList.pointer[i];
            return i;
        }
    return -1;
}

static void addAlias(AliasStruct *a) {
    // add object to global alias list
    if ((aliasList.pointer =
             realloc(aliasList.pointer,
                     sizeof(AliasStruct *) * (++aliasList.length))) == NULL) {
        perror("Error: allocating memory. ");
        fflush(stderr);
        exit(1);
    }
    aliasList.pointer[aliasList.length - 1] = a;
}

static void removeAlias(int index) {
    // free element target index
    for (int i = 0; i < aliasList.pointer[index]->length; i++) {
        free(aliasList.pointer[index]->token[i]);
        aliasList.pointer[index]->token[i] = NULL;
    }
    free(aliasList.pointer[index]->label);
    free(aliasList.pointer[index]);
    aliasList.pointer[index] = NULL;

    // shift all elements one position in the array to the left
    for (int i = index; i + 1 < aliasList.length; i++)
        aliasList.pointer[i] = aliasList.pointer[i + 1];

    --aliasList.length;  // decrement list elements number
}

/**
 * remove alias from the list data structure
 * usage: `unalias <alias name>`
 *
 */
static void unalias(char **token, int tokenLength) {
    AliasStruct *a = NULL;  // target alias to handle
    char *label = token[1];

    if (tokenLength == 2)
        goto case_unalias;
    else
        goto error_argument;

case_unalias : {
    int index;
    // if alias doesn't exist ignore.
    if (((index = findAlias(label, &a)) != -1)) removeAlias(index);
    return;  // continue
}

error_argument:
    // errors: if no argument or too many arguments to unalias
    fprintf(stderr, "%s", ERROR_UNALIAS);
    fflush(stderr);
    return;  // continue
}
