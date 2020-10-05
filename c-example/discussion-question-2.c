#include <stdio.h> 
#include <stdlib.h> 
#define ROW 2
#define COLUMN 4
void stack2DArray(); 
int main(void) { 
   stack2DArray(); 
   return 0; 
}

/** 2D Stack Allocated Array using identifier declaration **/ 
void stack2DArray() {
  int x[ROW][COLUMN] = {{1,2,3,4}, {5,6,7,8}}; 
  // 1. if both x and *x return the same address ...   
  printf("%p, %p, %p\n", x, *x, &**x);
  
  printf("%p\n", &**x + 1);    // implicitely:  address + sizeof(int)
  printf("%p\n", *(x) + 1);    // implicitely:  address + sizeof(int *) 
  printf("%p\n", x + 1);       // implicitely:  address + sizeof(int **)


}

