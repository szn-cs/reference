/* Celsius-Franheit Conversion tables: using floating point numbers */

#include <stdio.h> 

#define LOWER 0   // lower limit of table
#define UPPER 200 // upper limit
#define STEP 20   // step size

void celsiusToFarenheit() {
  float celsius, farenheit; 
  int lower = LOWER, upper = UPPER, step = STEP; // ranges of values with mathematical range: [lower, upper]
 
  for(celsius = lower; celsius <= upper; celsius += step) {
    farenheit = (9.0/5.0 * celsius) + 32.0;// formula conversion mathematically: F = 9/5 x C + 32
    printf("%3.0f\t%6.1f\n", celsius, farenheit);
  }
}

void farenheitToCelsius() {
  float celsius, farenheit; 
  int lower = LOWER, upper = UPPER, step = STEP; // ranges of values with mathematical range: [lower, upper]

  for(farenheit = lower; farenheit <= upper; farenheit += step) {
    celsius = 5.0 / 9.0 * (farenheit - 32);// formula conversion mathematically: C = 5/9 * (F - 32)
    printf("%3.0f\t%6.1f\n", farenheit, celsius);
  }
}

int main() {
  printf("\n%s\n", "Celsius to Farenheit Conversion Table:");
  celsiusToFarenheit();
  printf("\n%s\n", "Farenheit to Celsius Conversion Table:");
  farenheitToCelsius();
}
