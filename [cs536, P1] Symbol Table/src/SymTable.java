///////////////////////////////////////////////////////////////////////////////
// Main Class File: P1.java
// File: SymTable.java
// Semester: CS536 Spring 2021
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// CS Login: safi@cs.wisc.edu
// Lecturer's Name: Loris D'Antoni
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;

/**
 * SymTable: symbol-table data structure implementation
 * 
 * <p>
 * each HashMap in the list stores symbols corresponding to specific scope in
 * the compiled program.
 * </p>
 */
public class SymTable {
	// stores symbols by scope, mapping identifier name to its associated
	// information
	private List<HashMap<String, Sym>> table;

	/**
	 * constructor: initializes the SymTable's List field to contain a single,
	 * empty HashMap.
	 */
	public SymTable() {
		/* Note: LinkedList used for efficient insert/remove oprerations */
		this.table = new LinkedList<HashMap<String, Sym>>();
		this.table.add(new HashMap<>());
	}

	/**
	 * add the given name and sym to the first HashMap in the list
	 * 
	 * @param name identifier name corresponds to the hash map key
	 * @param sym  identifier's info instance corresponds to hash map value
	 * @throws EmptySymTableException   If this SymTable's list is empty
	 * @throws IllegalArgumentException If either name or sym (or both) is null
	 * @throws DuplicateSymException    If the first HashMap in the list already
	 *                                  contains the given name as a key
	 */
	public void addDecl(String name, Sym sym)
			throws DuplicateSymException, EmptySymTableException {
		// validate symbol-table state
		if (this.table.isEmpty())
			throw new EmptySymTableException();
		// validate parameters
		if (name == null || sym == null)
			throw new IllegalArgumentException("parameters must not be null");
		if (this.table.get(0).containsKey(name))
			throw new DuplicateSymException();

		this.table.get(0).put(name, sym);
	}

	/**
	 * add a new, empty HashMap to the symbol-table at the front
	 */
	public void addScope() {
		this.table.add(0, new HashMap<>());
	}

	/**
	 * retrieve symbol by identifier name (key) from first/front hash map
	 * element
	 * 
	 * @param name identifier name corresponds to the hash map key
	 * @return matching symbol, otherwise null.
	 * @throws EmptySymTableException If this SymTable's list is empty
	 */
	public Sym lookupLocal(String name) throws EmptySymTableException {
		if (this.table.isEmpty())
			throw new EmptySymTableException();

		return this.table.get(0).containsKey(name) ? this.table.get(0).get(name)
				: null;
	}

	/**
	 * retrieve symbol by identifier name (key) from any hash map in the
	 * symbol-table
	 * 
	 * @param name identifier name corresponds to the hash map key
	 * @return matching symbol, otherwise null.
	 * 
	 * @throws EmptySymTableException If this SymTable's list is empty
	 */
	public Sym lookupGlobal(String name) throws EmptySymTableException {
		if (this.table.isEmpty())
			throw new EmptySymTableException();

		for (HashMap<String, Sym> element : this.table)
			if (element.containsKey(name))
				return element.get(name);

		return null;
	}

	/**
	 * remove the HashMap from the front of the list
	 * 
	 * @throws EmptySymTableException If this SymTable's list is empty
	 */
	public void removeScope() throws EmptySymTableException {
		if (this.table.isEmpty())
			throw new EmptySymTableException();

		this.table.remove(0);
	}

	/**
	 * print symbol-table current status for debugging purposes
	 * 
	 * @return string representation of the symbol-table for debugging purposes
	 */
	public String print() {
		String output = "\nSym Table\n"; // output accumolator with required
											// format
		for (HashMap<String, Sym> scope : this.table) {
			output += scope.toString() + "\n";
		}
		System.out.println(output); // (as per specification)
		return output;
	}
}
