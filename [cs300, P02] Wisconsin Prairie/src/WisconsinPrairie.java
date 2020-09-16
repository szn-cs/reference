//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Wisconsin Prairie (P02 assignment)
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

import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Wisconsin Prairie a graphical application simulating the behavior of a set of cows in a Wisconsin
 * prairie.
 * <p>
 * It allows the user to add up to 10 cows to the prairie, and move them arbitrary within the
 * display window using the mouse.
 * 
 * @see Cow class - defined in the p2core library as part of the default package.
 * @author Safi Nassar
 *
 */
public class WisconsinPrairie {
  private static PApplet processing; // PApplet object that represents the graphic interface of the
                                     // WisconsinPrairie application
  private static PImage backgroundImage; // PImage object that represents the background image
  private static Cow[] cows; // array storing the current cows present in the Prairie
  private static Random randGen; // Generator of random numbers

  /**
   * @param args
   */
  public static void main(String[] args) {
    /*
     * Creates the display windows for the application, sets its dimension, checks for specific
     * callback methods, and then repeatedly updates its appearance and checks for user input.
     * 
     * Callback methods, such as setup(), draw(), mousePressed(), mouseReleased(), and keyPressed(),
     * are provided with access to a newly created processing PApplet object containing drawing
     * functionality from the "processing" library.
     */
    Utility.startApplication();
  }

  /**
   * Defines the initial environment properties of the application (which will be used to visualize
   * the Prairie area).
   * <p>
   * Environment properties include: screen size, background images to load, fonts, etc.
   * 
   * @param processingObj represents a reference to the graphical interface of the application
   */
  public static void setup(PApplet processingObj) {
    // JVM path delimiter (handled by JVM for specific platforms).
    final String PATH_SEPARATOR = "/";
    // relative path to images folder (.gif, .jpg, .tga, & .png supported)
    final String IMG_FOLDER_PATH = "images";
    // background image filename.
    final String BG_IMAGE_FILENAME = "background.png";
    // maximum number of cows possible to display on screen.
    final byte NUMBER_OF_COWS = (byte) 10;
    // initialize instance of generating random numbers of range represented by mathematical
    // notation: [0,1)
    randGen = new Random();
    // initialize the processing field to the one passed into the input argument parameter.
    processing = processingObj;
    // Dimensions of the display window when program initially loaded (width & height respectively).
    final int WINDOW_WIDTH = processing.width, WINDOW_HEIGHT = processing.height;

    // initialize and load the image of the background.
    backgroundImage =
        processing.loadImage(String.join(PATH_SEPARATOR, IMG_FOLDER_PATH, BG_IMAGE_FILENAME));
    // initialize cows array, setting the number of Cow objects that can be present on screen.
    cows = new Cow[NUMBER_OF_COWS];

    // Generate first cow at a random position.
    // {
    // // generates a random x and y positions of type float within the width & height of the
    // display
    // // window, respectively.
    // float pX = (float) randGen.nextInt(WINDOW_WIDTH);
    // float pY = (float) randGen.nextInt(WINDOW_HEIGHT);
    // // create a cow object placing it on a random position in the display window.
    // cows[0] = new Cow(processing, pX, pY);
    // }

    // Generate additional cows at the center.
    // {
    // // create additional cows objects in the center of the screen
    // for (int i = 0; i < cows.length; i++) {
    // // skip for occupied positions with a defined object.
    // if (cows[i] != null)
    // continue;
    // // x-position & y-position coordinates for the center of the display window.
    // float pX = (float) WINDOW_WIDTH / 2;
    // float pY = (float) WINDOW_HEIGHT / 2;
    // // create a cow object placing it on the center of the screen.
    // cows[i] = new Cow(processing, pX, pY);
    // }
    // }

  }

