//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Sustenance Boulevard (assignment P07)
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
 * Restaurant order management - using a sorted doubly-linked list to manage the ordered dishes
 * sorted by their timestamp. Where Head: earliest orders, Tail: Most recent orders.
 * 
 * @author Safi
 */
public class RestaurantOrders implements SortedListADT<Order> {
  private LinkedOrder head = null; // front of the doubly-linked list
  private LinkedOrder tail = null; // end of the doubly-linked list
  private int size = 0; // number of orders currently in the list
  private final int CAPACITY; // maximum number of orders for this list

  /**
   * A no-argument constructor which creates a list with capacity 20
   */
  public RestaurantOrders() {
    this.CAPACITY = 20;
  }

  /**
   * A one-argument constructor which creates a list with the provided positive capacity
   * 
   * @param capacity maximum number of elements in the list
   * @throws IllegalArgumentException if the provided capacity is 0 or negative
   */
  public RestaurantOrders(int capacity) {
    if (capacity <= 0)
      throw new IllegalArgumentException("Invalid capacity parameter value.");

    this.CAPACITY = capacity;
  }

  /**
   * Getter for capacity of list
   * 
   * @return capacity of the list
   */
  public int capacity() {
    return this.CAPACITY;
  }

  /**
   * Checks if the orders list is empty.
   * 
   * @returns true if and only if the list is currently empty; false otherwise
   */
  @Override
  public boolean isEmpty() {
    if (this.size == 0)
      return true;
    return false;
  }

  /**
   * Getter for size of list
   * 
   * @returns the number of orders currently in the list
   */
  @Override
  public int size() {
    return this.size;
  }

  /**
   * Removes all orders from this list
   */
  @Override
  public void clear() {
    this.head = this.tail = null; // remove link references
    this.size = 0;
  }

  /**
   * Checks if the list has room for another element
   * 
   * @return true if size isn't using the entire capacity, otherwise false.
   */
  private boolean isFull() {
    if (this.size >= this.CAPACITY)
      return true;
    return false;
  }

  /**
   * Creates a String representation of the orders in this list from head to tail, space-separated
   * 
   * @return formated string representation of the orders list
   */
  public String readForward() {
    String list = " "; // The getDishes() result of each Order object in the list, separated by
                       // spaces

    LinkedOrder l = this.head; // starting node
    while (l != null) {
      list += l.getOrder().getDishes() + " "; // dishes name with space separator
      l = l.getNext();
    }

    // formated message:
    return String.format("The list contains %d order(s): [%s]", size, list);
  }

  /**
   * Creates a String representation of the orders in this list from tail to head, space-separated
   * 
   * @return formated string representation of the orders list
   */
  public String readBackward() {
    String list = " "; // The getDishes() result of each Order object in the list, separated by
    // spaces

    LinkedOrder l = this.tail; // starting node
    while (l != null) {
      list += l.getOrder().getDishes() + " "; // dishes name with space separator
      l = l.getPrevious();
    }

    // formated message:
    return String.format("The list contains %d order(s): [%s]", size, list);
  }

  /**
   * Adds a new Order to this sorted list in the correct position (most recent order to tail) if
   * there is room in the list
   * 
   * @param newOrder the target order to add into the list
   * @throws IllegalArgumentException if newOrder has the same timestamp as an existing order, a
   *                                  negative timestamp , or is null.
   */
  @Override
  public void placeOrder(Order newOrder) throws IllegalArgumentException {
    // verify capacity
    if (isFull())
      return;
    // validate input order timestamp
    if (newOrder == null || !isValidateTimestamp(newOrder))
      throw new IllegalArgumentException("Order with the same timestamp already exists or null");

    // search for the correct position to insert the input order
    insert(newOrder, getCorrectOrderedPosition(newOrder)); // add order to list at index position
  }

