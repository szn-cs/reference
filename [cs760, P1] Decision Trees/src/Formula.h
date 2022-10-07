#pragma once

#include <bits/stdc++.h>
#include "Dataset.h"

using namespace std;

class InfoGain {
public:
  /*
    Event probability
    P(y) = (# outcomes in y) / (total # of possible outcomes in Y)
  */
  template <typename T = long, typename R = double /* accuracy of Entropy calculation */>
  static R calculateProbability(T outcomesOfEvent, T totalPossibleOutcomes) {
    return static_cast<double>(outcomesOfEvent) / static_cast<double>(totalPossibleOutcomes);
  }

  /*
    Probability of each array element - returns a map for each unique value with its probability
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static map<T, R> calculateProbability(const vector<T>& Y) {
    // count # of times the value (# of outcomes in event) occurs
    map<T, int> cMap{}; // # of repetitions of each unique value
    for (T y : Y)
      cMap[y]++; // each unique value will map to # of times it appears in the list.

    // calculate probability of each unique value
    map<T, R> pMap{};
    for (const auto& [y, count] : cMap)
      pMap[y] = InfoGain::calculateProbability<long, double>(count, Y.size() /*int->long cast*/);

    return pMap;
  }

  /*
    Joint probability (for dependent events A, B)
    P(A ∩ B) = P(A) * P(B)
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateJointProbability(R probability_A, R probability_B) {
    return probability_A * probability_B;
  }

  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateJointProbability(long outcomesOfEventA, long outcomesOfEventB, long totalPossibleOutcomes) {
    return InfoGain::calculateJointProbability<T, R>(
        InfoGain::calculateProbability<T, R>(outcomesOfEventA, totalPossibleOutcomes),
        InfoGain::calculateProbability<T, R>(outcomesOfEventB, totalPossibleOutcomes));
  }

  /*
    Shanon Entropy
    H(Y) = (-1) Σ of yϵY[ P(y) log_2(P(y))  ]
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateEntropy(const vector<T>& Y /* values list of a particular feature */) {
    map<T, R> pMap = InfoGain::calculateProbability<T, R>(Y);

    R entropy{0.0};
    for (const auto& [y, probability] : pMap) {
      if (probability <= 0)
        continue; // skip
      entropy -= probability * log2(probability);
    }

    return entropy;
  }

  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateEntropy(const vector<DataInstance<T>>& Y) {
    vector<bool> tmp{};
    for (DataInstance instance : Y)
      if (instance.output == false)
        tmp.push_back(0);
      else if (instance.output == true)
        tmp.push_back(1);
      else
        throw std::runtime_error("⚫ Unqualified value detected");

    return InfoGain::calculateEntropy<bool, R>(tmp);
  }

  /*
    Joint Entropy
    H(Y, X) = (-1) Σ of xϵX[ Σ of yϵY[ P(y, x) log_2(P(y, x))  ] ]
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateJointEntropy(const vector<T>& Y, const vector<T>& X) {
    R entropy{0.0}; // used for outer summation

    map<T, R> pMap_X = InfoGain::calculateProbability<T, R>(X);
    map<T, R> pMap_Y = InfoGain::calculateProbability<T, R>(Y);

    for (const auto& [x, probability_x] : pMap_X) {
      for (const auto& [y, probability_y] : pMap_Y) {
        const R probability_YX = InfoGain::calculateJointProbability<T, R>(probability_x, probability_y); // joint probability
        if (probability_YX <= 0)
          continue; // skip
        entropy += probability_YX * log2(probability_YX);
      }
    }

    entropy *= (-1);

    return entropy;
  }

  /*
    Conditional Entropy
    Entropy(Y|X) = H(Y,X) - H(X)
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateConditionalEntropy(const vector<T>& Y, const vector<T>& X) {
    return InfoGain::calculateJointEntropy<T, R>(Y, X) - InfoGain::calculateEntropy<T, R>(X);
  }

  /*
    information gain
    I(Y, X) = H(Y) - H(Y|X)
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateInformationGain(const vector<T>& Y, const vector<T>& X) {
    return InfoGain::calculateEntropy<T, R>(Y) - InfoGain::calculateConditionalEntropy<T, R>(Y, X);
  }

  /*
    information gain - specific case for binary labels (left/right child splits)
    InfoGain(D, A) = H(Parent) - [weighted sum of] H(children)
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateInformationGain(const vector<DataInstance<T>>& parentSet, const vector<DataInstance<T>>& leftSubset, const vector<DataInstance<T>>& rightSubset) {
    R entropyParent = InfoGain::calculateEntropy<T, R>(parentSet);
    R entropyLeftChild = InfoGain::calculateEntropy<T, R>(leftSubset);
    R entropyRightChild = InfoGain::calculateEntropy<T, R>(rightSubset);

    R weightedEntropyLeftChild = (static_cast<double>(leftSubset.size()) / static_cast<double>(parentSet.size())) * entropyLeftChild;
    R weightedEntropyRightChild = (static_cast<double>(rightSubset.size()) / static_cast<double>(parentSet.size())) * entropyRightChild;
    R sumOfWeightedChildrenEntropy = weightedEntropyLeftChild + weightedEntropyRightChild;
    return entropyParent - sumOfWeightedChildrenEntropy;
  }

  /*
    information gain ratio
    I(Y, X) / H(X)
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateInformationGainRatio(const vector<T>& Y, const vector<T>& X) {
    return InfoGain::calculateInformationGain<T, R>(Y, X) / InfoGain::calculateEntropy<T, R>(X);
  }

  /*
    information gain ratio - specific for binary case
    I(Y, X) / H(X)
  */
  template <typename T = float, typename R = double /* accuracy of Entropy calculation */>
  static R calculateInformationGainRatio(const vector<DataInstance<T>>& parentSet, const vector<DataInstance<T>>& leftSubset, const vector<DataInstance<T>>& rightSubset) {
    R infoGain = calculateInformationGain<T, R>(parentSet, leftSubset, rightSubset);
    if (infoGain == 0.0)
      return 0.0;

    R leftProportion = static_cast<double>(leftSubset.size()) / static_cast<double>(parentSet.size());
    R rightProportion = static_cast<double>(rightSubset.size()) / static_cast<double>(parentSet.size());
    R splitEntropy{0.0};
    splitEntropy -= (leftProportion == 0.0) ? 0 : (leftProportion)*log2(leftProportion);
    splitEntropy -= (rightProportion == 0.0) ? 0 : (rightProportion)*log2(rightProportion);
    return infoGain / splitEntropy;
  }
};
