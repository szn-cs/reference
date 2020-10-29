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
 * Manages capabilities common to all interactive objects in the game.
 * 
 * @author Safi
 */
public class InteractiveObject {
  // PApple instance - permitting access to PApple capabilities
  private static PApplet processing = null;

  private final String NAME; // the constant name identifying this interactive object
  private boolean isActive; // active means this interactive object is visible and can be interacted

  /**
   * Constructor: initialize object's identifier and activation state
   * 
   * @param name identifier of the object
   */
  public InteractiveObject(String name) {
    this.NAME = name; // initializes the name of this object
    this.activate(); // set isActive to true;
  }

  /**
   * Checks if object's identifier is the same as the input string name
   * 
   * @param name string to compare the identifier to.
   * @return true if identifier contents equal to input name, otherwise false.
   */
  public boolean hasName(String name) {
    if (name != null && NAME.equals(name))
      return true;
    return false;
  }

  /**
   * Checks if current object is active/intractable
   * 
   * @return true only when object is active, otherwise false.
   */
  public boolean isActive() {
    if (isActive == true)
      return true;
    return false;
  }

  /**
   * Activates current object
   */
  public void activate() {
    isActive = true;
  }

  /**
   * Deactivates current object
   */
  public void deactivate() {
    isActive = false;
  }

  /**
   * Updates the object state: This method is default implementation, where subclass types will
   * override and implement interaction logic with this object.
   * 
   * @return appropriate action involved in the object interaction. Returns null as default
   *         implementation.
   */
  public Action update() {
    return null;
  }

  /**
   * Initializes processing field with the program's PApplet library instance.
   * 
   * @param processing PApplet library instance managing the graphical window
   */
  public static void setProcessing(PApplet processing) {
    if (processing != null)
      InteractiveObject.processing = processing;
  }

  /**
   * Retrieves PApplet library instance of the program
   * 
   * @return processing field containing the PApplet instance
   */
  protected static PApplet getProcessing() {
    return InteractiveObject.processing;
  }

}