  /**
   * Validate an order's timestamp as duplicate timestamps are not allowed in the list.
   * 
   * @param o the target Order object
   * @return true if the input order has unique timestamp, otherwise false if it an order with the
   *         same timestamp already exist in the list.
   */
  private boolean isValidateTimestamp(Order o) {
    // case negative timestamp
    if ((new Order("", 0)).compareTo(o) == 1)
      return false;
    // case duplicate - reverse iteration (as it would be more likely for a duplicate to be found at
    // the end)
    LinkedOrder l = this.tail;
    while (l != null) {
      if (l.getOrder().compareTo(o) == 0)
        return false;
      l = l.getPrevious();
    }
    return true;
  }

  /**
   * Compare the target order to the ordered linked-list nodes data and retrieve its correct
   * position
   * 
   * @param Order o target order object
   * @return index position where the order should be placed
   */
  private int getCorrectOrderedPosition(Order o) {
    // forward iteration over the linked list
    LinkedOrder l = this.head;
    int i = 0;
    while (l != null && o.compareTo(l.getOrder()) == 1) {
      l = l.getNext();
      i++;
    }
    return i;
  }

  /**
   * Insert an order at specified index in the linked-list
   * 
   * @param o     Order object to be added
   * @param index position to add the object in the linked-list
   */
  private void insert(Order o, int index) {
    // previous and next neighboring nodes.
    LinkedOrder preceding = this.tail, succeeding = null; // default to head and tail nodes

    // get node that is currently at the target index
    LinkedOrder n = this.head;
    while (index-- > 0)
      n = n.getNext();
    if (n != null) {
      succeeding = n; // if any node currently at index will become the next node
      preceding = succeeding.getPrevious();
    }
    // create new linked-list node with appropriate links and data
    LinkedOrder newL = new LinkedOrder(o, preceding, succeeding);
    // update neighboring nodes
    if (preceding != null)
      preceding.setNext(newL);
    if (succeeding != null)
      succeeding.setPrevious(newL);
    // update head and tail
    if (succeeding == this.head)
      this.head = newL;
    if (preceding == this.tail)
      this.tail = newL;
    // increase size
    this.size++;
  }

  /**
   * Returns the order at position index of this list, without removing it
   * 
   * @param index of the element to return
   * @return the element of this sorted list at the given index
   * @throws IndexOutOfBoundsException if index is less than 0 or index is greater or equal to
   *                                   size()
   */
  @Override
  public Order get(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= this.size)
      throw new IndexOutOfBoundsException("Invalid index parameter");

    // forward iteration over the linked list
    LinkedOrder l = this.head;
    while (index > 0) {
      l = l.getNext();
      index--;
    }

    return l.getOrder();
  }

  /**
   * Find specified order's dishes in the list
   * <p>
   * (Note that matching the timestamp here does not matter, we are only concerned with the food
   * contained in the order)
   * 
   * @param findObject element to find in this list
   * @return the index of the first occurrence of findObject in this list, or -1 if this list does
   *         not contain that element.
   */
  @Override
  public int indexOf(Order findObject) {
    if (findObject == null)
      return -1;

    // forward iteration over the linked list
    LinkedOrder l = this.head;
    int i = 0;
    for (; i < this.size; i++) {
      if (l.getOrder().equals(findObject))
        return i;
      l = l.getNext();
    }

    return -1;
  }

  /**
   * Removes and returns the order at the given index
   * 
   * @param index of the element to be removed
   * @return the removed element
   * @throws IndexOutOfBoundsException if index is less than 0 or index is greater or equal to
   *                                   size()
   */
  @Override
  public Order removeOrder(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= this.size)
      throw new IndexOutOfBoundsException("Invalid index parameter");

    // forward iteration over the linked list
    LinkedOrder l = this.head;
    while (index > 0) {
      l = l.getNext();
      index--;
    }

    // update neighboring nodes and head/tail references
    LinkedOrder preceding = l.getPrevious(), succeeding = l.getNext();
    if (preceding == null) // case target node is header
      this.head = succeeding;
    else
      preceding.setNext(succeeding);
    if (succeeding == null) // case target node is tail
      this.tail = preceding;
    else
      succeeding.setPrevious(preceding);

    // update size
    this.size--;

    return l.getOrder(); // return removed node's order
  }

}
