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

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <errno.h>
#include <stdbool.h>
#include <signal.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>

#define INTERVAL 3

int signalCounter = 0; // # of time SIGUSR1 signal received

// schedual alarm - sending SIGALRM signal after 3 seconds
void schedualAlarm() {
  alarm(3);
}

void handleAlarm() {
  // get time (Linux date command format)
  time_t curtime;
  time(&curtime);
  
  // get process id
  int pid = getpid(); 

  // print pid and time
  printf("PID: %u CURRENT TIME: %s", pid, ctime(&curtime));
  // re-arm alarm
  schedualAlarm();
}

// interrupt handler Ctrl+c
void handleInterrupt() {
  printf("SIGINT handled.\nSIGUSR1 was handled %u times. Exiting now.\n", signalCounter);
  exit(0);
}

void handleUserSignal() {
  printf("SIGUSR1 handled and counted!\n");
  signalCounter++; 
}

// a periodic signal from an alarm
// a keyboard interrupt signal
// a user defined signal
int main() {
  // program usage info
  printf("Pid and time print every %u seconds.\n", INTERVAL);
  printf("Enter Ctrl-C to end the program.\n");  

  // register signal handlers:
  struct sigaction act1; // signal action struct
  memset(&act1, 0, sizeof(act1)); // clear memory
  act1.sa_handler = &handleAlarm;
  // register alarm signal
  if(sigaction(SIGALRM, &act1, NULL) == -1) {
    exit(1);
  };
  struct sigaction act2; // signal action struct
  memset(&act2, 0, sizeof(act2)); // clear memory
  act2.sa_handler = &handleUserSignal;
  // register user signal
  if(sigaction(SIGUSR1, &act2, NULL) == -1) {
    exit(1);
  };
  struct sigaction act3; // signal action struct
  memset(&act3, 0, sizeof(act3)); // clear memory
  act3.sa_handler = &handleInterrupt;
  // register user signal
  if(sigaction(SIGINT, &act3, NULL) == -1) {
    exit(1);
  };
  
  schedualAlarm(); // register alarm

  while(true) {}; // infinite loop

  return(0);
}