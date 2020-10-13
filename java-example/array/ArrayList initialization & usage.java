// This program shows how to use an ArrayList of items of type String.
// Try to create and use in your own an ArrayList of Integer items

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class implements a program that shows how to use an ArrayList of items of type String.
 * 
 * @author mouna
 *
 */
public class UsingArrayList {


  /**
   * Driver main method that includes a segment of code on how to use ArrayList objects
   * 
   * @param args input arguments to the main method if any
   */
  public static void main(String args[]) {
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    System.out.println(" Practice Example - Using ArrayList ");
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

    // Create an empty array list
    ArrayList<String> strings = new ArrayList<String>();
    System.out.println("Is strings null?: " + (strings == null));
    System.out.println("size: " + strings.size());
    System.out.println("isEmpty: " + strings.isEmpty());

    // Adding items to arrayList: Normal way of Array List initialization
    strings.add("Hello"); // The arraylist contains one object a String "Hello"
    // strings --> Hello
    strings.add("Welcome"); // adds "Welcome" at the end of the arraylist (here at
                            // the second position of the array list, i.e. at index 1)
    // strings --> Hello Welcome
    strings.add(0, "Greeting"); // it will add Greeting to the first position of the
                                // array list (at index 0)
    // strings --> Greeting Hello Welcome
    strings.add("Awesome"); // adds "Awesome" at the end of the array list
    // indexes 0 1 2 3
    // strings --> Greeting Hello Welcome Awesome
    strings.add(2, "Hi"); // adds Hi at index 2 of the list
    // indexes 0 1 2 3 4
    // strings --> Greeting Hello Hi Welcome Awesome


    // Display the contents of the array list
    System.out.println("\nDisplay the contents of the ArrayList strings:");
    System.out.println("Arraylist items: " + strings.toString());
    System.out.println("size: " + strings.size());
    // Output:
    // Display the contents of the ArrayList strings:
    // Arraylist items: [Greeting, Hello, Hi, Welcome, Awesome]
    // size: 5

    // use of a for-loop to display the contents of the array list
    System.out.println("\nDisplay the contents of the ArrayList strings using a for-loop:");
    System.out.print("Arraylist items: ");
    for (int i = 0; i < strings.size(); i++) {
      System.out.print(strings.get(i) + " ");
    }

    // Checking the index of an item of the array list
    System.out.println("\nChecking index of an item of the array list");;
    int pos = strings.indexOf("Greeting");
    System.out.println("The index of Greeting is: " + pos);
    pos = strings.indexOf("Great");
    System.out.println("The index of Great is: " + pos);

    // Checking if the array list is empty
    System.out.println("\nChecking if an array list is empty");
    boolean check = strings.isEmpty();
    System.out.println("Checking if the arraylist is empty: " + check);

    // Getting the size of the list
    System.out.println("\nGetting the size of the list");
    int size = strings.size();
    System.out.println("The size of the list is: " + size);

    // Checking if an item is included to the list
    System.out.println("\nChecking if an item is included to the list");
    boolean item = strings.contains("Wait");
    System.out.println("Checking if the arraylist contains the String Wait: " + item);
    item = strings.contains("Welcome");
    System.out.println("Checking if the arraylist contains the String Welcome: " + item);


    // Getting the item in a specific position
    System.out.println("\nGetting the item in a specific position");
    String element = strings.get(0);
    System.out.println("The item at the index 0 is: " + element);

    // System.out.println("The item at the index 5 is: " + strings.get(5));
    // --> IndexOutOfBoundsException thrown

    // Replacing an item
    System.out.println("\nReplacing an item");
    strings.set(1, "New");
    System.out.println("The arraylist after the replacement is: " + strings);

    // Removing items
    // removing the item at index 0
    System.out.println("\nRemoving an item from the array list given its index");
    strings.remove(0);
    System.out.println(
        "The arraylist after removing the item at index 0 from the array list: " + strings);

    // removing the first occurrence of an item
    System.out.println("\nRemoving the first occurrence of an item");
    strings.remove("Welcome");
    System.out.println("The final contents of the arraylist are: " + strings);

    // Converting an ArrayList to Array
    System.out.println("\nConverting an ArrayList to Array");
    String[] simpleArray = strings.toArray(new String[strings.size()]);
    System.out.println("The array created after the conversion of our arraylist is: "
        + Arrays.toString(simpleArray));

    // Retrieve items from an arraylist

    // One way to traverse an ArrayList: loop using index and size list
    System.out.println("\nRetrieving items with loop using index and size list");
    for (int i = 0; i < strings.size(); i++) {
      System.out.println("Index: " + i + " - Element: " + strings.get(i));
    }

    // Other Ways to create an ArrayList (non-empty ArrayList)
    // Creating an array list with the same value for all its items using Collections.ncopies
    System.out.println(
        "Creating an array list with the same value for all its items using Collections.ncopies");
    ArrayList<String> myList = new ArrayList<String>(Collections.nCopies(10, "Good"));
    System.out.println("ArrayList myList items: " + myList);
//    Creating an array list with the same value for all its items using Collections.ncopies
//    ArrayList myList items: [Good, Good, Good, Good, Good, Good, Good, Good, Good, Good]


    // Creating an array list and initializing its contents using Arrays.asList
    System.out
        .println("\nCreating an array list and initializing its contents using Arrays.asList");
    ArrayList<String> list = new ArrayList<String>(Arrays.asList("CS200", "CS300", "CS301"));
    System.out.println("ArrayList list items:" + list);
//    Creating an array list and initializing its contents using Arrays.asList
//    ArrayList list items:[CS200, CS300, CS301]


  }

}