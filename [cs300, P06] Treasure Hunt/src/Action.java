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
import java.util.ArrayList;

/**
 * Handles responses to an object being clicked or dragged onto another, as well as printing out
 * messages.
 * 
 * @author Safi
 */
public class Action {
  private String message = null; // message printed by this action (or null to do nothing)
  // any interactive object can be activated or deactivated.
  private InteractiveObject object;

  /**
   * Construct action only with message description.
   * 
   * @param message string describing what the action does.
   */
  public Action(String message) {
    this.message = message;
  }

  /**
   * Construct action only with an Interactive object to activate
   * 
   * @param object interactive object to activate
   */
  Action(InteractiveObject object) {
    this.object = object;
  }

  /**
   * Construct action with both interactive action and string message
   * 
   * @param message action description
   * @param object  interactive object to activate
   */
  Action(String message, InteractiveObject object) {
    this.message = message;
    this.object = object;
  }

  /**
   * Performs action by activating the current object and printing its message.
   * 
   * @param list of interactive objects in the game
   */
  public void act(ArrayList<InteractiveObject> list) {
    // print message if any
    if (this.message != null)
      System.out.println(this.message);

    // validate object field & list: must have an InteractiveObject field to do an action.
    if (this.object == null || list == null)
      return;

    this.object.activate(); // activate instance's interactive object.
    list.add(this.object); // add object to the input list of game interactive objects.
    this.object = null; // each interactive object is only ever activated once
  }
}

