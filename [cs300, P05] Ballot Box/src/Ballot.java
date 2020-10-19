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
 * Ballot: collects Candidates for each office position
 * 
 * @author Safi
 *
 */
public class Ballot {
  static private ArrayList<Party> parties = new ArrayList<Party>(); // Parties enrolled in election
  static private int counter = 0; // value for generating unique ID values for each Ballot

  /*
   * Votes corresponding to specific office (Parallel array).
   * 
   * null entry means no vote has (yet) been cast for that office.
   */
  private Candidate[] votes;
  final private int ID; // unique ID of Ballot

  /**
   * Constructor initializing Ballot with unique ID and votes array.
   */
  public Ballot() {
    // parallel array corresponding to office positions
    votes = new Candidate[Candidate.OFFICE.length];
    // create unique ID
    ID = ++counter;
  }

  /**
   * Adds a Party to the Ballot election options
   * 
   * @param p target party to add to parties list
   */
  public static void addParty(Party p) {
    // if the class already contains this party, do nothing
    if (parties.contains(p))
      return;

    parties.add(p);
  }

  /**
   * Retrieves all Candidates running for the given office
   * 
   * @param office positions for candidates election
   * @return list of candidates of specific office
   */
  public static ArrayList<Candidate> getCandidates(String office) {
    ArrayList<Candidate> c = new ArrayList<>(); // Candidates for a particular office

    // get candidates from each party if exists
    for (Party p : parties) {
      try {
        c.add(p.getCandidate(office)); // add candidate from particular party
      } catch (NoSuchElementException e) {
        continue; // Should not crash if a Party has no Candidate running for the given office
      }
    }

    return c;
  }

  /**
   * Get candidate corresponding particular office vote.
   * 
   * @param office position of voted candidate
   * @return Candidate that this Ballot voted for, or null if there is no vote for that office.
   */
  public Candidate getVote(String office) {
    for (int index = 0; index < Candidate.OFFICE.length; index++)
      if (Candidate.OFFICE[index].equals(office))
        return votes[index];

    return null; // no vote for this office
  }

  /**
   * Records the vote for the given Candidate at the appropriate index in the Ballot's array.
   * 
   * @param c voted candidate
   */
  public void vote(Candidate c) {
    int targetIndex; // Candidate’s position in the votes array should correspond to their
                     // Candidate.OFFICE position

    for (targetIndex = 0; targetIndex < Candidate.OFFICE.length; targetIndex++)
      if (Candidate.OFFICE[targetIndex].equals(c.getOffice()))
        break;

    // Can overwrite an existing vote
    votes[targetIndex] = c;
  }

  /**
   * Determines equality by comparing unique ID values of two Ballots.
   * 
   * @param o the object to compare to this Ballot
   * @return true if the Ballots have the same ID (argument is equal to this), false otherwise
   */
  @Override
  public boolean equals(Object o) {
    // if provided Obejct o is not a Ballot return false.
    if (!(o instanceof Ballot))
      return false;

    // else cast o to a Ballot and compare ids
    if (this.ID == ((Ballot) o).ID)
      return true;
    else
      return false;
  }

}
