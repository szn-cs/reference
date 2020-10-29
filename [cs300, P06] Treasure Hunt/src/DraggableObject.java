import processing.core.PApplet;

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
 * Allows the user to drag the object around the screen.
 * 
 * @author Safi
 */
public class DraggableObject extends VisibleObject {
  private boolean mouseWasPressed; // tracks whether the mouse was pressed during the last update()
  private boolean isDragging; // true when this object is being dragged by the user
  private int oldMouseX; // horizontal position of mouse during last update
  private int oldMouseY; // vertical position of mouse during last update

  /**
   * Constructor: initializes new draggable object
   * 
   * @param name object identifier, corresponding also to file name of graphical file for this
   *             object.
   * @param x    coordinate on the graphical window
   * @param y    coordinate on the graphical window
   */
  public DraggableObject(String name, int x, int y) {
    super(name, x, y); // initialize VisibleObject
    isDragging = false;
    oldMouseX = 0;
    oldMouseY = 0;
  }

  /**
   * Update implementation: updates the state of the draggable object by drawing the image, and
   * moves according to mouse drag each time isDragging changes from true to false, the drop()
   * method below will be called once
   * 
   * @return returns action only when mouse is first clicked on this object
   */
  @Override
  public Action update() {
    super.update(); // minimum action on each frame (draw object)
    PApplet p = InteractiveObject.getProcessing(); // get processing instance

    if (!p.mousePressed) {
      if (mouseWasPressed) { // not pressed, after prev was pressed
        /* drop and reset parameters */
        isDragging = false;
        mouseWasPressed = false;
        return drop(); // drop item and get corresponding action to perform when dropped
      } else {
        return null; // in case not pressed & neither prev
      }
    }

    /*
     * do last action on subsequent presses - should drag this object until the mouse button is
     * released
     */
    if (mouseWasPressed) { // is mouse pressed & was prev pressed
      int dx = p.mouseX - oldMouseX, dy = p.mouseY - oldMouseY; // distance to move each coordinate
      // While this object is being dragged, it should be moved by the same amount that the
      // mouse has moved between this and the previous call of the update() method
      this.move(dx, dy); // move object coordinates
      // reset mouse location parameters
      oldMouseX = p.mouseX;
      oldMouseY = p.mouseY;
    } else if (this.isOver(p.mouseX, p.mouseY)) { // mouse pressed first time on object
      /* set parameters and coordinates */
      isDragging = true;
      mouseWasPressed = true;
      // mouse location parameters
      oldMouseX = p.mouseX;
      oldMouseY = p.mouseY;
    }

    return null;
  }

  /**
   * Default implementation of drop method - subclass types will override this drop() method to
   * perform more interesting behavior.
   * 
   * @return the action should be performed after dragging ends. returns null for default
   *         implementation.
   */
  protected Action drop() {
    return null;
  }
}
