///////////////////////////////////////////////////////////////////////////////
//
// Copyright 2020 Jim Skrentny
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission for CS 354 Fall 2020, Deb Deppeler
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        n_in_a_row.c
// This File:        n_in_a_row.c
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
// Online sources:   Enumeration C reference https://en.cppreference.com/w/c/language/enum
//                   Error handling in C https://www.geeksforgeeks.org/error-handling-c-programs/
//                   Error codes reference https://www.thegeekstuff.com/2010/10/linux-error-codes/
////////////////////////////////////////////////////////////////////////////////
   
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h> 
     
char *DELIM = ",";  // string containing the delimiter characters
extern int errno; // error number from errno.h library

// numerals representing board state validity
enum STATE { 
    IN_VALID = 0, 
    VALID = 1 
};
// tokens representing the possible values the in game board. 
enum MARK { 
    EMPTY = 0 /*unmarked space*/, 
    x = 1, 
    o = 2 
};  
// define error codes. Strarting from 132 which won't conflict with errno.h library defined error codes
enum ERROR_CODE { 
    E_SIZE, 
    E_ARGUMENT_MISSING, 
    E_ARGUMENT_MULTIPLE, 
};

/**
 * Retreive appropriate error message
 * 
 * e: code integral matching the enum ERROR_CODE definitions
 * error message string.
 */
const char* getErrorMessage(enum ERROR_CODE e) {
    char *m = NULL; // message that should be allocated on the heap to be returned as a pointer
    //! Note: order of case values must match enum declaration declaration order.
    switch(e) {
        case E_SIZE: 
            m = "Invalid board size parsed from input file";
        break; 
        case E_ARGUMENT_MISSING: 
            m = "Missing filename argument";
        break; 
        case E_ARGUMENT_MULTIPLE: 
            m = "Only a single filename argument must be provided";
        break; 
        default: 
            m = "Unknown error code thrown";
    }
    return m;
}

/**
 * Tic-Tac-Toe Game - Usually a 3x3 grid board, allowing players to mark (either X or O) on the game board, until one player wins or there are no spaces available to mark. Winners should get 3 of their marks "in a row" either horizontally, vertically, or diagonally. 
 * 
 * usage: $`./n_in_a_row <input_filename>` where input-filename - data representing the tic-tac-toe board.
 * compilation: `gcc -Wall -m32 -std=gnu99`
 * 
 * Assignment requirements: 
    - Must follow style guidelines: https://canvas.wisc.edu/courses/205087/pages/programming-style-guide
    - must compile without warning errors.

*/

int get_dimensions(FILE *fp);
void getMarks(FILE *fp, int **board, int size);
int n_in_a_row(int **board, int size);  
void clear2DArray(int **array, int row, int column); 
void print2DArray(int **array, int *rows, int *columns, char *delimiter);
void* freeNestedArrays(int **array, int row); 
void throw(const char *m1, const char *m2);

/* 
 * This program processes a file containing the current game state, represented as a 2D grid of Xs and Os. 
 * It prints Valid if the input file contains a game board with either 1 or no winners (draw); 
 * and where there is at most 1 more X than O (#X = #O or #X = #O+1). 
 * 
 * Specification: 
        The program should print either "valid" or "invalid" followed by a newline (only these two outputs in lowercase will be accepted)
        Print valid only if the input file contains a valid board configuration, otherwise print invalid. To determine if the input board configuration is valid, you may also need to determine if there is a winner. You'll need to iterate over the board to check for winning lines, that is, n of the same marks in a row, column, or diagonal. 
        A valid board has:
        - an odd size; even size boards are invalid
        - either the same number Xs as Os, or 1 more X than O since we're assuming X always moves first
        - either no winner or one winner; X and O cannot both be winners
        - either one winning line (i.e., row, column, or diagonal), or two winning lines that intersect on one mark; two parallel winning lines are invalid
 * 
 * 
 * argc: command-line argument count
 * argv: command-line argument value
 */
