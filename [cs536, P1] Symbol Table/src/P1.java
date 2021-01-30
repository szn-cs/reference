import java.util.*;

/**
 * P1 class: tests the SymTable & related classes - Sym
 * <p>
 * Note: test methods tend to preserve names of corresponding methods being
 * verified & are prefixed with "test_".
 * </p>
 * 
 * @author Safi
 */
public class P1 {
   /**
    * test driver: calls unit test methods & annotates each test with pass/fail
    * status
    * 
    * @param args input arguments if any
    */
   public static void main(String[] args) {
      boolean status = true; // test state
      String m = "———————————\nUnit Tests:\n"; // output message accumolator
      HashMap<String, Boolean> u = new HashMap<>(); // unit test status info

      // run unit tests & record status
      { // custom exception definitions
         u.put("test_exception", test_exception() ? true : false);
      }
      { // Sym class & related functionality
         u.put("test_Sym", test_Sym() ? true : false);
      }
      { // SymTable class
         u.put("test_SymTable", test_SymTable() ? true : false);
         u.put("test_addDecl", test_addDecl() ? true : false);
         u.put("test_addScope", test_addScope() ? true : false);
         u.put("test_lookupLocal", test_lookupLocal() ? true : false);
         u.put("test_lookupGlobal", test_lookupGlobal() ? true : false);
         u.put("test_removeScope", test_removeScope() ? true : false);
         u.put("test_print", test_print() ? true : false);
      }

      // loop through unite test results
      for (Map.Entry<String, Boolean> entry : u.entrySet()) {
         if (entry.getValue())
            m += "✔️";
         else {
            status = false;
            m += "❌";
         }
         m += "  → " + entry.getKey() + "\n";
      }

      System.out.println(m + "\n⇒ " + (status ? "✔️  PASS" : "❌  FAIL") + " test suite status.");
   }

   /**
    * checks the correctness of the constructor & related getter/setter methods
    * implemented in the Sym class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_Sym() {
      try {
         Sym symbol;
         { // instance instantiation must not throw
            symbol = new Sym("integer");
         }
         { // getType method
            String type = symbol.getType();
            if (!type.equals("integer"))
               return false;
         }
         { // toString method
            String string = symbol.toString();
            if (!string.equals("integer"))
               return false;
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of custom checked exception classes
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_exception() {
      try {
         { // construction custom exceptions must not throw
           // without message
            new EmptySymTableException();
            new DuplicateSymException();
            // with message
            new EmptySymTableException("Message");
            new DuplicateSymException("Message");
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_SymTable() {
      try {
         SymTable s;
         { // constuction of symbol-table must not throw
            s = new SymTable();
         }
         { // must contain a default HashMap element
            String output = s.print();
            if (!output.equals("\nSym Table\n{}\n"))
               return false;
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_addDecl() {
      try {
         {

         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_addScope() {
      try {

      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;

   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_lookupLocal() {
      try {

      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;

   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_lookupGlobal() {
      try {

      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;

   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_removeScope() {
      try {

      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;

   }

   /**
    * checks the correctness of the constructor implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_print() {
      try {
         
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;

   }

}
