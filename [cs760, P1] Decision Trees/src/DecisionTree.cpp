#pragma once

#include <bits/stdc++.h>
#include "utility.cpp"
#include "Formula.cpp"
#include "BinaryTree.cpp"

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
  DataSet() = default;
  DataSet(vector<DataInstance<T>> list) : list(list) {
  }

public:
  vector<DataInstance<T>> list{};
  int numberOfFeatures{2}; // # of features is fixed for the examples given.
};

template <typename T>
class NodeData {
public:
  NodeData(DataSet<T>& dataset) : NodeData(dataset, -1, INFINITY){};
  NodeData(DataSet<T>& dataset, int splitFeature, T splitThreshold) : dataset(dataset), splitFeature(splitFeature), splitThreshold(splitThreshold){};

public:
  const DataSet<T> dataset{}; // dataset considered for split in this subtree level
  int splitFeature{-1};       // feature chosen to split the node by.
  T splitThreshold{};         // threshold to split numeric values on.
};

template <typename T>
class DecisionTree {
public:
  DecisionTree() = default;
  DecisionTree(DataSet<T>& dataset) : dataset(dataset) {
    root = make_shared<Node<T>>(dataset);
    for (int i = 0; i < dataset.numberOfFeatures; i++)
      candidateFeature.push_back(i);
  }

  T findBestCandidateThresholdSplit(int feature /*feature being investigated*/) {
    for (DataInstance i : dataset.list) {
      T c = i.input[feature]; // candidate threshold

      T infoGain = calculateInfoGainOnThresholdSplit(c, feature);
    }

    return 0.0;
  }

  T calculateInfoGainOnThresholdSplit(T c, int feature) {
    vector<int> S{}; // count split to each set (left/right)
    for (DataInstance instance : dataset.list) {
      T v = instance.input[feature];
      if (v >= c)
        S.push_back(1);
      else
        S.push_back(0);
    }

    InfoGain<int, T> IG{};
    T entropy = IG.calculateEntropy(S);

    return entropy;
  }

  // feature with most discriminatory power
  void findBestCandidateFeatureSplit() {
    T c{0.0}; // threshold
    for (int feature : candidateFeature) {
      c = findBestCandidateThresholdSplit(feature);
    }
  }

  int pickBestFeature() {
  }

public:
  DataSet<T> dataset{};
  shared_ptr<Node<T>> root{nullptr};
  vector<int> candidateFeature{}; // keep track of features that are not used yet (column indicies).
};
