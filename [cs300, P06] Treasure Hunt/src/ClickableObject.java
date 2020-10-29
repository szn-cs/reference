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
import processing.core.PApplet;


/**
 * Intractable objects by clicking
 * 
 * @author Safi
 */
public class ClickableObject extends VisibleObject {
  private Action action; // action returned from update when this object is clicked
  private boolean mouseWasPressed; // tracks whether the mouse was pressed during the last update()

  /**
   * Constructor: initializes this new object
   * 
   * @param name   object identifier, corresponding also to file name of graphical file for this
   *               object.
   * @param x      coordinate on the graphical window
   * @param y      coordinate on the graphical window
   * @param action action to perform on clicking the object
   */
  public ClickableObject(String name, int x, int y, Action action) {
    super(name, x, y); // initialize VisibleObject
    this.action = action;
    mouseWasPressed = false;
  }

  /**
   * Update implementation: updates the state of the clickable object
   * 
   * @return returns action only when mouse is first clicked on this object
   */
  @Override
  public Action update() {
    super.update(); // minimum action on each frame (draw object)
    PApplet p = InteractiveObject.getProcessing(); // get processing instance

    // Determine whether mouse button is currently pressed down using PApplet.mousePressed field
    if (!p.mousePressed) {
      mouseWasPressed = false; // unset flag
      return null;
    }

    /*
     * skip subsequent mouse presses (was not pressed on the previous update i.e. proceed only on
     * first pressed event). OR mouse is not over this clickable object
     */
    if (mouseWasPressed || !this.isOver(p.mouseX, p.mouseY))
      return null;

    this.mouseWasPressed = true; // set prev pressed flag
    return this.action; // return action to perform.
  }
}
