import java.util.EmptyStackException;
import java.util.NoSuchElementException;

//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: InboxReaderTester.java
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
 * Implements unit test methods to check the correctness of the implementation of the MessageStack,
 * Inbox, and MessageStackIterator classes defined in LIFO Inbox Reader programming assignment.
 *
 * @author Safi
 */
public class InboxReaderTester {

  /**
   * Calls test suite method
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    System.out.println("Tests status: " + (runInboxReaderTestSuite() ? "passing" : "failing"));
  }


  /**
   * Run all the test methods implemented in this class
   * 
   * @return true if all the test methods defined in this class pass, and false otherwise.
   */
  public static boolean runInboxReaderTestSuite() {
    final String postfix = "\n"; // message string ending
    String m = ""; // names of failing methods
    boolean status = true; // Testbench status

    if (!testStackConstructorIsEmptyPushPeek()) {
      status = false;
      m += "testStackConstructorIsEmptyPushPeek" + postfix;
    }

    if (!testStackPop()) {
      status = false;
      m += "testStackPop" + postfix;
    }

    if (!testInboxReadMessage()) {
      status = false;
      m += "testInboxReadMessage" + postfix;
    }

    if (!testInboxReceiveMessage()) {
      status = false;
      m += "testInboxReceiveMessage" + postfix;
    }

    if (!testInboxMarkAllMessagesAsRead()) {
      status = false;
      m += "testInboxMarkAllMessagesAsRead" + postfix;
    }

    if (!testMessageStackIterator()) {
      status = false;
      m += "testMessageStackIterator" + postfix;
    }

    if (!status)
      System.out.println(m);

    return status;
  }

