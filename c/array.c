#include <stdio.h> 
#include <stdlib.h> 

#define ROW 2
#define COLUMN 4

void stack2DArray(); 
void heap2DArray(); 

int main(void) { 
  // stack2DArray();
  // heap2DArray();

  return 0; 
}


/** 2D Stack Allocated Array using identifier declaration **/ 
void stack2DArray() {
  int x[ROW][COLUMN] = {{0,0,0,0}, {0,0,0,0}}; // initialize all element columns with zero

  for(int i = 0; i < ROW; i++) 
    for(int j = 0; j < COLUMN; j++) { 
      // Using indexing and address arithmetic
      x[i][j] = i + j;
      *(*x + i * COLUMN + j) = i + j;
    }
  
  for(int i = 0; i < ROW; i++)  {
    for(int j = 0; j < COLUMN; j++) {
      printf("[%i,%i]:%i\t", i, j, x[i][j]);
    }
    printf("\n");
  }
}

/** 2D Heap allocated array: using indexing **/ 
void heap2DArray() {
  int** a = malloc(sizeof(int*) * ROW); 
  for(int i = 0; i < ROW; i++)
    a[i] = malloc(sizeof(int) * COLUMN); 
  
  for(int i = 0; i < ROW; i++) 
    for(int j = 0; j < COLUMN; j++) {
      // using indexing and address arithmetic
      a[i][j] = i + j;
      *((*(a + i)) + j) = i + j; 
    }

  for(int i = 0; i < ROW; i++) {
    for(int j = 0; j < COLUMN; j++) 
      printf("[%i,%i]: %i\t", i, j, a[i][j]);
    printf("\n");
  }
  
}