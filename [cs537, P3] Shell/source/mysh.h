#ifndef __mysh_h
#define __mysh_h

#include <assert.h>
#include <getopt.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include "./message.h"

extern int errno;
extern char *optarg;
extern int optind, opterr, optopt;

// per line buffer size
#define LINE_SIZE 512
// # of supported tokens
#define TOKEN_SIZE 99

// program configuration parameters controlling input/output and behavior
struct Config {
  // program subroutines
  enum { INTERACTIVE, BATCH } functionality;
  // input & output params
  struct {
    FILE *input;  // command input stream
  } variable;
};

/* function definitions */
static void cliAdapter(int argc, char **argv, struct Config *config);
static void executeCommand(char *command, char **argument, FILE *sharedFile,
                           char *redirectFilename);
static int parse(char ***externalToken, char line[]);
static void executeStream(FILE *input, void (*f)(char *), int action);
static void batch(FILE *input);
static void prompt(FILE *input);
static void batchPrint(char *line);
static void promptPrint(char *line);
static int redirection(FILE *current, char *filename);
static int parseRedirection(char *line, char **filename);
static void alias(char **token, int tokenLength);
static void unalias(char **token, int tokenLength);

#endif  // __mysh_h
