import java.util.Arrays;

/**
 * This class explores examples of passing arrays to methods
 * @author mouna
 *
 */
public class PassingArraysToMethods {
  // When we pass an array as input argument to a method, a shallow copy of the array (its reference)
  // is actually passed to the method. Therefore, any change to the content of this array in the 
  // method will affect the array.
  // 
  // When we pass primitive type values to a method as input arguments, actually a copy of these values
  // are passed. Any change to these values (if not returned by the method) will be lost when the 
  // method returns.
  
  
  /**
   * Adds a String to an oversize array (list of strings)
   * @param arrayRef reference to an array of strings
   * @param currentSize size of the oversize array
   * @param addMe the element to be added to arrayRef
   * @return the new size of the oversize array
   */
  public static int addElement(String[] arrayRef, int currentSize, String addMe) {
    // Check that the array has space
    if (currentSize == arrayRef.length) {
      System.out.println("Unable to add a new element. The list is full.");
      return currentSize;
    }
    // If array has space, add the element to the array
    arrayRef[currentSize] = addMe;
    ++currentSize;
    return currentSize;
  }
  
  /**
   * Main method
   * @param args input arguments if any
   */
  public static void main(String[] args) {    
    String[] data = new String[5]; // -->  data: [null, null, null, null, null]  
    int size = 0;
    addElement(data, size, "A"); // --> data: ["A", null, null, null, null] size: 1
    addElement(data, size, "B"); // --> data: ["A", "B", null, null, null] size: 2
    addElement(data, size, "C"); // --> data: ["A", "B", "C", null, null] size: 3
    System.out.print("data: " + Arrays.toString(data)); // data: ["A", "B", "C", null, null]
    System.out.print(" size: " + size); // size: 3
      }

}