////////////////////////////////////////////////////////////////////////////////
//
// Copyright 2013,2019-2020, Jim Skrentny, (skrentny@cs.wisc.edu)
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission, CS354-Fall 2020, Deb Deppeler (deppeler@cs.wisc.edu)
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        division.c
// This File:        division.c
// Other Files:
// Semester:         CS 354 Fall 2020
//
// Author:           Safi Nassar
// Email:            nassar2@wisc.edu
// CS Login:         safi@cs.wisc.edu
//
/////////////////////////// OTHER SOURCES OF HELP //////////////////////////////
//
// Persons:
//
// Online sources:
//
////////////////////////////////////////////////////////////////////////////////

#include <assert.h>
#include <errno.h>
#include <signal.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <time.h>
#include <unistd.h>

// input buffer size
#define BUFFER_SIZE 100

// total count of how many division operations were successfully completed
int divisionOp = 0;

void promptInput(int *, int *);
void registerHandler();
/**
 * signal handlers
 */
void handleArithmeticSignal(int);
void handleInterrupt(int);

/**
 * Main registers handlers, makes calculation on user input integers
 *
 * argc: command-line argument count
 * argv: command-line argument value
 * program exit status
 */
int main(int argc, char *argv[]) {
  int int1, int2;  // input values

  registerHandler();  // register handlers

  while (1) {
    promptInput(&int1, &int2);

    // Calculate the quotient and remainder of input numbers division
    int quotient = int1 / int2;
    int remainder = int1 % int2;

    printf("%i / %i is %i with a remainder of %i\n", int1, int2, quotient,
           remainder);

    divisionOp++;
  }
}

/**
 * prompt for user input and parse integers
 *
 * int1: first user input target
 * int2: second user input target
 */
void promptInput(int *int1, int *int2) {
  // get first integer
  char *str = malloc(BUFFER_SIZE);
  printf("Enter first integer: ");
  if (fgets(str, BUFFER_SIZE, stdin) == NULL) {
    fprintf(stderr, "Could not read input. %s\n", strerror(errno));
    exit(1);
  }
  *int1 = atoi(str);

  // get second integer
  printf("Enter second integer: ");
  if (fgets(str, BUFFER_SIZE, stdin) == NULL) {
    fprintf(stderr, "Could not read input. %s\n", strerror(errno));
    exit(1);
  }
  *int2 = atoi(str);
}

/**
 * Register signal handlers to corresponding signal types
 */
void registerHandler() {
  struct sigaction act1;           // signal action struct
  memset(&act1, 0, sizeof(act1));  // clear memory
  act1.sa_handler = &handleArithmeticSignal;
  // register signal
  if (sigaction(SIGFPE, &act1, NULL) == -1) {
    fprintf(stderr, "Could not register signal handler. %s\n", strerror(errno));
    exit(1);
  };

  struct sigaction act2;           // signal action struct
  memset(&act2, 0, sizeof(act2));  // clear memory
  act2.sa_handler = &handleInterrupt;
  // register signal
  if (sigaction(SIGINT, &act2, NULL) == -1) {
    fprintf(stderr, "Could not register signal handler. %s\n", strerror(errno));
    exit(1);
  };
}

/**
 * Arithmetic signal handler (division by zero)
 *
 * signal: signal identifier
 */
void handleArithmeticSignal(int signal) {
  printf(
      "Error: a division by 0 operation was attempted.\n"
      "Total number of operations completed successfully: %i\n"
      "The program will be terminated.\n",
      divisionOp);
  exit(0);
}

/**
 * keyboard interrupt signal handler (Ctrl+c)
 *
 * signal: signal identifier
 */
void handleInterrupt(int signal) {
  printf(
      "\nTotal number of operations completed successfully: %i\n"
      "The program will be terminated.\n",
      divisionOp);
  exit(0);
}