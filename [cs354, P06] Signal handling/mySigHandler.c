////////////////////////////////////////////////////////////////////////////////
//
// Copyright 2013,2019-2020, Jim Skrentny, (skrentny@cs.wisc.edu)
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission, CS354-Fall 2020, Deb Deppeler (deppeler@cs.wisc.edu)
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        mySigHandler.c
// This File:        mySigHandler.c
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

// interval for schedualed alarm
#define INTERVAL 3

// # of time SIGUSR1 signal received
int signalCounter = 0;

void registerHandler();
/**
 * signal handlers
 */
void handleAlarm(int);
void handleInterrupt(int);
void handleUserSignal(int);

/**
 * Main registers signal handlers, scheduals alarm, and keeps program running
 * infinitely.
 *
 * argc: command-line argument count
 * argv: command-line argument value
 * program exit status
 */
int main(int argc, char* argv[]) {
  // program usage info
  printf(
      "Pid and time print every %u seconds.\nEnter Ctrl-C to end the "
      "program.\n",
      INTERVAL);
  registerHandler();  // register signal handlers
  alarm(INTERVAL);    // schedual alarm - sending SIGALRM signal after 3 seconds
  while (true)
    ;  // infinite loop
}

/**
 * Register signal handlers to corresponding signal types
 */
void registerHandler() {
  // action structs
  struct sigaction act1, act2, act3;
  // clear memory
  memset(&act1, 0, sizeof(act1));
  memset(&act2, 0, sizeof(act2));
  memset(&act3, 0, sizeof(act3));
  // set corresponding handlers
  act1.sa_handler = &handleAlarm;
  act2.sa_handler = &handleUserSignal;
  act3.sa_handler = &handleInterrupt;
  // register handlers to corresponding signals
  if ((sigaction(SIGALRM, &act1, NULL) & sigaction(SIGUSR1, &act2, NULL) &
       sigaction(SIGINT, &act3, NULL)) != 0) {
    fprintf(stderr, "Could not register signal handler. %s\n", strerror(errno));
    exit(1);
  }
}

/**
 * Alarm signal handler with periodic signal firing
 *
 * signal: signal identifier
 */
void handleAlarm(int signal) {
  // get process id
  int pid = getpid();
  // get time (Linux date command format)
  time_t curtime;
  if (time(&curtime) == -1) {
    fprintf(stderr, "Could not get current system time. %s\n", strerror(errno));
    exit(1);
  };

  // print pid and time
  char* timeString;
  if ((timeString = ctime(&curtime)) == NULL) {
    fprintf(stderr, "Error while interpreting time. %s\n", strerror(errno));
    exit(1);
  }
  printf("PID: %u CURRENT TIME: %s", pid, timeString);
  // re-arm alarm
  alarm(INTERVAL);  // schedual alarm - sending SIGALRM signal after 3 seconds
}

/**
 * keyboard interrupt signal handler (Ctrl+c)
 *
 * signal: signal identifier
 */
void handleInterrupt(int signal) {
  printf("\nSIGINT handled.\nSIGUSR1 was handled %u times. Exiting now.\n",
         signalCounter);
  exit(0);
}

/**
 * user-defined signal handler
 *
 * signal: signal identifier
 */
void handleUserSignal(int signal) {
  printf("SIGUSR1 handled and counted!\n");
  signalCounter++;
}