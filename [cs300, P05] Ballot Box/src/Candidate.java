//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Simple election voting program [P05 Assignment]
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

/**
 * Candidate information
 * 
 * @author Safi
 */
public class Candidate {
  // possible office titles. (Asc. order in case binary search is used)
  protected static final String[] OFFICE =
      {"Deep State", "President", "Secretary", "Vice President"};
  private String name; // candidate's name
  private String office; // candicate's office

  /**
   * Constructor initializing Candidate with proper fields
   * 
   * @param name   of candidate
   * @param office title of candidate
   * @throws IllegalArgumentException if office is not valid
   */
  public Candidate(String name, String office) throws IllegalArgumentException {
    // prepare arguments
    name = name.trim();
    office = office.trim();

    // validate: office position exists
    boolean officeExist = false;
    // linear search in array elements
    for (int i = 0; i < Candidate.OFFICE.length; i++) {
      if (Candidate.OFFICE[i].equals(office)) {
        officeExist = true;
        break;
      }
    }
    if (!officeExist)
      throw new IllegalArgumentException("Invalid office argument: no such office position exists");

    // initialize fields with arguments
    this.name = name;
    this.office = office;
  }

  /**
   * Getter name
   * 
   * @return candidate name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter office
   * 
   * @return candidate office
   */
  public String getOffice() {
    return this.office;
  }

  /**
   * Create a string from candidate instance with format: NAME (OFFICE)
   * 
   * @return string representation of candidate
   */
  @Override
  public String toString() {
    return String.format("%s (%s)", this.name, this.office);
  }
}
