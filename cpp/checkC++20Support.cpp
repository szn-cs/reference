#include <iostream>

// Check support for C++20 features: compilcation sould pass with no errors.
int main(int, char**) {
  int x = (10 <=> 20) > 0;
  std::cout << x << std::endl;
  return 0;
}