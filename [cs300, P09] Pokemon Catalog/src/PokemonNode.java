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
 * Provides a wrapper for Pokemon objects in the BST data structure used.
 * 
 * @author Safi
 */
public class PokemonNode {

  private Pokemon data; // data field of this PokemonNode
  private PokemonNode leftChild; // reference to the left child
  private PokemonNode rightChild; // reference to the right child

  /**
   * A one-argument constructor accepting a Pokemon instance field.
   * 
   * @param data a particular Pokemon instance
   * @throws IllegalArgumentException if data is null
   */
  public PokemonNode(Pokemon data) {
    if (data == null)
      throw new IllegalArgumentException("Invalid data argument passed - cannot be null");

    this.data = data;
    leftChild = rightChild = null; // initialize children nodes
  }

  /**
   * Getter left child node
   * 
   * @return a reference to the left child of this PokemonNode
   */
  public PokemonNode getLeftChild() {
    return leftChild;
  }

  /**
   * Getter right child node
   * 
   * @return a reference to the right child of this PokemonNode
   */
  public PokemonNode getRightChild() {
    return rightChild;
  }

  /**
   * Getter Pokemon data instance
   * 
   * @return a reference to the Pokemon contained in this PokemonNode
   */
  public Pokemon getPokemon() {
    return data;
  }

  /**
   * Sets the left child of this PokemonNode (null values allowed)
   * 
   * @param left child node reference
   */
  public void setLeftChild(PokemonNode left) {
    this.leftChild = left;
  }

  /**
   * Sets the right child of this PokemonNode (null values allowed)
   * 
   * @param right child node reference
   */
  public void setRightChild(PokemonNode right) {
    this.rightChild = right;
  }

}
