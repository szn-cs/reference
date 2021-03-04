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

typedef struct AliasStruct {
  char *label;   // alias command label
  char **token;  // token array
  int length;    // # of elements in array
} AliasStruct;

struct AliasList {
  AliasStruct **pointer;  // store for alias tokes mappings
  int length;             // # of elements in the array
} aliasList;

/* function definitions */
static void cliAdapter(int argc, char **argv, struct Config *config);
static FILE *createFileDescriptor(char *filename);
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
static inline bool isWhitespaceString(char *s);
static inline bool isWhitespace(char c);
static inline void trim(char *s);
static void alias(char **token, int tokenLength);
static void unalias(char **token, int tokenLength);
static int findAlias(char *label, AliasStruct **external_alias);
static void addAlias(AliasStruct *a);
static void removeAlias(int index);
static void printAlias(AliasStruct *a);

#endif  // __mysh_h
