/**
 * @file utility.c
 * @brief lambda and utility function
 * @copyright Copyright (c) 2021 by Safi Nassar
 */

#include "./utility.h"

/**
 * check if string is composed of only whitespace chars
 *
 * @param s string to test
 * @return true string is composed of whitespace characaters, otherwise false
 */
bool isWhitespaceString(char *s) {
    for (int i = 0; i < strlen(s); i++)
        if (!isWhitespace(s[i])) return false;
    return true;
}

/**
 * check if character is whitespace
 *
 * @param c character to test
 * @return true character is whitespace, otherwise false
 */
bool isWhitespace(char c) {
    char whitespace[] = " \n\t";
    for (int i = 0; i < strlen(whitespace); i++)
        if (c == whitespace[i]) return true;
    return false;
}

/**
 * trim string of whitespace (remove preceeding and leading whitespace)
 *
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
 * open file for reading with required error handling
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
