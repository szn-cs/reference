#include "./alias.h"

void printAlias(AliasStruct *a) {
    if (a == NULL) return;

    fprintf(stdout, "%s ", a->label);
    /* print each token separated by one space, and excluding null terminator */
    for (int j = 0; j < a->length - 1; j++)
        fprintf(stdout, "%s%s", a->token[j], (j < a->length - 2) ? " " : "");
    fprintf(stdout, "\n");
    fflush(stdout);
}

int findAlias(char *label, AliasStruct **external_alias) {
    for (int i = 0; i < aliasList.length; i++)
        if (strcmp(aliasList.pointer[i]->label, label) == 0) {
            *external_alias = aliasList.pointer[i];
            return i;
        }
    *external_alias = NULL;
    return -1;
}

void addAlias(AliasStruct **a) {
    if (*a == NULL) *a = calloc(1, sizeof(AliasStruct));

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

void removeAlias(int index) {
    freeAliasStruct(aliasList.pointer[index]);  // free element target index
    free(aliasList.pointer[index]);
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
 * free alias global list
 *
 */
void freeAliasList() {
    for (int i = 0; i < aliasList.length; i++)
        freeAliasStruct(aliasList.pointer[i]);

    free(aliasList.pointer);
    aliasList.pointer = NULL;
}

/**
 * free alias structure elements
 *
 * @param a alias struct to free
 */
void freeAliasStruct(AliasStruct *a) {
    free(a->label);
    a->label = NULL;

    for (int i = 0; i < a->length; i++) {
        free(a->token[i]);
        a->token[i] = NULL;
    }

    free(a->token);
    a->token = NULL;
    a->length = 0;  // reset length
}