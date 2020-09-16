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
 * @author Safi
 *
 */
public class WisconsinPrairie {
  private static PApplet processing; // PApplet object that represents the graphic interface of the
                                     // WisconsinPrairie application
  private static PImage backgroundImage; // PImage object that represents the background image
  private static Cow[] cows; // array storing the current cows present in the Prairie
  private static Random randGen; // Generator of random numbers

  /**
   * Starts up the application implemented in accompanied `p2core.jar` library, which in turn uses
   * the callback methods implemented in this class.
   * 
   * @param args input arguments if any
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

    // initialize and load the image of the background.
    backgroundImage =
        processing.loadImage(String.join(PATH_SEPARATOR, IMG_FOLDER_PATH, BG_IMAGE_FILENAME));
    // initialize cows array, setting the number of Cow objects that can be present on screen.
    cows = new Cow[NUMBER_OF_COWS];
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
    final int WINDOW_WIDTH = processing.width;
    final int WINDOW_HEIGHT = processing.height;

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
    for (Cow currentCow : cows) {
      // skip empty positions with no elements
      if (currentCow == null)
        continue;
      currentCow.draw(); // draw cow image to screen
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
    // get image PImage instance
    PImage image = cow.getImage();
    // retrieve the dimensions of the cow image (cow.png: 149/114 pixels)
    final short IMAGE_WIDTH = (short) image.width;
    final short IMAGE_HEIGHT = (short) image.height;
    // retrieve coordinate positions of cow object
    final float COW_X = cow.getPositionX(), COW_Y = cow.getPositionY();
    // Current coordinate positions of the mouse pointer on the window area
    final float MOUSE_X = processing.mouseX;
    final float MOUSE_Y = processing.mouseY;

    // shift coordinates of cow and mouse positions for relative comparison to zero, where the cow
    // center is now positioned on the x-axis & y-axis intersection.
    float mouseShiftX = MOUSE_X - COW_X, mouseShiftY = MOUSE_Y - COW_Y;
    // check if mouse is between boundaries from all sides by comparing the distance from center of
    // cow coordinates matching the width and height on all sides
    if (Math.abs(mouseShiftX) <= (float) IMAGE_WIDTH / 2
        && Math.abs(mouseShiftY) <= (float) IMAGE_HEIGHT / 2)
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
    // if a cow was matched
    if (targetCow != null)
      targetCow.setDragging(true); // start dragging the cow image
  }

  /**
   * Callback method called each time the mouse is released
   */
  public static void mouseReleased() {
    // iterate over all cow objects and toggle their dragging property to false
    for (Cow currentCow : cows) {
      // skip empty positions with no elements
      if (currentCow == null)
        continue;
      // Set dragging to false for every cow object No cow must be dragged when the mouse is
      // released.
      currentCow.setDragging(false);
    }
  }

  /**
   * Callback method called each time the user presses a key
   */
  public static void keyPressed() {
    char keyPressed = processing.key; // retrieve user pressed key
    switch (keyPressed) {
      // Add cows to prairie on pressing 'c' or 'C'
      case 'c':
      case 'C':
        addCowRandom(); // add new cow to the prairie (if the limit is not exceeded)
        break;
      // Remove cow prairie under the mouse if any on pressing 'D' or 'd'
      case 'd':
      case 'D':
        removeCowUnderMouse(); // remove a single cow from prairie
        break;
    }
  }

  /**
   * Adds a new cow object with a random position coordinates to the list of cows objects.
   */
  private static void addCowRandom() {
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
      // create a cow object placing it on a random position in the display window.
      cows[i] = new Cow(processing, pX, pY);

      break; // (or return) Stop after adding a single cow object.
    }
  }

  /**
   * Remove a single cow from the prairie if any present (with the lowest index stored in the array)
   */
  private static void removeCowUnderMouse() {
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
