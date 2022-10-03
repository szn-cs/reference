#pragma once

#include <bits/stdc++.h>

using namespace std;

template <typename T = float>
class DataInstance {
public:
  DataInstance(T feature1, T feature2, bool label) {
    input[0] = feature1;
    input[1] = feature2;
    output = label;
  }

public:
  array<T, 2> input{}; // instance of two numeric features
  bool output{};       // nominal label of 0, 1
};

template <typename T>
class DataSet {
public:
  DataSet(vector<DataInstance<T>> list) : list(list) {}

public:
  vector<DataInstance<T>> list{};
};