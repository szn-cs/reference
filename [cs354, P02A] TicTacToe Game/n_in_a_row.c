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
// Online sources:   Enumeration C reference 
//                      https://en.cppreference.com/w/c/language/enum
//                   Error handling in C 
//                      https://www.geeksforgeeks.org/error-handling-c-programs/
//                   Error codes reference 
//                      https://www.thegeekstuff.com/2010/10/linux-error-codes/
////////////////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h> 
#include <stdbool.h> 

#define DEBUG false // debug flag
extern int errno; // error number of `errno.h` library
char *DELIM = ",";  // delimiter characters of parsed data
// numerals representing board state validity
typedef enum { 
    IN_VALID = 0, 
    VALID = 1 
} State;
// tokens representing the possible values the in game board
typedef enum { 
    EMPTY = 0, // unmarked space
    X = 1, // X's user mark
    O = 2 // O's user mark
} Mark;
// define custom error codes
typedef enum { 
    E_SIZE, 
    E_ARGUMENT_MISSING, 
    E_ARGUMENT_MULTIPLE, 
    E_INVALID_MARK
} ErrorCode;

/**
 * Retreive appropriate error message
 * 
 * e: code integral matching the enum ErrorCode definitions of error message strings.
 */
const char* getErrorMessage(ErrorCode e) {
    char *m = NULL; // error message string
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
        case E_INVALID_MARK: 
            m = "Invalid mark parsed, the file contains invalid values";
        break;
        default: 
            m = "Unknown error code thrown";
    }
    return m;
}

// function prototype declarations:
int get_dimensions(FILE *fp);
void getMarks(FILE *fp, int **board, int size);
int n_in_a_row(int **board, int size); 
void countUserMark(int **board, int *size, int *countX, int *countO);
void toggleWinnerUser(int **board, int row, int column, bool *stateX, bool *stateO);
bool isWinHorizontal(int **board, int *size, int *line, int *middle, int *oppPairs);
bool isWinVertical(int **board, int *size, int *line, int *middle, int *oppPairs);
int isWinDiagonal(int **board, int *size, int *middle, int *oppPairs);
void clear2DArray(int **array, int row, int column); 
void print2DArray(int **array, int *rows, int *columns, char *delimiter);
void* freeNestedArrays(int **array, int row); 
void throw(const char *m1, const char *m2);

/**
 * This program processes a file containing the current game state, represented as a 2D 
 * grid board of Xs and Os. And checks if the game state conforms to the rules of 
 * the Tic-Tac-Toe game.
 *      usage: $`./n_in_a_row <input_filename>`
 *             where input-filename - data representing the tic-tac-toe board.
 *      compilation: `gcc -Wall -m32 -std=gnu99`
 * <p>
 * Tic-Tac-Toe Game - Usually a 3x3 grid board, allowing players to mark (either X or O) 
 * on the game board, until one player wins or there are no spaces available to mark. 
 * Winners should get 3 of their marks "in a row" either horizontally, vertically, 
 * or diagonally. 
 *      
 * argc: command-line argument count
 * argv: command-line argument value
 */
