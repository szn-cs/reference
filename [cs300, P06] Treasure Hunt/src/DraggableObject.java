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
 * Allows the user to drag the object around the screen.
 * 
 * @author Safi
 */
public class DraggableObject extends VisibleObject {
  private boolean mouseWasPressed; // similar to use in ClickableObject
  private boolean isDragging; // true when this object is being dragged by the user
  private int oldMouseX; // horizontal position of mouse during last update
  private int oldMouseY; // vertical position of mouse during last update

  public DraggableObject(String name, int x, int y) {
  } // initializes new draggable object

  @Override
  public Action update() {
    // PApplet.mousePressed, PApplet.mouseX, and PApplet.mouseY
    // to detect when the user presses down the mouse button and the mouse is over this object.

    // action should begin the dragging of this object which will continue until the mouse button is
    // released.

    // While this object is being dragged, it should be moved by the same amount that the
    // mouse has moved between this and the previous call of the update() method (use oldMouseX
    // and oldMouseY to track this).
    //
    // Each time this dragging ends, the drop method should be called
    // as a result. Although this class' drop method doesn't do anything interesting (it simply
    // returns
    // null), sub classes will be able to override this method with more interesting behavior.

    // Make sure that any action object returned from such drop() calls are returned from this
    // class'
    // update() method. Test this out by adding a new DraggableObject to the gameObjects list in
    // TreasureHunt.setup(), similar to how we were testing the koala. Create a new draggable object
    // with the name "key" at position (70,70) to accomplish what is shown in Fig. 1. You should
    // then try dragging this object around the playground scene, while the program runs.
  } // calls VisibleObject update() first, then moves
  // according to mouse drag
  // each time isDragging changes from true to false, the drop() method below will be
  // called once and any action objects returned from that method should then be
  // returned from update()

  protected Action drop() {
    return null;
  } // this method returns null.
  // subclass types will override this drop() method to perform more interesting behavior
}
