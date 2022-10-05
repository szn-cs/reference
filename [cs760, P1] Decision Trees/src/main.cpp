#include <bits/stdc++.h> // includes every standard library and STL include file
#include <iostream>
#include <cassert>
#include <iostream>
#include <string>

#include "utility.cpp"
#include "DecisionTree.cpp"

using namespace std;
using namespace utility;

inline int run() {
  cout << setprecision(6) << fixed;

  // load data sets:
  // Note; using float to match the data set accuracy needed, precision of 6 digits
  DataSet<float> D1 = readData<float>("../dataset/D1.txt");
  DataSet<float> D2 = readData<float>("../dataset/D2.txt");
  DataSet<int> D3leaves = readData<int>("../dataset/D3leaves.txt");
  DataSet<float> Dbig = readData<float>("../dataset/Dbig.txt");
  DataSet<float> Druns = readData<float>("../dataset/Druns.txt");

  // create decision trees:
  DecisionTree t1{D1};
  t1.findBestCandidateFeatureSplit();

  return 0;
}

int main(int, char**) {
  assert(utility::checkCpp20Support() == 0); // check if compiler supports C++20 features

  run();

  printf("[Main program ended gracefully.]\n");
  return 0;
}
