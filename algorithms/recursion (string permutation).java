import java.util.Arrays;

/**
 * This class implements methods which generate permutations
 *
 */
public class Permutations {

  /**
   * Swap two elements within an array given their indexes
   * 
   * @param data array of chars
   * @param i    index of the first element to swap
   * @param j    index of the second element to swap
   */
  private static void swap(char[] data, int i, int j) {
    char x = data[i];
    data[i] = data[j];
    data[j] = x;
  }

  /**
   * Generates and displays recursively all the possible permutations of a subarray of chars
   * starting from index currentIndex
   * 
   * @param data         an array of chars
   * @param currentIndex starting index of the subarray to be considered in this method call
   */
  private static void generateCharPermutationsHelper(char[] data, int currentIndex) {
    // base case (subarray which contains only one element (last element) -> reach one possible
    // permutation
    if (currentIndex == data.length - 1) {
      System.out.println(String.valueOf(data));// display this one possible permutation
    }
    // all elements from index 0 to currentIndex-1 are fixed
    // Generate all the possible permutations of the subarray defined from index currentIndex to
    // string.length-1
    for (int i = currentIndex; i < data.length; i++) {
      // swap data[currentIndex] and data[i]
      swap(data, currentIndex, i);
      // generate all the possible permutations from index currentIndex+1 to data.length-1
      generateCharPermutationsHelper(data, currentIndex + 1);
      // swap data[currentIndex] and data[i] back
      swap(data, currentIndex, i);
    }
  }

  /**
   * Generates and displays recursively all the possible permutations of an array of chars
   * 
   * @param string an array of chars
   */
  public static void generateCharPermutations(char[] string) {
    generateCharPermutationsHelper(string, 0);
  }

  public static void main(String[] args) {
    String s = "ABC";
    generateCharPermutations(s.toCharArray());

  }

}