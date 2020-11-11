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
 * Implements a data structure for holding linked-list nodes of restaurant orders.
 * 
 * @author Safi
 */
public class LinkedOrder {
  private final Order ORDER; // data field of this LinkedOrder
  private LinkedOrder previous; // reference to the Order before this one
  private LinkedOrder next; // reference to the Order after this one

  /**
   * A one-argument constructor which sets previous and next to null
   * 
   * @param order object holding order data
   * @throws IllegalArgumentException if order’s timestamp is negative
   */
  public LinkedOrder(Order order) throws IllegalArgumentException {
    if (!isOrderTimestampValid(order))
      throw new IllegalArgumentException("Order object timestamp is not valid");

    this.ORDER = order;
  }

  /**
   * A three-argument constructor which sets all fields as specified
   * 
   * @param order object holding order data
   * @param prev  the previous node in the linked list
   * @param next  the next node in the linked list
   * @throws IllegalArgumentException if order’s timestamp is negative
   */
  public LinkedOrder(Order order, LinkedOrder prev, LinkedOrder next)
      throws IllegalArgumentException {
    this(order);
    this.previous = prev;
    this.next = next;
  }

  /**
   * Check order validity
   * 
   * @param order object holding order data
   * @return false if order's timestamp is negative, otherwise true.
   */
  private boolean isOrderTimestampValid(Order order) {
    Order zeroTimestamp = new Order("", 0); // Dummy order
    if (zeroTimestamp.compareTo(order) == 1) // if the input order has negative timestamp
      return false;
    return true;
  }

  /**
   * Returns a reference to the order from this LinkedOrder
   * 
   * @return order object holding the order data
   */
  public Order getOrder() {
    return this.ORDER;
  }

  /**
   * Returns a reference to the previous LinkedOrder attached to this one
   * 
   * @return previous node in the linked list
   */
  public LinkedOrder getPrevious() {
    return this.previous;
  }

  /**
   * Returns a reference to the next LinkedOrder attached to this one
   * 
   * @return next node in the linked list
   */
  public LinkedOrder getNext() {
    return this.next;
  }

  /**
   * Sets the previous LinkedOrder attached to this one
   * 
   * @param previous node in the linked list
   */
  public void setPrevious(LinkedOrder previous) {
    this.previous = previous;
  }

  /**
   * Sets the next LinkedOrder attached to this one
   * 
   * @param next node in the linked list
   */
  public void setNext(LinkedOrder next) {
    this.next = next;
  }

}
