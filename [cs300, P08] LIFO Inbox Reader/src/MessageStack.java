import java.util.Iterator;

//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: MessageStack.java
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
 * A stack data structure for holding inbox messages according to LIFO scheduling policy
 * 
 * @author Safi
 */
public class MessageStack implements StackADT<Message>, Iterable<Message> {
  private LinkedNode<Message> top; // top of the linked stack
  private int size; // # of Message objects stored in stack

  @Override
  public void push(Message element) {
    // TODO Auto-generated method stub

  }

  @Override
  public Message pop() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Message peek() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Iterator<Message> iterator() {
    // Your MessageStack's iterator() method must return a new MessageStackIterator that starts at
    // the top of the stack of Messages.
    return null;
  }


}
