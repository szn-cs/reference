#ifndef __utility_h
#define __utility_h

#include <assert.h>
#include <getopt.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include "./message.h"

bool isWhitespaceString(char *s);
bool isWhitespace(char c);
void trim(char *s);
FILE *createFileDescriptor(char *filename);

#endif  // __utility_h
