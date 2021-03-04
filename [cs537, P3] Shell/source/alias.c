#include "./alias.h"

void printAlias(AliasStruct *a) {
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
    return -1;
}

void addAlias(AliasStruct *a) {
    // add object to global alias list
    if ((aliasList.pointer =
             realloc(aliasList.pointer,
                     sizeof(AliasStruct *) * (++aliasList.length))) == NULL) {
        perror("Error: allocating memory. ");
        fflush(stderr);
        exit(1);
    }
    aliasList.pointer[aliasList.length - 1] = a;
}

void removeAlias(int index) {
    // free element target index
    for (int i = 0; i < aliasList.pointer[index]->length; i++) {
        free(aliasList.pointer[index]->token[i]);
        aliasList.pointer[index]->token[i] = NULL;
    }
    free(aliasList.pointer[index]->label);
    aliasList.pointer[index]->label = NULL;
    free(aliasList.pointer[index]);
    aliasList.pointer[index] = NULL;

    // shift all elements one position in the array to the left
    for (int i = index; i + 1 < aliasList.length; i++)
        aliasList.pointer[i] = aliasList.pointer[i + 1];

    --aliasList.length;  // decrement list elements number
}
