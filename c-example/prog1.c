/* title: First C Program
* file: prog1.c
* author: Jim Skrentny
*/

#include <stdio.h> // for printf fprintf fgets
#include <stdlib.h> // for malloc
#include <string.h> // for strlen

int main() {
  // Prompt and read user's CS login
  char *str = malloc(50);
  
  printf("Enter your CS login: ");
  
  if (fgets (str, 50, stdin) == NULL)
    fprintf(stderr, "Error reading user input.\n");
  
  // Terminate the string
  int len = strlen(str);
  
  if (str[len - 1] == '\n') {
    str[len - 1] = '\0';
  }
  
  // Print out the CS login
  printf("Your login: %s\n", str);
  
  return 0;
}