int main(int argc, char *argv[]) {              
    // Verify number of input arguments
    if(argc != 2) {
        const char *m = (argc < 2) ? 
                getErrorMessage(E_ARGUMENT_MISSING) : 
                getErrorMessage(E_ARGUMENT_MULTIPLE);
        throw(m, "Invalid number of filename arguments"); 
    }
    
    // open file stream
    char *filename = *(argv + 1); // filename argument
    FILE *fp = fopen(filename, "r");
    if (fp == NULL) throw(strerror(errno /*ENOENT*/), "Can't open file for reading");

    int **board = NULL; // 2D heap allocated array representing the game board
    int size = 0; /* game board dimensions (rows & columns), total number of marks on 
                     the board will be in the range of 0 to size x size */ 
    State state = IN_VALID; // current board state validity

    size = get_dimensions(fp); // retrieve the board size.
    // validate input size value, fullfilling mathematical range:  [3, 99]
    if(size < 3 || size > 99) throw(getErrorMessage(E_SIZE), "");

    // create board with the matching dimesions (array of array)
    board = malloc(sizeof(int *) * size); 
    if(board == NULL) throw(strerror(errno /*ENOMEM*/), "Failed to allocated memory.");
    // create nested arrays
    for(int i = 0; i < size; i++) {
        *(board + i) = malloc(sizeof(int) * size); 
        if(*(board + i) == NULL) 
            throw(strerror(errno /*ENOMEM*/), "Failed to allocated memory.");
    }
    // initialize 2D array with zeros.
    clear2DArray(board, size, size); 

    // fill in-memory board with values from the file representation of the board marks
    getMarks(fp, board, size);     
    // print board
    if(DEBUG == true) print2DArray(board, &size, &size, DELIM);
    // check validity of board state
    state = n_in_a_row(board, size); 

    /* print game board validation message - i.e. Print valid only if the input file 
       contains a valid board configuration, otherwise print invalid. */
    switch(state) {
        case VALID: 
            printf("valid\n");
        break; 
        case IN_VALID: 
        default: 
            printf("invalid\n");
    }

    // free up dynamically allocated memory.
    board = freeNestedArrays(board, size); // free associated arrays & return NULL

    // close the file.
    if (fclose(fp) != 0)
        throw(strerror(errno /*EBADF or other*/), "Error while closing the file");     

    return 0;       
}

/**
 * Retrieves the size of the board from input file
 * 
 * fp: file pointer for input file
 * parsed size of board game
 */
int get_dimensions(FILE *fp) {      
    char *line = NULL;
    size_t len = 0;
    if (getline(&line, &len, fp) == -1)
        throw(strerror(errno /*EINVAL or ENOMEM*/), "Error in reading the file");
    char *token = NULL;
    token = strtok(line, DELIM);
    int size = atoi(token); // parsed size value
    free(line); line = NULL; // freeing buffer
    return size;
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
    char *line = NULL; // address of buffer containing the line text
    size_t len = 0; // buffer size
    char *token = NULL; // string of parsed portion
    
    for (int i = 0; i < size; i++) {
        // parse next line
        if (getline(&line, &len, fp) == -1) 
            throw(strerror(errno /*EINVAL or ENOMEM*/), "Error while reading the file");
        // Tokenize line using the delimiter character
        token = strtok(line, DELIM);
        // store user marks in the board array
        for (int j = 0; j < size; j++) {
            //! Note: atoi() function doesn't detect errors.
            Mark mark = atoi(token); // integer of parsed mark
            // validate parsed marks
            if(mark != EMPTY && mark != X && mark != O)
                throw(getErrorMessage(E_INVALID_MARK), "");
            // fill element in respective position
            *(*(board + i) + j) = mark;
            // continue scanning from previous successful call
            token = strtok(NULL, DELIM); 
        }
    }

    free(line); line = NULL; // free up buffer
}

/* 
 * Checks if current board state is valid.
 * <p> 
 * A valid game board has:
 *  ✔ an odd size; even size boards are invalid
 *  ✔ either the same number Xs as Os, or 1 more X than O (at most 1 more X than O), 
 *          since we're assuming X always moves first.
 *  ✔ either no winner or one winner (draw); X and O cannot both be winners
 *  ✔ either one winning line (i.e., row, column, or diagonal), or two winning lines 
 *          that intersect on one mark; two parallel winning lines are invalid.
 * <p>
 * Where a winning line is `size` number of the same marks in a row, column, or diagonal. 
 * [spec] Assumming: Maximum # of line permustations possible = 2 x 99 + 2 = 200
 * 
 * board: heap allocated 2D board
 * size: number of rows and columns; 
 * Returns 1 if and only if the board is in a valid state, 
 *      otherwise returns 0 (values corresponding to State enum).
 */
