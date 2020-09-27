#include <stdio.h> 

// try it out
int main(void) {
  int a[2][2] = {{1,1}, {1,1}};

  /*1*/ printf("%p\n", &a[1][1]);
  /*2*/ printf("%p\n", a + 1 + 1 ); // false
  /*3*/ printf("%p\n", *(a + 1) + 1 );
  /*4*/ printf("%p\n", *a + 1 + 1 ); // false
  /*5*/ printf("%p\n", *a + 1 + 1 + 1 );
  /*6*/ printf("%p\n", *(*a + 1)); // false
  /*7*/ printf("%p\n", **a + 1 + 1 + 1); // false
  /*8*/ printf("%p\n", **a); // false

  return 0; 
}