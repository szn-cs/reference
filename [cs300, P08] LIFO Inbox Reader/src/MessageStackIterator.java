import java.util.Iterator;
import java.util.NoSuchElementException;

//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: MessageStackIterator.java
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

// only imports permitted:
// import java.util.Iterator;
// import java.util.EmptyStackException;
// import java.util.NoSuchElementException;

/**
 * An iterator that iterates over Message stacks
 * 
 * @author Safi
 */
public class MessageStackIterator implements Iterator<Message> {
  private LinkedNode<Message> next; // keeps track of next element in the iteration

  /**
   * 
   * @param top represents the top of the stack
   */
  public MessageStackIterator(LinkedNode<Message> top) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean hasNext() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Message next() throws NoSuchElementException {
    // throw NoSuchElementException with a descriptive error message if the stack is exhausted and
    // the current element in the iteration does not have a next item.

    // Recall that when called for the first time, next() method defined in MessageStackIterator
    // class must return the message at the top or head of the stack (if it is not empty).
    return null;
  }
}
