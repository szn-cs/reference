///////////////////////// CUMULATIVE QUIZ FILE HEADER //////////////////////////
// Full Name:
// Campus ID:
// WiscEmail:
////////////////////////////////////////////////////////////////////////////////

// MAKE SURE TO SAVE your source file before uploading it to gradescope.
import java.util.NoSuchElementException;

/**
 * This class models a customer of a department store chain
 * 
 * TODO: Override the public int compareTo(Customer other){} method conforming to the specification
 * provided below. A customer is smaller than another customer if their phoneNumber
 * lexicographically precedes the other customer's phoneNumber. A customer is greater than another
 * customer if their phoneNumber is lexicographically greater than the other customer's phoneNumber.
 * Two customers are equals if they have the same phoneNumber. You can rely on the
 * String.compareTo() method to compare two phoneNumbers.
 * 
 */
class Customer implements Comparable<Customer> {
  // Do not add any additional data field to this class
  private String name; // name of this customer
  private String phoneNumber; // phone number of this customer
  private int rewardPoints; // reward points of this customer

  // Do not make any change to the constructor and the getter/setter methods of this class
  // The only method to change in this class is compareTo() method

  /**
   * Creates a new Customer
   * 
   * @param name         name to be assigned to this new customer
   * @param phoneNumber  phone number to be assigned to this new customer
   * @param rewardPoints initial rewardPoints to be assigned to this new customer
   */
  public Customer(String name, String phoneNumber, int rewardPoints) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.rewardPoints = rewardPoints;
  }

  /**
   * Compares this Customer to another customer based on their phone numbers
   * 
   * @param other other customer to compare to
   * @return a non-zero positive integer if this customer is greater than the other customer, zero
   *         if this customer equals other, a non-zero negative integer if this customer is less
   *         than the other customer.
   */
  @Override
  public int compareTo(Customer other) {
    return this.getPhoneNumber().compareTo(other.getPhoneNumber());
  }
  // MAKE SURE TO SAVE your source file before uploading it to gradescope.

  /**
   * Checks whether this Customer equals a specific object
   * 
   * @param o an object
   * @return true if this customer equals the input provided object and false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return o instanceof Customer && this.phoneNumber.equals(((Customer) o).phoneNumber);
  }

  /**
   * Returns a String representation of this customer
   * 
   * @return a String representation of this customer
   */
  @Override
  public String toString() {
    return this.name + "(" + this.phoneNumber + ")";
  }

  /**
   * Gets the name of this customer
   * 
   * @return the name of this customer
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the phone number of this customer
   * 
   * @return the phoneNumber of this customer
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Gets the reward points of this customer
   * 
   * @return the rewardPoints of this customer
   */
  public int getRewardPoints() {
    return rewardPoints;
  }

  /**
   * Changes the reward points of this customer
   * 
   * @param rewardPoints the rewardPoints to set
   */
  public void setRewardPoints(int rewardPoints) {
    this.rewardPoints = rewardPoints;
  }

}


// MAKE SURE TO SAVE your source file before uploading it to gradescope.
/**
 * This class models a customer catalog implemented as a binary search tree. The key search in this
 * customer catalog is the phoneNumber of the customers.
 *
 * TODOs: Complete the missing implementation of the following methods
 * 
 * 1. isEmpty() 2. size() 3. subscribeHelper() 4. find() 5. updateRewards() 6. getMax() 7.
 * unsubscribeHelper()
 */
public class CustomerCatalog {
  private BSTNode<Customer> root; // root of this BST
  private int size; // total number of customers stored in this customer catalog

  // You do not need to define a constructor for this class. The compiler will generate
  // automatically a no-argument constructor which creates a new empty CustomerCatalog.

  /**
   * Checks whether this CustomerCatalog is empty
   * 
   * @return true if this customer catalog is empty and false otherwise
   */
  public boolean isEmpty() {
    return size <= 0 ? true : false;
  }

  /**
   * Returns the size of this customer catalog
   * 
   * @return the total number of customers stored in this customer catalog
   */
  public int size() {
    return this.size;
  }
  // MAKE SURE TO SAVE your source file before uploading it to gradescope.

  /**
   * Subscribes/adds a new customer to this customer catalog
   * 
   * @param newCustomer to be added to this customer catalog
   * @throws IllegalArgumentException with or without error message if a match is found with
   *                                  newCustomer in this catalog
   */
  public void subscribe(Customer newCustomer) {
    // DO NOT MAKE ANY CHANGE TO THE IMPLEMENTATION OF THIS METHOD
    if (isEmpty()) {
      root = new BSTNode<Customer>(newCustomer);
    } else { // Add newCustomer to an non-empty catalog
      subscribeHelper(newCustomer, root);
    }
    size++;
  }

