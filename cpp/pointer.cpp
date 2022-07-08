#include <iostream>

using namespace std;

int main(int, char **) {
  int *p{};

  int var{11};

  p = &var;

  cout << *p << endl;
  return 0;
}