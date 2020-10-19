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

/**
 * BallotBox: collects Ballots & reports winner for each office.
 * 
 * @author Safi
 */
public class BallotBox {
  private ArrayList<Ballot> ballots; // ballots which have been cast

  /**
   * Default constructor
   */
  public BallotBox() {
    ballots = new ArrayList<Ballot>();
  }

  /**
   * Getter: total # of unique ballots
   * 
   * @return total number of unique Ballots in this BallotBox
   */
  public int getNumBallots() {
    int counter = 0;
    ArrayList<Ballot> dupList = new ArrayList<>(); // Ballots that appear more than once in list

    // count non duplicate ballots
    for (Ballot b : ballots)
      // if duplicate
      if ((ballots.indexOf(b) != ballots.lastIndexOf(b)) && !dupList.contains(b))
        dupList.add(b);
      else
        counter++;

    counter += dupList.size(); // count duplicate ballots once.

    return counter;
  }

  /**
   * Getter: Records all the existing Ballots' votes for the given office, returnning the winner
   * 
   * @param office position of the election
   * @return Candidate with the most votes
   */
  public Candidate getWinner(String office) {
    Candidate winner = null; // candidate with most votes

    // Retrieve the list of potential Candidates for the given office
    ArrayList<Candidate> candidates = Ballot.getCandidates(office);
    int[] counter = new int[candidates.size()]; // votes counter parallel array - corresponding to
                                                // candidates list
    // tracking how many votes each of the Candidates receives
    for (Ballot b : ballots) {
      if (b == null)
        continue;
      Candidate candidateVote = b.getVote(office); // get vote for office
      int index = candidates.indexOf(candidateVote); // get index of corresponding array
      counter[index]++; // add vote to counter
    }

    // Find the Candidate who has received the most votes
    int largestCountIndex = 0; // index with largest count
    for (int i = 1; i < counter.length; i++)
      if (counter[i] > counter[largestCountIndex])
        largestCountIndex = i;
    winner = candidates.get(largestCountIndex);

    // For this implementation, in the case of a tie between Candidates , return the Candidate who
    // appears first in the list of potential Candidates
    return winner;
  }

  /**
   * Adds a Ballot to the BallotBox iff the Ballot has not already been added.
   * 
   * @param incomingBallot ballot to add to the BallotBox
   */
  public void submit(Ballot incomingBallot) {
    // validate ballot: should not be a duplicate
    if (ballots.contains(incomingBallot))
      return; // skip

    ballots.add(incomingBallot);
  }

}
