/**
 * This class implements an iterative and a recursive versions of factorial
 *  function and compares their execution time
 */
public class Factorial {

  /**
   * Iterative version of factorial function
   * 
   * @param n number to compute its factorial
   * @return factorial n
   * @throws IllegalArgumentException if n is negative
   */
  public static long iterativeFactorial(long n) {
    // Running time complexity (problem size: n) = O(n)
    // Precondition (n >= 0)
    if (n < 0) // --> O(1)
      throw new IllegalArgumentException("WARNING: n should be a positive integer!");
    if (n == 0) // --> O(1)
      return 1;
    // n is a non-zero positive integer
    // --> 
    long factorial = 1; // O(1)
    // loop to compute factorial n iteratively --> n * O(1) = O(n)
    // 
    for (long i = n; i > 0; i--) // number of iterations at worst case * runtime of the code which
                                // will be run for each iteration
      factorial = factorial * i;
    return factorial; // O(1)
  }

  /**
   * Factorial function using a recursive method
   * 
   * @param n number to compute its factorial value
   * @return the factorial of n
   * @throws IllegalArgumentException if n < 0
   */
  public static long recursiveFactorial(long n) {
    // Running time complexity: O(n)
    // Precondition: n >=0
    if (n < 0) // --> O(1)
      throw new IllegalArgumentException("Warning: Only positive integers are accepted!");
    // call of a recursive method to compute factorial n
    return factorialHelper(n); // running time of factorialHelper is O(n)
  }

  /**
   * helper method to compute the factorial of a positive integer recursively
   * 
   * @param n number to compute its factorial value
   * @return factorial n
   */
  private static long factorialHelper(long n) {
    // runtime complexity = number of recursive calls at worst case * the running time
    // complexity of the code which will be run for every recursive call
    // = n * O(1) = O(n)
    if (n == 0) // base case --> O(1)
      return 1;
    return factorialHelper(n - 1) * n; // recursive case
  }

  /**
   * main method to test the implementations of factorial (iterative and recursive versions)
   * and compare their execution time
   * 
   * @param arg
   */
  public static void main(String arg[]) {
    long x, f;
    long startTime, endTime, executionTime; // time variables

    x = 15;
    System.out.println("Iterative Factorial ...");
    startTime = System.nanoTime(); // initialize start time
    // to system current time in ns
    f = iterativeFactorial(x);

    endTime = System.nanoTime();
    executionTime = endTime - startTime;
    System.out.println(x + "! = " + f + ".\t execution time = " + executionTime + " ns");

    System.out.println("\nRecursive Factorial ...");
    startTime = System.nanoTime(); // initialize start time to system current time in ns

    f = recursiveFactorial(x);

    endTime = System.nanoTime(); // in nanoseconds
    executionTime = endTime - startTime; // in ns
    System.out.println(x + "! = " + f + ".\t execution time = " + executionTime + " ns");
  }

}