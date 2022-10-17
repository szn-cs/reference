#pragma once

#include <bits/stdc++.h>
#include <iostream>
#include "Dataset.h"
#include "matplotlibcpp.h" // plotting
using namespace std::string_literals;

using namespace std;
namespace plt = matplotlibcpp;

template <typename T = float>
class DataInstance {
public:
  DataInstance(T feature1, T feature2, bool label) {
    input[0] = feature1;
    input[1] = feature2;
    output = label;
  }

  // TODO: create iterators for each features values

public:
  array<T, 2> input{}; // instance of two numeric features
  bool output{};       // nominal label of 0, 1
};

template <typename T>
class DataSet {
public:
  DataSet() = default;
  DataSet(vector<DataInstance<T>> list, int numberOfFeatures = 2) : list(list), numberOfFeatures(numberOfFeatures) {}

  bool getMajorityLabel() const {
    if (list.empty())
      return true;

    // count labels of true and false
    map<bool, int> countLabel{};
    for (DataInstance<T> i : list) {
      countLabel[i.output]++;
    }
    // check majority label
    auto comparison = countLabel[true] <=> countLabel[false];
    // If ans is less than zero
    if (comparison > 0 || comparison == 0)
      return true;
    else
      return false;
  }

  tuple<int, int> countBinaryLabel() const {
    int positiveCount{}, negativeCount{};
    for (DataInstance instance : list)
      (instance.output == true) ? positiveCount++ : negativeCount++;
    return make_tuple(positiveCount, negativeCount);
  }

  // check documentation https://matplotlib-cpp.readthedocs.io/en/latest/docs.html#matplotlibcpp-namespace
  void plotData(string formatTrue = "r*", string formatFalse = "b+", string title = "", tuple<float, float> xlim = {}, tuple<float, float> ylim = {}) const {
    std::vector<double> x{}; // axis-x feature 0
    std::vector<double> y{}; // axis-y feature 1

    // plt::clf();

    if (!title.empty())
      plt::title(title);

    {
      auto [x_start, x_end] = xlim;
      if (x_start != x_end)
        plt::xlim(x_start, x_end);
    }

    {
      auto [y_start, y_end] = ylim;
      if (y_start != y_end)
        plt::ylim(y_start, y_end);
    }

    // print points with true value
    for (DataInstance i : list) {
      if (bool(i.output) == true) {
        x.push_back(i.input[0]);
        y.push_back(i.input[1]);
      }
    }
    plt::plot(x, y, formatTrue);

    x.clear();
    y.clear();

    // print points with false value
    for (DataInstance i : list) {
      if (bool(i.output) == false) {
        x.push_back(i.input[0]);
        y.push_back(i.input[1]);
      }
    }
    plt::plot(x, y, formatFalse);
  }

  void outputPlot(string filename = "output") const {
    // plt::show();
    plt::legend();
    plt::save("../output/" + string(filename) + ".png");
  }

public:
  vector<DataInstance<T>> list{};
  int numberOfFeatures{}; // # of features is fixed for the examples given.
};
