///////////////////////////////////////////////////////////////////////////////
//
// Copyright 2020 Jim Skrentny
// Posting or sharing this file is prohibited, including any changes/additions.
// Used by permission, CS 354 Fall 2020, Deb Deppeler
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        myMagicSquare.c
// This File:        myMagicSquare.c
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
#include <err.h> 
#include <errno.h>
#include <stdbool.h> 

// function prototype declarations:
void clear2DArray(int **array, int row, int column); 
void print2DArray(int **array, int *rows, int *columns, char *delimiter);
void* freeNestedArrays(int **array, int row); 

const char *DELIMITER = ","; // columns delimiter
// Structure that represents a magic square
typedef struct {
    int size; // dimension of magic square (# of rows & columns each)
    int **magic_square; // pointer to heap allocated magic square
} MagicSquare;

/* 
 * Retrieve & validate user input of the magic square's size.
 * size range: odd and >=3
 * 
 * returns size of the 2D square (# of rows/columns)
 */
int getSize() {
    int size = 0; // square matrix dimension

    // Read user input: assuming an integer
    printf("Enter magic square's size (odd integer >=3)\n"); // prompt instruction
    char* line = NULL; // buffer pointer
    size_t n = 0; 
    if(getline(&line, &n, stdin) == -1) {
        fprintf(stderr, "Error in reading user input; %s\n", strerror(errno));
        exit(1);
    }
    // parse integer from input string
    size = atoi(line); 

    // validate dimension range: must be at least size 3
    if(size < 3) {
        fprintf(stderr, "Size must be >= 3.\n");
        exit(1);
    }

    // validate dimension parity: must be odd dimension
    if(size % 2 == 0) {
        fprintf(stderr, "Size must be odd.\n"); 
        exit(1);
    }

    free(line); line = NULL; // clean up buffer
    return size;   
} 

/*
 * Square matrix traversal following the Alternate Siamese algorithm. The function 
 * alters the values of row & column to position of the next cell traversal in the right 
 * cells' order that should be filled incrementally.
 * 
 * size: dimension of a square matrix
 * r: index value of current row position
 * c: index value of current column position
 */ 
void nextTraversal(int size, int *r, int *c) {
    // TODO: implement algorithm traversal rules.
    if(*c == size - 1) {
        *r = *r + 1;
        *c = 0;
    } else 
        *c = *c + 1;
}

/* 
 * Makes a magic square of size n using alternate Siamese algorithm.
 * <p>
 * Magic square possible generation algorithms: 
 *      1. Siamese method
 *      2. Alternate Siamese method
 * Every number in the matrix will be unique.
 * 
 * n: the number of rows and columns
 * completed MagicSquare struct
 */
MagicSquare* generateMagicSquare(int n) {
    // create 2D array (heap allocated)
    // allocate rows
    int **matrix = malloc(sizeof(int) * n);
    if(matrix == NULL) {
        fprintf(stderr, "Failed to allocated memory; %s\n", strerror(errno));
        exit(1);
    }
    // allocate columns
    for(int r = 0; r < n; r++)  {
        *(matrix + r) = malloc(sizeof(int) * n); 
        if(*(matrix + r) == NULL) {
            fprintf(stderr, "Failed to allocated memory; %s\n", strerror(errno));
            exit(1);
        }
    }
    // initialize 2D array with zeros.
    clear2DArray(matrix, n, n); 

    // allocate magic square's structure on heap
    MagicSquare* ms = malloc(sizeof(MagicSquare));
    if(ms == NULL) {
        fprintf(stderr, "Failed to allocated memory; %s\n", strerror(errno));
        exit(1);
    } 
    // initialize members
    ms->size = n; 
    ms->magic_square = matrix;

    // Generate numbers using Alternate Siamese method
    int cellsNumber = n*n; // total matrix size
    int r = 0, c = 0; // current starting cell position
    for(int value = 1; value <= cellsNumber; value++) {
        // set the incremental value of current cell following the method rules 
        *(*(matrix + r) + c) = value;
        // get next cell position according to algorithm used
        nextTraversal(ms->size, &r, &c);
    } 

    return ms;    
} 

/*  
 * Opens a new file (or overwrites the existing file) and writes the square in the 
 * specified format.
 * <p>
 * Format of output file: 
 *      • 1st line: size of the matrix (positive integer).
 *      • following lines: martix's rows, where columns will be delimited.
 *
 * magicSquare: the magic square to write to a file filename the name of the output file
 */
void fileOutputMagicSquare(MagicSquare *magicSquare, char *filename) {
    // open file descriptor
    FILE *fileStream = fopen(filename, "w");
    if (fileStream == NULL) {
        fprintf(stderr, "Can't open file for writing; %s\n", strerror(errno /*ENOENT*/));
        exit(1);
    }
    
    // write size value to file on first line
    if(fprintf(fileStream, "%i\n", magicSquare->size) < 0) {
        fprintf(stderr, "Error writing to file; %s\n", strerror(errno));
        exit(1);
    }

    // write matrix's rows line-by-line. 
    int **m = magicSquare->magic_square; // pointer to the 2D array 
    int *size = &(magicSquare->size); // pointer to size value
    for(int r = 0; r < *size; r++) {
        bool lastR = (r == *size - 1) ? true : false; // is last row
        for(int c = 0; c < *size; c++) {
            bool lastC = (c ==  *size - 1) ? true : false; // is last column
            // column ending or line ending
            const char *postfix = lastC ? lastR ? "" : "\n" : DELIMITER;
            // write current column to stream buffer
            if(fprintf(fileStream, "%i%s", *(*(m + r) + c), postfix) < 0) {
                fprintf(stderr, "Error writing to file; %s\n", strerror(errno));
                exit(1);
            }
        }
    }

    // close the file.
    if (fclose(fileStream) != 0) {
        fprintf(stderr, "Error while closing the file; %s\n", strerror(errno));
        exit(1);
    }
}

/* 
 * Generates a normal magic square of the user specified size and output the square to 
 * the output filename. 
 * <p>
 * A magic square is a matrix of size n x n with positive numbers from 1 … n2 arranged 
 * such that the numbers in any line (horizontal, vertical, and both main diagonals) 
 * sum to the same values. Where, constant sum: "magic constant", & integer n = "order"
 * of the magic square.
 * 
 * argc: command-line argument count
 * argv: command-line argument value
 */
int main(int argc, char *argv[]) {
    // validate argument presence: must pass filename program argument
    if(argc < 2) {
        char* executableName = (*argv) != NULL ? (*argv) : "./myMagicSquare";
        fprintf(stderr, "Usage: %s <output_filename>\n", executableName);
        exit(1);
    }
    
    char* filename = *(argv + 1); // Get output filename from input argument
    int size = getSize(); // retrieve size dimension of magic square from the user.
    
    // Generate the magic square
    MagicSquare *magicSquare = generateMagicSquare(size);
    // Output the magic square
    fileOutputMagicSquare(magicSquare, filename);

    // free struct and associated arrays
    magicSquare->magic_square = freeNestedArrays(magicSquare->magic_square, size);
    free(magicSquare); magicSquare = NULL;

    return 0;
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

                                                         
//				myMagicSquare.c      

