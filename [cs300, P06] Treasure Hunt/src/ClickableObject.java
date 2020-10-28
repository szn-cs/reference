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
import java.io.File;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Intractable objects by clicking
 * 
 * @author Safi
 */
public class ClickableObject extends VisibleObject {
  private Action action; // action returned from update when this object is clicked
  private boolean mouseWasPressed; // tracks whether the mouse was pressed
  // during the last update()
  // initializes this new object

  public ClickableObject(String name, int x, int y, Action action) {
  }

  @Override
  public Action update() {
    // determine whether mouse button is currentlypressed down 
    this.mousePressed; // PApplet.mousePressed field.
    
    
    
//    In order to return our action only when the mouse is rst pressed on
//        this clickable object (not repeatedly for as long as the button is held down), we'll make sure
//        that the mouse is currently pressed, is over this clickable object, and was not pressed on the
//        previous update.
    
    
//    The position of the mouse can be accessed through PApplet.mouseX and
//    PApplet.mouseY for this purpose.
    
  } // calls VisibleObject update, then returns
  // action only when mouse is first clicked
  // on this object
}
