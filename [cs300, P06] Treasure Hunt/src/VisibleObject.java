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
import java.io.File;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Visible object with a graphical representation in the game.
 * 
 * @author Safi
 */
public class VisibleObject extends InteractiveObject {
  private PImage image; // the graphical representation of this object
  /* position of the top left side corner of the image */
  private int x; // the horizontal position (in pixels of this object's left side)
  private int y; // the vertical position (in pixels of this object's top side)

  /**
   * Construct VisibleObject instance, initializing its image and coordinates.
   * 
   * @param name object identifier, corresponding also to file name of graphical file to load for
   *             this object.
   * @param x    coordinate on the graphical window
   * @param y    coordinate on the graphical window
   */
  public VisibleObject(String name, int x, int y) {
    super(name); // call InteractiveObject constructor
    PApplet p = InteractiveObject.getProcessing(); // get processing instance
    // load image for this visible object
    image = p.loadImage("images" + File.separator + name + ".png");
    // set coordinates
    this.x = x;
    this.y = y;
  }

  /**
   * Draws image at its position
   * 
   * @return null - no action required to be returned by default
   */
  @Override
  public Action update() {
    PApplet p = InteractiveObject.getProcessing(); // get processing instance
    p.image(image, x, y); // draw object at its position on each frame
    return null;
  }

  /**
   * Moves coordinates by specific distances
   * 
   * @param dx distance to move x coordinate
   * @param dy distance to move y coordinate
   */
  public void move(int dx, int dy) {
    // changes x by adding dx to it (and y by dy)
    this.x += dx;
    this.y += dy;
  }

  /**
   * Check if an input point is over the object's image.
   * 
   * @param x input point coordinate
   * @param y input point coordinate
   * @return true only when point x,y is over image, otherwise false.
   */
  public boolean isOver(int x, int y) {
    // Determine whether input point is in the bound of the object's image dimensions, by using
    // PImage dimensions & position of the object
    int differenceX = x - this.x, differenceY = y - this.y; // differences from left & top
                                                            // coordinates respectively.
    // starting from left to positive right axis direction: mathematically [x, x+width]
    boolean overX = differenceX >= 0 && differenceX <= image.width;
    // starting from top to positive downward axis direction: mathematically [y, y+height]
    boolean overY = differenceY >= 0 && differenceY <= image.height;

    if (overX && overY) // both coordinates are over the object's image
      return true;
    return false;
  }

  /**
   * Check if two visible objects overlap.
   * 
   * @param other another visible object with own coordinates.
   * @return true only when the input object's image overlaps this one's.
   */
  public boolean isOver(VisibleObject other) {
    // validate passed object: short-circuit if null passed.
    if (other == null)
      return false;

    /*
     * if both y and x axes overlap between the objects
     */
    boolean xAxisOverlap = VisibleObject.isOverlapLine(this.x, this.x + this.image.width, other.x,
        other.x + other.image.width);
    boolean yAxisOverlap = VisibleObject.isOverlapLine(this.y, this.y + this.image.height, other.y,
        other.y + other.image.height);

    if (xAxisOverlap && yAxisOverlap)
      return true;

    return false;
  }

  /**
   * Check if two lines overlap whether in the x or y axis.
   * 
   * @param l1s start coordinate of line 1
   * @param l1e end cooridinate of line 1
   * @param l2s start coordinate of line 2
   * @param l2e end coordinate of line 2
   * @return true if lines overlap at least partially, otherwise false
   */
  private static boolean isOverlapLine(int l1s, int l1e, int l2s, int l2e) {
    if (l1s > l2e || l2s > l1e)
      return false;
    return true;
  }
}
