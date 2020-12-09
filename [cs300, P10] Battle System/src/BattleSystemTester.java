//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Battle System (Assignment 10)
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

import java.util.NoSuchElementException;

/**
 * This class checks the correctness of the implementation of the methods defined in the MoveQueue &
 * BattleCharacter class.
 * 
 * @author Safi
 */
public class BattleSystemTester {

  /**
   * checks the correctness of the constructor operations implemented in the MoveQueue class
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testConstructor() {
    try {
      // constructor
      {
        // verify exception handling
        try {
          new MoveQueue(0);
          return false;
        } catch (IllegalArgumentException e) {
          // Expected behavior
        }
        try {
          new MoveQueue(-1);
          return false;
        } catch (IllegalArgumentException e) {
          // Expected behavior
        }

        // check empty queues
        MoveQueue q1 = new MoveQueue();
        MoveQueue q2 = new MoveQueue(2);
        if (!q1.toString().equals("[ ]") || !q2.toString().equals("[ ]"))
          return false;
        if (q1.size() != 0 || q2.size() != 0)
          return false;
        if (!q1.isEmpty() || !q2.isEmpty())
          return false;
      }
    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * checks the correctness of the compare and equals operations implemented in the BattleCharacter
   * class
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testBattleCharacter() {
    try {
      BattleCharacter.resetIDGenerator(); // reset static fields
      BattleCharacter c1 = new BattleCharacter("C1", new int[] {1, 1, 1, 1, 5});
      BattleCharacter c2 = new BattleCharacter("C2", new int[] {1, 1, 1, 1, 10});
      // reset static fields
      BattleCharacter.resetIDGenerator();
      BattleCharacter c3 = new BattleCharacter("C3", new int[] {1, 1, 1, 1, 20}); // same id
      BattleCharacter c4 = new BattleCharacter("C4", new int[] {1, 1, 1, 1, 5}); // equal speed

      // check equals functionality
      {
        if (c1.equals(c2))
          return false;
        if (c1.equals(null))
          return false;
        if (!c1.equals(c3))
          return false;
      }
      // check compare functionality
      {
        if (c1.compareTo(null) <= 0)
          return false;
        if (c1.compareTo(c2) >= 0)
          return false;
        if (c1.compareTo(c3) >= 0)
          return false;
        if (c1.compareTo(c4) <= 0 || c4.compareTo(c1) >= 0)
          return false;
        // check comparison to itself
        if (c1.compareTo(c1) != 0)
          return false;
      }
    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * checks the correctness of the enqueue & auxiliary operations implemented in the MoveQueue class
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testEnqueueMoveQueue() {
    try {
      BattleCharacter.resetIDGenerator(); // reset static fields

      BattleCharacter c1 = new BattleCharacter("C1", new int[] {1, 1, 1, 1, 1});
      BattleCharacter c2 = new BattleCharacter("C2", new int[] {1, 1, 1, 1, 2});
      BattleCharacter c3 = new BattleCharacter("C3", new int[] {1, 1, 1, 1, 3});
      BattleCharacter c4 = new BattleCharacter("C4", new int[] {1, 1, 1, 1, 4});
      BattleCharacter c5 = new BattleCharacter("C5", new int[] {1, 1, 1, 1, 5});
      BattleCharacter c6 = new BattleCharacter("C6", new int[] {1, 1, 1, 1, 6});
      BattleCharacter c7 = new BattleCharacter("C7", new int[] {1, 1, 1, 1, 7});

      // check exceptions
      {
        MoveQueue qLim = new MoveQueue(2); // limited capacity queue
        qLim.enqueue(c1);
        if (!qLim.toString().equals("[ C1(1, 1) | ]") || qLim.size() != 1 || qLim.isEmpty())
          return false;
        qLim.enqueue(c2);
        if (!qLim.toString().equals("[ C2(2, 2) | C1(1, 1) | ]") || qLim.size() != 2
            || qLim.isEmpty())
          return false;
        try {
          qLim.enqueue(c3);
          return false;
        } catch (IllegalStateException e) {
          // Expected behavior
        }
        try {
          qLim.enqueue(null);
          return false;
        } catch (IllegalArgumentException e) {
          // Expected behavior
        }
      }

      // verify order of nodes - complete binary tree
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c6);
        q.enqueue(c1);
        q.enqueue(c4);
        q.enqueue(c2);
        q.enqueue(c7);
        q.enqueue(c3);
        q.enqueue(c5);
        if (!q.toString().equals(
            "[ C7(7, 7) | C6(6, 6) | C5(5, 5) | C1(1, 1) | C2(2, 2) | C3(3, 3) | C4(4, 4) | ]")
            || q.size() != 7 || q.isEmpty())
          return false;
        if (q.peekBest() != c7 || q.size() != 7 || q.isEmpty())
          return false;
        q.clear();// check clear
        if (!q.toString().equals("[ ]") || q.size() != 0 || !q.isEmpty())
          return false;
      }
      // verify order non complete binary tree
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c5);
        q.enqueue(c1);
        q.enqueue(c6);
        q.enqueue(c3);
        if (!q.toString().equals("[ C6(6, 6) | C3(3, 3) | C5(5, 5) | C1(1, 1) | ]") || q.size() != 4
            || q.isEmpty())
          return false;
        q.clear();// check clear
        if (!q.toString().equals("[ ]") || q.size() != 0 || !q.isEmpty())
          return false;
      }
      // add equal nodes
      {
        // equal to c1
        BattleCharacter ce = new BattleCharacter("CE", new int[] {1, 1, 1, 1, 1});
        MoveQueue q = new MoveQueue(10);
        q.enqueue(ce);
        q.enqueue(c1);
        if (!q.toString().equals("[ C1(1, 1) | CE(8, 1) | ]") || q.size() != 2 || q.isEmpty())
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * checks the correctness of the dequeue & percolateDown operation implemented in the MoveQueue
   * class
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testDequeueMoveQueue() {
    try {
      BattleCharacter.resetIDGenerator(); // reset static fields
      BattleCharacter c1 = new BattleCharacter("C1", new int[] {1, 1, 1, 1, 1});
      BattleCharacter c2 = new BattleCharacter("C2", new int[] {1, 1, 1, 1, 2});
      BattleCharacter c3 = new BattleCharacter("C3", new int[] {1, 1, 1, 1, 3});
      BattleCharacter c4 = new BattleCharacter("C4", new int[] {1, 1, 1, 1, 4});
      BattleCharacter c5 = new BattleCharacter("C5", new int[] {1, 1, 1, 1, 5});
      BattleCharacter c6 = new BattleCharacter("C6", new int[] {1, 1, 1, 1, 6});
      BattleCharacter c7 = new BattleCharacter("C7", new int[] {1, 1, 1, 1, 7});

      // check exceptions
      {
        MoveQueue q = new MoveQueue(10);
        try {
          q.dequeue();
          return false;
        } catch (NoSuchElementException e) {
          // Expected behavior
        }
      }

      // queue with 1 node
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c1);
        if (q.dequeue() != c1)
          return false;
        if (!q.toString().equals("[ ]") || q.size() != 0 || !q.isEmpty())
          return false;
      }
      // queue with multiple nodes - percolate down to internal node
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c6);
        q.enqueue(c1);
        q.enqueue(c4);
        q.enqueue(c2);
        q.enqueue(c7);
        q.enqueue(c3);
        q.enqueue(c5);
        if (q.dequeue() != c7)
          return false;
        if (!q.toString()
            .equals("[ C6(6, 6) | C4(4, 4) | C5(5, 5) | C1(1, 1) | C2(2, 2) | C3(3, 3) | ]")
            || q.size() != 6 || q.isEmpty())
          return false;
      }
      // percolate down to leaf node and removal till empty
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c7);
        q.enqueue(c6);
        q.enqueue(c5);
        q.enqueue(c4);
        q.enqueue(c3);
        q.enqueue(c2);
        if (q.dequeue() != c7)
          return false;
        if (!q.toString().equals("[ C6(6, 6) | C4(4, 4) | C5(5, 5) | C2(2, 2) | C3(3, 3) | ]")
            || q.size() != 5 || q.isEmpty())
          return false;
        if (q.dequeue() != c6)
          return false;
        if (!q.toString().equals("[ C5(5, 5) | C4(4, 4) | C3(3, 3) | C2(2, 2) | ]") || q.size() != 4
            || q.isEmpty())
          return false;
        if (q.dequeue() != c5)
          return false;
        if (!q.toString().equals("[ C4(4, 4) | C2(2, 2) | C3(3, 3) | ]") || q.size() != 3
            || q.isEmpty())
          return false;
        if (q.dequeue() != c4)
          return false;
        if (!q.toString().equals("[ C3(3, 3) | C2(2, 2) | ]") || q.size() != 2 || q.isEmpty())
          return false;
        if (q.dequeue() != c3)
          return false;
        if (!q.toString().equals("[ C2(2, 2) | ]") || q.size() != 1 || q.isEmpty())
          return false;
        if (q.dequeue() != c2)
          return false;
        if (!q.toString().equals("[ ]") || q.size() != 0 || !q.isEmpty())
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * checks the correctness of the updateCharacter operation implemented in the MoveQueue class
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testUpdateCharacter() {
    try {
      BattleCharacter.resetIDGenerator(); // reset static fields
      BattleCharacter c1 = new BattleCharacter("C1", new int[] {1, 1, 1, 1, 1});
      BattleCharacter c2 = new BattleCharacter("C2", new int[] {1, 1, 1, 1, 2});
      BattleCharacter c3 = new BattleCharacter("C3", new int[] {1, 1, 1, 1, 3});
      BattleCharacter c4 = new BattleCharacter("C4", new int[] {1, 1, 1, 1, 4});
      BattleCharacter c5 = new BattleCharacter("C5", new int[] {1, 1, 1, 1, 5});
      BattleCharacter c6 = new BattleCharacter("C6", new int[] {1, 1, 1, 1, 6});
      BattleCharacter c7 = new BattleCharacter("C7", new int[] {1, 1, 1, 1, 7});

      // Update node
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c1);
        q.enqueue(c2);
        q.enqueue(c3);
        BattleCharacter.resetIDGenerator(); // reset static fields
        BattleCharacter c1update = new BattleCharacter("C1Update", new int[] {6, 6, 6, 6, 6});
        q.updateCharacter(c1update);
        if (!q.toString().equals("[ C1Update(1, 6) | C3(3, 3) | C2(2, 2) | ]") || q.size() != 3
            || q.isEmpty())
          return false;
      }
      // Update and remove died character
      {
        MoveQueue q = new MoveQueue(10);
        q.enqueue(c1);
        q.enqueue(c2);
        q.enqueue(c3);
        BattleCharacter.resetIDGenerator(); // reset static fields
        BattleCharacter c1update = new BattleCharacter("C1Update", new int[] {0, 6, 6, 6, 6});
        q.updateCharacter(c1update);
        if (!q.toString().equals("[ C3(3, 3) | C2(2, 2) | ]") || q.size() != 2 || q.isEmpty())
          return false;
      }
    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * Calls the test methods
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    boolean status = true;
    if (!testEnqueueMoveQueue()) {
      System.out.println("testEnqueueMoveQueue\n");
      status = false;
    }
    if (!testDequeueMoveQueue()) {
      System.out.println("testDequeueMoveQueue\n");
      status = false;
    }
    if (!testUpdateCharacter()) {
      System.out.println("testUpdateCharacter\n");
      status = false;
    }
    if (!testConstructor()) {
      System.out.println("testConstructor\n");
      status = false;
    }
    if (!testBattleCharacter()) {
      System.out.println("testBattleCharacter\n");
      status = false;
    }
    System.out.println("Test status: " + (status ? "passing" : "failing"));
  }
}
