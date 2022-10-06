#pragma once

#include <bits/stdc++.h>
#include "utility.h"
#include "Dataset.h"
#include "Formula.h"

using namespace std;

template <typename T = double>
class DecisionTree {
public:
  DecisionTree() = default;
  DecisionTree(const DataSet<T>& dataset) : dataset(dataset) { // used for root node
    for (int i = 0; i < dataset.numberOfFeatures; i++)
      candidateFeature.insert(i);
  };
  DecisionTree(vector<DataInstance<T>>& instanceList, unordered_set<int> candidateFeature) : dataset(DataSet{instanceList}), candidateFeature(candidateFeature) {
  }
  DecisionTree(DataSet<T>& dataset, unordered_set<int> candidateFeature) : dataset(dataset), candidateFeature(candidateFeature) {}

  /* returns threshold (equivalent to attribute of a feature) taht maximizes gain ratio */
  tuple<T, T> findBestCandidateThresholdSplit(int feature /*feature being investigated*/) {
    T c_best{};
    T gainRatio_max{};

    // 1. loop over each value of a particular feature
    for (DataInstance instance : dataset.list) {
      T c = instance.input[feature]; // candidate threshold

      // 2. split into 2 subsets using value as threshold
      vector<DataInstance<T>> leftSubset{}, rightSubset{};

      for (DataInstance instance : dataset.list) {
        T v = instance.input[feature];
        if (v >= c)
          leftSubset.push_back(instance); // left: "then" branch
        else
          rightSubset.push_back(instance); // right: "else" branch
      }

      // 3. Calculate Information gain of binary labels incurred by the split.
      T gainRatio{};
      gainRatio = InfoGain::calculateInformationGainRatio<T, T>(dataset.list, leftSubset, rightSubset);

      // 4. pick threshold that maximizes the gain ratio
      if (gainRatio > gainRatio_max) {
        c_best = c;
        gainRatio_max = gainRatio;
      }
    }

    return make_tuple(c_best, gainRatio_max);
  }

  // feature with most discriminatory power
  tuple<int, T> findBestCandidateFeatureSplit() {

    // 1. get best threshold/attribute split of each feature
    map<int, T> featureBestThresholdMap{};
    for (int feature : candidateFeature) {
      const auto [c, gainRatio] = findBestCandidateThresholdSplit(feature);
      featureBestThresholdMap[feature] = c;
    }

    // 2. get best feature to split on
    T feature_best{};
    T c_max{}; // threshold maximizing gain ratio
    for (const auto& [feature, c] : featureBestThresholdMap) {
      if (c > c_max) {
        feature_best = feature;
        c_max = c;
      }
    }

    return make_tuple(feature_best, c_max);
  }

  // split of a particular feature n corresponding threshold (attribute)
  tuple<shared_ptr<DecisionTree<T>>, shared_ptr<DecisionTree<T>>> createSubtreeSplit(int feature, T threshold) {
    // mark properties used for split on current root node
    splitFeature = feature;
    splitThreshold = threshold;

    // split into subsets
    vector<DataInstance<T>> leftSubset{}, rightSubset{};
    for (DataInstance instance : dataset.list) {
      T v = instance.input[feature];
      if (v >= threshold)
        leftSubset.push_back(instance); // left: "then" branch
      else
        rightSubset.push_back(instance); // right: "else" branch
    }

    // remove used feature
    unordered_set<int> candidateFeaturesRemaining = candidateFeature;
    candidateFeaturesRemaining.erase(candidateFeaturesRemaining.find(feature));

    // create subtress & set state
    leftSubtree = make_shared<DecisionTree<T>>(leftSubset, candidateFeaturesRemaining);
    rightSubtree = make_shared<DecisionTree<T>>(rightSubset, candidateFeaturesRemaining);

    // make_shared<DecisionTree>(leftSubtree)
    return make_tuple(leftSubtree, rightSubtree);
  }

  // iteratively split nodes on best threshold
  void createDecisionTreeLearner() {
    const auto [feature, threshold] = this->findBestCandidateFeatureSplit();
    auto [leftSubtree, rightSubtree] = this->createSubtreeSplit(feature, threshold);
  }

public:
  const DataSet<T> dataset{};            // dataset considered for split in this subtree level
  unordered_set<int> candidateFeature{}; // keep track of features that are not used yet (column indicies).

  int splitFeature{-1};       // feature chosen to split the node by.
  T splitThreshold{INFINITY}; // threshold to split numeric values on.

  shared_ptr<DecisionTree<T>> leftSubtree{nullptr};
  shared_ptr<DecisionTree<T>> rightSubtree{nullptr};
};
