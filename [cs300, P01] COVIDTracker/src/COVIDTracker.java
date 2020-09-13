//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: COVID Test Tracker (P01 assignment)
// Course: CS 300 Fall 2020
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// Lecturer: Hobbes LeGault
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: NONE
// Online Sources: 1. System.arraycopy usage from examples online:
//////////////// [https://www.programiz.com/java-programming/examples/concatenate-two-arrays].
//////////////// 2. Format floating point number with trailing zeros (Trim zeros):
//////////////// [https://stackoverflow.com/a/26132504]
//
///////////////////////////////////////////////////////////////////////////////

/**
 * COVID Test Tracker - manages the viral infection test results, with the goal of tracking each
 * individual's tests.
 * <p>
 * The program presents statistical overview of the population infection cases. Amongst the data
 * are: total number of cases, number of total individuals tested, proportions of positive &
 * negative tests, as well as proportions of unique individuals that were tested positive or
 * negative.
 * 
 * @author Safi
 * 
 */
public class COVIDTracker {

  /**
   * Adds ID to the appropriate test array if there is room.
   * 
   * @param pos   The current array of positive tests
   * @param neg   The current array of negative tests
   * @param id    The tested individual’s unique identifier String
   * @param isPos true if the test was positive, false otherwise
   * @return true if the new record was added, false otherwise
   */
  public static boolean addTest(String[] pos, String[] neg, String id, boolean isPos) {
    String[] testsArray; // target tests array to add the input test into.
    int testsArraySize = 0; // current size of target array (treat array as oversized array).

    // pick the matching tests array for the individual's test result.
    testsArray = isPos ? pos : neg;

    // find free space in the tests array (as no size information is provided)
    testsArraySize = getArraySize(testsArray);

    // check if there is empty space for the new entry (when array has capacity and is not full).
    if (testsArray.length > 0 && testsArraySize < testsArray.length) {
      testsArray[testsArraySize] = id; // add the individual's id in an empty space.
      return true; // entry added successfully.
    }

    return false; // failed to add id, array has no capacity (either full or zero length).
  }

  /**
   * Removes ID from all test arrays it is contained in (Remove any records of an individual).
   * 
   * @param pos The current array of positive tests
   * @param neg The current array of negative tests
   * @param id  The tested individual’s unique identifier String
   * @return true if one or more records were removed, otherwise false if the ID was not found in
   *         either array
   */
  public static boolean removeIndividual(String[] pos, String[] neg, String id) {
    // true if occurrence of id was found and removed from pos and neg arrays respectively.
    boolean removedPos = false, removedNeg = false;

    // remove from positive test array
    {
      int arraySize = getArraySize(pos);
      removedPos = removeOccurences(pos, arraySize, id);
      compactArray(pos, 0, arraySize - 1);
    }

    // remove from negative test array
    {
      int arraySize = getArraySize(neg);
      removedNeg = removeOccurences(neg, arraySize, id);
      compactArray(neg, 0, arraySize - 1);
    }

    // in case either of the test arrays had a single or more individual's records removed
    return removedPos || removedNeg;
  }

  /**
   * Retrieve the test statistics for the tests of all individuals.
   * 
   * @param pos The current array of positive tests
   * @param neg The current array of negative tests
   * @return formatted text with the tests statistical data of all tested people
   */
  public static String getPopStats(String[] pos, String[] neg) {
    // get sizes of oversize arrays
    int posSize = getArraySize(pos), negSize = getArraySize(neg);
    // holding texts with the calculations for each statistical data.
    String[] stats = new String[4];

    // # of total tests - the total number of positive and negative tests
    int totalTests = posSize + negSize;

    // Total individuals tested - the total number of unique individuals across both arrays
    int totalTestedIndividuals = 0;
    // merge test arrays of positive and negative into a new copy.
    String[] posNegArray = new String[pos.length + neg.length]; // a copy of merge pos & neg arrays
    System.arraycopy(pos, 0, posNegArray, 0, pos.length);
    System.arraycopy(neg, 0, posNegArray, pos.length, neg.length);
    // retrieve the unique individuals of both negative and positive tests.
    totalTestedIndividuals = uniqueElements(posNegArray);

    // Percent positive tests - the proportion of positive tests
    double positiveTestsPercent = 0.0;
    // Percent positive individuals - the proportion of individuals who have tested positive
    double positiveIndividualsPercent = 0.0;
    // prevent divide-by-zero arithmetic error.
    if (totalTests != 0) {
      // Mathematically: positive tests / total tests * 100
      positiveTestsPercent = totalTests == 0 ? 0.0 : (posSize * 100.0) / totalTests;
      // Mathematically: # of positive individuals / total individuals * 100
      positiveIndividualsPercent = (uniqueElements(pos) * 100.0) / totalTestedIndividuals;
    }

    stats[0] = String.format("Total tests: %d", totalTests);
    stats[1] = String.format("Total individuals tested: %d", totalTestedIndividuals);
    stats[2] = "Percent positive tests: " + String.format("%.1f%%", positiveTestsPercent);
    stats[3] = String.format("Percent positive individuals: %s%%",
        formatFloatingNumber(positiveIndividualsPercent));

    return String.join("\n", stats);
  }

