//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Inbox.java
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
 * Manages and stores all received/loaded unread and read messages
 * 
 * @author Safi
 */
public class Inbox {
  private MessageStack readMessageBox; // stack which stores read messages
  private MessageStack unreadMessageBox; // stack which stores unread messages

  public Inbox() {
    // This no-argument constructor creates a new empty inbox and initializes its instance fields.
    // Both unreadMessageBox and readMessageBox stacks of this inbox must be initially empty.

  }

  public String readMessage() {
    // Reads the message at the top of the unreadMessageBox.
    // Once read, the message must be moved from the unreadMessageBox
    // to the readMessageBox.
    // This method returns the string representation of the message at
    // the top of the unreadMessageBox, or

    // "Nothing in Unread" if the unreadMessageBox of this inbox is empty.
    if (empty)
      return "Nothing in Unread";

    // push and peek: have to call those both methods to build the content of the stack
    // appropriately, and then double check that the element at the top of the stack has been
    // correctly removed and returned when .pop() method is called.

  }

  public String peekReadMessage() {
    // Returns the string representation of the message at the top of the readMessageBox.
    // This method returns the string representation of the message at the top
    // readMessageBox and "Nothing in Read" if the readMessageBox is empty.


  }

  public int markAllMessagesAsRead() {
    // Marks all messages in the unread message box as read.
    // The unread message box must be empty after this method returns.
    // Every message marked read must be moved to the read messages box.
    // This method returns the total number of messages marked as read.
  }

  public void receiveMessage(Message newMessage) {
    // Pushes a newMessage into the unread message box
    // newMessage represents a reference to the received message
    // Note that this method can be invoked each time a new message
    // will be received and pushed to the unreadMessageBox.


  }

  public int emptyReadMessageBox() {
    // Removes permanently all the messages from the readMessageBox
    // This method returns the total number of the removed messages
  }

  public String getStatistics() {
    // Gets the statistics of this inbox
    // Returns a String formatted as follows:
    // "Unread (size1)" + "\n" + "Read (size2)",
    // where size1 and size2 represent the number of unread and read
    // messages respectively.
  }

  public String traverseUnreadMessages() {
    // Traverses all the unread messages and return a list of their
    // ID + " " + SUBJECT, as a string. Every string representation of a
    // message is provided in a new line.
    // This method returns a String representation of the contents of
    // the unread message box.
    // The returned output has the following format:
    // Unread(unreadMessageBox_size)\n + list of the messages in
    // unreadMessageBox (ID + " " + SUBJECT) each in a line.
  }

  public String traverseReadMessages() {
    // Traverses all the read messages and return a list of their string
    // representations, ID + " " + SUBJECT, each per new line, as a string
    // This method returns a String representation of the contents of
    // the read message box
    // The returned output has the following format:
    // Read(readMessageBox_size)\n + list of the messages in
    // readMessageBox (ID + " " + SUBJECT) each in a line.
  }

}
