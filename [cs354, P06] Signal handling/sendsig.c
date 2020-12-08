////////////////////////////////////////////////////////////////////////////////
//
// Copyright 2013,2019-2020, Jim Skrentny, (skrentny@cs.wisc.edu)
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission, CS354-Fall 2020, Deb Deppeler (deppeler@cs.wisc.edu)
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        sendsig.c
// This File:        sendsig.c
// Other Files:      mySigHandler.c
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

/**
 * Main parses arguments, and sends signal to another program
 *
 * argc: command-line argument count
 * argv: command-line argument value
 * program exit status
 */
int main(int argc, char *argv[]) {
  // user input
  int inputSignal;  // provided signal type
  int pid;          // process id

  // parse command line options
  int opt;
  while ((opt = getopt(argc, argv, "iu")) != -1) {
    switch (opt) {
      case 'i':
        inputSignal = SIGINT;
        break;
      case 'u':
        inputSignal = SIGUSR1;
        break;
    }
  }

  // validate cmd arguments
  if (optind != 2 || argc != 3) {
    printf("Usage: <signal type> <pid>\n");
    exit(1);
  }

  // parse process id
  pid = atoi(argv[2]);

  // send signal to running program
  if (kill(pid, inputSignal) == -1) {
    fprintf(stderr, "Could not send signal. %s\n", strerror(errno));
    exit(1);
  };

  return 0;
}