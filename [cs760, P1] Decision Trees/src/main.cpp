#include <bits/stdc++.h> // includes every standard library and STL include file
#include <iostream>
#include <cassert>
#include <iostream>
#include <string>

#include "utility.cpp"

using namespace std;
using namespace utility;

inline int run() {

  // Note; using float to match the data set accuracy needed, precision of 6 digits
  DataSet<float> D1 = readData<float>("../dataset/D1.txt");
  DataSet<float> D2 = readData<float>("../dataset/D2.txt");
  DataSet<int> D3leaves = readData<int>("../dataset/D3leaves.txt");
  DataSet<float> Dbig = readData<float>("../dataset/Dbig.txt");
  DataSet<float> Druns = readData<float>("../dataset/Druns.txt");

  // cout << setprecision(20) << fixed << D1.list[174].input[1] << endl;

  return 0;
}

int main(int, char**) {
  assert(utility::checkCpp20Support() == 0); // check if compiler supports C++20 features

  run();

  printf("[Main program ended gracefully.]\n");
  return 0;
}
