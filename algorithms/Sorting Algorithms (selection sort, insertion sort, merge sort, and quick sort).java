import java.util.Arrays;

/**
 * This class implements the following sorting algorithms insertionSort, selectionSort, mergeSort,
 * QuickSort
 * 
 * @author Mouna
 */
public class Sorting {

  /**
   * This method performs a selection sort on an unsorted array
   * 
   * @param array of N unsorted elements of any type integer
   */
  public static void selectionSort(int[] array) {
    // time complexity: O(?), problem size is the length of the array
    // Sorting the array starting from the first element (i = 0) and increment
    // i by one after each iteration
    for (int i = 0; i < array.length - 1; i++) {
      // <outer loop> T(n) = ?
      int index = i; // index of the smallest element at the unsorted
                     // portion of the array
      // Selecting the smallest element in the portion of the array [i .. n-1]
      for (int j = i + 1; j < array.length; j++) // <inner loop>
        if (array[j] < array[index])
          index = j;
      int smallerElement = array[index];
      // Set the smallest element at its position i (swap the elements at i and
      // the index of the smallest element in the unsorted portion of the array
      array[index] = array[i];
      array[i] = smallerElement;
    }
  }


  /**
   * This method performs an insertion sort on an unsorted array
   * 
   * @param array of N unsorted elements of integers
   */
  public static void insertionSort(int[] array) {
    // time complexity at worst case: O(?), n is the length of the array
    // T(n) = ?

    // Initially, the first element (at index 0), considering by itself, is sorted.
    // Now, starting from index 1 to n - 1, where n represents the
    // length of the array, sort the elements in positions 0 through index

    for (int index = 1; index < array.length; index++) { // <outer loop>
      int nextValue = array[index]; // save the value of the element at index position
      // sort elements from index to 0 by inserting the nextValue at the appropriate position
      // we suppose that the elements ranging from 0 to index-1 have been
      // already sorted in the previous iterations of the outer loop
      int i = index; // i defined outside the for loop because we need its value
      // Find index position i where to insert nextValue and swap larger elements to make room
      while (i > 0 && nextValue < array[i - 1]) {// <inner loop>
        array[i] = array[i - 1];
        i--;
      }
      // stop when nextValue > array[i-1] or i == 0
      array[i] = nextValue; // insert nextValue at the position i of the array
    }
  }// end of insertionSort method


  /**
   * This method performs a merge sort on an "unsorted" array
   * 
   * @param array array of N integers.
   */
  public static void mergeSort(int[] array) {
    int[] tmpArray = new int[array.length]; // create a temporary array
    // of the same length than the array to be sorted to place the merged result
    // Merge sort algorithm cannot be done in-place
    mergeSortHelper(array, tmpArray, 0, array.length - 1); // call the recursive merge
    // sort helper method
  }

  /**
   * Helper method to perform a merge sort that makes recursive calls
   * 
   * @param array    an array of Comparable items.
   * @param tmpArray an array to place the merged result
   * @param left     the left-most index of the sub-array with respect to the original array indices
   * @param right    the right-most index of the sub-array with respect to the original array
   *                 indices
   */
  private static void mergeSortHelper(int[] array, int[] tmpArray, int left, int right) {
    // T(n) = number of recursive calls * running time complexity of the code which will be run for
    // every recursive call
    // = ?
    if (left < right) { // The portion of the array contains at least two items
      int center = (left + right) / 2; // index of the item at the middle of the current sub-array
      mergeSortHelper(array, tmpArray, left, center); // recursive call to sort the left half of the
                                                      // sub-array
      mergeSortHelper(array, tmpArray, center + 1, right); // recursive call to sort the left half
                                                           // of the sub-array

      merge(array, tmpArray, left, center + 1, right); // merge the two sorted halves of the
                                                       // sub-array --> O(n)
    }
    // else //(an empty array or array that contains one element: a base case is reached): nothing
    // will be done,

  }

