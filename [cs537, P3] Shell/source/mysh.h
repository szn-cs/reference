#ifndef __mysh_h
#define __mysh_h

extern int errno;
extern char *optarg;
extern int optind, opterr, optopt;

#define BUFFER_SIZE 512

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
void batch();
void prompt();

#endif  // __mysh_h
