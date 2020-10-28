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
import processing.core.PImage;

/**
 * Visible object with a graphical representation in the game.
 * 
 * @author Safi
 */
public class VisibleObject extends InteractiveObject {
  private PImage image; // the graphical representation of this object
  private int x; // the horizontal position (in pixels of this object's left side)
  private int y; // the vertical position (in pixels of this object's top side)

  public VisibleObject(String name, int x, int y) {
  } // initialize this new VisibleObject
  // the image for this visible object should be loaded from :
  // "images"+File.separator+ name +".png"

  @Override
  public Action update() {
  } // draws image at its position before returning null

  public void move(int dx, int dy) {
  } // changes x by adding dx to it (and y by dy)

  public boolean isOver(int x, int y) {
    // make use of the width and height elds of the PImage class.
    // the position (x,y) of the interactive object is the position of the top left side corner of
    // its image
  } // return true only when point x,y is over image

  public boolean isOver(VisibleObject other) {
  } // return true only when other's image
  // overlaps this one's

}
