#include <bits/stdc++.h> // includes every standard library and STL include file
#include "Dataset.h"
#include "Formula.h"

using namespace std;
using namespace formula;

template <typename T>
class NN_1 {
public:
  NN_1() = default;
  NN_1(DataSet<T> dataset) : dataset(dataset) {}

  DataInstance<T> predict(T x, T y) {

    float min_value{MAXFLOAT};
    bool min_output{};

    for (auto i : dataset.list) {
      T d = calculateDistance_L1<T>(x, y, i.input[0], i.input[1]);
      if (d < min_value) {
        min_value = d;
        min_output = i.output;
      }
    }

    DataInstance<T> p{x, y, min_output};
    return p;
  }

public:
  DataSet<T> dataset{};
};