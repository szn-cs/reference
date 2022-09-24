/** 
  Documentaiton: for testing modified fsync() code inside the kernel
  gcc ./program.c -o program
**/ 

#include <stdio.h> 
#include <stdlib.h>
#include <unistd.h>
#include "./kernel/arch/x86/include/generated/uapi/asm/unistd_64.h"

#define SRC "./test"
#define DEST "./target"

void createFile();

int main(int argc, char* argv[] ) {
  printf("Test strat [\n");
  syscall(__NR_immortalCLang,3+3+5);
  createFile();
  syscall(__NR_immortalCLang,3+3+5);
  printf("] Test end\n");

  return 0;
}

void createFile() { 
   int num;
   FILE *fptr;

   // use appropriate location if you are using MacOS or Linux
   fptr = fopen("./file.txt","w+");

   if(fptr == NULL) { printf("Error!"); exit(1); }
   printf("[test.c] file descriptor = %i \n", fptr);
   
   int fd = fileno(fptr);
   int r = fsync(fd); 
   printf("[test.c] fsync call returned value = %i \n", fptr);

   fprintf(fptr, "data... \n"); fputs("more data... \n", fptr);
   fclose(fptr);
}