  /**
   * Helper method that merges two sorted halves of a subarray.
   * 
   * @param array    an array of Comparable items.
   * @param tmpArray an array to place the merged result.
   * @param leftPos  the left-most index of the subarray.
   * @param rightPos the index of the start of the second half.
   * @param rightEnd the right-most index of the subarray.
   */
  private static void merge(int[] array, int[] tmpArray, int leftPos, int rightPos, int rightEnd) {
    // T(n) = O(n)
    // illustration done in lecture, leftPos represents i, rightPos represents j, and
    // tmpPos represents k

    int leftEnd = rightPos - 1; // Last index of first/left half
    int tmpPos = leftPos; // Set tmpPos to the initial index of first/left half
    int numElements = rightEnd - leftPos + 1; // number of elements to merge

    // Main loop to compare elements of both partitions and merge them in a temporary array
    while (leftPos <= leftEnd && rightPos <= rightEnd)
      if (array[leftPos] <= array[rightPos])
        tmpArray[tmpPos++] = array[leftPos++];
      else
        tmpArray[tmpPos++] = array[rightPos++];

    // Copy rest of first/left half
    while (leftPos <= leftEnd)
      tmpArray[tmpPos++] = array[leftPos++];

    // Copy rest of right/second half
    while (rightPos <= rightEnd)
      tmpArray[tmpPos++] = array[rightPos++];

    // Copy tmpArray back
    for (int i = 0; i < numElements; i++, rightEnd--)
      array[rightEnd] = tmpArray[rightEnd];
  }



  /**
   * Performs a quick sort on an array of unsorted integers
   * 
   * @param array of integers
   */
  public static void quickSort(int[] array) {
    quickSort(array, 0, array.length - 1); // call the recursive quick sort helper method
  }

  /**
   * Helper method to perform a quick sort that makes recursive calls
   * 
   * @param array      of N elements
   * @param leftIndex  - left position of the partition of the array to sort
   * @param rightIndex - right position of the partition of the array
   */
  private static void quickSort(int[] array, int leftIndex, int rightIndex) {

    if (leftIndex >= rightIndex) {
      return; // Base case: If there are 1 or zero entries to sort, partition is trivially sorted
    }

    // Partition the data within the array. Value index returned from partitioning
    // is the location of the last item in low partition.
    int index = partition(array, leftIndex, rightIndex); // --> O(?)

    // Recursively sort low partition (leftIndex to index) and high partition (index + 1 to
    // rightIndex)

    quickSort(array, leftIndex, index); // recursive call on the low partition to sort small
                                        // elements
    quickSort(array, index + 1, rightIndex); // recursive call on the high partition to sort the
                                             // large elements
  }

  /**
   * Helper method that selects the median of a sub-array as pivot and partitions the data within
   * the array into small and large elements with respect to the pivot
   * 
   * @param array     of comparable N items
   * @param leftPos   : first index of subarray with respect to the original array
   * @param rightPos: last index of the subarry with respect to the original array
   * @return the index of the last element in the low partition (small elements partition)
   */
  private static int partition(int[] array, int leftPos, int rightPos) {
    int low = leftPos;
    int high = rightPos;
    int temp;
    boolean done = false;

    // Pick middle element as pivot
    int midpointPos = leftPos + (rightPos - leftPos) / 2; // (leftPos + rightPos)/2
    int pivot = array[midpointPos]; // The middle element is chosen as pivot
    while (!done) {

      // Increment low while array[low] < pivot
      while (array[low] < pivot) {
        low++;
      }

      // Decrement high while pivot < array[high]
      while (pivot < array[high]) {
        high--;
      }

      // If there are zero or one items remaining, all numbers are partitioned. Return high
      if (low >= high) {
        done = true;
      } else {
        // Swap references
        // Swap array[low] and array[high]
        temp = array[low];
        array[low] = array[high];
        array[high] = temp;
        // update low and high
        low++;
        high--;
      }
    }
    return high; // return the index of the last element in the low partition
  }
}