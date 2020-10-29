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
// Online Sources:
// - iterating through list while removing elements from it:
// https://stackoverflow.com/questions/1196586/calling-remove-in-foreach-loop-in-java
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * NOTE- Methods/Fields supported on Gradescope tester:
 * 
 * PApplet: PImage loadImage(String), void image(PImage,int,int), void size(int,int), void
 * getSurface().setTitle(String), and void main(String). int mouseX, int mouseY, and boolean
 * mousePressed.
 * 
 * PImage: int width, and int height
 */
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Treasure Hunst style adventure puzzle game: Involves graphical elements that the user can click
 * on, and can drag onto others to find clues, solve puzzles and eventually win the game. The user
 * must find a coin and place/drop it onto a target location shown in the map to win.
 * 
 * @author Safi
 */
public class TreasureHunt extends PApplet {
  private PImage backgroundImage; // loaded background image instance
  private ArrayList<InteractiveObject> gameObjects; // list of objects in game

  /**
   * Main method to launch the graphic application
   *
   * @param args input arguments
   */
  public static void main(String[] args) {
    // IMPORTANT: do not add any other statement to your TreasureHunt.main()
    PApplet.main("TreasureHunt");
  }

  /**
   * Sets the size of this application's display window
   */
  @Override
  public void settings() {
    size(800, 600);
  }

  /**
   * Defines initial environment properties, loads background images and fonts, loads the clues, and
   * initializes the instance fields, as the program starts.
   */
  @Override
  public void setup() {
    // Displays the title of the display window
    this.getSurface().setTitle("Treasure Hunt");
    // initialize processing object
    InteractiveObject.setProcessing(this);
    // initialize game objects
    gameObjects = new ArrayList<InteractiveObject>();
    // load the background image filename, introductory text messages, and interactive objects or
    // clues descriptions from a text file.
    loadGameSettings("clues" + File.separator + "treasureHunt.clues");

    // Manual test use cases:
    /*
     * Test case - Action act method: Now, test this out by creating a new visible object with the
     * name "phone" within your TreasureHunt.setup() method, and its position can be (700,490).
     * Instead of adding this visible object to the gameObjects array list, deactivate it before
     * creating an action that will activate it when the key is dragged over the chest. Then, make
     * sure that this visible object (the phone) appears when this happens.
     */
    /*
     * Test case - Test this out by adding a new DraggableObject to the gameObjects list in
     * TreasureHunt.setup(), similar to how we were testing the koala. Create a new draggable object
     * with the name "key" at position (70,70) to accomplish what is shown in Fig. 1. You should
     * then try dragging this object around the playground scene, while the program runs.
     */
    /*
     * Test case - To test this out, let's add a new VisibleObject to the gameObjects arraylist from
     * the setup() method (like our koala and key). The name of this new visible object will be
     * "chest" and its position can be (365,400). Then, change your key from a DraggableObject to a
     * DroppableObject with this chest as it's target. You'll also need an Action, one with the
     * message "Open sesame!" will be helpful. Make sure that dragging this key onto this chest
     * produces the expected output.
     */
  }

  /**
   * Updates the treasure hunt game display window
   */
  @Override
  public void draw() {
    // draw background image
    this.image(this.backgroundImage, 0, 0);

    /*
     * Iterative over list, removing elements from it. Update each interactive object on each frame
     * and perform their actions if any
     */
    int i = 0;
    while (i < gameObjects.size()) {
      if (gameObjects.get(i) == null) { // remove any nulls and skip
        gameObjects.remove(i);
        continue;
      }

      // update object, retrieving action if any
      Action action = gameObjects.get(i).update();
      // invoke action on object if any
      if (action != null)
        action.act(gameObjects);
      // remove deactivated objects from the game (e.g. key & chest disappear after key is dragged
      // onto the chest)
      if (!gameObjects.get(i).isActive()) {
        gameObjects.remove(i);
        continue;
      }

      i++; // increment regularly only when the element is preserved in the list
    }
  }


