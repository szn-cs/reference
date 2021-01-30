import java.util.*;

public class SymTable {
  private List<HashMap<String, Sym>> table; // stores symbols by scope (mapping a string
                                            // to Sym instance)

  /**
   * constructor: initializes the SymTable's List field to contain a single, empty HashMap.
   */
  SymTable() {
    /*
     * TODO: choose specific List implemention - ArrayList or LinkedList, according to SymTable
     * operations used
     */
    this.table = new ArrayList<HashMap<String, Sym>>();
    this.table.add(new HashMap<>());
  }

  /**
   * add the given name and sym to the first HashMap in the list
   * 
   * @param name
   * @param sym
   * @throws DuplicateSymException    If the first HashMap in the list already contains the given
   *                                  name as a key
   * @throws EmptySymTableException   If this SymTable's list is empty
   * @throws IllegalArgumentException If either name or sym (or both) is null
   */
  void addDecl(String name, Sym sym) throws DuplicateSymException, EmptySymTableException {

  }

  /**
   * Add a new, empty HashMap to the front of the list.
   */
  void addScope() {
    this.table.add(new HashMap<>());
  }

  /**
   * 
   * @param name
   * @return
   * @throws EmptySymTableException If this SymTable's list is empty
   */
  Sym lookupLocal(String name) {
    // Otherwise, if the first HashMap in the list contains name as a key, return the associated
    // Sym;
    // otherwise, return null.
    return new Sym(); // TODO:
  }


  /**
   * 
   * @param name
   * @return
   * 
   * @throws EmptySymTableException If this SymTable's list is empty
   */
  Sym lookupGlobal(String name) {
    // If any HashMap in the list contains name as a key, return the first
    // associated Sym (i.e., the one from the HashMap that is closest to the front of the list);
    // otherwise, return null.
    return new Sym(); // TODO:
  }


  /**
   * 
   * @throws EmptySymTableException If this SymTable's list is empty
   */
  void removeScope() {
    // otherwise, remove the HashMap from the front of the list. To clarify,
    // throw an exception only if before attempting to remove, the list is empty (i.e. there are no
    // HashMaps to remove).

  }

  /**
   * This method is for debugging
   */
  void print() {
    String output = "\nSym Table\n"; // output accumolator with required format
    for (HashMap<String, Sym> scope : this.table) {
      output += scope.toString() + "\n";
    }
    System.out.println(output);
  }
}

