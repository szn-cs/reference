#include <bits/stdc++.h> // includes every standard library and STL include file
#include <iostream>
#include <cassert>
#include <iostream>
#include <string>
#include <vector>

#include "utility.h"
#include "Formula.h"
#include "classes.h"
#include "matplotlibcpp.h" // plotting

using namespace std;
using namespace utility;
using namespace formula;

namespace plt = matplotlibcpp;

inline int run() {
  cout << setprecision(6) << fixed;

  // load data sets:
  // Note: using float to match the data set accuracy needed, precision of 6 digits
  const DataSet<float> D1 = readData<float>("../dataset/D2z.txt");
  // const DataSet<float> D2 = readData<float>("../dataset/emails.csv");

  { // plots dots
    NN_1<float> nn{D1};

    vector<array<float, 2>> input_list{};
    for (float i = -2.0; i <= 2.0 || compareDouble(i, 2.0); i = i + 0.1) {
      for (float t = -2.0; t <= 2.0 || compareDouble(t, 2.0); t = t + 0.1) {
        input_list.push_back({i, t});
      }
    }

    vector<DataInstance<float>> inputInstance_list{};
    for (auto [x, y] : input_list) {
      inputInstance_list.push_back(nn.predict(x, y));
    }

    for (DataInstance i : inputInstance_list) {
      auto [x, y] = i.input;
      if (bool(i.output) == true)
        plt::plot({x}, {y}, "g4");
      else
        plt::plot({x}, {y}, "r3");
    }

    D1.plotData("go", "ro", "D2z plot", {-2.0, 2.0}, {-2.0, 2.0});

    D1.outputPlot("1");
  }

  return 0;
}

int main(int, char**) {
  assert(utility::checkCpp20Support() == 0); // check if compiler supports C++20 features

  run();

  printf("[Main program ended gracefully.]\n");
  return 0;
}
