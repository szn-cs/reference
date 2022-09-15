#include <cassert>
#include <compare>
#include <iostream>

class C2;

class C {
  friend C operator+(const C&, const C&);
  friend std::ostream& operator<<(std::ostream&, const C&);
  friend std::istream& operator>>(std::istream&, const C&);
  friend C& operator+=(C&, C&);

 public:
  C() = default;
  explicit C(double x);
  ~C() = default;

  C operator+(const C& right) const {
    return C(this->_x + right._x);
  }  // operator overloading as member function

  double& operator[](
      size_t index) {  // returning reference allows modifying the values
    assert(index == 0);
    return this->_x;
  }

  // prefix operator
  void operator++() { ++_x; }

  // postfix
  C operator++(int) {
    C local_c(*this);
    ++(*this);
    return local_c;  // grab value first and then increment
  }

  // copy assignment oprerator (different from copy constructor)
  C& operator=(const C& right) {
    /** General flow
     * 1. release memory from this
     * 2. Allocate new dynamic memory
     * 3. Copy data over from right
     */

    if (this != &right) {
      this->_x = right._x;
    }
    return *this;
  }

  // copy assignemnt operator for different type
  C& operator=(const C2& right) { /*...*/
  }

  // Functor (call operator overloading)
  void operator()(int x) { /* ... */
  }

  // comparison operator
  bool operator<=(const C& other) const;

  /** Comparison 3-way operator - some latest version of C++ 20 implementation
   * support it.
   * Using std::partial_ordering because _x is a double which support it.

  std::partial_ordering C::operator<=>(const C& right) const {
    if (this->_x > right._x)
      return std::partial_ordering::greater;
    else if (this->_x == right._x)
      return std::partial_ordering::equivalent;
    else if (this->_x < right._x)
      return std::partial_ordering::less;
    else
      return std::partial_ordering::unordered;
  }

  bool C::operator==(const C& right) const {
    return (this->_x == right._x);
  }

  */

 private:
  double _x{};
};

// operator overloading as a standalone function (non-member)
inline C operator+(const C& left, const C& right) {
  return C(left._x + right._x);
}

// operator overloading of stream insertion operator
inline std::ostream& operator<<(std::ostream& os, const C& c) {
  os << "member: " << c._x;
  return os;
}

inline C& operator+=(C& left, C& right) {
  left._x += right._x;
  return left;
}

inline C operator--(C& c, int) {
  // ....
}

// Note: inline is used when the definition exists in the header file and is
// included multiple times.
inline std::istream& operator>>(std::istream& is, C& c) {
  double x;
  std::cout << "Type double value: " << std : endl;
  is >> x;

  c._x = x;

  return is;
}

void main() {
  {
    C a{1.0};
    C b{2.0};
    a + b;
    // equivalent to
    a.operator+(b);
    operator+(a, b);
  }

  {  // Overload subscript operator
    C a{1.0};
    a[0];
    // equevalent to:
    a.operator[](0);

    a[0] = 3.0;
  }

  {
    // stream insertion operator
    C a{1.0};
    std::cout << a;  // a stand alone function implemention follows the
                     // convetion rather than using a memeber function

    std::cin >> a;
  }
}
