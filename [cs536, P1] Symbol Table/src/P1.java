import java.util.*;

/**
 * P1 class: tests the SymTable & related classes - Sym
 * 
 * <p>
 * Note: test methods tend to preserve names of corresponding methods being
 * verified & are prefixed with "test_".
 * 
 * Note: "print" method is used althroughout the unit tests, which excludes it
 * from being tested.
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
    * checks the correctness of the addDecl method in the SymTable class
    * <p>
    * depends on removeScope method
    * </p>
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_addDecl() {
      try {
         SymTable table = new SymTable();
         { // key/vale entry
            table.addDecl("v1", new Sym("t1"));
            if (!table.print().equals("\nSym Table\n{v1=t1}\n"))
               return false;
            table.addDecl("v2", new Sym("t2"));
            if (!table.print().equals("\nSym Table\n{v1=t1, v2=t2}\n"))
               return false;
         }
         { // duplicate entry
            try {
               table.addDecl("v1", new Sym("t1"));
               return false;
            } catch (DuplicateSymException e) {
               // expected
            }
         }
         { // parameters validation
            try {
               table.addDecl(null, new Sym("t4"));
               return false;
            } catch (IllegalArgumentException e) {
               // expected
            }
            try {
               table.addDecl("v4", null);
               return false;
            } catch (IllegalArgumentException e) {
               // expected
            }
            try {
               table.addDecl(null, null);
               return false;
            } catch (IllegalArgumentException e) {
               // expected
            }
         }
         { // symbol-table state
            table = new SymTable();
            table.removeScope();
            try {
               table.addDecl("v3", new Sym("t3"));
               return false;
            } catch (EmptySymTableException e) {
               // expected
            }
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of the addScope implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_addScope() {
      try {
         SymTable table = new SymTable();
         { // add scope operation in order
            table.addScope();
            table.addDecl("v1", new Sym("t1"));
            if (!table.print().equals("\nSym Table\n{v1=t1}\n{}\n"))
               return false;
            table.addScope();
            table.addDecl("v2", new Sym("t2"));
            if (!table.print().equals("\nSym Table\n{v2=t2}\n{v1=t1}\n{}\n"))
               return false;
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of the lookupLocal implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_lookupLocal() {
      try {
         SymTable table = new SymTable();
         Sym symbol;
         table.addDecl("v1", new Sym("t1"));
         table.addDecl("v2", new Sym("t2"));
         table.addDecl("v3", new Sym("x")); // duplicate in another hash map
         table.addScope();
         table.addDecl("v3", new Sym("t3"));
         table.addDecl("v4", new Sym("t4"));
         { // lookup symbol by name - where list has multiple hash map
            symbol = table.lookupLocal("v3");
            if (!symbol.toString().equals("t3"))
               return false;
            symbol = table.lookupLocal("v4");
            if (!symbol.toString().equals("t4"))
               return false;
         }
         { // lookup symbol by name - where list has one hash map
            table.removeScope();
            symbol = table.lookupLocal("v1");
            if (!symbol.toString().equals("t1"))
               return false;
            symbol = table.lookupLocal("v2");
            if (!symbol.toString().equals("t2"))
               return false;
         }
         { // empty symbol-table
            table.removeScope();
            try {
               table.lookupLocal("v1");
               return false;
            } catch (EmptySymTableException e) {
               // expected
            }
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;

   }

   /**
    * checks the correctness of the lookupGlobal implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_lookupGlobal() {
      try {
         SymTable table = new SymTable();
         Sym symbol;
         table.addDecl("v0", new Sym("scope1-t0"));
         table.addDecl("v1", new Sym("t1"));
         table.addDecl("v2", new Sym("t2"));
         table.addScope();
         table.addDecl("v3", new Sym("t3"));
         table.addDecl("v4", new Sym("t4"));
         table.addDecl("v0", new Sym("scope2-t0"));
         table.addScope();
         { // lookup symbol by name - where list has multiple hash map
            symbol = table.lookupGlobal("v0");
            if (!symbol.toString().equals("scope2-t0"))
               return false;
            symbol = table.lookupGlobal("v4");
            if (!symbol.toString().equals("t4"))
               return false;
            symbol = table.lookupGlobal("v1");
            if (!symbol.toString().equals("t1"))
               return false;
         }
         { // lookup symbol by name - where list has one hash map
            table.removeScope();
            table.removeScope();
            symbol = table.lookupGlobal("v0");
            if (!symbol.toString().equals("scope1-t0"))
               return false;
            symbol = table.lookupGlobal("v1");
            if (!symbol.toString().equals("t1"))
               return false;
         }
         { // empty symbol-table
            table.removeScope();
            try {
               table.lookupGlobal("v1");
               return false;
            } catch (EmptySymTableException e) {
               // expected
            }
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

   /**
    * checks the correctness of the removeScope implemented in the SymTable class
    * 
    * @return true: verifies a correct functionality, otherwise false.
    */
   public static boolean test_removeScope() {
      try {
         SymTable table = new SymTable();
         table.addScope();
         table.addScope();
         { // remove scope operation
            table.removeScope();
            if (!table.print().equals("\nSym Table\n{}\n{}\n"))
               return false;
            table.removeScope();
            if (!table.print().equals("\nSym Table\n{}\n"))
               return false;
            table.removeScope();
            if (!table.print().equals("\nSym Table\n"))
               return false;
         }
         { // empty symbol-table
            try {
               table.removeScope();
               return false;
            } catch (EmptySymTableException e) {
               // expected
            }
         }
      } catch (Exception e) {
         System.out.println("Unexpected Error thrown.");
         return false;
      }
      return true;
   }

}
