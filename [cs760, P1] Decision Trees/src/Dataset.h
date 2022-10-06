#pragma once

#include <bits/stdc++.h>
#include "Dataset.h"

using namespace std;

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

public:
  vector<DataInstance<T>> list{};
  int numberOfFeatures{}; // # of features is fixed for the examples given.
};
