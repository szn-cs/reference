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
static void cliAdapter(int argc, char **argv, struct Config *config);
static FILE *createFileDescriptor(char *filename);
static void executeCommand(char **token, FILE *sharedFile);
static int parse(char ***externalToken, char line[]);
static void executeStream(FILE *input, void (*f)(char *), int action);
static void batch(FILE *input);
static void prompt(FILE *input);
static void batchPrint(char *line);
static void promptPrint(char *line);

#endif  // __mysh_h
