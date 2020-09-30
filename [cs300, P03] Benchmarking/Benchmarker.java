//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Benchmark runner (P03 assignment - Benchmarking)
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

// IO modules
import java.io.File;
import java.io.FileWriter;
// Exception classes to handle errors
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Benchmarking of algorithms runtime efficiency
 * 
 * @author Safi
 */
public class Benchmarker {

  /**
   * Calls benchmarking methods implemented in this class to test associated algorithms.
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    File file = new File("benchmark.txt"); // file to save the benchmark results to.
    long[] set = generateTestData(100000L, 10, 5); // generate testing values
    createResultsFile(file, set); // execute benchmark & write results to file
  }

  /**
   * Generate long values increasing by specific factor.
   * 
   * @param initial      starting value of the generated set
   * @param factor       multiplication factor used in generating the integers
   * @param valuesNumber number of values to generate including starting value
   * @return array of generated relative values
   */
  private static long[] generateTestData(long initial, int factor, int valuesNumber) {
    long[] set = new long[valuesNumber]; // set array
    set[0] = initial; // add initial value
    // generate numbers
    for (int i = 1; i < valuesNumber; i++)
      set[i] = set[i - 1] * factor; // calculate factor of previous element
    return set;
  }

  /**
   * Runs & Tracks execution time in milliseconds of algorithms using input size.
   * 
   * @see `ComparisonMehtods.java` file for methods tested
   * @param n input size of problem
   * @return formatted string with input n and the elapsed times
   * @throws NoSuchElementException if the return values of the comparison methods are different
   */
  public static String compare(long n) throws NoSuchElementException {
    long bruteForceResult, formulaResult; // result of methods execution
    long bruteForceTime, formulaTime; // results for different algorithms

    // bruteForce method profile
    {
      long initial = System.currentTimeMillis();
      bruteForceResult = ComparisonMethods.bruteForce(n);
      long current = System.currentTimeMillis();
      bruteForceTime = current - initial; // calculate elapsed time
    }

    // constantTime method profile
    {
      long initial = System.currentTimeMillis();
      formulaResult = ComparisonMethods.constantTime(n);
      long current = System.currentTimeMillis();
      formulaTime = current - initial; // calculate elapsed time
    }

    // verify algorithms results
    if (bruteForceResult != formulaResult)
      throw new NoSuchElementException("Results of comparison methods do not match.");

    // format compared results message
    String comparison = n + "\t" + bruteForceTime + "\t" + formulaTime + "\n";

    return comparison;
  }

  /**
   * Collects a series of comparison results with multiple different input values, and writes them
   * to a specified file.
   * 
   * @see `ComparisonMehtods.java` file for methods tested
   * @param f       target file to output results to
   * @param queryNs array of problem sizes to profile
   */
  public static void createResultsFile(File f, long[] queryNs) {
    FileWriter fw; // opened file to write to.
    String[] results = new String[queryNs.length];

    try {
      fw = new FileWriter(f); // open a file for writing
    } catch (IOException e) {
      System.out.println("Exception encountered, unable to complete method.");
      return;
    }

    // execute comparing tests with values from the set.
    for (int i = 0; i < queryNs.length; i++) {
      try {
        results[i] = compare(queryNs[i]);
      } catch (NoSuchElementException e) {
        System.out.println(e.getMessage());
        continue;
      }
    }

    // write results to file
    for (int i = 0; i < results.length; i++) {
      // skip null positions
      if (results[i] == null)
        continue;

      // write string to file
      try {
        fw.write(results[i]);
      } catch (IOException | IndexOutOfBoundsException e) {
        System.out.println("Exception encountered while writing for value N = " + i);
        continue;
      }
    }

    // close file
    try {
      fw.close();
    } catch (IOException e) {
      System.out.println("Exception encountered while closing file.");
    }
  }
}