  /**
   * Retrieve the test statistics for an individual.
   * 
   * @param pos The current array of positive tests
   * @param neg The current array of negative tests
   * @param id  The tested individual’s unique identifier String
   * @return formatted text with the tests statistical data of a tested individual
   */
  public static String getIndividualStats(String[] pos, String[] neg, String id) {
    // holding texts with the calculations for each statistical data.
    String[] stats = new String[3];

    // the total number of tests for the individual
    int totalIndividualTests = getOccurrences(pos, id) + getOccurrences(neg, id);
    // the total number of positive and negative tests for the individual
    int totalIndividualPositive = getOccurrences(pos, id); // Positive
    int totalIndividualNegative = getOccurrences(neg, id); // Negative

    stats[0] = String.format("Total tests: %d", totalIndividualTests);
    stats[1] = String.format("Positive: %d", totalIndividualPositive);
    stats[2] = String.format("Negative: %d", totalIndividualNegative);

    return String.join("\n", stats);
  }

  /**
   * Format to string taking into account trailing zeros.
   * <p>
   * Cases examples: 10.0d -> "10.0", 5.0d -> "5.0", 6.0002d -> "6.0002"
   * 
   * @param floatingNumber Number with decimal point (floating number)
   * @return Formated string of the decimal number with trailing zeros trimmed.
   */
  private static String formatFloatingNumber(double floatingNumber) {
    // in case no fractional part
    if (floatingNumber % 1.0 == 0)
      return Double.toString(floatingNumber);
    else
      return String.format("%.9f", floatingNumber);
  }

  /**
   * Generic implementation to determine the size of an array (when behaving like an oversize array)
   * 
   * @param array An oversize array with missing size information
   * @return size of the array (# of inserted elements)
   */
  private static int getArraySize(Object[] array) {
    if (array.length == 0) // short-circuit evaluation for zero length arrays.
      return 0;

    for (int i = 0; i < array.length; i++)
      if (array[i] == null)
        return i; // size will be matching the lowest index with a null reference.

    return array.length; // when no empty space found, then array is full (size = capacity).
  }

  /**
   * Generic implementation to remove repeated occurrences of an item from an oversize array.
   * (Compacts the arrays so there are no empty spaces in the middle of data).
   * 
   * @param array Oversize array containing elements.
   * @param item  Target item/element to remove the occurrences of in the array.
   * @return true if one or more records were removed, otherwise false where no occurences found.
   */
  private static boolean removeOccurences(Object[] array, int arraySize, Object item) {
    boolean found = false; // true if item occurrence was found and removed.
    for (int i = 0; i < arraySize; i++) {
      // on id occurrence, remove the record.
      if (array[i] != null && array[i].equals(item)) {
        array[i] = null;
        found = true;
      }
    }

    return found;
  }

  /**
   * Generic implementation to compact an unordered array by removing in-between nulls.
   * 
   * @param array      Target array of reference values.
   * @param startIndex Lowest index limiting range of work.
   * @param endIndex   Highest index limiting range of work.
   */
  private static void compactArray(Object[] array, int startIndex, int endIndex) {
    int i = startIndex; // current index
    /*
     * loop through array range, replacing any nulls in the range with the last element in range.
     * Note: The algorithm used to replace nulls leverages the idea that the array is unordered. In
     * the process the loop closes in from both ends of the array during iteration.
     */
    while (i <= endIndex) {
      // make sure last index in range is not null, otherwise limit the range and try again
      if (array[endIndex] == null) {
        endIndex--;
        continue;
      }

      // when current index of the array is null replace it with the element.
      if (array[i] == null) {
        // override null with the last element.
        array[i] = array[endIndex];
        array[endIndex] = null;
        endIndex--;
      }

      i++;
    }
  }

  /**
   * Generic implementation to retrieve the number of unique elements in an array.
   * 
   * @param array Any array of objects
   * @return number of unique items/elements in the array
   */
  private static int uniqueElements(Object[] array) {
    int numberOfUniqueElements = 0;
    Object[] tmpArray = array.clone();
    for (Object element : tmpArray) {
      if (element != null) {
        numberOfUniqueElements++;
        removeOccurences(tmpArray, tmpArray.length, element);
      }
    }

    return numberOfUniqueElements;
  }

  /**
   * Generic implementation to detect the # of occurrences of an item from an array.
   * 
   * @param array Any array of reference types.
   * @param item  The value to count it's occurrences in the target array.
   * @return number of repeated occurrences of the item in the array.
   */
  private static int getOccurrences(Object[] array, Object item) {
    int occurrences = 0;
    for (int i = 0; i < array.length; i++) {
      // increment on found occurrence
      if (array[i] != null && array[i].equals(item))
        occurrences++;
    }
    return occurrences;
  }

}