int n_in_a_row(int **board, int size) {
    // validate board size: must be odd
    if(size % 2 == 0) return IN_VALID;

    int countX = 0, countO = 0;  // count users marks
    // number of polar sides (element pairs of opposite side), excluding middle element. 
    int polarPairs = (size - 1) / 2; 
    /* middle index of lines. The middle positions are taken as reference values for 
       determining if there is a winning line. */
    int middleIndex = ((size + 1) / 2) - 1; 
    // number of wins for each line configuration
    int nRowWin, nColumnWin, nDiagonalWin; 
    nRowWin = nColumnWin = nDiagonalWin = 0; // initialize
    // user winning state
    bool winnerX = false, winnerO = false;

    // count users marks on board
    countUserMark(board, &size, &countX, &countO);
    // validate proportion of marks: same number Xs as Os, or at most 1 more X than O
    if(countX != countO && (countX - countO) != 1)
        return IN_VALID;    

    /* Search for winning lines */
    // row & column lines check
    for(int l = 0; l < size; l++) {
        // Row line check
        if(isWinHorizontal(board, &size, &l, &middleIndex, &polarPairs)) {
            nRowWin++; 
            // associate win to X and O users
            toggleWinnerUser(board, l, 0, &winnerX, &winnerO); 
        }
        // Column line check
        if(isWinVertical(board, &size, &l, &middleIndex, &polarPairs)) {
            nColumnWin++;    
            // associate win to X and O users
            toggleWinnerUser(board, 0, l, &winnerX, &winnerO); 
        }
    }
    // diagonal lines check
    nDiagonalWin = isWinDiagonal(board, &size, &middleIndex, &polarPairs); 
    // associate win to X and O users
    if(nDiagonalWin > 0)
        toggleWinnerUser(board, middleIndex, middleIndex, &winnerX, &winnerO); 

    /* validate number of same type win (row, column, diagonal): single win for each type. 
       as two parallel winning lines are invalid, neither cross lines. Diagonal cannot be
       parallel. */
    if(nRowWin > 1 || nColumnWin > 1) return IN_VALID;

    // validate winner user: X and O cannot both be winners.
    if(winnerX && winnerO) return IN_VALID;

    /* validate # of winning lines: either one winning line, or two winning lines 
       that intersect on one mark. */
    if((nRowWin + nColumnWin + nDiagonalWin) > 2) return IN_VALID; 

    // fallback: either no winner or one winner, with rules applied. 
    return VALID;   
}

/**
 * Update user counter states to match the occurrences of each user's mark
 * 
 * board: heap allocated 2D board
 * size: number of elements in a line;
 * countX: X user counter state 
 * countO: O user counter state
 */
void countUserMark(int** board, int *size, int *countX, int *countO) {
    for(int r = 0; r < *size; r++)
        for(int c = 0; c < *size; c++) {
            Mark element = *(*(board + r) + c);
            switch(element) {
                case X: 
                    *countX = *countX + 1;
                break;
                case O: 
                    *countO = *countO + 1;
                break;
                case EMPTY: 
                default: 
                    continue; // skip
            }
        }
}

/**
 *  Set winner user of specified winning line
 * 
 * board: heap allocated 2D array representing the game board
 * row: index of row
 * column: index of column
 * stateX: boolean winning state of X
 * stateO: boolean winning state of O
 * alter the winning state of the user associated with the specified position
 */
void toggleWinnerUser(int **board, int row, int column, bool *stateX, bool *stateO) {
    Mark mark = *(*(board + row) + column); 
    switch(mark) {
        case X: 
            *stateX = true;
        break;
        case O: 
            *stateO = true;
        break; 
        default: ; // Skip
    }
}

/**
 * Check if horizontal line (row) is a winning line
 * 
 * board: heap allocated 2D board
 * size: number of elements in a line;
 * line: index of line in the board
 * middle: index of middle element in the line
 * polarPairs: number of polar/opposite corresponding element pairs in a line
 * validation of a line conforming to a winning state
 */
