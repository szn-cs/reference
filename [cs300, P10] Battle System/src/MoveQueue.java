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
 * Implements a priority queue using max-heap array of BattleCharacter objects, keeping track of the
 * maximum character.
 * 
 * @author Safi
 */
public class MoveQueue implements PriorityQueueADT<BattleCharacter> {
  // a max-heap array of BattleCharacters. The root (greatest battle character) is at index 0.
  private BattleCharacter[] data;
  // an int which keeps tack of the number of BattleCharacters stores in this MoveQueue
  private int size;

  /**
   * Constructor that builds an empty priority queue with array size or the given capacity.
   * 
   * @param capacity length of array used to implement the heap
   * @throws IllegalArgumentException if capacity is zero or negative.
   */
  public MoveQueue(int capacity) {
    if (capacity <= 0)
      throw new IllegalArgumentException("Invalid argument capacity");
    data = new BattleCharacter[capacity];
    size = 0;
  }

  /**
   * Constructor that builds an empty priority queue with array size 10.
   */
  public MoveQueue() {
    this(10);
  }

  /**
   * Returns a String representation of the current contents of the MoveQueue in order from first to
   * last.
   * 
   * @return string representing the queue
   * @author Michelle
   */
  @Override
  public String toString() {
    String s = ("[ ");
    for (int i = 0; i < size; i++) {
      s += (data[i].toString() + " | ");
    }
    s += ("]");
    return s;
  }

  /**
   * Checks if this MoveQueue priority queue is empty.
   * 
   * @return true if it is empty and false otherwise.
   */
  @Override
  public boolean isEmpty() {
    if (size() == 0)
      return true;
    else
      return false;
  }

  /**
   * Get current size of this priority queue.
   * 
   * @return the size of this priority queue
   */
  @Override
  public int size() {
    return this.size;
  }

  /**
   * Removes all the elements from this priority queue.
   */
  @Override
  public void clear() {
    // Note: clearing the elements isn't necessary, just as a safety measure.
    for (int i = 0; i < data.length; i++)
      data[i] = null;
    this.size = 0;
  }

  /**
   * Adds the given element to the priority queue in the correct position based on the natural
   * ordering of the elements.
   * 
   * @param element to be added to this queue
   * @throws IllegalArgumentException if element is null
   * @throws IllegalStateException    when priority queue is full
   */
  @Override
  public void enqueue(BattleCharacter element) {
    if (element == null)
      throw new IllegalArgumentException("Invalid element argument");
    if (size() >= data.length)
      throw new IllegalStateException("Error: no space left in priority queue");

    int nextIndex = size(); // next free position
    data[nextIndex] = element; // insert to the binary tree from left to right
    size++;
    percolateUp(nextIndex); // perculate up the inserted element
  }

  /**
   * Recursively propagates max-heap order violations up. Checks to see if the current node i
   * violates the max-heap order property by checking its parent. If it does, swap them and continue
   * to ensure the heap condition is satisfied.
   * 
   * @param i index of the current node in this heap
   */
  protected void percolateUp(int i) {
    int p = (i - 1) / 2; // parent index
    while (i != 0 && data[i].compareTo(data[p]) > 0) {
      // swap elements
      BattleCharacter temp = data[p];
      data[p] = data[i];
      data[i] = temp;
      // update indecies
      i = p;
      p = (i - 1) / 2;
    }
  }

  /**
   * Returns and removes the element at the front (aka root position) of priority queue (the element
   * having the highest priority).
   * 
   * @return the removed element
   * @throws NoSuchElementException if this queue is empty
   */
  @Override
  public BattleCharacter dequeue() {
    if (isEmpty())
      throw new NoSuchElementException("Error: Queue is empty");
    int i = 0; // index of element at front
    BattleCharacter c = data[i]; // front character
    if (size() > 1)
      // swap with last node
      data[i] = data[size() - 1];
    else
      data[i] = null; // clear root
    size--;
    percolateDown(i); // percolate down
    return c;
  }

  /**
   * Returns without removing the element at the front (aka root position) of this queue (the
   * element having the highest priority).
   * 
   * @return the element with the highest priority in this queue
   * @throws NoSuchElementException if this queue is empty
   */
  @Override
  public BattleCharacter peekBest() {
    if (isEmpty())
      throw new NoSuchElementException("Queue is empty");
    return data[0];
  }

  /**
   * Recursively propagates max-heap order violations down. Checks to see if the current node i
   * violates the max-heap order property by checking its children. If it does, swap it with the
   * optimal child and continue to ensure the heap condition is met.
   * 
   * @param i index of the current node in this heap
   */
  protected void percolateDown(int i) {
    int cl = 2 * i + 1; // left child index
    int cr = 2 * i + 2; // right child index

    // if there is child/children
    while (cl < size()) {
      int g; // greatest child index
      if (cr < size())
        g = data[cr].compareTo(data[cl]) > 0 ? cr : cl;
      else
        g = cl;

      if (data[i].compareTo(data[g]) < 0) {
        // swap nodes
        BattleCharacter temp = data[i];
        data[i] = data[g];
        data[g] = temp;
        // update current node
        i = g;
        cl = 2 * i + 1; // left child index
        cr = 2 * i + 2; // right child index
      } else
        break; // current node is in right position
    }
  }

  /**
   * Eliminates all heap order violations from the heap data array
   */
  protected void heapify() {
    int i = (size() / 2) - 1; // node index, initialized with the largest index internal node
    while (i >= 0) {
      percolateDown(i);
      i--;
    }
  }

  /**
   * Updates a character in the heap array
   * 
   * @param updateChara the new object to update the character with
   */
  public void updateCharacter(BattleCharacter updateChara) {
    if (updateChara == null)
      return;

    // (1) Find matching character in the MoveQueue (Rely on the BattleCharacter.equals() method to
    // find the match with updated).
    int foundIndex = -1;
    for (int i = 0; i < size(); i++)
      if (data[i].equals(updateChara)) {
        foundIndex = i;
        break;
      }

    if (foundIndex == -1)
      return; // not found

    // (2) Replace it with the updated version of the character. If character is dead, remove it
    // entirely.
    data[foundIndex] = updateChara;

    if (!data[foundIndex].isAlive()) {
      data[foundIndex] = null;
      // (3) Make sure the structure is maintained. Note: You can also use the code below to
      // eliminate holes (null references) in the array.
      data[foundIndex] = data[size - 1]; // gapIndex is the index of the dead character
      data[size - 1] = null;
      size--;
    }

    // Then, call heapify() method to eliminate all the ordering violations.
    heapify();
  }
}