  /**
   * Checks for the correctness of the constructor of the MessageStack, MessageStack.push(),
   * MessageStack.peek(), MessageStack.isEmpty(), and MessageStack.size() methods.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise.
   */
  public static boolean testStackConstructorIsEmptyPushPeek() {
    try {
      MessageStack ms = new MessageStack();
      Message m1 = new Message("t1", "m1");
      Message m2 = new Message("t2", "m2");
      Message m3 = new Message("t3", "m3");

      // (1) Create a new MessageStack object. The new created stack must be empty and its size must
      // be zero.
      {
        if (!(ms.isEmpty() == true && ms.size() == 0))
          return false;
        if (ms.peek() != null)
          return false;
      }

      // (2) You can consider calling peek method on the empty stack. An EmptyStackException is
      // expected to be thrown by the peek method call.
      {
        try {
          ms.pop();
          return false;
        } catch (EmptyStackException e) {
          // expected behavior
        }
      }

      // (3) Then, push a Message onto the stack and then use peek to verify that the correct item
      // is at the top of the stack. Make sure also to check that isEmpty() must return false and
      // the size of the stack is one. The peek() method call should not make any change to the
      // contents of the stack.
      {
        ms.push(m1);
        if (ms.size() != 1 || ms.isEmpty() == true)
          return false;
        if (!ms.peek().equals(m1) || ms.size() != 1 || ms.isEmpty() == true)
          return false;
        if (ms.pop().equals(m1) || ms.size() != 0 || ms.isEmpty() != true)
          return false;
      }

      // (4) You can further consider pushing other elements onto the stack. Then, each call of
      // peek() method should return the correct Message object that should be at the top of the
      // stack. After pushing a new message to the stack double check that the size of the stack was
      // incremented by one.
      {
        ms.push(m1);
        if (!(ms.peek().equals(m1) && ms.size() == 1 && ms.isEmpty() == false))
          return false;
        ms.push(m2);
        if (!(ms.peek().equals(m2) && ms.size() == 2 && ms.isEmpty() == false))
          return false;
        ms.push(m3);
        if (!(ms.peek().equals(m3) && ms.size() == 3 && ms.isEmpty() == false))
          return false;
        if (!ms.pop().equals(m3) || ms.size() != 2 || ms.isEmpty() == true)
          return false;
        if (!ms.pop().equals(m2) || ms.size() != 1 || ms.isEmpty() == true)
          return false;
        if (!ms.pop().equals(m1) || ms.size() != 0 || ms.isEmpty() != true)
          return false;
        // make sure no other elements exist
        try {
          ms.pop();
          return false;
        } catch (EmptyStackException e) {
          // expected behavior
        }
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }


  /**
   * Checks for the correctness of MessageStack.pop(). It calls MessageStack.push() and
   * MessageStack.peek() methods (checking their correctness too). This method must consider at
   * least the test scenarios provided in the detailed description of these javadocs.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise.
   */
  public static boolean testStackPop() {
    try {
      MessageStack ms = new MessageStack();
      Message m1 = new Message("t1", "m1");
      Message m2 = new Message("t2", "m2");
      Message m3 = new Message("t3", "m3");

      // (1) Try to create a new empty MessageStack. Then, try calling pop method on the empty
      // stack. An EmptyStackException is expected to be thrown as a result.
      {
        try {
          ms.pop();
          return false;
        } catch (EmptyStackException e) {
          // expected behavior
        }
      }

      // (2) Try to push a message to the stack. Then, try to call the pop method on the stack which
      // contains only one element. Make sure that the pop() operation returned the expected
      // message, and that the stack is empty and its size is zero.
      {
        ms.push(m1);
        if (ms.size() != 1 || ms.isEmpty())
          return false;
        if (!ms.pop().equals(m1) || ms.size() != 0 || !ms.isEmpty())
          return false;
        // verify that stack doesn't hold any element
        try {
          ms.pop();
          return false;
        } catch (EmptyStackException e) {
          // expected behavior
        }
      }

      // (3) Then, try to push at least three messages, then call pop method on the stack which
      // contains 3 elements, the element at the top of the stack must be returned and removed from
      // the stack and its size must be decremented by one. You can further keep popping the
      // elements of the stack one by one until it becomes empty and check each time that the pop
      // operation performs appropriately.This test method must return false if it detects at least
      // one defect.
      {
        ms.push(m1);
        if (!(ms.peek().equals(m1) && ms.size() == 1 && ms.isEmpty() == false))
          return false;
        ms.push(m2);
        if (!(ms.peek().equals(m2) && ms.size() == 2 && ms.isEmpty() == false))
          return false;
        ms.push(m3);
        if (!(ms.peek().equals(m3) && ms.size() == 3 && ms.isEmpty() == false))
          return false;
        if (!ms.pop().equals(m3) || ms.size() != 2 || ms.isEmpty() == true)
          return false;
        if (!ms.pop().equals(m2) || ms.size() != 1 || ms.isEmpty() == true)
          return false;
        if (!ms.pop().equals(m1) || ms.size() != 0 || ms.isEmpty() != true)
          return false;
        // make sure no other elements exist
        try {
          ms.pop();
          return false;
        } catch (EmptyStackException e) {
          // expected behavior
        }
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * Checks for the correctness of the Inbox.ReadMessage() method.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise.
   */
  public static boolean testInboxReadMessage() {
    try {
      Inbox inbox = new Inbox();
      Message m1 = new Message("t1", "m1");
      Message m2 = new Message("t2", "m2");
      Message m3 = new Message("t3", "m3");
      Message m4 = new Message("t4", "m4");

      // (1) when Inbox.unreadMessageBox is empty
      {
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals("Nothing in Read"))
          return false;
        if (!inbox.readMessage().equals("Nothing in Read"))
          return false;
        // verify sizes are not changed
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.traverseUnreadMessages().equals("Unread(0)\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;
      }

      // (2) when Inbox.unreadMessageBox is not empty. You have to make sure the read message
      // has been popped off the Inbox.unreadMessageBox and pushed into the Inbox.readMessageBox
      // You can rely on Inbox.peekReadMessage() and Inbox.getStatistics() to check the method
      // behavior was as expected.
      {
        inbox.receiveMessage(m1);
        if (!inbox.getStatistics().equals("Unread (1)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals(m1.toString()))
          return false;
        if (!inbox.traverseUnreadMessages()
            .equals("Unread(1)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;

        inbox.receiveMessage(m2);
        if (!inbox.getStatistics().equals("Unread (2)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals(m2.toString()))
          return false;
        if (!inbox.traverseUnreadMessages().equals("Unread(2)\n" + m1.getID() + " "
            + m1.getSUBJECT() + "\n" + m2.getID() + " " + m2.getSUBJECT() + "\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;

        inbox.receiveMessage(m3);
        if (!inbox.getStatistics().equals("Unread (3)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals(m3.toString()))
          return false;
        if (!inbox.traverseUnreadMessages()
            .equals("Unread(3)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
                + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;

        inbox.receiveMessage(m4);
        if (!inbox.getStatistics().equals("Unread (4)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals(m4.toString()))
          return false;
        if (!inbox.traverseUnreadMessages()
            .equals("Unread(3)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
                + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n" + m4.getID()
                + " " + m4.getSUBJECT() + "\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;

        // read messages:
        if (!inbox.readMessage().equals(m4.toString()))
          return false;
        if (!inbox.peekReadMessage().equals(m3.toString()))
          return false;
        // verify sizes are not changed
        if (!inbox.getStatistics().equals("Unread (3)" + "\n" + "Read (1)"))
          return false;

        if (!inbox.readMessage().equals(m3.toString()))
          return false;
        if (!inbox.peekReadMessage().equals(m2.toString()))
          return false;
        // verify sizes are not changed
        if (!inbox.getStatistics().equals("Unread (2)" + "\n" + "Read (2)"))
          return false;

        if (!inbox.readMessage().equals(m2.toString()))
          return false;
        if (!inbox.peekReadMessage().equals(m1.toString()))
          return false;
        // verify sizes are not changed
        if (!inbox.getStatistics().equals("Unread (1)" + "\n" + "Read (3)"))
          return false;

        if (!inbox.readMessage().equals(m1.toString()))
          return false;
        if (!inbox.peekReadMessage().equals("Nothing in Read"))
          return false;
        // verify sizes are not changed
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (4)"))
          return false;

        if (!inbox.readMessage().equals("Nothing in Read"))
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * Checks for the correctness of the Inbox.ReceiveMessage() method
   * 
   * @return true when this test verifies a correct functionality, and false otherwise.
   */
  public static boolean testInboxReceiveMessage() {
    try {
      Inbox inbox = new Inbox();
      Message m1 = new Message("t1", "m1");
      Message m2 = new Message("t2", "m2");
      Message m3 = new Message("t3", "m3");

      inbox.receiveMessage(null);
      if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (0)"))
        return false;
      if (!inbox.peekReadMessage().equals("Nothing in Read"))
        return false;
      if (!inbox.traverseUnreadMessages().equals("Unread(0)\n"))
        return false;
      if (!inbox.traverseReadMessages().equals("Read(0)\n"))
        return false;

      inbox.receiveMessage(m1);
      if (!inbox.getStatistics().equals("Unread (1)" + "\n" + "Read (0)"))
        return false;
      if (!inbox.peekReadMessage().equals(m1.toString()))
        return false;
      if (!inbox.traverseUnreadMessages()
          .equals("Unread(1)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n"))
        return false;
      if (!inbox.traverseReadMessages().equals("Read(0)\n"))
        return false;

      inbox.receiveMessage(m2);
      if (!inbox.getStatistics().equals("Unread (2)" + "\n" + "Read (0)"))
        return false;
      if (!inbox.peekReadMessage().equals(m2.toString()))
        return false;
      if (!inbox.traverseUnreadMessages().equals("Unread(2)\n" + m1.getID() + " " + m1.getSUBJECT()
          + "\n" + m2.getID() + " " + m2.getSUBJECT() + "\n"))
        return false;
      if (!inbox.traverseReadMessages().equals("Read(0)\n"))
        return false;

      inbox.receiveMessage(m3);
      if (!inbox.getStatistics().equals("Unread (3)" + "\n" + "Read (0)"))
        return false;
      if (!inbox.peekReadMessage().equals(m3.toString()))
        return false;
      if (!inbox.traverseUnreadMessages()
          .equals("Unread(3)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
              + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n"))
        return false;
      if (!inbox.traverseReadMessages().equals("Read(0)\n"))
        return false;

      // allow duplicate messages to be added
      inbox.receiveMessage(m3);
      if (!inbox.getStatistics().equals("Unread (4)" + "\n" + "Read (0)"))
        return false;
      if (!inbox.peekReadMessage().equals(m3.toString()))
        return false;
      if (!inbox.traverseUnreadMessages()
          .equals("Unread(4)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
              + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n" + m3.getID()
              + " " + m3.getSUBJECT() + "\n"))
        return false;
      if (!inbox.traverseReadMessages().equals("Read(0)\n"))
        return false;

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * Checks for the correctness of the Inbox.markAllMessagesAsRead() method
   * 
   * @return true when this test verifies a correct functionality, and false otherwise.
   */
  public static boolean testInboxMarkAllMessagesAsRead() {
    try {
      Inbox inbox = new Inbox();
      Message m1 = new Message("t1", "m1");
      Message m2 = new Message("t2", "m2");
      Message m3 = new Message("t3", "m3");

      // check on empty list
      {
        int readMessageNumber = inbox.markAllMessagesAsRead();
        if (readMessageNumber != 0)
          return false;
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals("Nothing in Read"))
          return false;
        if (!inbox.traverseUnreadMessages().equals("Unread(3)\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;
      }

      // add messages:
      inbox.receiveMessage(m1);
      inbox.receiveMessage(m2);
      inbox.receiveMessage(m3);

      // verify status before the tested method call
      if (!inbox.getStatistics().equals("Unread (3)" + "\n" + "Read (0)"))
        return false;
      if (!inbox.peekReadMessage().equals(m3.toString()))
        return false;
      if (!inbox.traverseUnreadMessages()
          .equals("Unread(3)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
              + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n"))
        return false;
      if (!inbox.traverseReadMessages().equals("Read(0)\n"))
        return false;

      // test functionality on non-empty list
      {
        // mark as read
        int readMessageNumber = inbox.markAllMessagesAsRead();
        if (readMessageNumber != 3)
          return false;
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (3)"))
          return false;
        if (!inbox.peekReadMessage().equals("Nothing in Read"))
          return false;
        if (!inbox.traverseUnreadMessages().equals("Unread(0)\n"))
          return false;
        if (!inbox.traverseReadMessages()
            .equals("Read(0)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
                + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n"))
          return false;
      }

      // verify repeated calls
      {
        int readMessageNumber = inbox.markAllMessagesAsRead();
        if (readMessageNumber != 0)
          return false;
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (3)"))
          return false;
        if (!inbox.peekReadMessage().equals("Nothing in Read"))
          return false;
        if (!inbox.traverseUnreadMessages().equals("Unread(0)\n"))
          return false;
        if (!inbox.traverseReadMessages()
            .equals("Read(3)\n" + m1.getID() + " " + m1.getSUBJECT() + "\n" + m2.getID() + " "
                + m2.getSUBJECT() + "\n" + m3.getID() + " " + m3.getSUBJECT() + "\n"))
          return false;
      }

      // test related functionalities of emptying the message stacks
      {
        int removedNumber = inbox.emptyReadMessageBox();
        if (removedNumber != 3)
          return false;
        if (!inbox.getStatistics().equals("Unread (0)" + "\n" + "Read (0)"))
          return false;
        if (!inbox.peekReadMessage().equals("Nothing in Read"))
          return false;
        if (!inbox.traverseUnreadMessages().equals("Unread(0)\n"))
          return false;
        if (!inbox.traverseReadMessages().equals("Read(0)\n"))
          return false;
        // repeated call
        removedNumber = inbox.emptyReadMessageBox();
        if (removedNumber != 0)
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * Checks for the correctness of MessageStackIterator.hasNext() and MessageStackIterator.next()
   * methods.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise.
   */
  public static boolean testMessageStackIterator() {
    try {
      Message m1 = new Message("t1", "m1");
      Message m2 = new Message("t2", "m2");
      Message m3 = new Message("t3", "m3");

      // empty iterator
      {
        MessageStackIterator st = new MessageStackIterator(null);
        if (st.hasNext())
          return false;
        try {
          st.next();
          return false;
        } catch (NoSuchElementException e) {
          // expected behavior
        }
      }

      // You can rely on the constructor of the LinkedNode class which takes two input parameters
      // (setting both data and next instance fields) to create a chain of linked nodes (at least 3
      // linked nodes) which carry messages as data fields. Then, create a new
      // MessageStackIterator() and pass it the head of the chain of linked nodes that you created.
      // n1 -> n2 -> n3 -> null
      LinkedNode<Message> n3 = new LinkedNode<Message>(m3);
      LinkedNode<Message> n2 = new LinkedNode<Message>(m2, n3);
      LinkedNode<Message> n1 = new LinkedNode<Message>(m1, n2);
      MessageStackIterator st = new MessageStackIterator(n1);

      // (1) Try to call next() on the iterator. The first call of next() must return the message at
      // the head of your chain of linked nodes.
      // (2) Try to call hasNext() on your iterator, it must return true.
      if (!st.hasNext())
        return false;
      if (!st.next().equals(n1.getData()))
        return false;

      // (3) The second call of next() must return the message which has been initially at index 1
      // of your chain of linked nodes.
      if (!st.hasNext())
        return false;
      if (!st.next().equals(n2.getData()))
        return false;

      // (4) The third call of next() on your iterator must return the message initially at index 2
      // of your chain of linked nodes.
      if (!st.hasNext())
        return false;
      if (!st.next().equals(n3.getData()))
        return false;

      // (4) If you defined a chain of 3 linked nodes in this scenario, hasNext() should return
      // false, and the fourth call of next() on the iterator must throw a NoSuchElementException.
      if (st.hasNext())
        return false;
      try {
        st.next();
        return false;
      } catch (NoSuchElementException e) {
        // expected behavior
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

}
