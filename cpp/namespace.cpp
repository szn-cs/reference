
void func() {}

namespace X {
void func() {}
void f() {
  ::func();  // calls global defined func
  func();    // calls namespace's defined func
}
}  // namespace X

namespace Y {
void func() {}
}  // namespace Y

namespace Z {
double func2(int a, double b);  // declaration
}

namespace Z {
double func2(int a, double b) { return b; }  // implementation
}  // namespace Z

namespace P {
namespace C {
int x{};
}
}  // namespace P

namespace Alias = P::C;

void main() {
  Z::func2(1, 1.0);
  P::C::x = 2;
}

using namespace Y;  // allows access to the namespace without prefix.
using Y::func;

// anonymous namespace is unique to translation unit.
namespace {  // anonymous namespace
double add(double a, double b);
}

namespace {
double add(double a, double b) { return a; }
}  // namespace