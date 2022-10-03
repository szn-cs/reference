#pragma once

#include <bits/stdc++.h>
#include "DecisionTree.cpp"

using namespace std;

namespace utility {
int checkCpp20Support() { return (10 <=> 20) > 0; }

void dummyLoop(size_t length) {
  for (size_t i = 0; i < length; i++) {
    i--;
    i++;
  }
}

vector<string> readFileLines(string path) {
  fstream file;
  string line{};
  vector<string> lines{};

  file.open(path, ios::in);
  assert(file.is_open());

  while (getline(file, line)) {
    lines.push_back(line);
  }

  file.close();

  printf("- Read %u lines.\n", lines.size());
  return lines;
}

template <typename T = float>
vector<DataInstance<T>> readLineValues(vector<string> lines) {
  vector<DataInstance<T>> instanceList{};

  for (string l : lines) {
    T feature1{}, feature2{};
    bool label{};

    // TODO: NOTE: double precision issue - istringstream converts some doubles to close values with long decimal value
    istringstream ls(l); // line stream
    ls >> setprecision(6) >> fixed >> feature1;
    ls >> setprecision(6) >> fixed >> feature2;
    ls >> label;

    {
      // assert(is_same<T, double>::value || is_same<T, int>::value);
      // if (is_same<T, double>::value)
      // feature1 = stod(l);
      // feature2 = stod(l);
      // else if (is_same<T, int>::value)
      //   feature1 = stoi(l);
    }

    instanceList.push_back(DataInstance<T>(feature1, feature2, label));
  }

  return instanceList;
}

template <typename T>
DataSet<T> readData(string path) {
  vector<DataInstance<T>> l = readLineValues<T>(readFileLines(path));
  return DataSet<T>(l);
}
}; // namespace utility