bool isWinHorizontal(int **board, int *size, int *line, int *middle, int *polarPairs) {
    // use middle line value as reference, as a common element.
    int common = *(*(board + *line) + *middle); 
    // skip / short-circuit for empty positions
    if(common == EMPTY) return false; 
    /* check opposite pairs on each iteration & verify equality to previous elements */
    for (int i = 0; i < *polarPairs; i++) {
        int *current = &*(*(board + *line) + i); // current element
        // corresponding polar/opposite element in the line
        int *opposite = &*(*(board + *line)+ (*size - 1 - i)); 
        /* compare current pair to previous, & current element to it's corresponding 
           opposite element */
        if(*current != common || *current != *opposite) return false;
    }
    return true;
}

/**
 *  Check if vertical line (column) is a winning line
 * 
 *  board: heap allocated 2D board
 *  size: number of elements in a line;
 *  line: index of line in the board
 *  middle: index of middle element in the line
 *  polarPairs: number of polar/opposite corresponding pairs in a line
 *  validation of a line conforming to a winning state
 */
bool isWinVertical(int **board, int *size, int *line, int *middle, int *polarPairs) {
    // use middle line value as reference for comparison, as a common element.
    int common = *(*(board + *middle) + *line); 
    // skip / short-circuit for empty positions
    if(common == EMPTY) return false; 
    /* check opposite pairs on each iteration & verify equality to previous elements */
    for (int i = 0; i < *polarPairs; i++) {
        int *current = &*(*(board + i) + *line); // current element
        // corresponding opposite element
        int *opposite = &*(*(board + (*size - 1 - i)) + *line); 
        /* compare current pair to previous, & current element to it's corresponding 
           opposite element */
        if(*current != common || *current != *opposite) return false;
    }
    return true;
}

/**
 *  Check if diagonal lines are winning lines
 * 
 *  board: heap allocated 2D board
 *  size: number of elements in a line;
 *  middle: index of middle element in the line
 *  polarPairs: number of polar/opposite corresponding pairs in a line
 *  number of winning diagonal lines (0, 1, or 2)
 */
int isWinDiagonal(int **board, int *size, int *middle, int *polarPairs) {
    // use center value as reference, i.e. a common element for comparison.
    int common = *(*(board + *middle) + *middle);
    // skip / short-circuit for empty positions
    if(common == EMPTY) return false; 
    // the two diagonal permutation wins possible
    int ddWin = true, daWin = true; 
    // check diagonal wins
    for (int i = 0; (i < *polarPairs) && !(ddWin == false && daWin == false); i++) {
        if(ddWin) {
            // diagonal descending (R-to-L)
            int current = *(*(board + i) + i); // current element
            // corresponding polar/opposite element in the line
            int o = *size - 1 - i; // opposite index
            int opposite = *(*(board + o) + o); 
            /* compare current pair to previous, & current element to it's corresponding 
               opposite element */
            if(current != common || current != opposite) ddWin = false;
        }
        if(daWin) {
            // diagonal ascending (R-to-L)
            int flipped = *size - 1 - i; // index in comparison to inverse diagonal line
            int current = *(*(board + flipped) + i); // current element
            // corresponding polar/opposite element in the line
            int opposite = *(*(board + i) + flipped); 
            /* compare current pair to previous, & current element to it's corresponding 
               opposite element */
            if(current != common || current != opposite) daWin = false;
        }
    }

    // return number of diagonal winning lines.
    return ddWin + daWin;
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
 * print all elements in the 2D array, each row in a separate line, 
 * and each column separated by a delimiter. 
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
 * NULL to be assigned to the array pointer (as chaining api)
 */
void* freeNestedArrays(int **array, int row) {
    // freeup memory of nested arrays
    while(row-- > 0) {
        free(*(array + row)); 
        *(array + row) = NULL; 
    }
    free(array); // free main/parent array
    return NULL; // to be assigned to pointer identifier scoped in caller function.
}

/**
 * Print supplied error messages and exit program with fail code 1
 * 
 * m1: Main code error message
 * m2: Additional custom message to be printed out along with the code error message.
 */
void throw(const char *m1, const char *m2) {
    // print messages
    printf("%s. %s\n", m1, m2);
    exit(1);
}


                                        // FIN
