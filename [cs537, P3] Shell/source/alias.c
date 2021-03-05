/**
 * @file alias.c
 * @brief alias array data structure related functions
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#include "./alias.h"

/**
 * print a single alias entry
 *
 * @param a pointer to alias data structure
 */
void printAlias(AliasStruct *a) {
    if (a == NULL) return;

    fprintf(stdout, "%s ", a->label);
    /* print each token separated by one space, and excluding null terminator */
    for (int j = 0; j < a->length - 1; j++)
        fprintf(stdout, "%s%s", a->token[j], (j < a->length - 2) ? " " : "");
    fprintf(stdout, "\n");
    fflush(stdout);
}

/**
 * find a registered alias in the list
 *
 * @param label alias label string to search for
 * @param externalAlias pointer to alias variable to modify
 * @return int index of found alias entry, or -1 if not found
 */
int findAlias(char *label, AliasStruct **externalAlias) {
    for (int i = 0; i < aliasList.length; i++)
        if (strcmp(aliasList.pointer[i]->label, label) == 0) {
            *externalAlias = aliasList.pointer[i];
            return i;
        }
    *externalAlias = NULL;
    return -1;
}

/**
 * insert an alias entry into the global alias list
 *
 * @param a pointer to alias to add, the variable is modified with newly
 * allocated instance if non is provided
 */
void addAlias(AliasStruct **a) {
    if (*a == NULL) *a = realloc(*a, sizeof(AliasStruct));

    // add object to global alias list
    ++aliasList.length;  // increment length
    if ((aliasList.pointer = realloc(
             aliasList.pointer, sizeof(AliasStruct *) * aliasList.length)) ==
        NULL) {
        perror("Error: allocating memory. ");
        fflush(stderr);
        exit(1);
    }

    aliasList.pointer[aliasList.length - 1] = *a;  // add element to the end
}

/**
 * remove alias entry from global alias list
 *
 * @param index number of target alias entry in the global list
 */
void removeAlias(int index) {
    freeAliasStruct(aliasList.pointer[index],
                    true);  // free element target index
    aliasList.pointer[index] = NULL;

    shiftArrayElement(index, aliasList.length, (void **)aliasList.pointer);

    // resize list && decrement list elements number
    --aliasList.length;
    if (aliasList.length != 0) {
        if ((aliasList.pointer =
                 realloc(aliasList.pointer,
                         sizeof(AliasStruct *) * aliasList.length)) == NULL) {
            perror("Error: allocating memory. ");
            fflush(stderr);
            exit(1);
        }
    }
}

/**
 * shift all elements one position in the array to the left
 *
 * @param start index to begin shifting to
 * @param end index of last element to shift
 * @param pointer array of pointers
 */
void shiftArrayElement(int start, int end, void **pointer) {
    for (int i = start; i + 1 < end; i++) pointer[i] = pointer[i + 1];
}

/**
 * free alias global list and all associated structures
 *
 */
void freeAliasList() {
    for (int i = 0; i < aliasList.length; i++) {
        freeAliasStruct(aliasList.pointer[i], true);
        aliasList.pointer[i] = NULL;
    }
    free(aliasList.pointer);
    aliasList.pointer = NULL;
}

/**
 * free alias structure elements
 *
 * @param a alias struct to free
 * @param freePointer flag - whether to free the alias struct pointer itself
 */
void freeAliasStruct(AliasStruct *a, bool freePointer) {
    free(a->label);
    a->label = NULL;

    for (int i = 0; i < a->length; i++) {
        free(a->token[i]);
        a->token[i] = NULL;
    }

    free(a->token);
    a->token = NULL;
    a->length = 0;  // reset length

    if (freePointer) free(a);
}
