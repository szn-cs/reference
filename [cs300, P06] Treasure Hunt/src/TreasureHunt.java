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


// References to use only of these api methods:
// - two fields within PImage: int width, and int height.
// - three fields within PApplet: int mouseX, int mouseY, and boolean mousePressed.
// - five methods within PApplet: PImage loadImage(String), void image(PImage,int,int),
// void size(int,int), void getSurface().setTitle(String), and void main(String).

/**
 * Spicfies a terget for this kind of interactive object to be dropped on along with an action that
 * is produced when this happens.
 * 
 * @author Safi
 */
class DroppableObject extends DraggableObject {
  private VisibleObject target; // object over which this object can be dropped
  private Action action; // action that results from dropping this object
  // over target
  // initialize new object

  public DroppableObject(String name, int x, int y, VisibleObject target, Action action) {
  }

  @Override
  protected Action drop() {
    // Recall that this drop() method that we are overloading is called whenever this objects is
    // done
    // being dragged. We just need to check whether this object is over it's target and whether it's
    // target is active at that time. When both of these conditions are true, this method should 1)
    // deactivate both this and the target objects, and 2) return this object's action.

    /*
     * To test this out, let's add a new VisibleObject to the gameObjects arraylist from the setup()
     * method (like our koala and key). The name of this new visible object will be "chest" and its
     * position can be (365,400). Then, change your key from a DraggableObject to a DroppableObject
     * with this chest as it's target. You'll also need an Action, one with the message
     * "Open sesame!" will be helpful. Make sure that dragging this key onto this chest produces the
     * expected output.
     */
  } // returns action and deactivates objects
  // in response to successful drop
  // When this object is over its target and its target is active:
  // deactivate both this object and the target object, and return action,
  // otherwise return null
}


/**
 * Allows the user to drag the object around the screen.
 * 
 * @author Safi
 */
class DraggableObject extends VisibleObject {
  private boolean mouseWasPressed; // similar to use in ClickableObject
  private boolean isDragging; // true when this object is being dragged by the user
  private int oldMouseX; // horizontal position of mouse during last update
  private int oldMouseY; // vertical position of mouse during last update

  public DraggableObject(String name, int x, int y) {
  } // initializes new draggable object

  @Override
  public Action update() {
    // PApplet.mousePressed, PApplet.mouseX, and PApplet.mouseY
    // to detect when the user presses down the mouse button and the mouse is over this object.

    // action should begin the dragging of this object which will continue until the mouse button is
    // released.

    // While this object is being dragged, it should be moved by the same amount that the
    // mouse has moved between this and the previous call of the update() method (use oldMouseX
    // and oldMouseY to track this).
    //
    // Each time this dragging ends, the drop method should be called
    // as a result. Although this class' drop method doesn't do anything interesting (it simply
    // returns
    // null), sub classes will be able to override this method with more interesting behavior.

    // Make sure that any action object returned from such drop() calls are returned from this
    // class'
    // update() method. Test this out by adding a new DraggableObject to the gameObjects list in
    // TreasureHunt.setup(), similar to how we were testing the koala. Create a new draggable object
    // with the name "key" at position (70,70) to accomplish what is shown in Fig. 1. You should
    // then try dragging this object around the playground scene, while the program runs.
  } // calls VisibleObject update() first, then moves
  // according to mouse drag
  // each time isDragging changes from true to false, the drop() method below will be
  // called once and any action objects returned from that method should then be
  // returned from update()

  protected Action drop() {
    return null;
  } // this method returns null.
  // subclass types will override this drop() method to perform more interesting behavior
}


/**
 * Intractable objects by clicking
 * 
 * @author Safi
 */
class ClickableObject extends VisibleObject {
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


/**
 * Visible object with a graphical representation in the game.
 * 
 * @author Safi
 */
class VisibleObject extends InteractiveObject {
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


/**
 * Manages capabilities common to all interactive objects in the game.
 * 
 * @author Safi
 */
class InteractiveObject {
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


/**
 * Handles responses to an object being clicked or dragged onto another, as well as printing out
 * messages.
 * 
 * @author Safi
 */
class Action {
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


/**
 * Treasure Hunst style adventure puzzle game: Involves graphical elements that the user can click
 * on, and can drag onto others to find clues, solve puzzles and eventually win the game. The user
 * must find a coin and place/drop it onto a target location shown in the map to win.
 * 
 * @author Safi
 */
public class TreasureHunt extends PApplet {
  private PImage backgroundImage; // loaded background image instance
  private ArrayList<InteractiveObject> gameObjects;


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
    this.getSurface().setTitle("Treasure Hunt"); // Displays the title of the display window
    /* TODO: Complete the implementation of this method */
    // load background image
    backgroundImage = this.loadImage("images" + File.separator + "background-scene.png");
    // initialize processing object
    InteractiveObject.setProcessing(this);
    
    // load the background image lename, and introductory text message, and interactive objects or clues descriptions from a text le.
    loadGameSettings("clues" + File.separator + "treasureHunt.clues");
    
    // initialize game objects
    gameObjects = new ArrayList<InteractiveObject>();

    
    
    // test 1 - create visible koala
    InteractiveObject k = new VisibleObject("koala", 350, 65)
    // test 2 - create clickable koala instead.
//    InteractiveObject k =  new ClickableObject("koala", 350, 65,
//        new Action("What a cute stuffed koala! It looks like a real one!"));

        
    // add visible objects to the game
    gameObjects.add(k);

  }

  /**
   * Updates the treasure hunt game display window
   */
  @Override
  public void draw() {
    /* TODO: Implement this method */
    this.image(this.backgroundImage, 0, 0); // draw background image

    // update each interactive object
    for (InteractiveObject o : gameObjects) {
      Action action = o.update();
      // invoke action on object if any
      if (action != null)
        action.act();
    }

    /*
     * Now that we have a way for interactive objects to be activated, let's remove deactivated
     * objects from our gameObjects array list. This will be the job of our TreasureHunt's draw()
     * method. After calling update() on every interactive object in gameObjects list, our
     * TreasureHunt's draw() method should search through and remove all deactivated objects from
     * the gameObjects array list. After implementing this, you should see the key and chest
     * disappear after the key is dragged onto the chest.
     */
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
