import java.util.Scanner;

/**
 * This class implements a piggy bank driver application
 * 
 * @author Mouna
 *
 */
public class PiggyBankDriver {

  private final static int CAPACITY = 20; // capacity of the piggy bank in terms of number of coins
  private final static String WELCOME_MSG =
      "======== WELCOME to the Piggy Bank Application ========";
  private final static String GOODBYE_MSG =
      "======== Thank you for using this Application! ========";
  private final static String MENU =
      "\nCOMMAND MENU:\n" + "[A <coin>] Add one coin value in cents to the piggy bank.\n"
          + "[B] Display the Balance of the piggy bank in dollars.\n"
          + "[E] Empty the piggy bank (remove all coins from the piggy bank).\n"
          + "[R] Remove an arbitrary coin from the piggy bank.\n"
          + "[W <cents>] Withdraw at least a given amount in cents.\n"
          + "[P] Print/display the content of the piggy bank.\n" + "[Q] Quit the application.\n"
          + "[H] Help (display this Menu).";

  /**
   * Processes and runs one user command line
   * 
   * @param command a user command line
   * @param coins an oversize array which holds all the coins stored in a piggy bank
   * @param size the total number of coins held in the piggy bank
   * @return the size of coins array after command is run
   */
  private static int processUserCommandLine(String command, int[] coins, int size) {
    // split and parse the command line
    String[] input = command.trim().split(" ");
    int[] output;
    switch (input[0].toUpperCase()) {
      case "A": // add a coin to the piggy bank
        size = PiggyBank.addCoin(Integer.parseInt(input[1]), coins, size);
        break;
      case "B": // display the balance of the piggy bank
        System.out.println("Balance: $" + PiggyBank.getBalance(coins, size) / 100.0 + ".");
        break;
      case "E": // empty the piggy bank
        size = PiggyBank.emptyPiggyBank(coins, size);
        break;
      case "R": // remove an arbitrary coin from the piggy bank
        size = PiggyBank.removeCoin(coins, size);
        break;
      case "W": // withdraw a specific amount in cents from the piggy bank
        int amount = Integer.parseInt(input[1]); // amount to be removed in cents
        System.out.println("Removing " + amount + " cents:");
        output = PiggyBank.withdraw(amount, coins, size);
        size = output[0];
        int quarter = PiggyBank.COINS_VALUES[3];
        int dime = PiggyBank.COINS_VALUES[2];
        int nickel = PiggyBank.COINS_VALUES[1];
        int penny = PiggyBank.COINS_VALUES[0];
        int withdrawalAmount =
            output[1] * quarter + output[2] * dime + output[3] * nickel + output[4] * penny;
        if (withdrawalAmount != 0)
          System.out.println("All done. " + withdrawalAmount + " cents removed.");
        break;
      case "P": // print/display the piggy bank information
        PiggyBank.printPiggyBank(coins, size);
        break;
      case "H": // display the menu
        System.out.println(MENU);
        break;
      default:
        System.out.println("WARNING. Invalid command. Please enter H and refer to the menu.");
    }
    return size;

  }

  /**
   * Driver method for the piggy bank application (reads and processes user command lines)
   * 
   * @param scanner
   */
  private static void driver() {
    // create an oversize array which represents the piggy bank
    int[] coins = new int[CAPACITY]; // create piggy bank
    int size = 0; // number of coins held in the piggy bank so far

    // Create a scanner to read the user input command lines
    Scanner scanner = new Scanner(System.in);
    // set prompt command line message
    String promptCommandLine = "\nENTER COMMAND: ";

    // display menu
    System.out.println(MENU);
    // prompt user
    System.out.print(promptCommandLine);
    // read first user command line
    String line = scanner.nextLine().trim();
    char c = line.charAt(0); // first character in the user command line

    while (Character.toUpperCase(c) != 'Q') {
      // parse and process the user command line
      size = processUserCommandLine(line, coins, size);
      System.out.print(promptCommandLine);
      // read next user command line
      line = scanner.nextLine().trim();
      c = line.charAt(0); // first character in the user command line
    }

    // close the scanner
    scanner.close();
  }

  /**
   * Main method for the piggy bank application
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    // display Welcome Message
    System.out.println(WELCOME_MSG);
    // read and process user command lines
    driver();
    // display good bye message
    System.out.println(GOODBYE_MSG);
  }

}
