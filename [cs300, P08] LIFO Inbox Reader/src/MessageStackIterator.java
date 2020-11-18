//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: MessageStackIterator.java (Assignment P08)
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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator implementation that iterates over Messages in a stack
 * 
 * @author Safi
 */
public class MessageStackIterator implements Iterator<Message> {
  private LinkedNode<Message> next; // keeps track of next element in the iteration

  /**
   * Constructor receiving the node in a linked-list to start iterating from
   * 
   * @param top represents the top of the stack
   */
  public MessageStackIterator(LinkedNode<Message> top) {
    next = top;
  }

  /**
   * Check if the iteration has move Message elements
   * 
   * @return true if the iteration has more elements.
   */
  @Override
  public boolean hasNext() {
    return next != null ? true : false;
  }

  /**
   * Get the next Message in the iteration
   * 
   * @return the next Message element in the iteration
   * @throws NoSuchElementException when exhausting the stack, and no more elements left.
   */
  @Override
  public Message next() throws NoSuchElementException {
    // throw NoSuchElementException with a descriptive error message if the stack is exhausted and
    // the current element in the iteration does not have a next item.
    if (!hasNext())
      throw new NoSuchElementException("Stack exhausted, no more elements in the iteration");

    // When called for the first time, next() method defined in MessageStackIterator
    // class must return the message at the top or head of the stack (if it is not empty).
    Message m = next.getData(); // get the next message
    next = next.getNext(); // advance iterator
    return m; // return requested message
  }
}
