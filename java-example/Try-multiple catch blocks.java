import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Reader {

  private static Scanner scanner;

  public static void main(String[] args) {

    // create a Scanner object to read user input
    scanner = new Scanner(System.in);

    System.out.println("Welcome to our Reader App!!! ");
    // prompt user
    System.out.print("Enter an integer: ");
    try {

      int x = scanner.nextInt();
      int y = Math.abs(x);
      System.out.println("The absolute value of " + x + " is " + y + ".");

    }
    // The order of the catch blocks matters!
	// Put the catch bloc to catch the most specific exception type first
    catch (InputMismatchException e1) {
      System.out.println("Inside InputMismatchException catch block.");
    } catch (IllegalStateException e2) {
      System.out.println("Inside IllegalArgumentException catch block.");
    } catch (NoSuchElementException e3) {
      System.out.println("Inside NoSuchElementException catch block.");
    }
    // catch(NullPointerException e0) { // a NullPointerException will be thrown 
                                        // if scanner is null
    // System.out.println("Error! A NullPointerException has been thrown!");
    // }// It would better to fix the error rather than catch a NullPointerException
    catch (RuntimeException e4) { // RuntimeException is the super type 
	                             // of all unchecked exceptions in Java
      System.out.println("Inside RuntimeException catch block.");
    }


    // Keep in Mind! When you have multiple catch blocks, put the catch block of the most specific
    // type of exception first.
    // The order of catch blocks of unrelated catch blocks does not matter.

    // RuntimeException is the super class of all unchecked exceptions in Java
    // Any other type of exception is checked.

    System.out.println("Bye!"); // display good-bye message

  }
}
