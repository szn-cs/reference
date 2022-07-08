#include <iomanip>
#include <iostream>

using namespace std;

// constants
const int x1{5};
constexpr int x2{x1 + 1};
const constinit int x4{x2};
static_assert(x2 == 6);

int main(int, char**) {
  int r;       // contains garbage data
  int e{};     // initializes to 0
  int x{100};  // declare, define, with value initialize.
  int t = 5;   // copy initialization

  // int y{5.5};  // value initialization produces error/warnning (safe
  // conversion)
  int z(5.5);  // direct/functional initialization applies implicit/silent
               // conversion
  unsigned int c{55u};
  unsigned int c2{5'555'5u};
  unsigned int b{0b101010101010u};

  // cout << x + y + z << endl;

  float r{5.5f};
  // scientific notation
  double d{3.475e-11};  // multiply 10^-11
  // Infinity
  double i{1.0 / 0.0};
  cout << "infinity: " << i << endl;

  unsigned long l1{999ul};
  long long l1{999ll};

  char c{'a'};
  char c2{65};
  cout << c << " " << c2 << " " << static_cast<int>(c) << endl;

  cout << fixed << showpoint
       << setprecision(50);  // show 50 digits after point and show value in
                             // fixed notation.
  cout << 5.472359024357 + 123.4123e-11 << endl;

  bool flag{false};
  cout << flag << " " << true << endl;
  cout << boolalpha;  // force true/false output format instead of 1/0
  cout << flag << " " << true << endl;

  return 0;
}
