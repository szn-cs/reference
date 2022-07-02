#include <iostream>

using namespace std;

int main() {
  int arr[5]{2, 3, 8, 9, 6};

  int max = 0;

  for (int i{0}; i < 5; ++i) {
    if (max < arr[i]) max = arr[i];
  }

  cout << max << endl;
  auto result = (10 <=> 20) > 0;
  cout << result;

  return 0;
}
