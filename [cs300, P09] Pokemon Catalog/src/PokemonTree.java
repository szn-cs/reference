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

import java.util.NoSuchElementException;

/**
 * This class implements a binary search tree (BST) which stores a set of Pokemons. The left subtree
 * contains the Pokemons who are less powerful than the Pokemon stored at a parent node. The right
 * subtree contains the Pokemons who are more powerful than the Pokemon stored at a parent node.
 * 
 * @author Safi
 */
public class PokemonTree {
  private PokemonNode root; // root of this binary search tree
  private int size; // total number of Pokemons stored in this tree.

  /**
   * Checks whether this binary search tree (BST) is empty
   * 
   * @return true if this PokemonTree is empty, false otherwise
   */
  public boolean isEmpty() {
    return size == 0 ? true : false;
  }

  /**
   * Get the number of Pokemons stored in this BST.
   * 
   * @return the size of this PokemonTree
   */
  public int size() {
    return size;
  }

  /**
   * Recursive helper method to add a new Pokemon to a PokemonTree rooted at current.
   * 
   * @param current    The "root" of the subtree we are inserting new pokemon into.
   * @param newPokemon The Pokemon to be added to a BST rooted at current.
   * @return true if the newPokemon was successfully added to this PokemonTree, false otherwise
   */
  public static boolean addPokemonHelper(Pokemon newPokemon, PokemonNode current) {
    int comparison = current.getPokemon().compareTo(newPokemon);

    // base case: if a duplicate pokemon
    if (comparison == 0)
      return false;

    // base case: no child in target subtree
    if (comparison > 0 && current.getLeftChild() == null
        || comparison < 0 && current.getRightChild() == null) {
      // insert into the appropriate position
      if (comparison > 0)
        current.setLeftChild(new PokemonNode(newPokemon));
      else
        current.setRightChild(new PokemonNode(newPokemon));
      return true;
    }

    // reduction case: when children present
    return (comparison > 0) ? addPokemonHelper(newPokemon, current.getLeftChild())
        : addPokemonHelper(newPokemon, current.getRightChild());
  }

  /**
   * Adds a new Pokemon to this PokemonTree
   * 
   * @param newPokemon a new Pokemon to add to this BST.
   * @return true if the new was successfully added to this BST, and returns false if there is a
   *         match with this Pokemon already already stored in this BST.
   */
  public boolean addPokemon(Pokemon newPokemon) {
    if (newPokemon == null) // skip null enteries
      return false;

    if (isEmpty()) { // Add new to an empty PokemonTree
      root = new PokemonNode(newPokemon);
      size++;
      return true;
    }

    // Add new to an non-empty PokemonTree
    boolean status = addPokemonHelper(newPokemon, root); // add to node tree, and return operation
                                                         // status
    if (status)
      size++;
    return status;
  }

  /**
   * Recursive helper method which returns a String representation of the BST rooted at current. An
   * example of the String representation of the contents of a PokemonTree is provided in the
   * description of the above toString() method.
   * 
   * @param current reference to the current PokemonNode within this BST.
   * @return a String representation of all the Pokemons stored in the sub-tree PokemonTree rooted
   *         at current in increasing order with respect to the CP values. Returns an empty String
   *         "" if current is null.
   */
  public static String toStringHelper(PokemonNode current) {
    if (current == null)
      return "";

    String aggregator = ""; // build up the string
    // left subtree
    if (current.getLeftChild() != null)
      aggregator += toStringHelper(current.getLeftChild());

    aggregator += current.getPokemon().toString() + "\n";

    // right subtree
    if (current.getRightChild() != null)
      aggregator += toStringHelper(current.getRightChild());

    return aggregator;
  }