  /**
   * Recursive helper method to add newCustomer to the subtree rooted at current
   * 
   * @param newCustomer new customer to subscribe
   * @param current     root of a subtree
   * @throws IllegalArgumentException with or without error message if the subtree rooted at current
   *                                  contains already a match with newCustomer
   */
  protected static void subscribeHelper(Customer newCustomer, BSTNode<Customer> current) {
    // DO NOT CHECK whether newCustomer is null or not. The method will automatically throw
    // a NullPointerException if this case occurs.
    int comparison = newCustomer.compareTo(current.getData());
    // base case1 : subtree contains a match with newCustomer
    if (comparison == 0)
      throw new IllegalArgumentException();
    // else try to insert newCustomer to the left subtree
    // base case2: add newCustomer as the left child of current
    // recursive case: recurse left
    else if (comparison < 0) {
      if (current.getLeft() != null)
        subscribeHelper(newCustomer, current.getLeft());
      else
        current.setLeft(new BSTNode<Customer>(newCustomer));
    }

    // else try to insert newCustomer to the right subtree
    // base case3: add newCustomer as the right child of current
    // recursive case: recurse right
    else {
      if (current.getRight() != null)
        subscribeHelper(newCustomer, current.getRight());
      else
        current.setRight(new BSTNode<Customer>(newCustomer));

    }
  }
  // MAKE SURE TO SAVE your source file before uploading it to gradescope.

  /**
   * Finds a customer given their phone number within this catalog
   * 
   * @param phoneNumber phone number of a specific customer
   * @return the customer whose phone number has a match with the provided one as input
   * @throws NoSuchElementException with the error message "No match found!" if the customer with
   *                                the specific phone number was not found in this customer catalog
   */
  public Customer find(String phoneNumber) {
    // DO NOT CHECK WHETHER phoneNumber is null or not. A NullPointerException will be automatically
    // thrown if this case occurs.
    Customer c = findHelper(phoneNumber, this.root);
    if (c == null)
      throw new NoSuchElementException("No match found!");
    else
      return c;
  }

  /**
   * Recursive helper method which looks for a customer given their phone number in the BST rooted
   * at current
   * 
   * @param phoneNumber phone number of the customer to search in the subtree rooted at current
   * @param current     root of a subtree of this BST
   * @return the customer whose phoneNumber matches with the provided one and null if no match with
   *         phoneNumber was not found in the subtree rooted at current
   * 
   */
  protected static Customer findHelper(String phoneNumber, BSTNode<Customer> current) {
    // DO NOT MAKE ANY CHANGE TO THE IMPLEMENTATION OF THIS METHOD
    if (current == null) // reach a leaf or this binary search tree empty
      return null; // unsuccessful search

    if (phoneNumber.compareTo(current.getData().getPhoneNumber()) == 0)
      return current.getData(); // successful search
    if (phoneNumber.compareTo(current.getData().getPhoneNumber()) < 0)
      return findHelper(phoneNumber, current.getLeft()); // recurse on the left child (left
                                                         // sub-tree)
    return findHelper(phoneNumber, current.getRight()); // recurse on the right child (right
                                                        // sub-tree)
  }

  /**
   * Updates the reward points of the customer whose phone number is provided as input. It PRINTS
   * "No match found!" if the customer was not found in this catalog. This method MUST NOT throw any
   * exception.
   * 
   * @param phoneNumber phone number of a potential customer
   * @param points      rewards points to set
   */
  public void updateRewards(String phoneNumber, int points) {
    try {
      Customer c = find(phoneNumber);
      c.setRewardPoints(points);
    } catch (NoSuchElementException | NullPointerException e) {
      System.out.print("No match found!");
    }
  }

  /**
   * Returns the greatest customer (with respect to the result of Customer.compareTo() method) in
   * the subtree rooted at current
   * 
   * @param current root of a subtree of a binary search tree
   * @return the greatest customer in the subtree rooted at current
   * @throws a NoSuchElementException with or without error message if current is null.
   */
  protected static Customer getMax(BSTNode<Customer> current) {
    if (current == null)
      throw new NoSuchElementException();

    BSTNode<Customer> n = current;
    while (n.getRight() != null)
      n = n.getRight();

    return n.getData();

  }
  // MAKE SURE TO SAVE your source file before uploading it to gradescope.

