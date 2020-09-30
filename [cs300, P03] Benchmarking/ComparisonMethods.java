//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Algorithms for benchmarking (P03 assignment - Benchmarking)
// Course: CS 300 Fall 2020
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// Lecturer: Hobbes LeGault
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: NONE
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////


/**
 * Algorithms for calculating the sum of a number set.
 * 
 * @author Safi
 */
public class ComparisonMethods {

  /**
   * Calculates and returns the sum of all integers 1 to n. Uses incremental addition in a loop for
   * calculation.
   * <p>
   * Complexity: O(n)
   * 
   * @param n range of arithmetic series to sum
   * @return sum of the number set/range provided
   */
  public static long bruteForce(long n) {
    // calculate sum of integers from 1 to n
    long sum = 0;
    for (; n > 0; n--)
      sum += n;
    return sum;
  }

  /**
   * Calculates and returns the sum of all integers 1 to n. Using a formula for calculation
   * (geometry based).
   * <p>
   * Complexity: O(1)
   * 
   * Mathematical formula: For sum of all numbers between 1 to n. s = (n+1) x n / 2
   * 
   * @param n range of arithmetic series to sum
   * @return sum of the number set/range provided
   */
  public static long constantTime(long n) {
    long sum = (n + 1) * n / 2;
    return sum;
  }

}
