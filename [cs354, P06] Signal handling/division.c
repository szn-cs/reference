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

#define BUFFER_SIZE 100

int divisionOp = 0; // total count of how many division operations were successfully completed

void handleArithmeticSignal(int signal)  {
  printf("Error: a division by 0 operation was attempted.");
  printf("\nTotal number of operations completed successfully: %i\n", divisionOp);
  printf("The program will be terminated.\n");
  exit(0);
}

void handleInterrupt(int signal) {
  printf("\nTotal number of operations completed successfully: %i\n", divisionOp);
  printf("The program will be terminated.\n");
  exit(0);
}

int main() {
  int int1, int2; // input values

  struct sigaction act1; // signal action struct
  memset(&act1, 0, sizeof(act1)); // clear memory
  act1.sa_handler = &handleArithmeticSignal;
  // register user signal
  if(sigaction(SIGFPE, &act1, NULL) == -1) {
    exit(1);
  };

  struct sigaction act2; // signal action struct
  memset(&act2, 0, sizeof(act2)); // clear memory
  act2.sa_handler = &handleInterrupt;
  // register user signal
  if(sigaction(SIGINT, &act2, NULL) == -1) {
    exit(1);
  };

  while(1) {
    // prompt for user input integers
    char *str = malloc(BUFFER_SIZE);
    printf("Enter first integer: ");
    if (fgets(str, BUFFER_SIZE, stdin) == NULL) {
        exit(1);
    }
    int1 = atoi(str); 
    printf("Enter second integer: ");
    if (fgets(str, BUFFER_SIZE, stdin) == NULL) {
      exit(1);
    }
    int2 = atoi(str);
    
    // Calculate the quotient and remainder of input numbers division
    struct div_t { int quot; int rem; };
    int quotient = int1 / int2; 
    int remainder = int1 % int2;

    printf("%i / %i is %i with a remainder of %i\n", int1, int2, quotient, remainder);

    divisionOp++;
  }
}