int main(int argc, char *argv[]) {              
    // Verify number of input arguments
    if(argc != 2) {
        char *m = "Invalid number of filename arguments"; 
        (argc < 2) ? throw(getErrorMessage(E_ARGUMENT_MISSING), m) : throw(getErrorMessage(E_ARGUMENT_MULTIPLE), m); 
    }
    
    //Open the file and check if it opened successfully.
    char *filename = *(argv + 1); // get filename from input argument
    FILE *fp = fopen(filename, "r");
    if (fp == NULL) throw(strerror(errno /*ENOENT*/), "Can't open file for reading");

    // game board dimensions (rows & columns), where total number of marks on the board will be in the range of 0 to size*size; 
    int size = 0;   
    // current board state validity
    enum STATE state = IN_VALID;  

    //retrieve the board size.
    size = get_dimensions(fp);
    // validate input size value, range mathematically:  [3, 99]
    if(size < 3 || size > 99)
        throw(getErrorMessage(E_SIZE), "");

    // Create board with the matching dimesions, represnted by 2D heap allocated array.
    int **board = malloc(sizeof(int *) * size); 
    if(board == NULL) throw(strerror(errno /*ENOMEM*/), "Failed to allocated memory.");
    // create nested arrays
    for(int i = 0; i < size; i++) {
        *(board + i) = malloc(sizeof(int) * size); 
        if(*(board + i) == NULL) throw(strerror(errno /*ENOMEM*/), "Failed to allocated memory.");
    }
    // initialize 2D array with zeros.
    clear2DArray(board, size, size); 

    // fill in-memory board with values from the file representation of the board marks
    getMarks(fp, board, size);     

    // debug board
    print2DArray(board, &size, &size, DELIM); 

    // 
    state = n_in_a_row(board, size); 

    // print game board validation message
    switch(state) {
        case VALID: 
            printf("valid\n");
        break; 
        case IN_VALID: 
        default: 
            printf("invalid\n");
    }

    // Free up dynamically allocated memory.
    board = freeNestedArrays(board, size); // free associated arrays & return NULL

    //Close the file.
    if (fclose(fp) != 0) throw(strerror(errno /*EBADF or other*/), "Error while closing the file");     

    return 0;       
}       


/* 
 * Retrieves from the first line of the input file,
 * the size of the board (number of rows and columns).
 * 
 * fp: file pointer for input file
 * extracted size of board game
 */
int get_dimensions(FILE *fp) {      
    char *line = NULL;
    size_t len = 0;
    if (getline(&line, &len, fp) == -1) {
        printf("Error in reading the file.\n");
        exit(1);
    }

    char *token = NULL;
    token = strtok(line, DELIM);
    return atoi(token);
}

/**
 * Parse file line by line, extracting board marks, and filling them into the in-memory board array.
 * Assuming there are `size` lines where each line has `size` numbers (columns) separated by commas. 
 *  
 * fp: file pointer for input file
 * board: address of 2D array representing the board grid
 * size: number of rows and columns
 */
void getMarks(FILE *fp, int **board, int size) {
    char *line = NULL; // address of buffer containing the line text // TODO: buffer should be free up manually
    size_t len = 0; // buffer size
    char *token = NULL; // string of parsed portion
    
    for (int i = 0; i < size; i++) {
        if (getline(&line, &len, fp) == -1) throw("Error while reading the file", "");
        // Tokenize line wrt the delimiter character
        token = strtok(line, DELIM);
        // store user marks in the board array
        for (int j = 0; j < size; j++) {
            *(*(board + i) + j) = atoi(token); //! Note: atoi() function doesn't detect errors.
            token = strtok(NULL, DELIM); // continue scanning string from the previous successful call
        }
    }
}

/* 
 * Returns 1 if and only if the board is in a valid state, otherwise returns 0 (These values correspond to the enum STATE).
 * 
 * board: heap allocated 2D board
 * size: number of rows and columns; 
 */
int n_in_a_row(int **board, int size) {


    return IN_VALID;


    return VALID;   
} 

/**
 * Set all elements of 2D array to zero. 
 * 
 * array: input 2-dimensional array of array. 
 * row: # of array rows. 
 * columns: pointer to # of array columns
 */ 
void clear2DArray(int **array, int row, int column) {
    while(row-- > 0) 
        while(column-- > 0) 
            *(*(array + row) + column) = 0;
}

/**
 * print all elements in the 2D array, each row in a separate line, and each column separated by a delimiter. 
 * 
 * array: input 2-dimensional array of array. 
 * row: # of array rows. 
 * column: # of array columns
 * delimiter: separator symbol between columns
 */ 
void print2DArray(int **array, int *rows, int *columns, char *delimiter) {
    for(int r = 0; r < *rows; r++) {
        for(int c = 0; c < *columns; c++) {
            // print current element with delimiter when required
            printf("%i%s", *(*(array + r) + c), (c != *columns - 1) ? delimiter : ""); 
        }
        printf("\n");
    }
    printf("\n");
}

/**
 * Freeup nested arrays & parent array for a heap-allocated array of arrays.
 * 
 * array: input 2-dimensional array of array. 
 * row: # of rows in the array.
 * NULL to bea assigned to the array pointer.
 */
void* freeNestedArrays(int **array, int row) {
    while(row-- > 0) {
        free(array[row]); array[row] = NULL; // freeup memory of nested arrays
    };
    free(array); // free main/parent array
    return NULL; // to be assigned to the pointer identifier scoped to the caller function.
}

/**
 * Print supplied error messages and exit program with fail code 1
 * 
 * m1: Main code error message
 * m2: Additional custom message (string) to be printed out along with the code error message.
 */
void throw(const char *m1, const char *m2) {
    // print messages
    printf("%s: %s;\n", m1, m2);
    exit(1);
}


                                        // FIN
