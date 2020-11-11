///////////////////////// CUMULATIVE QUIZ FILE HEADER //////////////////////////
// Full Name:
// Campus ID:
// WiscEmail:
////////////////////////////////////////////////////////////////////////////////


import java.util.ArrayList;
import java.util.Collections; // will be used for sorting your Furniture Inventory list later

/**
 * This class models Furniture objects which can be added to an inventory. Each object contains a
 * furniture's inventory number, a name, and weight, with accessors for these three data fields.
 * 
 * TODO: Modify this class to implement the Comparable interface, so that a Furniture object can be
 * compared to another Furniture object, and any collection of Furniture objects can be sorted.
 * 
 * Note: Do NOT add any additional data fields! You are allowed to add private helper methods if needed.
 * Note: MAKE SURE TO SAVE your source file before uploading it to gradescope.
 */
class Furniture {
  private static int inventoryNumGenerator = 1000; // generator of unique inventory numbers
  private final int INVENTORY_ID; // a unique inventory number assigned to this furniture
  private String name; // name of this furniture
  private int weight; // weight in lbs of this furniture

  /**
   * Create a new Furniture object
   * 
   * @param name   the name of the furniture
   * @param weight the weight of the Furniture in pounds
   */
  public Furniture(String name, int weight) {
    // TODO: Complete the implementation of this constructor to initialize all the instance fields
    INVENTORY_ID = 0; // CHANGE THIS, included only to avoid compile errors
  }

  /**
   * Access the name of this furniture
   * 
   * @return the Furniture's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Access the weight of this Furniture
   * 
   * @return the Furniture's weight in LB
   */
  public int getWeight() {
    return this.weight;
  }

  /**
   * Access the inventory number of this Furniture
   * 
   * @return the inventory number of this Furniture
   */
  public int getInventoryId() {
    return this.INVENTORY_ID;
  }

  /**
   * Should return true if and only if the Object o is a Furniture with the same name as this one.
   * 
   * @param o the Object to compare to
   * @return true if the Object is a Furniture, and this furniture and object have equal names;
   *         false otherwise
   */
  @Override
  public boolean equals(Object o) {
    // TODO complete this implementation!
    return false; // CHANGE THIS, included only to avoid compile errors
  }

  // TODO: add any methods required by the Comparable interface here.
  // The furniture objects will be compared with respect to their weights.
  // A furniture is greater than another furniture if its weight is higher than the other
  // furniture's weight.
  // Two furniture items are equal if they have the same weight.
  // A furniture item is smaller than another furniture item if its weight is lower than the other
  // item's weight.
}

// Note: MAKE SURE TO SAVE your source file before uploading it to gradescope.


/**
 * The FurnitureInventory class contains various Furniture objects, and offers various utility
 * methods for analyzing the contents of this inventory.
 * Note: Do NOT add any additional data fields! You are allowed to add private helper methods if needed.
 */
public class FurnitureInventory {

  // private static fields
  private static String[] names = {"desk", "chair", "sofa", "table", "book case", "lamp", "laptop",
      "desktop", "paper shredder", "printer"}; // names of all the furniture objects stored in this
                                               // inventory
  // private instance fields
  private ArrayList<Furniture> list; // list of all the furniture elements which belong to this
                                     // inventory

  // constructors
  /**
   * Default constructor: initializes this inventory's list of furniture.
   */
  public FurnitureInventory() {
    // TODO: initialize the FurnitureInventory's instance fields

  }

  /**
   * Creates a backup copy of a given FurnitureInventory object
   * 
   * @param toCopy the FurnitureInventory to copy
   * @return a new FurnitureInventory object with the same contents as toCopy
   */
  public static FurnitureInventory copyOf(FurnitureInventory toCopy) {
    // TODO: create a new FurnitureInventory which is a deep copy (not the deepest) of toCopy
    // The two objects will have different ArrayLists objects which contain
    // the same elements

    return null; // CHANGE THIS, included only to avoid compile errors
  }


  // mutators
  /**
   * Adds a Furniture to the FurnitureInventory
   * 
   * @param f the Furniture to add
   */
  public void addFurniture(Furniture f) {
    // TODO add the provided Furniture to this FurnitureInventory's list
    // You do not need to check whether the furniture to add is null or not


  }

  // accessors
  /**
   * Calculates the number of occurrences of a specific furniture in the list of this inventory
   * 
   * @param f a given furniture
   * @return the number of occurrences of f in the list of this inventory
   */
  public int getNumOccurrences(Furniture f) {
    // TODO calculate the number of furniture objects which match with f.
    // You do not need to check whether f is null or not.

    return 0; // CHANGE THIS, included only to avoid compile errors
  }

  /**
   * A private helper method to sort the list of this inventory in ascending order. Useful for the
   * getHeaviestFurniture() method!
   * 
   * NOTE: do not uncomment this method until you have correctly implemented the Comparable
   * interface in the Furniture class; it will cause an error and your code will not compile.
   */
  // TODO uncomment the code below before you proceed
//  private void sortInventory() {
//    Collections.sort(this.list);
//  }

  /**
   * Gets the inventory id of the heaviest Furniture stored within this FurnitureInventory
   * 
   * @return the inventory id of the highest-weight (heaviest) furniture item in the list of this
   *         FurnitureInventory
   */
  public int getHeaviestFurniture() {
    // TODO find and return the inventory id of the heaviest Furniture in this FurnitureInventory
    // Hint: implement the Comparable interface for Furniture first, then use the helper method
    // below.
    

  }

// Note: MAKE SURE TO SAVE your source file before uploading it to gradescope.
}
