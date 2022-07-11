#include <iostream>

void func1();

using namespace std;

int main(int, char **) {
  int *p{};

  int var{11};

  p = &var;

  cout << *p << endl;

  func1();

  return 0;
}

void func1() {
  cout << "> func1" << endl;

  const char *const arr[]{"sentence1", "sentence2", "sentence3"};

  for (const char *element : arr) {
    cout << element << endl;
  }
  for (int i{0}; i < size(arr); ++i) {
    cout << arr[i] << endl;
    cout << *arr[i] << endl;
  }

  return;
}