public class PerfectOversizeArrays {
   
   public static void main(String[] args) {
      // daysOfWeek is a perfect size array. 
      // It stores 7 elements (its size equals its capacity (length))
      // All the elements of a perfect size array are used when we create the array
      String[] daysOfWeek = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
      
      // cs300Roster is an oversize array. 
      // An oversize array is defined by a reference to the array and 
      // an integer variable which keeps track of the size of the array
      // (number of elements stored in the array).
      // An oversize array is initially empty, meaning that its size is 0
      // The size of an oversize array cannot excced its length
      // The size of an oversize array is incremented by one each time
      // a new element is added to the array and decremented by one each time
      // an element is removed from the array
      // All the elements stored in an oversize array must be located in the
      // range of indices from 0 to size-1
      // All elements located in an oversize array  in the range
      // of indices from size to length-1 are unused.

      // Example of an oversize array
      String[] cs300Roster = new String[150]; 
      int size = 0;
      
      
   }
}