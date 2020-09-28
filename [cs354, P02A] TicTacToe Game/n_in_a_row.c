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
#include <stdbool.h> 
     
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
    X = 1, 
    O = 2 
};  
// define error codes. Strarting from 132 which won't conflict with errno.h library defined error codes
enum ERROR_CODE { 
    E_SIZE, 
    E_ARGUMENT_MISSING, 
    E_ARGUMENT_MULTIPLE, 
    E_INVALID_MARK
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
        case E_INVALID_MARK: 
            m = "Invalid mark parsed, the file contains invalid values";
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
bool isWinningLineHorizontal(int **board, int *size, int *line, int *middle, int *polarPairs);
bool isWinningLineVertical(int **board, int *size, int *line, int *middle, int *polarPairs);
int isWinningLineDiagonal(int **board, int *size, int *middle, int *polarPairs);
void toggleWinnerUser(int **board, int row, int column, bool *stateX, bool *stateO);
void countUserMark(int** board, int *size, int *countX, int *countO);

/* 
 * This program processes a file containing the current game state, represented as a 2D grid board of Xs and Os. 
 * And checks if the game state conforms to the rules of the Tic-Tac-Toe game. 
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

    // check validity of board state
    state = n_in_a_row(board, size); 

    // print game board validation message - i.e. Print valid only if the input file contains a valid board configuration, otherwise print invalid.
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
            //! Note: atoi() function doesn't detect errors.
            int mark = atoi(token); // integer of parsed mark
            if(mark != EMPTY && mark != X && mark != O) throw(getErrorMessage(E_INVALID_MARK), "");
            *(*(board + i) + j) = mark; // fill element in respective position
            token = strtok(NULL, DELIM); // continue scanning string from the previous successful call
        }
    }
}

/* 
 * Checks if current board state is valid.
 * <p> 
 * A valid game board has:
 *  ✔ an odd size; even size boards are invalid
 *  ✔ either the same number Xs as Os, or 1 more X than O (at most 1 more X than O), since we're assuming X always moves first.
 *  ✔ either no winner or one winner (draw); X and O cannot both be winners
 *  ✔ either one winning line (i.e., row, column, or diagonal), or two winning lines that intersect on one mark; two parallel winning lines are invalid.
 * Where a winning line is `size` number of the same marks in a row, column, or diagonal. 
 * [For reference] Assuminh: Maximum number of line permustations possible = 2 x 99 + 2 = 200
 * 
 * board: heap allocated 2D board
 * size: number of rows and columns; 
 * Returns 1 if and only if the board is in a valid state, otherwise returns 0 (values corresponding to STATE enum).
 */
int n_in_a_row(int **board, int size) {
    // even size boards are invalid
    if(size % 2 == 0) return IN_VALID;

    // either the same number Xs as Os, or at most 1 more X than O
    int countX = 0, countO = 0; 
    countUserMark(board, &size, &countX, &countO);
    if(countX != countO && (countX - countO) != 1)
        return IN_VALID;    

    int polarPairs = (size - 1) / 2; // number of polar sides (element pair of opposite side), excluding the middle element. 
    int middleIndex = ((size + 1) / 2) - 1; // middle index used for middle of row, or column, or center of element for diagonal. The middle positions are taken as reference values for determining if there is a winning line.
    // number of wins for each line configuration
    int nRowWin, nColumnWin, nDiagonalWin; 
    nRowWin = nColumnWin = nDiagonalWin = 0;
    // user winning state
    bool winnerX = false, winnerO = false;

    // row and column check for winning lines
    for(int l = 0; l < size; l++) {
        // Row line check
        if(isWinningLineHorizontal(board, &size, &l, &middleIndex, &polarPairs)) {
            nRowWin++; 
            // associate win to X and O users
            toggleWinnerUser(board, l, 0, &winnerX, &winnerO); 
        }
        // Column line check
        if(isWinningLineVertical(board, &size, &l, &middleIndex, &polarPairs)) {
            nColumnWin++;    
            // associate win to X and O users
            toggleWinnerUser(board, 0, l, &winnerX, &winnerO); 
        }
    }

    // diagonal check for winning lines
    nDiagonalWin = isWinningLineDiagonal(board, &size, &middleIndex, &polarPairs); 
    // associate win to X and O users
    if(nDiagonalWin > 0)
        toggleWinnerUser(board, middleIndex, middleIndex, &winnerX, &winnerO); 

    // Whenever an additional win of the same type is detected return invalid. Based on the fact that there cannot be two wins of the same type (row, column, diagonal). as two parallel winning lines are invalid.
    if(nRowWin > 1 || nColumnWin > 1 || nDiagonalWin > 1) return IN_VALID;

    // X and O cannot both be winners
    if(winnerX && winnerO) return IN_VALID;

    // either one winning line, or two winning lines that intersect on one mark; 
    if((nRowWin + nColumnWin + nDiagonalWin) > 2) return IN_VALID; 

    // either no winner or one winner.
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
            int element = *(*(board + r) + c);
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
    switch(board[row][column]) { 
        case X: 
            *stateX = true;
        break;
        case O: 
            *stateO = true;
        break; 
    }
}

/**
 *  Check if horizontal line (row) is a winning line
 * 
 * board: heap allocated 2D board
 * size: number of elements in a line;
 * line: index of line in the board
 * middle: index of middle element in the line
 * polarPairs: number of polar/opposite corresponding pairs in a line
 * validation of a line conforming to a winning state
 */
bool isWinningLineHorizontal(int **board, int *size, int *line, int *middle, int *polarPairs) {
    int common = board[*line][*middle]; // use middle line value as reference, as a common element.
    // skip / short-circuit for empty positions
    if(common == EMPTY)
        return false; 
    // check opposite pairs on each iteration and verify they are equal to the previous elements
    for (int i = 0; i < *polarPairs; i++) {
        int *current = &board[*line][i]; // current element
        int *opposite = &board[*line][*size - 1 - i]; // corresponding polar/opposite element in the line
        // compare current element to common element, and current element to it's corresponding opposite element
        if(*current != common || *current != *opposite) 
            return false;
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
bool isWinningLineVertical(int **board, int *size, int *line, int *middle, int *polarPairs) {
    int common = board[*middle][*line]; // use middle line value as reference, as a common element.
    // skip / short-circuit for empty positions
    if(common == EMPTY)
        return false; 
    // check opposite pairs on each iteration and verify they are equal to the previous elements
    for (int i = 0; i < *polarPairs; i++) {
        int *current = &board[i][*line]; // current element
        int *opposite = &board[*size - 1 - i][*line]; // corresponding polar/opposite element in the line
        // compare current element to common element, and current element to it's corresponding opposite element
        if(*current != common || *current != *opposite) 
            return false;
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
int isWinningLineDiagonal(int **board, int *size, int *middle, int *polarPairs) {
    int common = board[*middle][*middle]; // use center value as reference, i.e. a common element for comparison.
    // skip / short-circuit for empty positions
    if(common == EMPTY)
        return false; 
    // check the two diagonal permutation wins
    int ddWin = true, daWin = true; 
    for (int i = 0; (i < *polarPairs) && !(ddWin == false && daWin == false); i++) {
        if(ddWin) {
            // diagonal descending (R-to-L)
            int current = board[i][i]; // current element
            // corresponding polar/opposite element in the line
            int o = *size - 1 - i; // opposite index
            int opposite = board[o][o]; 
            // compare current element to common element, and current element to it's corresponding opposite element
            if(current != common || current != opposite) 
                ddWin = false;
        }
        if(daWin) {
            // diagonal ascending (R-to-L)
            int flipped = *size - 1 - i; // flipped index in comparison to the opposite diagonal line
            int current = board[flipped][i]; // current element
            // corresponding polar/opposite element in the line
            int opposite = board[i][flipped]; 
            // compare current element to common element, and current element to it's corresponding opposite element
            if(current != common || current != opposite) 
                daWin = false;
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
    printf("%s. %s\n", m1, m2);
    exit(1);
}


                                        // FIN
