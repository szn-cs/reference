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
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Unit tests for COVID Tracker program.
 * 
 * @author Safi
 * 
 */
public class COVIDTrackerTester {

  /**
   * Calls the test methods implemented in this class and displays output
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    System.out.println("testAddTest(): " + testAddTest());
    System.out.println("testRemoveIndividual(): " + testRemoveIndividual());
    System.out.println("testGetPopStats(): " + testGetPopStats());
    System.out.println("testGetIndividualStats(): " + testGetIndividualStats());

    // testing auxiliary methods
    // System.out.println("testGetArraySize(): " + getArraySize());
  }

  /**
   * Checks whether addTest() works as expected
   * 
   * @return true if method functionality is verified, false otherwise
   */
  public static boolean testAddTest() {
    // set UIDs representing different individuals.
    final String ID1 = "AB1234", ID2 = "CD2345", ID3 = "EF3456";

    // two empty arrays -> true; also checking that arrays were updated properly
    String[] pos = new String[2];
    String[] neg = new String[2];
    if (!COVIDTracker.addTest(pos, neg, ID1, false) || neg[0] == null)
      return false;
    if (!COVIDTracker.addTest(pos, neg, ID2, true) || pos[0] == null)
      return false;

    // two arrays with space -> true
    if (!COVIDTracker.addTest(pos, neg, ID2, false) || neg[1] == null)
      return false;

    // one full array but adding to one with space -> true
    if (!COVIDTracker.addTest(pos, neg, ID3, true) || pos[1] == null)
      return false;

    // one array with space but adding to full one -> false
    String[] pos2 = new String[2];
    if (COVIDTracker.addTest(pos2, neg, ID3, false))
      return false;

    // two full arrays -> false
    if (COVIDTracker.addTest(pos, neg, ID3, true))
      return false;

    // both arrays with zero capacity (edge case) -> false
    String[] arrayZero = new String[0];
    if (COVIDTracker.addTest(arrayZero, arrayZero, "EF3456", true))
      return false;

    return true;
  }

  /**
   * Checks whether removeIndividual() works as expected
   * 
   * @return true if method functionality is verified, false otherwise.
   */
  public static boolean testRemoveIndividual() {
    // set UIDs representing different individuals.
    final String ID1 = "AB1234", ID2 = "CD2345", ID3 = "EF3456";

    // two arrays contain occurrence, with multiple & single occurrences -> true;
    String[] pos = {ID1, ID2, ID3};
    String[] neg = {ID1, ID3, ID1};
    if (!COVIDTracker.removeIndividual(pos, neg, ID1) || containsValue(pos, ID1)
        || containsValue(neg, ID1))
      return false;

    // two arrays do not contain occurrence -> false
    if (COVIDTracker.removeIndividual(pos, neg, ID1))
      return false;

    // one array containing an occurrence -> true
    if (!COVIDTracker.removeIndividual(pos, neg, ID2) || containsValue(pos, ID2)
        || containsValue(neg, ID2))
      return false;

    // One empty array, another containing occurrence -> true
    String[] pos2 = new String[2];
    if (!COVIDTracker.removeIndividual(pos2, neg, ID3) || containsValue(pos2, ID3)
        || containsValue(neg, ID3))
      return false;

    // Two empty arrays -> false
    if (COVIDTracker.removeIndividual(pos2, neg, ID3))
      return false;

    // both arrays with zero capacity (edge case) -> false
    String[] arrayZero = new String[0];
    if (COVIDTracker.removeIndividual(arrayZero, arrayZero, ID1))
      return false;

    return true;
  }

  /**
   * Helper method to check if a reference value exists in an array.
   * 
   * @param array   An array containing reference values.
   * @param element Value to search for in the array.
   * @return true if value object occurs in the array.
   */
  private static boolean containsValue(Object[] array, Object element) {
    for (int i = 0; i < array.length; i++) {
      // is current object equals the value searched for.
      if (array[i] != null && array[i].equals(element))
        return true;
    }
    return false;
  }

