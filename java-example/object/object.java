// File header

/**
 * This class models the type Student
 * 
 * @author Mouna
 *
 */
public class Student {
  private String name; // Student's name (instance field)
  private double test1, test2, test3; // Grades on three tests. (instance fields)
  private static int numberOfStudents; // number of students already created 
                                       // (static/class field)

  /**
   * Creates a new Studing without initializing its instance fields
   */
  public Student() {
    numberOfStudents++;
  }

  /**
   * Returns the average of the grades of this student on the three tests
   * 
   * @return average grade of this student
   */
  public double getAverage() {
    // compute and return average test grade
    return (test1 + test2 + test3) / 3;
  }

  /**
   * Uses objects of type Student
   * 
   * @param args input parameters if any
   */
  public static void main(String[] args) {
    // Declare four variables of type Student
    Student std1, std2, std3, std4; // all these references are initially null
    std1 = new Student(); // Create a new Student object and store its reference in the variable
                          // std1
    std2 = new Student(); // Create a second Student object and store its reference into std2
    String s = std1.name;
    System.out.println(std1);

    // Set values of some instance fields belonging to the created objects
    std1.name = "Leon Lett";
    std2.name = "Norah Write";

    System.out.println(std1.name.charAt(0));

    std3 = std2; // Copy the reference value in std2 into the reference std3
                 // std3 is a shallow copy of std2
    std3.test1 = 95.6;
    std2.test2 = 84.2;

    std1 = null; // The student object with name "Leon Lett"
                 // will be garbage collected
  }

}