#ifndef __mysh_h
#define __mysh_h

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
static void cliAdapter(int, char **, struct Config *);
static FILE *createFileDescriptor(char *);
static void executeCommand(char **);
static int parse(char ***, char *);
static void executeStream(FILE *, void (*f)(char *), int);
static void batch();
static void prompt();
static void batchPrint(char *);
static void promptPrint(char *);

#endif  // __mysh_h
