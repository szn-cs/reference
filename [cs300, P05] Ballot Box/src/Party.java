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

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Election Party: managing candidates affiliated with a party
 * 
 * @author Safi
 *
 */
public class Party {
  private String name; // party's name
  private ArrayList<Candidate> candidates = new ArrayList<>(); // candidates affiliated with party

  /**
   * Constructor initializing party with proper fields
   * 
   * @param name of party
   */
  public Party(String name) {
    this.name = name.trim();
  }

  /**
   * Getter name
   * 
   * @return party name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter number of Candidates belonging to the Party
   * 
   * @return party's candidates number
   */
  public int getSize() {
    return candidates.size();
  }

  /**
   * Getter: Find the candidate form the Party running the given office.
   * 
   * @param office of target candidate to find
   * @return candidate of provided office
   * @throws NoSuchElementException if no Candidate is found.
   */
  public Candidate getCandidate(String office) throws NoSuchElementException {
    // prepare input argument
    office = office.trim();

    // search for matching candidate
    Candidate c = null; // candidate found
    // linear search in array elements
    for (Candidate e : candidates) {
      // check whether each Candidate’s office matches the query String
      if (e.getOffice().equals(office)) {
        c = e; // candidate found
        break;
      }
    }
    if (c == null)
      throw new NoSuchElementException("No candidate matching the requested office exists.");

    return c;
  }

  /**
   * Adds a Candidate to the Party
   * 
   * @param c candidate to add
   * @throws IllegalArgumentException if the provided Candidate is running for the same office as
   *                                  another member of the Party
   */
  public void addCandidate(Candidate c) throws IllegalArgumentException {
    if (c == null) // make sure no nulls are passed
      return;

    // validate: no other Candidate in this Party is running for the same office
    for (Candidate e : candidates)
      if (e.getOffice().equals(c.getOffice()))
        throw new IllegalArgumentException("Invalid candidate: office position already taken.");

    candidates.add(c);
  }


}
