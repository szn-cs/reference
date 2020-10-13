/**
 * This class implements a bunch of search algorithms linear search, and binary search (recursive
 * and iterative versions)
 */
public class Search {

  /**
   * Searches for an item with an array using linear search or sequential search method
   * 
   * @param data array storing n integers (data is not necessary sorted)
   * @param x    key item to search
   * @return the index of x if there is a a match within the data array, -1 otherwise
   */
  public static int linearSearch(int[] data, int x) {
    // Time complexity: O(n), problem size n is the number of items stored within the data array

    // traverse the array looking for a match with x
    for (int i = 0; i < data.length; i++)
      if (data[i] == x) // element found
        return i;
    return -1; // element not found

  }

  /**
   * A recursive implementation of binary search algorithm
   * 
   * @param data sorted array of integers
   * @param x    target element to search for
   * @return index of x if it's found in the array or -1 otherwise
   */
  public static int recursiveBinarySearch(int[] data, int x) {
    // Recursive binary search algorithm
    return binarySearchRecursiveHelper(data, x, 0, data.length - 1); // O(log(n))
  }

  /**
   * Helper method for the recursiveBinarySearch method performs the recursive calls over the
   * different portions of the array as the search process progresses
   * 
   * @param data   array of sorted elements
   * @param target key element to search
   * @param low    first index of the portion of the array to consider in the search process
   * @param high   last index of the portion of the array to consider in the search process
   * @return index of target if stored in the portion [low, high] of the array data, -1 otherwise
   */
  private static int binarySearchRecursiveHelper(int[] data, int target, int low, int high) {
    // Running time: O(log(n)) = number of recursive calls at worst case * runtime complexity of the code
    // which will be run for every recursive call
    // T(n) = O(log(n)) * O(1) = O(log(n))
    if (low > high) { // empty portion of the array
      return -1; // target not found (base case)
    } else {
      int mid = (low + high) / 2; // index of the median
      if (data[mid] < target) { // too small; go right
        return binarySearchRecursiveHelper(data, target, mid + 1, high);
      } else if (data[mid] > target) { // too large; go left
        return binarySearchRecursiveHelper(data, target, low, mid - 1);
      } else { // data[mid] equals the target
        return mid; // target found (base case)
      }
    }
  }

  /**
   * An iterative implementation of binary search algorithm
   * 
   * @param data sorted array of integers
   * @param x    target element to search for
   * @return index of x if it's found in the array or -1 otherwise
   */
  public static int binarySearch(int[] data, int x) {
    // Time complexity O(log(n)), problem size n is the size of the array data
    int low = 0;
    int high = data.length - 1;

    while (low <= high) {

      int mid = (low + high) / 2; // index of the median element
                                  // in the array
      if (x < data[mid]) { // too small; go right
        high = mid - 1;
      }

      if (x > data[mid]) { // too large; go left
        low = mid + 1;
      }

      if (x == data[mid]) { // target found
        return mid; // return target index
      }
    }
    return -1; // target not found
  }


  /**
   * tests the implementation of the different search algorithms
   * 
   * @param args
   */
  public static void main(String[] args) {

    /**
     * Linear Search - example
     */
    // array of integers
    int[] data = new int[] {90, 70, 50, 30, 60, 40, 20, 80, 100, 10};
    System.out.print("data: ");
    for (int e : data) {
      System.out.print(e + " ");
    }
    System.out.println();
    System.out.println("index of 20: " + linearSearch(data, 20));
    System.out.println("index of 35: " + linearSearch(data, 35));

    // ###########################################

    /**
     * Linear versus Binary Search - sorted array
     */
    // create a sorted array of integers
    int[] array = new int[] {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

    System.out.print("\ndata: ");
    // print out array elements
    for (int e : array) {
      System.out.print(e + " ");
    }
    System.out.println(); // print new line

    // Sequential search
    System.out.println("~~~~~~Linear Search~~~~~~");
    System.out.println("index of " + 90 + ": " + linearSearch(array, 90));

    System.out.println("index of " + 25 + ": " + linearSearch(array, 25));

    // iterative binary search
    System.out.println("~~~~~~Iterative Binary Search~~~~~~");
    System.out.println("index of " + 90 + ": " + binarySearch(array, 90));

    System.out.println("index of " + 25 + ": " + binarySearch(array, 25));

    // recursive binary search
    System.out.println("~~~~~~Recursive Binary Search~~~~~~");
    System.out.println("index of " + 90 + ": " + recursiveBinarySearch(array, 90));

    System.out.println("index of " + 25 + ": " + recursiveBinarySearch(array, 25));

  }

}