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
  DecisionTree(vector<DataInstance<T>>& instanceList, set<int> candidateFeature) : dataset(DataSet{instanceList}), candidateFeature(candidateFeature) {
  }
  DecisionTree(DataSet<T>& dataset, set<int> candidateFeature) : dataset(dataset), candidateFeature(candidateFeature) {}

  /* returns threshold (equivalent to attribute of a feature) taht maximizes gain ratio */
  tuple<T, double, double> findBestCandidateThresholdSplit(int feature /*feature being investigated*/) {
    T c_best{};
    double gainRatio_max{};
    double gainInfo_corresponding{};

    // create set of unique candidate thresholds
    set<T> candidateThreshold;
    for (DataInstance instance : dataset.list) {
      T c = instance.input[feature]; // candidate threshold
      candidateThreshold.insert(c);
    }

    // 1. loop over each value of a particular feature
    for (T c : candidateThreshold) {

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
      auto [gainRatio, infoGain] = InfoGain::calculateInformationGainRatio<T, double>(dataset.list, leftSubset, rightSubset);

      // printf("\nFeature=%u ", feature);
      // cout << "Threshold=" << c << "\t";
      // cout << "GainRatio=" << gainRatio << "\t";
      // cout << "InfoGain=" << infoGain << endl;

      // 4. pick threshold that maximizes the gain ratio
      if (gainRatio > gainRatio_max) {
        c_best = c;
        gainRatio_max = gainRatio;
        gainInfo_corresponding = infoGain;
      }
    }

    return make_tuple(c_best, gainRatio_max, gainInfo_corresponding);
  }

  // feature with most discriminatory power
  tuple<int, T, double> findBestCandidateFeatureSplit() {
    // 1. get best threshold/attribute split of each feature
    map<int, tuple<T, double, double>> featureBestThresholdMap{};
    for (int feature : candidateFeature) {
      const auto [c, gainRatio, infoGain] = findBestCandidateThresholdSplit(feature);
      featureBestThresholdMap[feature] = make_tuple(c, gainRatio, infoGain);
    }

    // 2. get best feature to split on
    T feature_best{};
    T c_max{};              // threshold maximizing gain ratio
    double gainRatio_max{}; // maximum gain ratio accross features
    double infoGain_corresponding{};
    for (const auto& [feature, featureTuple] : featureBestThresholdMap) {
      const auto& [c, gainRatio, infoGain] = featureTuple;
      if (c > c_max) {
        feature_best = feature;
        c_max = c;
        gainRatio_max = gainRatio;
        infoGain_corresponding = infoGain;
      }
    }

    cout << "infoGain=" << infoGain_corresponding << "\t ";

    return make_tuple(feature_best, c_max, gainRatio_max);
  }

  tuple<vector<DataInstance<T>>, vector<DataInstance<T>>> splitIntoSubsets(int feature, T threshold) {
    // split into subsets
    vector<DataInstance<T>> leftSubset{}, rightSubset{};
    for (DataInstance instance : dataset.list) {
      T v = instance.input[feature];
      if (v >= threshold)
        leftSubset.push_back(instance); // left: "then" branch
      else
        rightSubset.push_back(instance); // right: "else" branch
    }

    return make_tuple(leftSubset, rightSubset);
  }

  // recursive split nodes on best threshold
  // split of a particular feature n corresponding threshold (attribute)
  tuple<shared_ptr<DecisionTree<T>>, shared_ptr<DecisionTree<T>>> createDecisionTreeLearner() {
    // check base case 1:
    if (this->is_empty())
      goto LEAF;

    {
      const auto [feature, threshold, maxGainRatio] = this->findBestCandidateFeatureSplit();
      auto [leftSubset, rightSubset] = this->splitIntoSubsets(feature, threshold);

      // check base case 2:
      // entropy of any candidcate split is zero.
      double entropyLeftChild = InfoGain::calculateEntropy<T, double>(leftSubset);
      double entropyRightChild = InfoGain::calculateEntropy<T, double>(rightSubset);
      if (entropyLeftChild == 0.0 && entropyRightChild == 0.0 && maxGainRatio == 0.0)
        goto LEAF;

      // base case 3:
      if (maxGainRatio == 0.0)
        goto LEAF;

      { // mark properties used for split on current root node
        splitFeature = feature;
        splitThreshold = threshold;
        // remove used feature
        set<int> candidateFeaturesRemaining = candidateFeature;
        candidateFeaturesRemaining.erase(candidateFeaturesRemaining.find(feature));
        // create subtress & set state
        leftSubtree = make_shared<DecisionTree<T>>(leftSubset, candidateFeaturesRemaining);
        rightSubtree = make_shared<DecisionTree<T>>(rightSubset, candidateFeaturesRemaining);

        {
          auto [positive, negative] = dataset.countBinaryLabel();
          printf("Feature=%u ", feature);
          cout << "Threshold=" << threshold << " ";
          cout << "GainRatio=" << maxGainRatio << "\t";
          cout << "[+:" << positive << " , -:" << negative << "]" << endl;
        }
        // recursive call
        cout << "LEFT" << endl;
        leftSubtree->createDecisionTreeLearner();
        cout << "RIGHT" << endl;
        rightSubtree->createDecisionTreeLearner();

        return make_tuple(leftSubtree, rightSubtree);
      }
    }

  LEAF : {

    this->markLeaf();
    cout << std::boolalpha;
    cout << "Leaf with label: " << this->getMajorityLabel()
         << endl;

    return make_tuple(nullptr, nullptr);
  }
  }

  bool getMajorityLabel() {
    return this->dataset.getMajorityLabel();
  }

  void markLeaf() {
    this->isLeaf = true;
  }

  bool is_empty() {
    return dataset.list.empty();
  }

public:
  const DataSet<T> dataset{};  // dataset considered for split in this subtree level
  set<int> candidateFeature{}; // keep track of features that are not used yet (column indicies).

  int splitFeature{-1};                       // feature chosen to split the node by.
  T splitThreshold{static_cast<T>(INFINITY)}; // threshold to split numeric values on.

  shared_ptr<DecisionTree<T>> leftSubtree{nullptr};
  shared_ptr<DecisionTree<T>> rightSubtree{nullptr};

  bool isLeaf{};
};