  /**
   * Get a String representation of all the Pokemons stored within this BST in the increasing order,
   * separated by a newline "\n".
   * <p>
   * For instance: "[Pikachu CP:123 (A:1 S:2 D:3)]" + "\n" + "[Eevee CP:224 (A:2 S:2 D:4)]" + "\n" +
   * [Lapras CP:735 (A:7 S:3 D:5)] + "\n" + "[Mewtwo CP:999 (A:9 S:9 D:9)]" + "\n"
   * 
   * @return a String representation of all the Pokemons stored within this BST sorted in an
   *         increasing order with respect to the CP values. Returns an empty string "" if this BST
   *         is empty.
   */
  public String toString() {
    if (isEmpty())
      return "";

    return toStringHelper(root); // call recursive helper on root tree
  }

  /**
   * Search for a Pokemon (Pokemon) given the CP value as lookup key.
   * 
   * @param cp combat power of a Pokemon
   * @return the Pokemon whose CP value equals our lookup key.
   * @throws a NoSuchElementException with a descriptive error message if there is no Pokemon found
   *           in this BST having the provided CP value
   */
  public Pokemon lookup(int cp) {
    if (isEmpty())
      throw new NoSuchElementException("No pokemon found: empty BST");
    return lookupHelper(cp, root);
  }

  /**
   * Recursive helper method to lookup a Pokemon given a reference Pokemon with the same CP in the
   * subtree rooted at current
   * 
   * @param find    a reference to a Pokemon target we are lookup for a match in the BST rooted at
   *                current.
   * @param current "root" of the subtree we are looking for a match to find within it.
   * @return reference to the Pokemon stored stored in this BST which matches find.
   * @throws NoSuchElementException with a descriptive error message if there is no Pokemon whose CP
   *                                value matches target value, stored in this BST.
   */
  public static Pokemon lookupHelper(int cp, PokemonNode current) {
    // compare cp value to current pokemon stats
    int comparison = current.getPokemon().getCP() - cp;

    // base case: current node's pokemon equals cp
    if (comparison == 0)
      return current.getPokemon();

    // base case: no further subtrees to lookup in
    if (comparison > 0 && current.getLeftChild() == null
        || comparison < 0 && current.getRightChild() == null)
      throw new NoSuchElementException("No pokemon found in BST matching the provided CP value");

    // reduction case: search in the appropriate subtrees
    return (comparison > 0) ? lookupHelper(cp, current.getLeftChild())
        : lookupHelper(cp, current.getRightChild());
  }

  /**
   * Computes and returns the height of this BST, counting the number of nodes (PokemonNodes) from
   * root to the deepest leaf.
   * 
   * @return the height of this Binary Search Tree
   */
  public int height() {
    if (isEmpty())
      return 0;

    return heightHelper(root);
  }

  /**
   * Recursive helper method that computes the height of the subtree rooted at current
   * 
   * @param current pointer to the current PokemonNode within a PokemonTree
   * @return height of the subtree rooted at current, counting the number of PokemonNodes
   */
  public static int heightHelper(PokemonNode current) {
    // base case: null subtree
    if (current == null)
      return 0;

    // reduction case: pick the largest height of subtrees
    return 1
        + Math.max(heightHelper(current.getLeftChild()), heightHelper(current.getRightChild()));
  }

  /**
   * Find the Pokemon of the least powerful Pokemon in this BST.
   * 
   * @return the Pokemon of the least powerful Pokemon in this BST and null if this tree is empty.
   */
  public Pokemon getLeastPowerfulPokemon() {
    if (isEmpty())
      return null;

    PokemonNode n = root; // current node
    while (n.getLeftChild() != null)
      n = n.getLeftChild();

    return n.getPokemon(); // pokemon at left-most position in the BST
  }

  /**
   * Find the Pokemon of the most powerful Pokemon in this BST.
   * 
   * @return the Pokemon of the most powerful Pokemon in this BST, and null if this tree is empty.
   */
  public Pokemon getMostPowerfulPokemon() {
    if (isEmpty())
      return null;

    PokemonNode n = root; // current node
    while (n.getRightChild() != null)
      n = n.getRightChild();

    return n.getPokemon(); // pokemon at right-most position in the BST
  }

}
