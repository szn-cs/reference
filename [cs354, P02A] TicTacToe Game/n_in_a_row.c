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
// Online sources:   
////////////////////////////////////////////////////////////////////////////////
   
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
     
char *DELIM = ",";  // commas ',' are a common delimiter character for data strings

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

void get_dimenstions(FILE *fp, int *size);
int n_in_a_row(int **board, int size);  
  
 

/* ❌
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
 *  // TODO: program must check the return values for errors of the library functions, malloc(), fopen(), and fclose(). Handle errors by displaying an appropriate error message and then calling exit(1).
 * 
 * argc: CLA count
 * argv: CLA value
 */
int main(int argc, char *argv[]) {              
     
    //TODO: Check if number of command-line arguments is correct.
    printf("Ok?");

    exit(0);

/*
    //Open the file and check if it opened successfully.
    FILE *fp = fopen(*(argv + 1), "r");
    if (fp == NULL) {
        printf("Can't open file for reading.\n");
        exit(1);
    }

    //Declare local variables.
    int size;

    //retrieve the board size.
    get_dimensions(fp, &size);

    //TODO: Dynamically/Heap allocate a 2D array of dimensions retrieved above.


    //Read the file line by line. Assuming there are `size` lines where each line has `size` numbers (columns) separated by commas.
    //Tokenize each line wrt the delimiter character to store the values in your 2D array.
    char *line = NULL;
    size_t len = 0;
    char *token = NULL;
    for (int i = 0; i < size; i++) {

        if (getline(&line, &len, fp) == -1) {
            printf("Error while reading the file.\n");
            exit(1);
        }

        // Assuming only 0, 1, or 2 will be the values in the game board. Tokens representation: 0 => unmarked space, 1 => X, 2 => O 
        token = strtok(line, DELIM);
        for (int j = 0; j < size; j++) {
            //TODO: Complete the line of code below
            //to initialize your 2D array.
            //  <ADD ARRAY ACCESS CODE HERE>  = atoi(token);
            token = strtok(NULL, DELIM);
        }
    }

    //TODO: Call the function n_in_a_row and print the appropriate output depending on the function's return value.



    //TODO: Free all dynamically allocated memory. program must properly free up all dynamically allocated memory at the end of the program.

    //Close the file.
    if (fclose(fp) != 0) {
        printf("Error while closing the file.\n");
        exit(1);
    } 


    // TODO: after printing error message call exit(1) if the user invokes the program incorrectly (for example, without any arguments, or with two or more arguments.
     

*/
    return 0;       
}       


/* 
 * Retrieves from the first line of the input file,
 * the size of the board (number of rows and columns).
 * 
 * fp: file pointer for input file
 * size: pointer to size
 */
void get_dimensions(FILE *fp, int *size) {      
    char *line = NULL;
    size_t len = 0;
    if (getline(&line, &len, fp) == -1) {
        printf("Error in reading the file.\n");
        exit(1);
    }

    char *token = NULL;
    token = strtok(line, DELIM);
    *size = atoi(token);
}

/* ❌
 * Returns 1 if and only if the board is in a valid state.
 * Otherwise returns 0.
 * 
 * board: heap allocated 2D board
 * size: number of rows and columns; represting the game board size (square board), where total number of marks on the board will be in the range of 0 to size*size (i.e. dismension of board); Assuming `size` is a positive integer in the range of 3 to 99 inclusive, mathematically:  [3, 99]
 */
int n_in_a_row(int **board, int size) {


    return 0;   
} 

                                        // FIN
