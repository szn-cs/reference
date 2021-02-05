/**
 * @file my-rev.c
 * @brief a simplified implementation of 'rev' unix command.
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

extern int errno;
extern char *optarg;
extern int optind, opterr, optopt;

// messages defined in specification
#define MESSAGE_UTILITY_INFO "my-rev from CS537 Spring 2021"
#define MESSAGE_USAGE_INFO                        \
  "usage: `my-rev [<options>] [-f <filename>]`\n" \
  "\t-f   file path to manipulate contents of\n"  \
  "\t-V   information about utility\n"            \
  "\t-h   documentation information`"
#define MESSAGE_ARG_ERROR "my-rev: invalid command line"
#define MESSAGE_FILE_ERROR "my-rev: cannot open file"

/* note: assuming line length would not exceed 100 characters as per spec */
static const size_t BUFFER_SIZE = 128;  // maximum line length
// program configuration parameters controlling input/output and behavior
struct Config {
  // program subroutines
  enum { REVERSE, USAGE, INFO } functionality;
  // input & output params
  struct {
    FILE *target;  // dictionary input stream
    FILE *output;  // result output stream
  } variable;
};

/* function definitions */
static void cliAdapter(int argc, char ***argv, struct Config *param);
static void reverseLine(FILE *in, FILE *out);
static void reverseString(char *str);
static void printInformation();
static void printDocumentation();

/**
 * reverse each line of an input dictionary on a character-by-character basis
 *
 * *functionality:*
 * [core]
 * → reverse target lines
 *      [input variables]
 *      - dictionary input of lines
 *      [output result]
 *      - reversed string lines
 * [auxiliary]
 * → provide information about program
 * → provide usage documentation
 *
 * *supported behavior:*
 * [input data type/format]
 * → ASCII characters only
 * → words starting with "-" unsupported
 * [input source]
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
  struct Config config = {REVERSE, {stdin, stdout}};

  // parse CLI arguments: update configuration of variables & execution function
  cliAdapter(argc, &argv, &config);

  // execute requested functionality
  switch (config.functionality) {
    case REVERSE:
      reverseLine(config.variable.target, config.variable.output);
      break;
    case USAGE:
      printDocumentation();
      break;
    case INFO:
      printInformation();
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
  int nextOption;  // option key
  opterr = 0;      // suppress printing errors

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
        config->functionality = REVERSE;
        // create requested file stream
        if ((config->variable.target = fopen(optarg, "r")) == NULL)
          goto fileError;
        if (argc - optind != 0) goto argumentError;  // must be last argument
        return;
      case '\1':  // parse non-option arguments
      case '?':   // unknown argument
      default:
        // error parsing, unknown character, missing required argument
        goto argumentError;
    }
  }

  return;  // otherwise no arguments were parsed.

argumentError:
  printf("%s\n", MESSAGE_ARG_ERROR);
  exit(1);

fileError:
  printf("%s\n", MESSAGE_FILE_ERROR);
  exit(1);
}

/**
 * reverse each line in dictionary character-by-character, and print them out
 *
 * handling new lines by ignoring them when reversing
 *
 * @param target dictionary (list of words/lines) to reverse
 * @param output stream to print results
 */
static void reverseLine(FILE *target, FILE *output) {
  char buffer[BUFFER_SIZE];  // read-in buffer

  // read input line by line
  while (fgets(buffer, BUFFER_SIZE, target) != NULL) {
    bool suffixNewLine = false;  // should new line be added

    // if a single character (including newline) skip reverse
    if (strlen(buffer) < 2) goto print;

    // remove suffix new line
    if (buffer[strlen(buffer) - 1] == '\n') {
      suffixNewLine = true;
      buffer[strlen(buffer) - 1] = '\0';
    }

    reverseString(buffer);  // reverse string (excluded of new line character)

    // add back new line
    if (suffixNewLine) {
      buffer[strlen(buffer)] = '\n';
      buffer[strlen(buffer) + 1] = '\0';
    }

  print:
    fprintf(output, "%s", buffer);
  }
}

/**
 * character-wise reverse of a string
 *
 * @param str to be reversed character-by-character
 */
static void reverseString(char *str) {
  for (int l = 0, h = strlen(str) - 1; l < h; ++l, --h) {
    char temporary = str[l];
    str[l] = str[h];
    str[h] = temporary;
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