  /**
   * This method loads a background image, prints out some introductory text, and then reads in a
   * set of interactive objects descriptions from a text file with the provided name. These
   * represent the different clues for our treasure hunt adventure game. The image is stored in
   * this.backgroundImage, and the activated interactive objects are added to the this.gameObjects
   * list.
   * 
   * @param filename - relative path of file to load, relative to current working directory
   */
  private void loadGameSettings(String filename) {
    // start reading file contents
    Scanner fin = null;
    int lineNumber = 1; // report first line in file as lineNumber 1
    try {
      fin = new Scanner(new File(filename));

      // read and store background image
      String backgroundImageFilename = fin.nextLine().trim();
      backgroundImageFilename = "images" + File.separator + backgroundImageFilename + ".png";
      backgroundImage = loadImage(backgroundImageFilename);
      lineNumber++;

      // read and print out introductory text
      String introductoryText = fin.nextLine().trim();
      System.out.println(introductoryText);
      lineNumber++;

      // then read and create new objects, one line per interactive object
      while (fin.hasNextLine()) {
        String line = fin.nextLine().trim();
        if (line.length() < 1)
          continue;

        // fields are delimited by colons within a given line
        String[] parts = line.split(":");
        InteractiveObject newObject = null;

        // first letter in line determines the type of the interactive object to create
        if (Character.toUpperCase(line.charAt(0)) == 'C')
          newObject = loadNewClickableObject(parts);
        else if (Character.toUpperCase(line.charAt(0)) == 'D')
          newObject = loadNewDroppableObject(parts);

        // even deactivated object references are being added to the gameObjects arraylist,
        // so they can be found.
        // these deactivated object references will be removed, when draw() is first called
        gameObjects.add(newObject);
        if (Character.isLowerCase(line.charAt(0))) // lower case denotes non-active object
          newObject.deactivate();
        lineNumber++;
      }

      // catch and report warnings related to any problems experienced loading this file
    } catch (FileNotFoundException e) {
      System.out.println("WARNING: Unable to find or load file: " + filename);
    } catch (RuntimeException e) {
      System.out.println("WARNING: Problem loading file: " + filename + " line: " + lineNumber);
      e.printStackTrace();
    } finally {
      if (fin != null)
        fin.close();
    }
  }

  /**
   * Helper method to retrieve interactive objects' references from the gameObjects list, based on
   * their names. If multiple objects have that name, this method will return the first
   * (lowest-index) reference found.
   * 
   * @param name is the name of the object that is being found
   * @return a reference to an interactive object with the specified name, or null when none is
   *         found
   */
  private InteractiveObject findObjectByName(String name) {
    for (int i = 0; i < gameObjects.size(); i++)
      if (gameObjects.get(i).hasName(name)) {
        return gameObjects.get(i);
      }
    System.out.println("WARNING: Failed to find an interactive object with name: " + name);
    return null;
  }

  /**
   * This method creates and returns a new ClickableObject based on the properties specified as
   * strings within the provided parts array.
   * 
   * @param parts contains the following strings in this order: - C: indicates that a
   *              ClickableObject is being created - name: the name of the newly created interactive
   *              object - x: the starting x position (as an int) for this object - y: the starting
   *              y position (as an int) for this object - message: a string of text to display when
   *              this object is clicked - name of the object to activate (optional): activates this
   *              object when clicked
   * @return the newly created object
   */
  private ClickableObject loadNewClickableObject(String[] parts) {
    // C: name: x: y: message: name of object to activate (optional)

    // parse parts
    String name = parts[1].trim();
    int x = Integer.parseInt(parts[2].trim());
    int y = Integer.parseInt(parts[3].trim());
    String message = parts[4].trim();
    InteractiveObject activate = null;
    if (parts.length > 5)
      activate = findObjectByName(parts[5].trim());
    // create new clickable object
    ClickableObject newObject = new ClickableObject(name, x, y, new Action(message, activate));
    return newObject;
  }

  /**
   * This method creates and returns a new DroppableObject based on the properties specified as
   * strings within the provided parts array.
   * 
   * @param parts contains the following strings in this order: - D: indicates that a
   *              DroppableObject is being created - name: the name of the newly created droppable
   *              object - x: the starting x position (as an int) for this object - y: the starting
   *              y position (as an int) for this object - message: a string of text to display when
   *              this object is dropped on target - name of the object to activate (optional):
   *              activates this object when dropped on target
   * 
   * @return the newly created droppable object
   */
  private DroppableObject loadNewDroppableObject(String[] parts) {
    // D: name: x: y: target: message: name of object to activate (optional)

    // parse parts
    String name = parts[1].trim();
    int x = Integer.parseInt(parts[2].trim());
    int y = Integer.parseInt(parts[3].trim());
    InteractiveObject dropTarget = findObjectByName(parts[4].trim());
    if (!(dropTarget instanceof VisibleObject))
      dropTarget = null;
    String message = parts[5].trim();
    InteractiveObject activate = null;
    if (parts.length > 6)
      activate = findObjectByName(parts[6].trim());
    // create new droppable object
    DroppableObject newObject =
        new DroppableObject(name, x, y, (VisibleObject) dropTarget, new Action(message, activate));
    return newObject;
  }

}
