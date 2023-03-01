/**
 * This class models the reference type Person
 * 
 * @author Mouna
 *
 */
public class Person {

  /**
   * ⭐ Indicates whether some object obj equals to this Person. Two persons are equals if they are of
   * the same age.
   * 
   * @param obj the reference object with which to compare.
   * @return true if this Person equals the provided object obj, and false otherwise
   */  
  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Person) && (((Person)obj).age == this.age);
  }
  
  /**
   * ⭐ Returns a string representation of this person in the format "first last(age)"
   * @return a String representation of this person
   */
  @Override
  public String toString() {
    return this.first + " " + this.last + "(" + age + ")";
  }


  private String first; // first name of this Person
  private String last; // last name of this Person
  private int age; // age of this Person

  /**
   * Creates a new Person with given first and last names
   * 
   * @param first the first name of this person
   * @param last  the last name of this person
   * @param age   the age to be assigned to this person
   */
  public Person(String first, String last, int age) {
    this.first = first;
    this.last = last;
    this.age = age;
  }

  /**
   * Returns the first name of this person
   * 
   * @return the first name of this person
   */
  public String getFirst() {
    return this.first;
  }

  /**
   * Returns the last name of this person
   * 
   * @return the last name of this person
   */
  public String getLast() {
    return last;
  }

  /**
   * Sets first name of this person to a given string
   * 
   * @param first the first to set
   */
  public void setFirst(String first) {
    this.first = first;
  }


  /**
   * Sets the last name of this person
   * 
   * @param last the last to set
   */
  public void setLast(String last) {
    this.last = last;
  }

  /**
   * Getter for the age of this Person
   * 
   * @return the age of this person
   */
  public int getAge() {
    return age;
  }

  /**
   * Sets the age of this person
   * 
   * @param age the age to set
   */
  public void setAge(int age) {
    this.age = age;
  }


  
  /**
   * Main method
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    Person p1 = new Person("Sarah", "Madison", 25);
    Person p2 = new Person("Emily", "Middleton", 18);
    Person p3 = new Person("Andy", "Worker", 25);
    if (p1.equals(p2))
      System.out.println(p1+ " and " + p2 + " are equals.");
    else
      System.out.println(p1 + " and " + p2+ " are NOT equals.");

    if (p1.equals(p3))
      System.out.println(p1 + " and " + p3 + " are equals.");
    else
      System.out.println(p1 + " and " + p3 + " are NOT equals.");
    // Output:
    // Sarah Madison(25) and Emily Middleton(18) are NOT equals.
    // Sarah Madison(25) and Andy Worker(25) are equals.
  }
}