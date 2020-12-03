import java.util.NoSuchElementException;

//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: PokemonNode.java
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

/**
 * This class checks the correctness of the implementation of the methods defined in the class
 * PokemonTree.
 * 
 * @author Safi
 */
public class PokemonTreeTester {

  /**
   * Checks the correctness of the implementation of both addPokemon() and toString() methods
   * implemented in the PokemonTree class.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testAddPokemonToStringSize() {
    try {
      PokemonTree t = new PokemonTree();
      Pokemon p0 = new Pokemon("P0", "1,0,0");
      Pokemon p1 = new Pokemon("P1", "1,1,1");
      Pokemon p2 = new Pokemon("P2", "2,2,2");
      Pokemon p3 = new Pokemon("P3", "3,3,3");
      Pokemon p4 = new Pokemon("P4", "4,4,4");
      Pokemon p5 = new Pokemon("P5", "5,5,5");
      Pokemon p6 = new Pokemon("P6", "6,6,6");
      Pokemon p7 = new Pokemon("P7", "7,7,7");
      Pokemon p8 = new Pokemon("P8", "8,8,8");
      Pokemon p9 = new Pokemon("P7", "9,9,9");
      String s = ""; // aggregator of the representation of the tree

      // (1) Create a new empty PokemonTree, and check that its size is 0, it is empty, and
      // that its string representation is an empty string "".
      if (!t.isEmpty() || t.size() != 0 || !t.toString().equals(""))
        return false;
      // (2) try adding one Pokemon and then check that the addPokemon() method call returns true,
      // the tree is not empty, its size is 1, and the .toString() called on the tree returns the
      // expected output.
      if (!t.addPokemon(p5))
        return false;
      if (t.isEmpty() || t.size() != 1 || !t.toString().equals(s += p5 + "\n"))
        return false;
      // (3) Try adding another Pokemon which is more powerful than the one at the root
      if (!t.addPokemon(p6))
        return false;
      if (t.isEmpty() || t.size() != 2 || !t.toString().equals(s += p6 + "\n"))
        return false;
      // (4) Try adding a third Pokemon which is less powerful than the one at the root
      if (!t.addPokemon(p4))
        return false;
      if (t.isEmpty() || t.size() != 3 || !t.toString().equals(s = p4 + "\n" + s))
        return false;
      // (5) Try adding at least two further Pokemons such that one must be added at the left
      // subtree, and the other at the right subtree. For all the above scenarios, and more, double
      // check each time that size() method returns the expected value, the add method call returns
      // true, and that the .toString() method returns the expected string representation of the
      // contents of the binary search tree in an ascendant order from the most powerful Pokemon to
      // the least powerful one.
      if (!t.addPokemon(p3))
        return false;
      if (t.isEmpty() || t.size() != 4 || !t.toString().equals(s = p3 + "\n" + s))
        return false;
      if (!t.addPokemon(p2))
        return false;
      if (t.isEmpty() || t.size() != 5 || !t.toString().equals(s = p2 + "\n" + s))
        return false;
      if (!t.addPokemon(p7))
        return false;
      if (t.isEmpty() || t.size() != 6 || !t.toString().equals(s += p7 + "\n"))
        return false;
      if (!t.addPokemon(p8))
        return false;
      if (t.isEmpty() || t.size() != 7 || !t.toString().equals(s += p8 + "\n"))
        return false;

      // (6) Try adding a Pokemon whose CP value was used as a key for a Pokemon already stored in
      // the tree. Make sure that the addPokemon() method call returned false, and that the size of
      // the tree did not change.
      if (t.addPokemon(p5))
        return false;
      if (t.isEmpty() || t.size() != 7 || !t.toString().equals(s))
        return false;
      if (t.addPokemon(p8))
        return false;
      if (t.isEmpty() || t.size() != 7 || !t.toString().equals(s))
        return false;

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * This method checks mainly for the correctness of the PokemonTree.lookup() method.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testAddPokemonAndLookup() {
    PokemonTree t = new PokemonTree();
    Pokemon p0 = new Pokemon("P0", "1,0,0");
    Pokemon p1 = new Pokemon("P1", "1,1,1");
    Pokemon p2 = new Pokemon("P2", "2,2,2");
    Pokemon p3 = new Pokemon("P3", "3,3,3");
    Pokemon p4 = new Pokemon("P4", "4,4,4");
    Pokemon p5 = new Pokemon("P5", "5,5,5");
    Pokemon p6 = new Pokemon("P6", "6,6,6");
    Pokemon p7 = new Pokemon("P7", "7,7,7");
    Pokemon p8 = new Pokemon("P8", "8,8,8");
    Pokemon p9 = new Pokemon("P7", "9,9,9");

    try {
      // (1) Create a new PokemonTree. Then, check that calling the lookup() method with any valid
      // CP value must throw a NoSuchElementException.
      try {
        t.lookup(0);
        t.lookup(5);
        return false;
      } catch (NoSuchElementException e) {
        // Expected behavior
      }

      // (2) Consider a PokemonTree of height 3 which consists of at least 5 PokemonNodes. Then, try
      // to call lookup() method to search for the Pokemon at the root of the tree, then a Pokemon
      // at the right and left subtrees at different levels. Make sure that the lookup() method
      // returns the expected output for every method call.
      t.addPokemon(p5);
      t.addPokemon(p7);
      t.addPokemon(p6);
      t.addPokemon(p9);
      t.addPokemon(p3);
      t.addPokemon(p2);
      t.addPokemon(p4);
      if (t.lookup(p5.getCP()) != p5 || t.lookup(p7.getCP()) != p7 || t.lookup(p6.getCP()) != p6
          || t.lookup(p9.getCP()) != p9 || t.lookup(p3.getCP()) != p3 || t.lookup(p2.getCP()) != p2
          || t.lookup(p4.getCP()) != p4)
        return false;

      // (3) Consider calling .lookup() method on a non-empty PokemonTree with a CP value not stored
      // in the tree, and ensure that the method call throws a NoSuchElementException.
      try {
        t.lookup(p0.getCP());
        t.lookup(p8.getCP());
        return false;
      } catch (NoSuchElementException e) {
        // expected behavior
      }

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;

  }

  /**
   * Checks for the correctness of PokemonTree.height() method.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testHeight() {
    try {
      PokemonTree t = new PokemonTree();
      Pokemon p0 = new Pokemon("P0", "1,0,0");
      Pokemon p1 = new Pokemon("P1", "1,1,1");
      Pokemon p2 = new Pokemon("P2", "2,2,2");
      Pokemon p3 = new Pokemon("P3", "3,3,3");
      Pokemon p4 = new Pokemon("P4", "4,4,4");
      Pokemon p5 = new Pokemon("P5", "5,5,5");
      Pokemon p6 = new Pokemon("P6", "6,6,6");
      Pokemon p7 = new Pokemon("P7", "7,7,7");
      Pokemon p8 = new Pokemon("P8", "8,8,8");
      Pokemon p9 = new Pokemon("P7", "9,9,9");

      // (1) ensures that the height of an empty Pokemon tree is zero.
      if (t.height() != 0)
        return false;

      // (2) ensures that the height of a tree which consists of only one node is 1.
      t.addPokemon(p5);
      if (t.height() != 1)
        return false;

      // (3) ensures that the height of a PokemonTree with the following structure (in spec) for
      // instance is 4.
      t.addPokemon(p3);
      t.addPokemon(p4);
      t.addPokemon(p7);
      t.addPokemon(p6);
      t.addPokemon(p9);
      t.addPokemon(p8);
      if (t.height() != 4)
        return false;

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;

  }

  /**
   * Checks for the correctness of PokemonTree.getLeastPowerfulPokemon() method.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testGetLeastPowerfulPokemon() {
    try {
      PokemonTree t = new PokemonTree();
      Pokemon p0 = new Pokemon("P0", "1,0,0");
      Pokemon p1 = new Pokemon("P1", "1,1,1");
      Pokemon p2 = new Pokemon("P2", "2,2,2");
      Pokemon p3 = new Pokemon("P3", "3,3,3");
      Pokemon p4 = new Pokemon("P4", "4,4,4");
      Pokemon p5 = new Pokemon("P5", "5,5,5");
      Pokemon p6 = new Pokemon("P6", "6,6,6");
      Pokemon p7 = new Pokemon("P7", "7,7,7");
      Pokemon p8 = new Pokemon("P8", "8,8,8");
      Pokemon p9 = new Pokemon("P7", "9,9,9");

      if (t.getLeastPowerfulPokemon() != null)
        return false;

      t.addPokemon(p5);
      if (t.getLeastPowerfulPokemon() != p5)
        return false;
      t.addPokemon(p3);
      if (t.getLeastPowerfulPokemon() != p3)
        return false;
      t.addPokemon(p4);
      if (t.getLeastPowerfulPokemon() != p3)
        return false;
      t.addPokemon(p7);
      if (t.getLeastPowerfulPokemon() != p3)
        return false;
      t.addPokemon(p6);
      if (t.getLeastPowerfulPokemon() != p3)
        return false;
      t.addPokemon(p9);
      if (t.getLeastPowerfulPokemon() != p3)
        return false;
      t.addPokemon(p8);
      if (t.getLeastPowerfulPokemon() != p3)
        return false;
      t.addPokemon(p0);
      if (t.getLeastPowerfulPokemon() != p0)
        return false;

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * Checks for the correctness of PokemonTree.getMostPowerfulPokemon() method.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testGetMostPowerfulPokemon() {
    try {
      PokemonTree t = new PokemonTree();
      Pokemon p0 = new Pokemon("P0", "1,0,0");
      Pokemon p1 = new Pokemon("P1", "1,1,1");
      Pokemon p2 = new Pokemon("P2", "2,2,2");
      Pokemon p3 = new Pokemon("P3", "3,3,3");
      Pokemon p4 = new Pokemon("P4", "4,4,4");
      Pokemon p5 = new Pokemon("P5", "5,5,5");
      Pokemon p6 = new Pokemon("P6", "6,6,6");
      Pokemon p7 = new Pokemon("P7", "7,7,7");
      Pokemon p8 = new Pokemon("P8", "8,8,8");
      Pokemon p9 = new Pokemon("P7", "9,9,9");

      if (t.getMostPowerfulPokemon() != null)
        return false;

      t.addPokemon(p5);
      if (t.getMostPowerfulPokemon() != p5)
        return false;
      t.addPokemon(p3);
      if (t.getMostPowerfulPokemon() != p5)
        return false;
      t.addPokemon(p4);
      if (t.getMostPowerfulPokemon() != p5)
        return false;
      t.addPokemon(p7);
      if (t.getMostPowerfulPokemon() != p7)
        return false;
      t.addPokemon(p6);
      if (t.getMostPowerfulPokemon() != p7)
        return false;
      t.addPokemon(p9);
      if (t.getMostPowerfulPokemon() != p9)
        return false;
      t.addPokemon(p8);
      if (t.getMostPowerfulPokemon() != p9)
        return false;

    } catch (Exception e) {
      System.out.println("Unexpected Error thrown");
      return false;
    }
    return true;
  }

  /**
   * Calls the test methods
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    boolean status = true;
    if (!testAddPokemonToStringSize()) {
      System.out.println("testAddPokemonToStringSize\n");
      status = false;
    }
    if (!testAddPokemonAndLookup()) {
      System.out.println("testAddPokemonAndLookup\n");
      status = false;
    }
    if (!testHeight()) {
      System.out.println("testHeight\n");
      status = false;
    }
    if (!testGetLeastPowerfulPokemon()) {
      System.out.println("testGetLeastPowerfulPokemon\n");
      status = false;
    }
    if (!testGetMostPowerfulPokemon()) {
      System.out.println("testGetMostPowerfulPokemon\n");
      status = false;
    }
    System.out.println("Test status: " + (status ? "passing" : "failing"));
  }

}