  /**
   * Draws and updates the application display window. This callback method called in an infinite
   * loop.
   * <p>
   * Events registered triggering display window redraws: mouse press, dragged or moved, or on key
   * press, etc.
   */
  public static void draw() {
    // Dimensions of the display window when program initially loaded (width & height respectively).
    final int WINDOW_WIDTH = processing.width, WINDOW_HEIGHT = processing.height;
    // Current coordinate positions of the mouse pointer on the window area
    final float mouseX = processing.mouseX, mouseY = processing.mouseY;

    // Draw the background image at the center of the screen (display window)
    processing.image(backgroundImage, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
    // Draw/Redraw every cow object on the display window, keeping them visible in the Prairie
    // window
    drawCows();
  }

  /**
   * Draw/Redraw every cow object into the display window
   */
  private static void drawCows() {
    // iterate over the cows objects redrawing them to screen
    for (int i = 0; i < cows.length; i++) {
      // skip empty positions with no elements
      if (cows[i] == null)
        continue;
      cows[i].draw(); // draw cow image to screen
    }
  }

  /**
   * Checks if the mouse is over a given cow whose reference is provided as input parameter
   *
   * @param cow reference to a given cow object
   * @return true if the mouse is over the given cow object (i.e. over the image of the cow), false
   *         otherwise
   */
  public static boolean isMouseOver(Cow cow) {
    boolean isMouseOver = false; // whether mouse is over the cow image
    // Current coordinate positions of the mouse pointer on the window area
    final float mouseX = processing.mouseX, mouseY = processing.mouseY;
    // retrieve the dimensions of the cow image (cow.png: 149/114 pixels)
    PImage image = cow.getImage(); // get image PImage instance
    final short IMAGE_WIDTH = (short) image.width, IMAGE_HEIGHT = (short) image.height;
    // retrieve coordinate positions of cow object
    final float cowX = cow.getPositionX(), cowY = cow.getPositionY();

    // shift coordinates of cow and mouse positions for relative comparison to zero, where the cow
    // center is now positioned on the x-axis & y-axis intersection.
    float mouseShiftX = mouseX - cowX, mouseShiftY = mouseY - cowY;
    // check if mouse is between boundaries from all sides by comparing the distance from center of
    // cow coordinates matching the width and height on all sides
    if (Math.abs(mouseShiftX) <= IMAGE_WIDTH / 2 && Math.abs(mouseShiftY) <= IMAGE_HEIGHT)
      isMouseOver = true;

    return isMouseOver;
  }

  /**
   * Callback method called each time the user presses the mouse
   */
  public static void mousePressed() {
    Cow targetCow = null; // The cow object the mouse should drag

    // check if the mouse is over one of the cow objects stored in the cows array.
    for (Cow currentCow : cows) {
      // skip empty positions with no elements
      // Note: I'm not sure if the "for in" loop in Java includes skips null values
      if (currentCow == null)
        continue;
      // check if current cow is under the mouse pointer
      if (isMouseOver(currentCow)) {
        targetCow = currentCow; // set the match cow object
        // If the mouse is over more than one cow, only the cow stored at the lowest index within
        // the cows array will be dragged.
        break;
      }
    }

    // if a cow was match
    if (targetCow != null)
      targetCow.setDragging(true); // start dragging the cow image
  }

  /**
   * Callback method called each time the mouse is released
   */
  public static void mouseReleased() {
    // iterate over all cow objects and toggle their dragging property to false
    for (int i = 0; i < cows.length; i++) {
      // skip empty positions with no elements
      if (cows[i] == null)
        continue;
      // Set dragging to false for every cow object No cow must be dragged when the mouse is
      // released.
      cows[i].setDragging(false);
    }
  }

  /**
   * Callback method called each time the user presses a key
   */
  public static void keyPressed() {
    char keyPressed = processing.key; // retrieve user pressed key

    // Add cows to prairie on pressing 'c' or 'C'
    if (keyPressed == 'c' || keyPressed == 'C') {
      addCow(); // add new cow to the prairie (if the limit is not exceeded)
    }

    // Remove cow prairie under the mouse if any on pressing 'D' or 'd'
    if (keyPressed == 'd' || keyPressed == 'D') {
      removeCow(); // remove a single cow from prairie
    }

  }

  /**
   * Adds a new cow object with a random position coordinates to the list of cows objects.
   */
  private static void addCow() {
    // Dimensions of the display window when program initially loaded (width & height respectively).
    final int WINDOW_WIDTH = processing.width, WINDOW_HEIGHT = processing.height;

    // search through the cows array for a null reference and add a cow object
    for (int i = 0; i < cows.length; i++) {
      // skip occupied positions
      if (cows[i] != null)
        continue;

      // generates a random x and y positions of type float within the width & height of the
      // display window, respectively.
      float pX = (float) randGen.nextInt(WINDOW_WIDTH);
      float pY = (float) randGen.nextInt(WINDOW_HEIGHT);
      // if found an empty space in the array replace the first (lowest index) null reference with a
      // new Cow object located at a random position of the display window.
      cows[i] = new Cow(processing, pX, pY);
      // place cow into random positions on the screen.
      // cows[0].setPositionX(pX);
      // cows[0].setPositionY(pY);

      break; // (or return) Stop after adding a single cow object.
    }
  }

  /**
   * Remove a single cow from the prairie if any present (with the lowest index stored in the array)
   */
  private static void removeCow() {
    // search through the cows array for a cow object that is under the mouse to remove it
    for (int i = 0; i < cows.length; i++) {
      // skip empty array positions
      if (cows[i] == null)
        continue;
      // skip cows that are not under the mouse
      if (!isMouseOver(cows[i]))
        continue;

      // remove the cow element found from the array
      cows[i] = null;
      break; // (or return) Stop after removing a single cow object.
    }
  }
}
