// USE for testing rsync modified code in kernel
// gcc ./program.c -o program

#include <stdio.h> 
#include <stdlib.h>

#define SRC "./test"
#define DEST "./target"

void createFile() { 
   int num;
   FILE *fptr;

   // use appropriate location if you are using MacOS or Linux
   fptr = fopen("./file.txt","rw");

   if(fptr == NULL)
   {
      printf("Error!");   
      exit(1);             
   }
   
   fprintf(fptr,"%d", 01010101010101010101);

   fclose(fptr);
}

void rsyncCall() { 
    // system("rsync -avu " SRC " " DEST);
}

int main() {
  printf("progam is executing...");

  rsyncCall(); 

  createFile();

  printf("progam terminating...");

  return 0;
}
