#include <iostream>
#include <limits>

using namespace std;

// size of standard defined types
int main(int, char**) {
  // int of 2 bytes instead of 4 bytes
  short int s;
  signed short int s2;
  unsigned short int s3;

  // 4 bytes int
  int i;
  signed int i2;
  unsigned int i3;
  cout << "# of bytes in int: " << sizeof(int)
       << " with # of digits: " << numeric_limits<unsigned>::digits << endl;

  // 4 or 8 bytes
  long l;
  long int l2;
  signed long int l3;
  unsigned long int l4;
  cout << "# bytes of long: " << sizeof(long) << endl;

  // 16 bytes
  cout << "# bytes of long long: " << sizeof(long long) << endl;

  // 4 bytes
  cout << "# bytes float: " << sizeof(float) << endl;
  // 8 bytes
  cout << "# bytes double: " << sizeof(double) << endl;
  // 16 bytes
  cout << "# bytes long double: " << sizeof(long double) << endl;

  // 1 byte
  cout << "# bytes boolean: " << sizeof(bool) << endl;

  // 1 byte
  cout << "# bytes character: " << sizeof(char) << endl;
  cout << "# bytes unicode char: " << sizeof(char32_t) << endl;

  // Array
  int a1[]{1, 2, 3, 4, 5};
  char a2[]{'a', 'b', 'c', 'd'};  // non-terminated c-string
  cout
      << "c-style string (char arrays): " << a2
      << endl;  // direct output with no null terminated string is unpredictable
  cout << size(a2) << endl;
  char a3[]{'a', 'b', 'c', 'd', '\0'};
  char a5[]{"abcd"};  // appends implicit '\0'
  char a4[5]{'a', 'b', 'c',
             'd'};  // 5th element default initialization is null terminator
  cout << "c-style proper string: " << a3 << endl;
  cout << size(a3) << endl;

  return 0;
}