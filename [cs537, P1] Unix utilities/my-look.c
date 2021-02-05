/**
 * @file my-look.c
 * @brief a simplified implementation of 'look' unix command.
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

extern int errno;
extern char *optarg;
extern int optind, opterr, optopt;

// user-interface messages:
#define MESSAGE_UTILITY_INFO "my-look from CS537 Spring 2021"
#define MESSAGE_USAGE_INFO                                            \
  "usage: `my-look [<options>] [-f <filename>] <string to search>`\n" \
  "\t-f   file path of dictionary\n"                                  \
  "\t-V   information about utility\n"                                \
  "\t-h   documentation information`"
#define MESSAGE_ARG_ERROR "my-look: invalid command line"
#define MESSAGE_FILE_ERROR "my-look: cannot open file"

/* note: assuming input lines are a subset of those in
 * /usr/share/dict/words (longest line ~30 chars) */
static const size_t BUFFER_SIZE = 128;  // maximum line length
// program configuration parameters controlling input/output and behavior
struct Config {
  // program subroutines
  enum { COMPARE, USAGE, INFO } functionality;
  // input & output params
  struct {
    char *prefix;  // search string input
    FILE *target;  // dictionary input stream
    FILE *output;  // result output stream
  } variable;
};

/* function definitions */
static void cliAdapter(int argc, char ***argv, struct Config *param);
static void findPrefixedLine(char *str, FILE *in, FILE *out);
static void printInformation();
static void printDocumentation();

/**
 * find all dictionary lines beginning with a prefix string, & prints them to
 * the user
 *
 * *functionality:*
 * [core]
 * → compare target lines with prefix string
 *      [input variables]
 *      - search target string lines
 *      - prefix string
 *      [output result]
 *      - matching string lines
 * [auxiliary]
 * → provide information about program
 * → provide usage documentation
 *
 * *supported behavior:*
 * [comparison]
 * → case insensitive only
 * [input data type/format]
 * → ASCII characters only
 * → words starting with "-" unsupported
 * [input source for search target variable]
 * → standard input stream
 * → file stream
 * [output result target]
 * → standard output stream
 *
 * *user interface:*
 * → command-line
 *      [option flags & values]
 *      -V prints utility information
 *      -h prints help usage documentation
 *      -f reads <filename> as the input dictionary stream
 *
 * @param argc command-line argument count
 * @param argv command-line argument value
 * @return int program exit code
 */
int main(int argc, char *argv[]) {
  // set default variables and program configuration parameters
  struct Config config = {COMPARE, {NULL, stdin, stdout}};

  // parse CLI arguments: update configuration of variables & execution function
  cliAdapter(argc, &argv, &config);

  // execute requested functionality
  switch (config.functionality) {
    case USAGE:
      printDocumentation();
      break;
    case INFO:
      printInformation();
      break;
    case COMPARE:
    default:
      findPrefixedLine(config.variable.prefix, config.variable.target,
                       config.variable.output);
      break;
  }

  // clean up
  if (fclose(config.variable.target) != 0) {
    printf("my-look: cannot close file\n");
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
static void cliAdapter(int argc, char ***argv, struct Config *config) {
  if (argc <= 1)
    goto argumentError;  // short-circuit when no arguments (beside command)

  opterr = 0;          // suppress printing errors
  int nextOption;      // option key
  int repetition = 0;  // number of case repetition for 'f' key

  // parse option & non-option arguments:
  while ((nextOption = getopt(argc, *argv, "-Vhf:")) != -1) {
    switch (nextOption) {
      case 'V':
        config->functionality = INFO;
        return;
      case 'h':
        config->functionality = USAGE;
        return;
      case 'f':
        // create requested file stream
        if ((config->variable.target = fopen(optarg, "r")) == NULL)
          goto fileError;
        repetition++;
        break;
      case '\1':  // parse non-option arguments
        config->functionality = COMPARE;
        config->variable.prefix = optarg;
        // must be last argument and no previous option repititions
        if (argc - optind != 0 || repetition > 1) goto argumentError;
        return;
      case '?':
      default:
        // error parsing, unknown character, missing required argument
        goto argumentError;
    }
  }

argumentError:
  printf("%s\n", MESSAGE_ARG_ERROR);
  exit(1);

fileError:
  printf("%s\n", MESSAGE_FILE_ERROR);
  exit(1);
}

/**
 * compare (case-insensitive) each line in a stream to a prefix string, and
 * print those matching
 *
 * @param prefix string to search for
 * @param target dictionary (list of words)
 * @param output stream to print results
 */
static void findPrefixedLine(char *prefix, FILE *target, FILE *output) {
  char buffer[BUFFER_SIZE];  // read-in buffer

  // read input line by line
  while (fgets(buffer, BUFFER_SIZE, target) != NULL) {
    // check if line begins with prefix
    if (strncasecmp(prefix, buffer, strlen(prefix)) == 0) {
      char *saveptr;  // irrelevant (for repeated calls)
      char *token = strtok_r(buffer, "\n", &saveptr);  // remove new line
      fprintf(output, "%s\n", token);
    }
  }
}

/**
 * print utility information
 */
static void printInformation() { printf("%s\n", MESSAGE_UTILITY_INFO); }

/**
 * print usage documentation
 */
static void printDocumentation() { printf("%s\n", MESSAGE_USAGE_INFO); }
