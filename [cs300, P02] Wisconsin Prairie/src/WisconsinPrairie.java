//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: WisconsinPrairie.java
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
    final String PATH_SEPARATOR = "/"; // JVM path delimiter (handled by JVM for specific
                                       // platforms).
    final String IMG_FOLDER_PATH = "images"; // relative path to images folder (.gif, .jpg, .tga,
                                             // and .png supported)
    final String BG_IMAGE_FILENAME = "background.png"; // background image filename.
    final byte NUMBER_OF_COWS = (byte) 10; // maximum number of cows possible to display on screen.
    randGen = new Random(); // initialize instance of generating random numbers of range represented
                            // by mathematical notation: [0,1)
    // initialize the processing field to the one passed into the input argument parameter.
    processing = processingObj;
    // Dimensions of the display window when program initially loaded (width & height respectively).
    final int WINDOW_WIDTH = processing.width, WINDOW_HEIGHT = processing.height;

    // initialize and load the image of the background.
    backgroundImage =
        processing.loadImage(String.join(PATH_SEPARATOR, IMG_FOLDER_PATH, BG_IMAGE_FILENAME));
    // Draw the background image at the center of the screen (display window)
    processing.image(backgroundImage, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);

    // initialize cows array, setting the number of Cow objects that can be present on screen.
    cows = new Cow[NUMBER_OF_COWS];

    // Generate first cow at a random position.
    {
      // generates a random x-position of type float within the width of the display window
      float pX = (float) randGen.nextInt(WINDOW_WIDTH);
      // generates a random y-position of type float within the height of the display window
      float pY = (float) randGen.nextInt(WINDOW_HEIGHT);
      // create a cow object placing it on a random position in the display window.
      cows[0] = new Cow(processing, pX, pY);
      cows[0].draw(); // draw image on display area.
    }

    // Generate additional cows at the center.
    {
      // create additional cows objects in the center of the screen
      for (int i = 0; i < cows.length - 1; i++) {
        // skip for occupied positions with a defined object.
        if (cows[i] != null)
          continue;
        // create a cow object placing it on the center of the screen.
        cows[i] = new Cow(processing, (float) WINDOW_WIDTH / 2, (float) WINDOW_HEIGHT / 2);
        cows[i].draw(); // draw image on the display area.
      }
    }

  }

}
