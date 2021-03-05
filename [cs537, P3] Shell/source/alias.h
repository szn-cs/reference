/**
 * @file alias.h
 * @brief alias array data structure related functions
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#ifndef __alias_h
#define __alias_h

#include <assert.h>
#include <getopt.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include "./message.h"

typedef struct AliasStruct {
    char *label;   // alias command label
    char **token;  // token array
    int length;    // # of elements in array
} AliasStruct;

struct AliasList {
    AliasStruct **pointer;  // store for alias tokes mappings
    int length;             // # of elements in the array
} aliasList;

int findAlias(char *label, AliasStruct **externalAlias);
void addAlias(AliasStruct **a);
void removeAlias(int index);
void printAlias(AliasStruct *a);
void shiftArrayElement(int start, int end, void **pointer);
void freeAliasList();
void freeAliasStruct(AliasStruct *a, bool freePointer);

#endif  // __alias_h
