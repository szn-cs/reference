// recursive algorithm; source: https://learn.zybooks.com/zybook/WISCCOMPSCI300Fall2020/chapter/6/section/4?content_resource_id=42398434
public class sort_merge {
  public static void merge(int [] numbers, int i, int j, int k) {
     int mergedSize = k - i + 1;       // Size of merged partition
     int mergedNumbers [] = new int[mergedSize]; // Temporary array for merged numbers
     int mergePos;                     // Position to insert merged number
     int leftPos;                      // Position of elements in left partition
     int rightPos;                     // Position of elements in right partition

     mergePos = 0;
     leftPos = i;                      // Initialize left partition position
     rightPos = j + 1;                 // Initialize right partition position

     // Add smallest element from left or right partition to merged numbers
     while (leftPos <= j && rightPos <= k) {
        if (numbers[leftPos] < numbers[rightPos]) {
           mergedNumbers[mergePos] = numbers[leftPos];
           ++leftPos;
        } 
        else {
           mergedNumbers[mergePos] = numbers[rightPos];
           ++rightPos;
        }
        ++mergePos;
     }

     // If left partition is not empty, add remaining elements to merged numbers
     while (leftPos <= j) {
        mergedNumbers[mergePos] = numbers[leftPos];
        ++leftPos;
        ++mergePos;
     }

     // If right partition is not empty, add remaining elements to merged numbers
     while (rightPos <= k) {
        mergedNumbers[mergePos] = numbers[rightPos];
        ++rightPos;
        ++mergePos;
     }

     // Copy merge number back to numbers
     for (mergePos = 0; mergePos < mergedSize; ++mergePos) {
        numbers[i + mergePos] = mergedNumbers[mergePos];
     }
  }

  public static void mergeSort(int [] numbers, int i, int k) {
     int j;

     if (i < k) {
        j = (i + k) / 2;  // Find the midpoint in the partition

        // Recursively sort left and right partitions
        mergeSort(numbers, i, j);
        mergeSort(numbers, j + 1, k);

        // Merge left and right partition in sorted order
        merge(numbers, i, j, k);
     }
  }

  public static void main(String [] args) {
     int [] numbers = {10, 2, 78, 4, 45, 32, 7, 11};
     int i;

     System.out.print("UNSORTED: ");
     for (i = 0; i < numbers.length; ++i) {
        System.out.print(numbers[i] + " ");
     }
     System.out.println();

     /* initial call to selection sort with index */
     mergeSort(numbers, 0, numbers.length - 1);

     System.out.print("SORTED: ");
     for (i = 0; i < numbers.length; ++i) {
        System.out.print(numbers[i] + " ");
     }
     System.out.println();
  }
}