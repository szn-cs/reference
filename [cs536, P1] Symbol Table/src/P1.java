import java.util.*;

/**
 * To test your SymTable implementation, you will write a main program in a file named P1.java. The
 * program must not expect any command-line arguments or user input. It can read from one or more
 * files; if you set it up to do that, be sure to hand in the file(s) along with P1.java.
 * 
 * Be sure that your P1.java tests all of the Sym and SymTable operations and all situations under
 * which exceptions are thrown. Also think about testing both “boundary” and “non-boundary” cases.
 * 
 * It is up to you how your program works. A suggested approach is to write your program so that
 * output is only produced if one of the methods that it is testing does not work as expected (e.g.,
 * if the lookupLocal method of the SymTable class returns null when you expect it to return a
 * non-null value). This will make it much easier to determine whether your test succeeds or fails.
 * The one exception to this approach is that P1.java will need to test the print method of the
 * SymTable class and that will cause output to be produced.
 * 
 * 
 */

/**
 * P1 class: tests the SymTable & related classes.
 * 
 * @author Safi
 */
public class P1 {
   /**
    * test driver: calls unit test methods & annotates each test with pass/fail status
    * 
    * @param args input arguments if any
    */
   public static void main(String[] args) {
      boolean status = true; // test state
      String m = "Unit Tests:\n"; // output message accumolator
      HashMap<String, Boolean> u = new HashMap<>(); // unit test status info

      // run unit tests & record status
      u.put("testSymTableConstructor", testSymTableConstructor() ? true : false);

      // loop through unite test results
      for (Map.Entry<String, Boolean> entry : u.entrySet()) {
         m += "|→ " + entry.getKey() + " ";
         if (entry.getValue())
            m += "✔️";
         else {
            status = false;
            m += "❌";
         }
      }

      System.out.print(m + "\n\n");
      System.out.println((status ? "✔️  PASS" : "❌  FAIL") + " test suite status.");
   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality. false: otherwise
    */
   public static boolean testSymTableConstructor() {
      try {

         {
            SymTable s = new SymTable();
            s.print();
         }

      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }
}
