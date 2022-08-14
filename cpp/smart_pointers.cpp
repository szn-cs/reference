#include <memory>
class A {
}

void main() {
}

void unique_ptr() {
  {
    A* a = new A();
    std::unique_ptr<A> u_a{
        a};  // manages the memory of "a" pointer, releases pointer memory when
             // variable goes out of scope.
    u_a.get();    // return the address it manages.
    u_a.reset();  // explicitly release memory.
  }

  {
    std::unique_ptr<A> u_a =
        std::make_unique<A>();  // allocates memory it manages
    // equivalent to:
    std::unique_ptr<A> u_a{new A()};
  }

  {
    std::unique_ptr<int> u_a = std::make_unique<int>();
    std::unique_ptr<int> u_b =
        std::move(u_a);  // move ownership to another unique pointer that will
                         // manage the memory instead.
  }

  {  // unique pointer to array
    auto array_ptr =
        std::unique_ptr<A[]>(new A[3]{A(), A(), A()});  // array initialization
    auto array_ptr2 = std::make_unique<A[]>(3);         // 3 element array
  }
}

void shared_ptr() {
  {
    std::shared_ptr<A> s_a1{new A()};
    std::shared_ptr<A> s_a2 = s_a1;
    s_a1.use_count();  // get reference count
    s_a1.get();        // get managed pointer
  }

  {
    std::shared_ptr<int> s_a =
        std::make_shared<int>(5.0);  // initialize without new operator
  }

  {  // move ownership from unique to shared poitner
    std::unique_ptr<int> u = std::make_unique<int>(10);
    std::shared_ptr<int> s =
        std::move(u);  // u will point to nullptr and shared pointer will manage
                       // the pointee instead.
  }

  {  // casting pointer
    std::shared_ptr<int> p = std::make_shared<int>(10);
    static_cast<bool>(p);
  }

  {
    std::shared_ptr<int[]> s_arr_ptr(
        new int[10]{1, 2, 3, 4});  // shared pointer to array
  }
}

void weak_ptr() {
  {
    std::shared_ptr<int> s_a = std::make_shared<int>(10);
    std::weak_ptr<int> w_a(s_a);
    std::shared_ptr<int> s_b =
        w_a.lock();  // use pointer by converting from weak to shared.
  }

  {
    class C {
     public:
      C() = default;
      ~C();

     private:
      std::weak_ptr<C> a;  // prevents leaked memory when releasing objects that
                           // reference another objects
    };
  }
}