
class C_P {
 public:
  C_P() = default;
  virtual ~C_P();  // required for deleting a derived class object using a
                   // pointer of base class. To make sure specific destructor is
                   // called too.

  void print() {}
  virtual void print_virtual() {}
  virtual void print_final() {}

  static int x;
};

class C_C : public C_P {  // allows access to public members only of base class.
  using C_P ::C_P;        // Inheriting the constructor:  will generate a
                    // constructor that will initialize only the base class
                    // e.g. C_C() : C_P(){};

 public:
  void print() {}  // override base class's member function
  virtual void print_virtual() override {}  // override specifier prevents
                                            // symbol spelling mistakes (typos)
  void print_final()
      final{};  // prevent subclasses from overriding this function.

  static int x;  // overriden
};

// No further subclasses allowed
class C_C2 final : public C_P {
};  // C_C2 declared as final and cannot be derived from.

void main() {
  C_C c{};

  c.print();
  c.C_P::print();

  {
    // polymorphism
    C_P* p = new C_C;
    C_P& ref{*p};
    // polymorphism using virtual functions
    ref.print_virtual();  // most specific virtual function is called.
  }

  {  // object slicing (size decreases)
    C_C a{};
    C_P b = a;
  }

  {  // dynamic cast
    C_P* c_p = new C_C();
    C_C* c_c =
        dynamic_cast<C_C*>(c_p);  // returns nullptr if transformation fails

    C_C ref{};
    C_P& c_p_ref = ref;
    C_C* c_c_p{dynamic_cast<C_C*>(&ref)};
  }
};
