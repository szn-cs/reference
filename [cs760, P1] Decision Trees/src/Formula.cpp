#pragma once

#include <bits/stdc++.h>

using namespace std;

template <typename T = float, typename R = T /* accuracy of Entropy calculation */>
class InfoGain {
public:
  /*
    Event probability
    P(y) = (# outcomes in y) / (total # of possible outcomes in Y)
  */
  R calculateProbability(long outcomesOfEvent, long totalPossibleOutcomes) {
    return static_cast<double>(outcomesOfEvent) / static_cast<double>(totalPossibleOutcomes);
  }

  /*
    Probability of each array element - returns a map for each unique value with its probability
  */
  map<T, R> calculateProbability(const vector<T>& Y) {
    // count # of times the value (# of outcomes in event) occurs
    map<T, long> cMap; // # of repetitions of each unique value
    for (T y : Y)
      cMap[y]++; // each unique value will map to # of times it appears in the list.

    // calculate probability of each unique value
    map<T, R> pMap;
    for (T y : Y)
      pMap[y] = calculateProbability(cMap[y], Y.size() /*int->long cast*/);

    return pMap;
  }

  /*
    Joint probability (for dependent events A, B)
    P(A ∩ B) = P(A) * P(B)
  */
  R calculateJointProbability(R probability_A, R probability_B) {
    return probability_A * probability_B;
  }
  R calculateJointProbability(long outcomesOfEventA, long outcomesOfEventB, long totalPossibleOutcomes) {
    return calculateJointProbability(
        calculateProbability(outcomesOfEventA, totalPossibleOutcomes),
        calculateProbability(outcomesOfEventB, totalPossibleOutcomes));
  }

  /*
    Shanon Entropy
    H(Y) = (-1) Σ of yϵY[ P(y) log_2(P(y))  ]
  */
  R calculateEntropy(const vector<T>& Y /* values list of a particular feature */) {
    map<T, R> pMap = calculateProbability(Y);

    R entropy{0.0};
    for (const auto& [y, probability] : pMap) {
      if (probability <= 0)
        continue; // skip
      entropy -= probability * log2(probability);
    }

    return entropy;
  }

  /*
    Joint Entropy
    H(Y, X) = (-1) Σ of xϵX[ Σ of yϵY[ P(y, x) log_2(P(y, x))  ] ]
  */
  R calculateJointEntropy(const vector<T>& Y, const vector<T>& X) {
    R entropy{0.0}; // used for outer summation

    map<T, R> pMap_X = calculateProbability(X);
    map<T, R> pMap_Y = calculateProbability(Y);

    for (const auto& [x, probability_x] : pMap_X) {
      for (const auto& [y, probability_y] : pMap_Y) {
        const R probability_YX = calculateJointProbability(probability_x, probability_y); // joint probability
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
  R calculateConditionalEntropy(const vector<T>& Y, const vector<T>& X) {
    return calculateJointEntropy(Y, X) - calculateEntropy(X);
  }

  /*
    information gain
    I(Y, X) = H(Y) - H(Y|X)
  */
  R calculateInformationGain(const vector<T>& Y, const vector<T>& X) {
    return calculateEntropy(Y) - calculateConditionalEntropy(Y, X);
  }

  /*
    information gain ratio
    I(Y, X) / H(X)
  */
  R calculateInformationGainRatio(const vector<T>& Y, const vector<T>& X) {
    return calculateInformationGain(Y, X) / calculateEntropy(X);
  }
};
