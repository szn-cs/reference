#include <stdio.h> 

/* redirect input to output: usage of streams and EOF value (Ctrl+D) */
void printStreamOfChar() { 
  int c; // use int to allow EOF character used by stdio library to be saved (which is larger than char 1 byte)

  while( (c = getchar()) != EOF )
    putchar(c);
}

/* count number of input characters */
void countInputChar() {
  long counter = 0; // Note: could use double to store larger numbers (as double stores mantissa /  significant numbers in a different way & it sacrifices precision)
  for (;getchar() != EOF; counter++)
    ; // empty body
  printf("%ld\n", counter);
}

/* count blanks, tabs, & newlines in a steam */
void countBlankTabNewline() {
  long counter = 0; // or could use double for allowing larger numbers
  int c; // NOTE: using int instead of char to allow storage of EOF stdio symbolic constant.
  while((c = getchar()) != EOF) {
    switch (c) {
      // NOTE: to pass backspaces as input, switching to raw mode is required.
      case ' ': 
      case '\t': 
      case '\n': 
        printf("✅");
        counter++;
      break;
      default: 
        printf("❌");
      break;
    }
  }
  printf("\n%ld\n", counter);
}

/* copy input to output replacing reccurring blanks */
void removeRecurreningBlankStream() {
  int input; // NOTE: using integer instead of char to allow for EOF storage (which is an integer bigger than 1 byte of char type equivalent numerical values)
  int previousChar;
  while ((input = getchar()) != EOF) {
    if(input == ' ' && previousChar == ' ') 
      continue;
    putchar(input); 
    previousChar = input;
  }
}

/* count words, newlines, characters from an input stream */
#define OUT 0 
#define IN 1
void countWord() {
  int c; // character stream input
  int state = OUT; // current state in the stream
  int countWord = 0, countNewLine = 0, countCharacter = 0;
  while( (c = getchar()) != EOF) {
    countCharacter++; 
    switch(c) {
      case '\n': 
        countNewLine++;
      case ' ': 
      case '\t': 
        state = OUT;
      break; 
      default: 
        if(state == OUT) {
          state = IN; 
          countWord++;
        }
      break;
    }
  }
  printf("word: %i, newline: %i, character: %i", countWord, countNewLine, countCharacter);
}

/* counts reccurrences of digits, white-space, and other characters separately */
void countDigitWhiteSpaceAndOther() {
  int c; // character input
  int cWhitespace, cOther; // Counters
  cWhitespace = cOther = 0; // initialize counters
  int cDigit[10]; // digits counter
  int cDigitLength = sizeof(cDigit)/sizeof(int); 
  // initialize digits counter elements: 
  for(int i = 0; i < cDigitLength; i++) 
    cDigit[i] = 0;

  while((c = getchar()) != EOF) {
    if(c == ' ' || c == '\t' || c == '\n')  // whitespace
      ++cWhitespace; 
    else if (c >= '0' && c <= '9') // digits
      ++cDigit[c - '0']; // match values to proper indexes
    else // other 
      ++cOther; 
      }
  
  printf("Digits: ");
  for(int i = 0; i < cDigitLength; i++) 
    printf("#%c: %d,", '0' + i, cDigit[i]);
  printf(", White-space: %d, Other characters: %d\n", cWhitespace, cOther);
}

int main() {
  /* uncomment a function to execute */
  // printStreamOfChar();
  // countInputChar();
  // countBlankTabNewline();
  // removeRecurreningBlankStream();
  // countWord();
  countDigitWhiteSpaceAndOther();
}

