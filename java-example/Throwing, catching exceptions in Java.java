import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class Election {
  private static ArrayList<String> voters = new ArrayList<String>();

  /**
   * Adds the name's of a voter to the voters list
   *
   * @param voterName - name of a potential voter
   * @param voterAge  - age of the potential voter
   * @throws NullPointerException if voterName is null
   * @throws DataFormatException  if voterAge is less than 18
   */
  public static void registerToVote(String voterName, int voterAge) throws DataFormatException {
    if (voterName == null)
      throw new NullPointerException("Cannot add a null reference to the list of voters");
    // NullPointerException is an unchecked exception
    if (voterAge < 18)
      throw new DataFormatException("WARNING! The age of a voter must be at least 18.");
    // DataFormatException is a checked exception. If it is thrown without being caught, you have 
    // to declare it in the method signature using a throws clause
    voters.add(voterName);

  }
}
