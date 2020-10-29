//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Treasure Hunt game (P06 assignment)
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

// import java.io.FileNotFoundException;
// import java.util.ArrayList;
// import java.util.Scanner;

/**
 * Spicfies a terget for this kind of interactive object to be dropped on along with an action that
 * is produced when this happens.
 * 
 * @author Safi
 */
public class DroppableObject extends DraggableObject {
  private VisibleObject target; // object over which this object can be dropped
  private Action action; // action that results from dropping this object over target

  /**
   * Constructor: initialize new droppable object
   * 
   * @param name   object identifier, corresponding also to file name of graphical file for this
   *               object.
   * @param x      coordinate on the graphical window
   * @param y      coordinate on the graphical window
   * @param target object dropped on
   * @param action performed when dropping on target
   */
  public DroppableObject(String name, int x, int y, VisibleObject target, Action action) {
    super(name, x, y); // initialize draggable object
    this.target = target;
    this.action = action;
  }

  /**
   * Performs action on successful drop and deactivates the involved objects -
   * 
   * @return action in response to a successful drop, otherwise null
   */
  @Override
  protected Action drop() {
    // this object is over its target & its target is active:
    if (this.isOver(target) && target.isActive()) {
      // deactivate both this object and the target object
      this.deactivate();
      target.deactivate();
      // return action to perform
      return this.action;
    }

    return null; // skip doing no action
  }
}


