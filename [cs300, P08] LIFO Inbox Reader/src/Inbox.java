//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Inbox.java (Assignment P08)
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

/**
 * Manages and stores all received/loaded unread and read messages
 * 
 * @author Safi
 */
public class Inbox {
  private MessageStack readMessageBox; // stack which stores read messages
  private MessageStack unreadMessageBox; // stack which stores unread messages

  /**
   * No-argument constructor creates a new empty inbox and initializes its instance fields.
   */
  public Inbox() {
    // Both unreadMessageBox and readMessageBox stacks of this inbox must be initially empty.
    readMessageBox = new MessageStack();
    unreadMessageBox = new MessageStack();
  }

  /**
   * Retrieve the most recent message from the unread list
   * 
   * @return the string representation of the message at the top of the unreadMessageBox, or null if
   *         empty
   */
  public String readMessage() {
    // Reads the message at the top of the unreadMessageBox.
    try {
      Message m = unreadMessageBox.peek();
      // move from the unreadMessageBox to the readMessageBox.
      readMessageBox.push(unreadMessageBox.pop());
      return m.toString();
    } catch (EmptyStackException e) {
      // the unreadMessageBox of this inbox is empty.
      return "Nothing in Unread";
    }
  }

  /**
   * Retrieves message string at the top readMessageBox
   * 
   * @return the string representation of the message at the top of the readMessageBox, and "Nothing
   *         in Read" if the readMessageBox is empty.
   */
  public String peekReadMessage() {
    try {
      Message m = readMessageBox.peek();
      return m.toString();
    } catch (EmptyStackException e) {
      // the unreadMessageBox of this inbox is empty.
      return "Nothing in Read";
    }

  }

  /**
   * Marks all messages in the unread message box as read
   * 
   * @return the total number of messages marked as read
   */
  public int markAllMessagesAsRead() {
    int total = unreadMessageBox.size(); // original number of unread messages
    // The unread message box must be empty after this method returns and Every message marked read
    // must be moved to the read messages box.
    while (!unreadMessageBox.isEmpty())
      readMessage();
    return total;
  }

  /**
   * Pushes a newMessage into the unread message box
   * 
   * @param newMessage a reference to the received message
   */
  public void receiveMessage(Message newMessage) {
    if (newMessage == null)
      return; // ignore null inputs
    unreadMessageBox.push(newMessage);
  }

  /**
   * Removes permanently all the messages from the readMessageBox
   * 
   * @return total number of the removed messages
   */
  public int emptyReadMessageBox() {
    if (readMessageBox.isEmpty())
      return 0;
    int total = readMessageBox.size(); // original number of messages in read list
    readMessageBox = new MessageStack(); // just create a new empty stack and let garbage collector
                                         // deal with the existing dangling reference
    return total;
  }

  /**
   * Gets the statistics of this inbox
   * 
   * @return a String formatted as follows: "Unread (size1)" + "\n" + "Read (size2)", where size1
   *         and size2 represent the number of unread and read messages respectively.
   */
  public String getStatistics() {
    return "Unread (" + unreadMessageBox.size() + ")" + "\n" + "Read (" + readMessageBox.size()
        + ")";
  }

  /**
   * Traverses all the unread messages and return a list of their ID + " " + SUBJECT, as a string.
   * 
   * @return String representation of the contents of the unread message box. The returned output
   *         has the following format: Unread(unreadMessageBox_size)\n + list of the messages in
   *         unreadMessageBox (ID + " " + SUBJECT) each in a line.
   */
  public String traverseUnreadMessages() {
    String s = "Unread(" + unreadMessageBox.size() + ")\n";
    for (Message m : unreadMessageBox)
      s += m.getID() + " " + m.getSUBJECT() + "\n";
    return s;
  }

  /**
   * Traverses all the read messages and return a list of their string representations, ID + " " +
   * SUBJECT, each per new line, as a string
   * 
   * @return a String representation of the contents of the read message box. The returned output
   *         has the following format: Read(readMessageBox_size)\n + list of the messages in
   *         readMessageBox (ID + " " + SUBJECT) each in a line.
   */
  public String traverseReadMessages() {
    String s = "Read(" + readMessageBox.size() + ")\n";
    for (Message m : readMessageBox)
      s += m.getID() + " " + m.getSUBJECT() + "\n";
    return s;
  }

}