  /**
   * Unsubscribes/removes a customer given their phone number from this catalog. This method does
   * nothing if no match found with phoneNumber in this catalog.
   * 
   * @param phoneNumber of the customer to unsubscribe
   */
  public void unsubscribe(String phoneNumber) {
    // DO NOT MAKE ANY CHANGE TO THE IMPLEMENTATION OF THIS METHOD
    Customer unsubscribe = new Customer("unsubscribe", phoneNumber, 0);
    root = unsubscribeHelper(unsubscribe, root);
    size--;
  }

  /**
   * Removes a customer from this catalog if match found with the provided one
   * 
   * @param customer a customer equal to the customer to be removed
   * @param current  the root of a subtree of a binary search tree of customers
   * @return the new root of the subtree after removing the specified customer and null if no match
   *         found
   */
  protected static BSTNode<Customer> unsubscribeHelper(Customer customer,
      BSTNode<Customer> current) {
    if (current == null) { // customer not found
      return null;
    }
    if (customer.compareTo(current.getData()) < 0) {
      // TODO recurse on the left subtree rooted at current.getLeft()
      // Hint: current.setLeft(/* recursive call */);
      current.setLeft(unsubscribeHelper(customer, current.getLeft()));
    } else if (customer.compareTo(current.getData()) > 0) {
      // TODO recurse on the right subtree
      current.setRight(unsubscribeHelper(customer, current.getRight()));
    } else // customer found
    if (current.getLeft() != null && current.getRight() != null) { // current has two children
      // TODO replace the data of current with the data field value of its predecessor
      BSTNode<Customer> predecessor = current.getLeft();
      while (predecessor.getRight() != null)
        predecessor = predecessor.getRight();
      current.setData(predecessor.getData());
      // remove the predecessor
      current.setLeft(unsubscribeHelper(current.getData(), current.getLeft()));
    } else // current has up to one child
    if (current.getLeft() != null) { // current has one left child
      current = current.getLeft();
    } else { // current has one right child or zero children
      // TODO Set current to its right child
      current = current.getRight();
    }
    return current; // return the new root of this subtree otherwise the changes to current will be
                    // lost
  }
  // MAKE SURE TO SAVE your source file before uploading it to gradescope.

  /**
   * Returns a String representation of the contents of this customer catalog sorted in the
   * increasing order
   * 
   * @return a String representation of this customer catalog
   */
  @Override
  public String toString() {
    return toStringHelper(root);
  }

  /**
   * Returns a string representation of this customer catalog
   * 
   * @param current root of a subtree of a BST storing customers
   * @return a String representation of the subtree rooted at current
   */
  protected static String toStringHelper(BSTNode<Customer> current) {
    String s = "";
    if (current == null)
      return s;
    else {
      // visit left subtree
      s = s + toStringHelper(current.getLeft());
      // visit data within the current node
      s = s + (current.getData()) + "; ";
      // visit right subtree
      s = s + toStringHelper(current.getRight());
      return s;
    }
  }

  /**
   * Runs a demo of this code
   */
  public static void demo() {
    CustomerCatalog catalog = new CustomerCatalog();
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 0 ; Contents:
    catalog.subscribe(new Customer("c1", "500", 10));
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 1 ; Contents: c1(500);
    catalog.subscribe(new Customer("c2", "600", 20));
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 2 ; Contents: c1(500); c2(600);
    catalog.subscribe(new Customer("c3", "300", 15));
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 3 ; Contents: c3(300); c1(500); c2(600);
    catalog.subscribe(new Customer("c4", "200", 50));
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 4 ; Contents: c4(200); c3(300); c1(500); c2(600);
    catalog.subscribe(new Customer("c5", "530", 10));
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 5 ; Contents: c4(200); c3(300); c1(500); c5(530); c2(600);
    catalog.subscribe(new Customer("c6", "400", 15));
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 6 ; Contents: c4(200); c3(300); c6(400); c1(500); c5(530); c2(600);
    System.out.println("Try to update rewards for phone 400. ");
    catalog.updateRewards("400", 50);
    // Try to update rewards for phone 400.
    System.out.print("Try to update rewards for phone 608-003. ");
    catalog.updateRewards("608-003", 100);
    // Try to update rewards for phone 608-003. No match found!
    catalog.unsubscribe("300");
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 5 ; Contents: c4(200); c6(400); c1(500); c5(530); c2(600);
    catalog.unsubscribe("600");
    System.out.println("Size: " + catalog.size() + " ; Contents: " + catalog);
    // Size: 4 ; Contents: c4(200); c6(400); c1(500); c5(530);
  }

  /**
   * main method
   * 
   * @param args input arguments
   */
  public static void main(String[] args) {
    demo();
  }
}
