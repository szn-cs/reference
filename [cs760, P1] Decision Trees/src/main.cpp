#include <bits/stdc++.h> // includes every standard library and STL include file
#include <iostream>
#include <cassert>
#include <iostream>
#include <string>

#include "utility.cpp"

using namespace std;
using namespace utility;

inline int run() {
  DataSet<double> D1 = readData<double>("../dataset/D1.txt");
  DataSet<double> D2 = readData<double>("../dataset/D2.txt");
  DataSet<int> D3leaves = readData<int>("../dataset/D3leaves.txt");
  DataSet<double> Dbig = readData<double>("../dataset/Dbig.txt");
  DataSet<double> Druns = readData<double>("../dataset/Druns.txt");

  return 0;
}

int main(int, char**) {
  assert(utility::checkCpp20Support() == 0); // check if compiler supports C++20 features

  run();

  printf("[Main program ended gracefully.]\n");
  return 0;
}
