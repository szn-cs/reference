#include <bits/stdc++.h> // includes every standard library and STL include file
#include <iostream>
#include <cassert>
#include <iostream>
#include <string>
#include <vector>

#include "utility.h"
#include "DecisionTree.h"

using namespace std;
using namespace utility;

namespace plt = matplotlibcpp;
using namespace std;

inline int run() {
  cout << setprecision(6) << fixed;

  // load data sets:
  // Note: using float to match the data set accuracy needed, precision of 6 digits
  const DataSet<float> D1 = readData<float>("../dataset/D1.txt");
  const DataSet<float> D2 = readData<float>("../dataset/D2.txt");
  const DataSet<int> D3leaves = readData<int>("../dataset/D3leaves.txt");
  const DataSet<float> Dbig = readData<float>("../dataset/Dbig.txt");
  const DataSet<float> Druns = readData<float>("../dataset/Druns.txt");

  // TODO: testing requires extension of implementation
  // const DataSet<string> Data_test = readData<string>("../dataset/test.txt");

  // create decision trees:

  // { // ✅
  //     DecisionTree<float> t1{D1};
  //     t1.createDecisionTreeLearner();
  // }

  // { // ✅
  //   const DataSet<int> refuseToSplitDataset = readData<int>("../dataset/refuseToSplitDataset.txt");
  //   DecisionTree<int> t2{refuseToSplitDataset};
  //   t2.createDecisionTreeLearner();
  // }

  // { // ✅
  //   DecisionTree<float> t1{Druns};
  //   t1.createDecisionTreeLearner();
  // }

  // { // ✅
  //   DecisionTree<int> t3{D3leaves};
  //   t3.createDecisionTreeLearner();
  // }

  // { // ✅
  //   DecisionTree<float> t4{D1};
  //   t4.createDecisionTreeLearner();
  // }

  // { // ✅
  //   DecisionTree<float> t5{D2};
  //   t5.createDecisionTreeLearner();
  // }

  { // plots dots
    D1.plotData("1", "r*");
    D2.plotData("2", "b*");
  }

  return 0;
}

int main(int, char**) {
  assert(utility::checkCpp20Support() == 0); // check if compiler supports C++20 features

  run();

  printf("[Main program ended gracefully.]\n");
  return 0;
}
