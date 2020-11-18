//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: MessageStack.java (Assignment P08)
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

import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * A stack data structure for holding inbox messages according to LIFO scheduling policy
 * 
 * @author Safi
 */
public class MessageStack implements StackADT<Message>, Iterable<Message> {
  private LinkedNode<Message> top = null; // top of the linked stack
  private int size = 0; // # of Message objects stored in stack

  /**
   * Add a Message to this stack
   * 
   * @param element a Message to be added
   * @throws java.lang.IllegalArgumentException with a descriptive error message if the input
   *                                            element is null
   */
  @Override
  public void push(Message element) throws IllegalArgumentException {
    if (element == null) // if no message passed
      throw new IllegalArgumentException("Invalid null Message parameter passed");

    if (top == null) // top is not set
      top = new LinkedNode<Message>(element);
    else {
      LinkedNode<Message> n = new LinkedNode<Message>(element); // incoming pushed node
      n.setNext(top); // chain the node
      top = n; // set stack top
    }

    size++; // increment size
  }

  /**
   * Remove the Message on the top of this stack and return it
   * 
   * @return the Message element removed from the top of the stack
   * @throws java.util.EmptyStackException without error message if the stack is empty
   */
  @Override
  public Message pop() throws EmptyStackException {
    if (isEmpty()) // if empty
      throw new EmptyStackException();
    Message m = top.getData(); // extract top message data
    top = top.getNext(); // update stack removing the top element
    size--; // decrement size
    return m; // return message
  }

  /**
   * Get the Message element on the top of this stack
   * 
   * @return the Message element on the stack top
   * @throws java.util.EmptyStackException without error message if the stack is empty
   */
  @Override
  public Message peek() throws EmptyStackException {
    if (isEmpty()) // if empty
      throw new EmptyStackException();

    return top.getData(); // get top message data
  }

  /**
   * Check whether this stack is empty or not
   * 
   * @return true if this stack contains no elements, otherwise false
   */
  @Override
  public boolean isEmpty() {
    return (size == 0) ? true : false;
  }

  /**
   * Get the number of elements in this stack
   * 
   * @return the size of the stack
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * An iterator implemenation for the stack messages
   * 
   * @return an iterator over Messages of the stack through usage of MessageStackIterator class
   */
  @Override
  public Iterator<Message> iterator() {
    return new MessageStackIterator(top); // iterator which starts at the top of the stack of
                                          // Messages
  }
}
