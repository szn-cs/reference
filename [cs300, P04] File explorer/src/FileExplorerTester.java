//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: File explorer test runner
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

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unit tests for File Explorer program
 * 
 * @author Safi
 */
public class FileExplorerTester {
  // fixtures
  static private List<String> contents_cs300 =
      Arrays.asList(new String[] {"grades", "lecture notes", "programs", "quizzes preparation",
          "reading notes", "syllabus.txt", "todo.txt"});
  static private List<String> allFiles_cs300 = Arrays.asList(new String[] {"ExceptionHandling.txt",
      "proceduralProgramming.txt", "UsingObjectsAndArrayLists.txt", "AlgorithmAnalysis.txt",
      "Recursion.txt", "COVIDTracker.java", "COVIDTrackerTester.java", "WisconsinPrairie.java",
      "Benchmarker.java", "ComparisonMethods.java", "Program01.pdf", "Program02.pdf",
      "Program03.pdf", "codeSamples.java", "outline.txt", "zyBooksCh1.txt", "zyBooksCh2.txt",
      "zyBooksCh3.txt", "zyBooksCh4.txt", "syllabus.txt", "todo.txt"});
  static private List<String> txtOnly_cs300 = Arrays.asList(new String[] {"ExceptionHandling.txt",
      "proceduralProgramming.txt", "UsingObjectsAndArrayLists.txt", "AlgorithmAnalysis.txt",
      "Recursion.txt", "outline.txt", "zyBooksCh1.txt", "zyBooksCh2.txt", "zyBooksCh3.txt",
      "zyBooksCh4.txt", "syllabus.txt", "todo.txt"});

