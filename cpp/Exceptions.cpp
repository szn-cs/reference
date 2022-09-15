#include <string>

void main() {
  try {
    throw 0;

  } catch (int ex) {
    throw;  // rethrow the exception (without copying)
    // throw ex; // slices information (removes derived classes and keeps base
    // class - in case expection is an object)
  } catch (std::string ex) {
  } catch (...) {
    // Ellipses catch block - catches all types of exceptions.
  }
}

void func() noexcept {  // exception thrown are not propagated to calling
                        // fucntions.
}

class C {
 public:
  C() {}
  ~C() noexcept(false) {
    throw;
  }  // propagate exceptions in deconstructors which have implicit noexcept
     // applied by default
};