  /**
   * Checks whether getPopStats() works as expected
   * 
   * @return true if method functionality is verified, false otherwise
   */
  public static boolean testGetPopStats() {
    // two example arrays containing some ids and null values
    {
      final String FIXTURE = "Total tests: 5\n" + "Total individuals tested: 3\n"
          + "Percent positive tests: 60.0%\n" + "Percent positive individuals: 100.0%";
      String[] pos = {"EF3456", "CD2345", "AB1234"};
      String[] neg = {"AB1234", "CD2345", null};
      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    // Empty arrays example
    {
      final String FIXTURE = "Total tests: 0\n" + "Total individuals tested: 0\n"
          + "Percent positive tests: 0.0%\n" + "Percent positive individuals: 0.0%";
      String[] pos = {null, null};
      String[] neg = {null, null, null};
      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    // negative empty array - resulting in no fractional part
    {
      final String FIXTURE = "Total tests: 3\n" + "Total individuals tested: 3\n"
          + "Percent positive tests: 0.0%\n" + "Percent positive individuals: 0.0%";
      String[] pos = {};
      String[] neg = {"EF3456", "CD2345", "AB1234"};
      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    // arrays with different IDs with proportion resulting in fractional part
    {
      final String FIXTURE = "Total tests: 10\n" + "Total individuals tested: 9\n"
          + "Percent positive tests: 70.0%\n" + "Percent positive individuals: 66.666666667%";
      String[] pos = {"EF3456", "CD2345", "AB1234", "TY2343", "GR9995", "PL8968", "TY2343"};
      String[] neg = {"XY2343", "RR9995", "ZL8968"};
      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    // arrays with IDs resulting in proportion with non repeating floating-point/decimal.
    {
      final String FIXTURE = "Total tests: 50\n" + "Total individuals tested: 27\n"
          + "Percent positive tests: 24.0%\n" + "Percent positive individuals: 37.037037037%";

      // Porduce similar data set from program specification example (look at the FIXTURE above).
      String[] pos, neg;
      {
        // # of positive individuals = 10, # of negative individuals = 17
        final int NUMBER_POS_INDIVIDUAL = 10, NUMBER_NEG_INDIVIDUAL = 17;
        // 12 positive tests & 38 negative tests for corresponding arrays
        final int NUMBER_POS_TEST = 12, NUMBER_NEG_TEST = 38;
        // create 27 unique IDs
        String[] uniqueIDs = new String[27];
        for (int i = 0; i < uniqueIDs.length; i++)
          uniqueIDs[i] = Integer.toString(i);
        pos = new String[NUMBER_POS_TEST];
        neg = new String[NUMBER_NEG_TEST];
        // fill in unique ID occurrences for each array
        System.arraycopy(uniqueIDs, 0, pos, 0, NUMBER_POS_INDIVIDUAL);
        System.arraycopy(uniqueIDs, NUMBER_POS_INDIVIDUAL, neg, 0, NUMBER_NEG_INDIVIDUAL);
        // fill null enteries with repeated individual data for each array
        for (int i = 0; i < pos.length; i++)
          if (pos[i] == null)
            pos[i] = pos[1]; // set repeated occurence of an ID
        for (int i = 0; i < neg.length; i++)
          if (neg[i] == null)
            neg[i] = neg[1]; // set repeated occurence of an ID
      }

      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    // All unique enteries
    {
      final String FIXTURE = "Total tests: 6\n" + "Total individuals tested: 6\n"
          + "Percent positive tests: 50.0%\n" + "Percent positive individuals: 50.0%";
      String[] pos = {"TY2343", "GR9995", "PL8968"};
      String[] neg = {"EF3456", "CD2345", "AB1234"};
      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    // Equal size positive & negative arrays with 2 individuals having 1 positive & 1 negative test
    {
      final String FIXTURE = "Total tests: 4\n" + "Total individuals tested: 2\n"
          + "Percent positive tests: 50.0%\n" + "Percent positive individuals: 100.0%";
      String[] pos = {"TY2343", "GR9995"};
      String[] neg = {"TY2343", "GR9995"};
      String result = COVIDTracker.getPopStats(pos, neg);
      if (!result.equals(FIXTURE))
        return false;
    }

    return true;
  }

  /**
   * Checks whether getIndividualStats() works as expected
   * 
   * @return true if method functionality is verified, false otherwise
   */
  public static boolean testGetIndividualStats() {
    // two example arrays containing some ids and null values
    {
      final String FIXTURE = "Total tests: 3\n" + "Positive: 2\n" + "Negative: 1";
      String[] pos = {"EF3456", "CD2345", "CD2345", "AB1234"};
      String[] neg = {"AB1234", "CD2345", null};
      String result = COVIDTracker.getIndividualStats(pos, neg, "CD2345");
      if (!result.equals(FIXTURE))
        return false;
    }

    // Empty arrays example
    {
      final String FIXTURE = "Total tests: 0\n" + "Positive: 0\n" + "Negative: 0";
      String[] pos = {null, null};
      String[] neg = {null, null, null};
      String result = COVIDTracker.getIndividualStats(pos, neg, "CD2345");
      if (!result.equals(FIXTURE))
        return false;
    }

    return true;
  }

}
