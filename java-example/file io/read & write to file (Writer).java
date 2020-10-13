import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The ReadStudentNames class implements a program that uses the File and Scanner classes to read a
 * file. Refer to the example for reading a file on zybook for another alternative for reading lines
 * from a file using the FileReader class.
 * 
 * defines an ArrayList of Students. Student is a provided class. Each student has a first name and a last name. The constructor of the Student class takes two input parameters of type String which represent respectively the first and last names of the student to be created.
 * 
 * samples of files to be read by the program: studentnames.txtPreview the document, and students.txtPreview the document.
 * 
 * @author Mouna AYARI BEN HADJ KACEM
 */

public class StudentNamesReaderWriter {
  // create an empty ArrayList of students
  private static ArrayList<Student> students = new ArrayList<Student>();

  /**
   * Reads and processes a file that should contain first and last names of a set of students
   * 
   * @param fileName filename of the file to read by this method
   */
  public static void readStudentFile(String fileName) {
    File inputFile; // Declare a reference to a File object
    Scanner input = null; // Declare a reference to a Scanner object
    // String studentName;

    try {
      // Create a File and Scanner objects
      inputFile = new File(fileName); // Set up a file given its name
      input = new Scanner(inputFile); // Create a Scanner Object from File

      // Read the file content using a loop
      while (input.hasNextLine()) {

        // do something with data...
        String studentName = input.nextLine().trim(); // read the next line using the scanner from
                                                      // the file
        // and remove extra spaces by calling trim() method
        if (studentName.length() < 1) // detect empty lines
          continue; // skip this line

        String studentNames[] = studentName.split(" ");
        if (studentNames.length != 2) {
          System.out.println("Error: Found miss formatted line: " + studentName);
          continue; // go to the next line
        }
        // add new Student to students list
        students.add(new Student(studentNames[0], studentNames[1]));
      }
    } catch (FileNotFoundException e) {
      // do something with the exception e... log, etc.
      // e.printStackTrace(); // Display the exception trace message
      // e.getMessage(); // returns the exception trace message in a String object
      System.out.println("Error: File Not Found");
    } finally {
      System.out.println("Inside Finally block.");
      if (input != null) // if the Scanner object WAS NOT created (FileNotFoundException)
        // Otherwise a NullPointerException will be thrown since input will be null.
        input.close();
    }
  }

  /**
   * writeStudentsNamestoFile reads the content of the ArrayList students and store it in a file
   * conforming to a given syntax
   * 
   * @param filename String representing the name including the path of the file where to save the
   *                 list of students
   */
  public static void writeStudentsNamestoFile(String filename) {

    File outputFile;
    PrintStream writer = null;

    try {
      // 1 Create a File and PrintWriter objects
      outputFile = new File(filename);
      writer = new PrintStream(outputFile);

      // 2. read the content and then store in an array using a loop
      for (int i = 0; i < students.size(); i++) {
        writer.println(students.get(i).getLastName() + ", " + students.get(i).getFirstName());
      }
    } catch (IOException e) {
      System.err.println("WARNING: Error while opening or saving the file!");
    } finally {
      // 3. close the file and print the result
        if (writer != null)
          writer.close();
    }
  }

  /**
   * Displays the list of students
   */
  public static void displayStudents() {
    System.out.println("---------------------------");
    System.out.println("Students ArrayList Content:");
    if (students.isEmpty())
      System.out.println("Empty List.");
    for (int i = 0; i < students.size(); i++) {
      System.out.println(students.get(i).getFirstName() + " " + students.get(i).getLastName());
    }
  }

  /**
   * Driver method
   * 
   * @param args input arguments
   */
  public static void main(String[] args) {
    String fileName = "studentnames.txt"; // User defined file name
    readStudentFile(fileName);
    displayStudents();
    String outputfileName = "outputStudentNames.txt";
    writeStudentsNamestoFile(outputfileName);
  }

}