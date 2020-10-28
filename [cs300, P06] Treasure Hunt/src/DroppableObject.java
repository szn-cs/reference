//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Treasure Hunst game (P06 assignment)
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
  private Action action; // action that results from dropping this object
  // over target
  // initialize new object

  public DroppableObject(String name, int x, int y, VisibleObject target, Action action) {
  }

  @Override
  protected Action drop() {
    // Recall that this drop() method that we are overloading is called whenever this objects is
    // done
    // being dragged. We just need to check whether this object is over it's target and whether it's
    // target is active at that time. When both of these conditions are true, this method should 1)
    // deactivate both this and the target objects, and 2) return this object's action.

    /*
     * To test this out, let's add a new VisibleObject to the gameObjects arraylist from the setup()
     * method (like our koala and key). The name of this new visible object will be "chest" and its
     * position can be (365,400). Then, change your key from a DraggableObject to a DroppableObject
     * with this chest as it's target. You'll also need an Action, one with the message
     * "Open sesame!" will be helpful. Make sure that dragging this key onto this chest produces the
     * expected output.
     */
  } // returns action and deactivates objects
  // in response to successful drop
  // When this object is over its target and its target is active:
  // deactivate both this object and the target object, and return action,
  // otherwise return null
}


