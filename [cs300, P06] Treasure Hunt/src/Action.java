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

  public Action(String message) {
  } // create and initialize this new action


  // new Actions can be constructed with either a String message, an Interactive object to activate,
  // or both.
  // Action(InteractiveObject object)
  // Action(String message, InteractiveObject object)

  public void act(ArrayList<InteractiveObject> list) {
    /*
     * When the act() method is called on an action that has a non-null InteractiveObject eld, the
     * following three things must happen:
     * 
     * 1. The activate() method of that interactive object should be called,
     * 
     * 2. That object should be added to the array list passed in as an input parameter,
     * 
     * 3. This action's object instance eld should be changed to null (so that each interactive
     * object is only ever activated once).
     * 
     * 
     * Notice that this change temporarily breaks our TreasureHunt's draw() method. So, we'll need
     * to modify our call of act() there to pass its gameObjects array list as an input argument.
     * Now, test this out by creating a new visible object with the name "phone" within your
     * TreasureHunt.setup() method, and its position can be (700,490). Instead of adding this
     * visible object to the gameObjects array list, deactivate it before creating an action that
     * will activate it when the key is dragged over the chest. Then, make sure that this visible
     * object (the phone) appears when this happens.
     */
  } // when message is not null, message is printed to System.out
}

