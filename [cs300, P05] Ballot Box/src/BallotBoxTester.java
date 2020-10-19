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
 * Tests bench for all package classes
 * 
 * @author Safi
 */
public class BallotBoxTester {

  /**
   * Calls the test methods implemented in the packge's classes and displays output
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    System.out.format("testCandidate: %b\n", testCandidate());
    System.out.format("testParty: %b\n", testParty());
    System.out.format("testBallot: %b\n", testBallot());
    System.out.format("testBallotBox: %b\n", testBallotBox());
  }

  /**
   * Candidate unit tests: constructor, accessors, toString method.
   * 
   * @return test state
   */
  public static boolean testCandidate() {
    try {
      // check constructor and accessors:
      // valid arguments
      {
        // fixture: valid arguments
        final String n1 = "A", o1 = Candidate.OFFICE[0];
        final String n2 = "B", o2 = Candidate.OFFICE[1];
        // construct instances
        Candidate c1 = new Candidate(n1, o1);
        Candidate c2 = new Candidate(n2, o2);
        if (!(n1.equals(c1.getName()) && o1.equals(c1.getOffice()))
            || !(n2.equals(c2.getName()) && o2.equals(c2.getOffice()))) {
          System.out.println("Expected accessors name & office to equal initialization arguments.");
          return false;
        }
      }
      // invalid arguments
      {
        // fixture: valid arguments
        final String n = "A", o = "X"; // invalid office entry
        try {
          new Candidate(n, o); // construct candidate
          System.out.println("Must throw a IllegalArgumentException invalid argumnets.");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }
      }

      // check overriden toString method
      {
        // fixture:
        final String n = "Name Surname", o = Candidate.OFFICE[0]; // valid arguments
        final String expected = String.format("%s (%s)", n, o); // expected output
        Candidate c = new Candidate(n, o);
        String actual = c.toString();
        if (!actual.equals(expected)) {
          System.out.println("toString method returning unexpected result.");
          return false;
        }
      }

    } catch (Exception e) {
      System.out.println("Fail: has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Party unit tests: instance constructor & methods
   * 
   * @return test state
   */
  public static boolean testParty() {
    try {

      // fixtures:
      final String n = "A"; // invalid office entry
      final Candidate c1 = new Candidate("C1", Candidate.OFFICE[0]),
          c2 = new Candidate("C2", Candidate.OFFICE[1]),
          c_invalid = new Candidate("C3", Candidate.OFFICE[0]); // same office candidate

      // construct
      Party p = new Party(n);
      Party p_empty = new Party(n);

      // addCandidate modifier: Add valid candidate
      p.addCandidate(c1);
      p.addCandidate(c2);

      // name accessor
      if (!p.getName().equals(n)) {
        System.out.println("Expected name accessor to equal initialization arguments.");
        return false;
      }

      // addCandidate modifier: Add invalid candidate
      try {
        p.addCandidate(c_invalid); // candidate with a taken office position
        System.out.println(
            "Must throw a IllegalArgumentException invalid argumnets for invalid candidate.");
        return false;
      } catch (IllegalArgumentException e) {
        // Expected behavior
      }

      // getSize accessor
      if (p.getSize() != 2) { // verify correct size for valid and invalid additions of candidates
        System.out.println("Expected size accessor to return correct candidates numbers.");
        return false;
      }
      if (p_empty.getSize() != 0) {
        System.out.println("Expected size accessor of empty party to return zero.");
        return false;
      }

      // getCandidate accessor: existing and non-existing candidates
      if (p.getCandidate(Candidate.OFFICE[0]) != c1) { // find existing candidate
        System.out.println("Expected to get candidate that exists.");
        return false;
      }
      try {
        p.getCandidate(Candidate.OFFICE[2]); // find non-existing candidate
        System.out.println("Must throw a NoSuchElementException for non-existing candidate.");
        return false;
      } catch (NoSuchElementException e) {
        // Expected behavior
      }

    } catch (Exception e) {
      System.out.println("Fail: has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Ballot unit tests: instance constructor & methods
   * 
   * @return test state
   */
  public static boolean testBallot() {
    try {
      // Check ballot equality functionality
      {
        Ballot b1 = new Ballot();
        Ballot b2 = new Ballot();

        // Check whether they’re equal.
        if (b1.equals(b2)) {
          System.out.println("Expected ballots to be different.");
          return false;
        }
        // Check whether a Ballot object is equal to itself.
        if (!b1.equals(b1)) {
          System.out.println("Expected a Ballot to equal itself.");
          return false;
        }
      }

      // prepare data instances
      Party p1 = new Party("P1"), p2 = new Party("P2");
      for (int i = 0; i < 3; i++) // add 3 candidates for offices 0, 1, 2
        p1.addCandidate(new Candidate("P1C" + i, Candidate.OFFICE[i]));
      for (int i = 0; i < 2; i++) // add 2 candidates for offices 0, 1
        p2.addCandidate(new Candidate("P2C" + i, Candidate.OFFICE[i]));

      // Check add party and get candidates accessors/modifiers
      {
        // add parties
        Ballot.addParty(p1);
        Ballot.addParty(p2);
        Ballot.addParty(p1); // if already exist do nothing.

        // get candidates
        ArrayList<Candidate> candidates; // list of possible Candidates for a given office
        candidates = Ballot.getCandidates(Candidate.OFFICE[2]); // candidates from 1 party
        if (candidates.size() != 1 || !candidates.get(0).getName().equals("P1C2")) {
          System.out.println("Expected different list of candidates to be retrieved from 1 party.");
          return false;
        }
        candidates = Ballot.getCandidates(Candidate.OFFICE[0]); // candidates from all parties
        if (candidates.size() != 2 || !candidates.get(0).getName().equals("P1C0")
            || !candidates.get(1).getName().equals("P2C0")) {
          System.out
              .println("Expected different list of candidates to be retrieved from 2 parties.");
          return false;
        }
      }

      // Check voting functionality
      {
        Ballot b = new Ballot(); // construct Ballot
        // votes for office 0, 1. while don't cast vote for office 2
        b.vote(p1.getCandidate(Candidate.OFFICE[0])); // vote for office 0
        b.vote(p2.getCandidate(Candidate.OFFICE[1])); // vote for office 1
        // Check ballot vote values
        Candidate c; // candidate voted for
        c = b.getVote(Candidate.OFFICE[0]);
        if (!c.getName().equals("P1C0")) {
          System.out.println("Mismatch vote - ballot's candidate vote doesn't match actual voted.");
          return false;
        }
        c = b.getVote(Candidate.OFFICE[1]);
        if (!c.getName().equals("P2C1")) {
          System.out.println("Mismatch vote - ballot's candidate vote doesn't match actual voted.");
          return false;
        }
        c = b.getVote(Candidate.OFFICE[2]);
        if (c != null) {
          System.out.println("Non casted vote should be null.");
          return false;
        }
      }

    } catch (Exception e) {
      System.out.println("Fail: has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * BallotBox unit tests: instance constructor & methods
   * 
   * @return test state
   */
  public static boolean testBallotBox() {
    try {
      // prepare data of related instances
      Party p1 = new Party("P1"), p2 = new Party("P2");
      for (int i = 0; i < Candidate.OFFICE.length; i++) {
        p1.addCandidate(new Candidate("P1C" + i, Candidate.OFFICE[i]));
        p2.addCandidate(new Candidate("P2C" + i, Candidate.OFFICE[i]));
      }
      Ballot.addParty(p1);
      Ballot.addParty(p2);
      // Ballots: 2x"P1C0" versus 1x"P2C0"
      Ballot b1 = new Ballot();
      b1.vote(p1.getCandidate(Candidate.OFFICE[0]));
      Ballot b2 = new Ballot();
      b2.vote(p1.getCandidate(Candidate.OFFICE[0]));
      Ballot b3 = new Ballot();
      b3.vote(p2.getCandidate(Candidate.OFFICE[0]));
      // expected winner name
      final String expectedWinner = "P1C0";

      BallotBox box = new BallotBox();
      // add ballots
      box.submit(b1);
      box.submit(b2);
      box.submit(b3);
      // add duplicates
      box.submit(b1);
      box.submit(b1);
      box.submit(b3);

      // # of ballots
      if (box.getNumBallots() != 3) {
        System.out.println("# of ballots (with duplicates) doesn't match expected value.");
        return false;
      }

      // check winner
      String actualWinner = box.getWinner(Candidate.OFFICE[0]).getName();
      if (!actualWinner.equals(expectedWinner)) {
        System.out.println("Expected a different winner candidate.");
        return false;
      }

    } catch (Exception e) {
      System.out.println("Fail: has thrown a non expected exception.");
      e.printStackTrace();
      return false;
    }

    return true;
  }
}
