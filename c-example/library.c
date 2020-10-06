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