  /**
   * Test listContents method functionality
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test state
   */
  public static boolean testListContents(File folder) {
    try {

      // list contents of cs300 folder
      {
        String u = "Scenario 1.1: "; // # unit test
        List<String> expected = contents_cs300; // expected output content
        ArrayList<String> actual = FileExplorer.listContents(folder); // cs300 immediate contents

        // verify number of contents
        if (actual.size() != expected.size()) {
          System.out.println(u + "Must contain 7 elements.");
          return false;
        }

        // verify dirents list
        for (int i = 0; i < expected.size(); i++) {
          if (!actual.contains(expected.get(i))) {
            System.out.println(u + "List missmatch. Missing: " + expected.get(i));
            return false;
          }
        }
      }

      // list the contents of the grades folder
      {
        String u = "Scenario 1.2: "; // # unit test
        int expectedSize = 0; // Folder expected to be empty
        File f = new File(folder.getPath() + File.separator + "grades"); // folder path
        List<String> actual = FileExplorer.listContents(f); // get immediate contents list
        if (actual.size() != expectedSize) {
          System.out.println(u + "Expected folder to be empty.");
          return false;
        }
      }


      // list the contents of the p02 folder
      {
        String u = "Scenario 1.3: "; // # unit test
        int expectedSize = 1;
        String expectedContent = "WisconsinPrairie.java";
        File f = new File(folder.getPath() + File.separator + "programs" + File.separator + "p02");
        List<String> actual = FileExplorer.listContents(f); // list of contents
        if (actual.size() != expectedSize || !actual.contains(expectedContent)) {
          System.out.println(
              u + "Expected files # = " + expectedSize + "; Including file: " + expectedContent);
          return false;
        }
      }


      // Try to list the contents of a file
      {
        String u = "Scenario 1.4: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "todo.txt");
        try {
          List<String> actual = FileExplorer.listContents(f); // list contents
          System.out.println(u + "Must throw a NotDirectoryException for non directory inputs.");
          return false;
        } catch (NotDirectoryException e) { // catch only the expected exception
          // Expected behavior -- no problem detected
        }
      }

      // Try to list the contents of not found directory/file
      {
        String u = "Scenario 1.5: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "music.txt");
        try {
          List<String> l = FileExplorer.listContents(f); // list contents of inexistent file
          System.out.println(u + "Must throw NotDirectoryException for non-existent input file.");
          return false;
        } catch (NotDirectoryException e) { // catch only the expected exception
          // Expected behavior -- no problem detected
        }
      }

    } catch (Exception e) {
      System.out.println("Fail: FileExplorer.listContents() has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Check that only the base terminating case of the recursive function is correct.
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test case state
   */
  public static boolean testDeepListBaseCase(File folder) {
    try {
      // call on folder with no subfolders - there should be no recursive calls.
      {
        String u = "Scenario 2.1: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "grades");
        List<String> l = FileExplorer.deepListContents(f);
        if (l.size() != 0) {
          System.out.println(u + "Expected contents list to be empty for empty directories.");
          return false;
        }
      }

      // Try to list the contents of not found directory/file
      {
        String u = "Scenario 2.2: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "music.txt");
        try {
          List<String> l = FileExplorer.deepListContents(f); // list contents of inexistent file
          System.out.println(u + "Must throw NotDirectoryException for non-existent input file.");
          return false;
        } catch (NotDirectoryException e) { // catch only the expected exception
          // Expected behavior -- no problem detected
        }
      }

    } catch (Exception e) {
      System.out
          .println("Fail: FileExplorer.deepListContents() has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Check recursive calls logic and resulting contents list
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test case state
   */
  public static boolean testDeepListRecursiveCase(File folder) {
    try {
      // check examples that should produce recursive calls
      {
        String u = "Scenario 2.3: "; // # unit test
        List<String> expected = allFiles_cs300; // expected output content
        ArrayList<String> actual = FileExplorer.deepListContents(folder); // cs300 all files
        // verify number of contents
        if (actual.size() != expected.size()) {
          System.out.println(u + "Must contain " + expected.size() + " elements.");
          return false;
        }
        // verify dirents list
        for (int i = 0; i < expected.size(); i++) {
          if (!actual.contains(expected.get(i))) {
            System.out.println(u + "List missmatch. Missing: " + expected.get(i));
            return false;
          }
        }
      }

    } catch (Exception e) {
      System.out
          .println("Fail: FileExplorer.deepListContents() has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Test searchByName method functionality
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test state
   */
  public static boolean testSearchByFileName(File folder) {
    try {

      // find existing file
      {
        String u = "Scenario 3.1: "; // # unit test
        String expected =
            "cs300" + File.separator + "reading notes" + File.separator + "zyBooksCh1.txt";
        String actual = FileExplorer.searchByName(folder, "zyBooksCh1.txt"); // search for file
        if (!actual.equals(expected)) {
          System.out.println(u + "Path of file doesn't match expected: " + actual);
          return false;
        }
      }

      // Try to list the contents of not found directory/file
      {
        String u = "Scenario 3.2: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "music.txt");
        try {
          String s = FileExplorer.searchByName(f, "zyBooksCh1.txt");
          System.out.println(u + "Must throw NoSuchElementException for non-existent input file.");
          return false;
        } catch (NoSuchElementException e) { // catch only the expected exception
          // Expected behavior -- no problem detected
        }
      }

    } catch (Exception e) {
      System.out.println("Fail: FileExplorer.searchByName() has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Test searchByKey method functionality
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test state
   */
  public static boolean testSearchByKey(File folder) {
    // search files of specific extension
    {
      String u = "Scenario 4.1: "; // # unit test
      List<String> expected = txtOnly_cs300; // expected output content
      ArrayList<String> actual = FileExplorer.searchByKey(folder, ".txt");

      // verify number of contents
      if (actual.size() != expected.size()) {
        System.out.println(u + "Must contain " + expected.size() + " elements.");
        return false;
      }

      // verify dirents list
      for (int i = 0; i < expected.size(); i++) {
        if (!actual.contains(expected.get(i))) {
          System.out.println(u + "List missmatch. Missing: " + expected.get(i));
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Check that only the base terminating case of the recursive function is correct.
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test case state
   */
  public static boolean testSearchByKeyBaseCase(File folder) {
    try {
      // call on folder with no subfolders - there should be no recursive calls.
      {
        String u = "Scenario 4.2: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "grades");
        List<String> l = FileExplorer.searchByKey(f, ".txt");
        if (l.size() != 0) {
          System.out.println(u + "Expected contents list to be empty for empty directories.");
          return false;
        }
      }

      // Try to list the contents of not found directory/file
      {
        String u = "Scenario 4.3: "; // # unit test
        File f = new File(folder.getPath() + File.separator + "music.txt");
        List<String> l = FileExplorer.searchByKey(f, ".txt"); // list contents of inexistent file

        if (l.size() != 0) {
          System.out.println(u + "Expected empty contents list when internal exceptions handled.");
          return false;
        }
      }

    } catch (Exception e) {
      System.out.println("Fail: FileExplorer.searchByKey() has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Test searchBySize method functionality
   * 
   * @param folder "cs300" filesystem mockup folder
   * @return test state
   */
  public static boolean testSearchBySize(File folder) {
    // not required to implement.
    return true;
  }

  /**
   * Calls the test methods implemented in this class and displays output
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    System.out.println("testListContents: " + testListContents(new File("cs300")));
    System.out.println("testDeepListBaseCase: " + testDeepListBaseCase(new File("cs300")));
    System.out
        .println("testDeepListRecursiveCase: " + testDeepListRecursiveCase(new File("cs300")));
    System.out.println("testSearchByFileName: " + testSearchByFileName(new File("cs300")));
    System.out.println("testSearchByKey: " + testSearchByKey(new File("cs300")));
    System.out.println("testSearchByKeyBaseCase: " + testSearchByKeyBaseCase(new File("cs300")));
    // System.out.println("testSearchBySize: " + testSearchBySize(new File("cs300")));
  }

}
