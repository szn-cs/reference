///////////////////////// CUMULATIVE QUIZ FILE HEADER //////////////////////////
// Full Name: Safi Nassar
// Campus ID: 9082786709
// WiscEmail: nassar2@wisc.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * This class implements recursive methods to create palindromic (mirrored) numerical sequences.
 * 
 * TODO: Complete the implementation of the three methods below: 1. Write simpleBetaPattern() 2.
 * Write betaPattern() 3. Write testBetaPattern()
 *
 */
public class BetaNumberPattern {
  /**
   * Creates a simple number pattern, with the negative numbers going forward to zero and then
   * backward to the provided start negative number, resulting into a mirrored/palindromic sequence
   * of numbers.
   * 
   * You do NOT need to consider the case where the provided input number is not negative
   * 
   * @param number a negative integer
   * @return a palindromic string of numbers beginning and ending with the provided start number
   *         mirrored by zero, e.g., if number is -4, the returned string will be "-4 -3 -2 -1 0 -1
   *         -2 -3 -4" The different numbers in the returned string must be separated by only one
   *         space.
   */
  public static String getSimplePattern(int number) {
    String result = "";
    // Base case
    if (number >= 0)
      return "0";
    else {
      // TODO: recursive case: build recursively the simple number pattern
      // related to the provided input by adding this number onto
      // either end of a recursive string of the other numbers.
      String r = getSimplePattern(number + 1);

      result = number + " " + r + " " + number;
      // System.out.println(result);

    }

    // Debugging suggestion: uncomment this statement to see what you're returning
    return result;
  }

  /**
   * Recursive method to return the following number pattern as a string. Given a negative integer
   * as input number, add a positive integer (a step) continually until 0 or positive value is
   * reached, and then continually subtract the step until the first negative integer is again
   * reached. If a positive value is reached, it must be excluded from the returned number pattern.
   * The latter must contain only a sequence of negative integers.
   * 
   * You do NOT need to consider the case where the provided number is not negative You do NOT need
   * to consider the case where the provided step is not positive
   * 
   * @param number a negative integer
   * @param step   a positive integer to add
   * @return a string representation of the number pattern according to the above description. E.g.
   *         (-12, 3) => "-12 -9 -6 -3 0 -3 -6 -9 -12". E.g. (-11, 3) => "-11 -8 -5 -2 -5 -8 -11".
   *         The different numbers in the resulted string must be separated by only one space.
   */
  public static String betaPattern(int number, int step) {
    String result = "";
    // Base case
    if (number == 0)
      return "0";
    else if (number + step > 0)
      return "" + number;
    else {
      // TODO: recursive case: build recursively the simple number pattern
      // related to the provided input by adding this number onto
      // either end of a recursive string of the other numbers.
      String r = betaPattern(number + step, step);

      result = number + " " + r + " " + number;
      // System.out.println(result);

    }

    // Debugging suggestion: uncomment this statement to see what you're returning
    return result;
  }

  /**
   * Checks the correctness of the implementation of the getSimplePattern() method
   * 
   * @return true if the method passes this test and false otherwise
   */
  public static boolean testGetSimplePattern() {
    try {
      // test scenario 1
      String expectedOutput = "-4 -3 -2 -1 0 -1 -2 -3 -4";
      if (!getSimplePattern(-4).equals(expectedOutput))
        return false;
      // test scenario 2 (let's consider a larger number)
      int number = -15;
      // let's build the output
      String increasingHalf = "-15 -14 -13 -12 -11 -10 -9 -8 -7 -6 -5 -4 -3 -2 -1";
      String decreasingHalf = "-1 -2 -3 -4 -5 -6 -7 -8 -9 -10 -11 -12 -13 -14 -15";
      expectedOutput = increasingHalf + " " + 0 + " " + decreasingHalf;
      if (!getSimplePattern(-15).equals(expectedOutput))
        return false;

    } catch (StackOverflowError e1) {
      System.out.println("Problem detected! StackOverflowError thrown from "
          + "testGetSimplePattern" + "recursive method calls.");
      return false;
    } catch (Exception e2) {
      return false; // no exception is expected to be thrown
    }
    return true;
  }

  /**
   * Checks the correctness of the implementation of the betaPattern() method
   * 
   * @return true if the method passes this test and false otherwise
   */
  public static boolean testBetaPattern() {
    // Note that in your test scenarios you must consider only negative integers
    // for number and positive integers for step

    // TODO
    // 1. Define a first test scenario where the resulting output of
    // numPattern(number, step) is mirrored with respect to 0.
    // DO NOT consider the input values (-12, 3) provided in the
    // method header of the betaPattern methodfor this test scenario.
    // Define your own scenario.
    try {
      // test scenario 1


      String expectedOutput = "-10 -8 -6 -4 -2 0 -2 -4 -6 -8 -10";
      if (!betaPattern(-10, 2).equals(expectedOutput))
        return false;


      // (-11, 3) => "-11 -8 -5 -2 -5 -8 -11"
      expectedOutput = "-17 -12 -7 -2 -7 -12 -17";
      if (!betaPattern(-17, 5).equals(expectedOutput))
        return false;


    } catch (StackOverflowError e1) {
      System.out.println("Problem detected! StackOverflowError thrown from " + "testBetaPattern"
          + "recursive method calls.");
      return false;
    } catch (Exception e2) {
      return false; // no exception is expected to be thrown
    }
    return true;



  }

  public static void main(String[] args) {
    System.out.println("testGetSimplePattern: " + testGetSimplePattern());
    System.out.println("testBetaPattern: " + testBetaPattern());


  }

}
