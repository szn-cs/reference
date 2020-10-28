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
import processing.core.PApplet;


/**
 * Manages capabilities common to all interactive objects in the game.
 * 
 * @author Safi
 */
public class InteractiveObject {
  private final String NAME; // the constant name identifying this interactive object
  private boolean isActive; // active means this interactive object is visible and can be interacted
                            // with

  // permitting access to PApple capabilities (like loadImage() and image())
  private static PApplet processing = null;

  public InteractiveObject(String name) {
  } // initializes the name of this object,
  // and sets isActive to true

  public boolean hasName(String name) {
  } // returns true only when contents of name equal NAME

  public boolean isActive() {
  } // returns true only when isActive is true

  public void activate() {
  } // changes isActive to true

  public void deactivate() {
  } // changes isActive to false

  public Action update() {
    return null;
  } // this method returns null
  // subclass types will override this update() method to do more interesting things

  public static void setProcessing(PApplet processing) {
  } // initializes processing field

  protected static PApplet getProcessing() {
  } // accessor method to retrieve this static field

}

