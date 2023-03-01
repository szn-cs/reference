#include <stdio.h> 

// function prototype declarations
void printStreamOfChar(void);
void countInputChar(void);
void countBlankTabNewline(void);
void removeRecurreningBlankStream(void);
void countWord(void);
void countDigitWhiteSpaceAndOther(void);
int exponentiation(int base, int exponent);
/* Initial outline: 
  while (line input exist)  
    check if it is longer the preious longest
      save it 
      save its length
  print longest line
*/
void printLongestline(void);

int main() {
  /* uncomment a function to execute */
  // printStreamOfChar();
  // countInputChar();
  // countBlankTabNewline();
  // removeRecurreningBlankStream();
  // countWord();
  // countDigitWhiteSpaceAndOther();
  // printf("%d^%d = %i", 2, 5, exponentiation(2, 5));
  // printLongestline(); 
}

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

/* Raising a base number to the n-th power; n >= 0 */
int exponentiation(int base, int exponent) {
  int power; // result of exponentiation
  for(power = 1; exponent > 0; exponent--)
    power *= base; 
  return power;
}

/* print the longest line and its length */
#define MAXIMUM_LINE_SIZE 1000
void printLongestline() {
  /*
    initial outline: 
    while(character input exist & not newline or EOF and not exeeding limit) 
      write to array line
      track length line
    return length

  */
  int getLine(char lineArray[], int limitInputSize); 
  /* outline: 
    interate over `from` array
      copy each character to `to` array assuming there is enough room
  */
  void copyArray(char from[], char to[]);

  char currentLine[MAXIMUM_LINE_SIZE]; 
  int currentLineLength;
  char longest[MAXIMUM_LINE_SIZE]; 
  int longestLength = 0; 

  while( (currentLineLength = getLine(currentLine, MAXIMUM_LINE_SIZE)) > 0) {
    if(currentLineLength > longestLength) {
      copyArray(currentLine, longest); 
      longestLength = currentLineLength; 
    }
  }

  printf("Longest line of length %i is: %s", longestLength, longest);
}
int getLine(char line[], int limit) {
  int length = 0;
  char c;
  int i; 
  for(i = 0; (c = getchar()) != EOF && c != '\n' && i < limit; i++)
    line[i] = c; 
  if(c == '\n') { // count newline character as part of line
    i++;
    line[i] = c;
  }
  line[i+1] = '\0'; // add terminating character
  length = i; 
  return length;
}
void copyArray(char from[], char to[]) {
  // assuming there is space
  for(int i = 0; (to[i] = from[i]) != '\0'; i++);
} 