#include "./utility.h"

bool isWhitespaceString(char *s) {
    for (int i = 0; i < strlen(s); i++)
        if (!isWhitespace(s[i])) return false;
    return true;
}

bool isWhitespace(char c) {
    char whitespace[] = " \n\t";
    for (int i = 0; i < strlen(whitespace); i++)
        if (c == whitespace[i]) return true;
    return false;
}

/**
 * trim string of whitespace
 * (modified from
 * https://stackoverflow.com/questions/122616/how-do-i-trim-leading-trailing-whitespace-in-a-standard-way)
 *
 * @param s string to be trimmed
 */
void trim(char *s) {
    if (s == NULL) return;  // shortcircuit
    char *p = s;
    int l = strlen(p);
    while (isWhitespace(p[l - 1])) p[--l] = 0;
    while (*p && isWhitespace(*p)) ++p, --l;
    memmove(s, p, l + 1);
}

/**
 * open file for reading with error handling
 *
 * @param filename name of the file to open
 * @return FILE* file descriptor / input stream
 */
FILE *createFileDescriptor(char *filename) {
    FILE *s = NULL;
    // create requested file stream
    if ((s = fopen(filename, "r")) == NULL) goto fileError;
    return s;

fileError:
    // - batch file does not exist or cannot be opened
    fprintf(stderr, ERROR_FILE(filename));
    fflush(stderr);
    exit(1);
}
