///////////////////////////////////////////////////////////////////////////////
// Main Class File: P1.java
// File: Sym.java
// Semester: CS536 Spring 2021
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// CS Login: safi@cs.wisc.edu
// Lecturer's Name: Loris D'Antoni
//
///////////////////////////////////////////////////////////////////////////////

/**
 * Sym class: represents a symbol entry in the symbol-table data structure.
 * 
 * <p>
 * symbol instances store information about each identifier token retlating to
 * its declaration or scope (e.g. name, type, location stored at runtime)
 * </p>
 * 
 * @author Safi
 */
public class Sym {
	private String type; // type of identifier (e.g. "int", "double", etc.)

	/**
	 * constructor: initializes the Sym to have the given type.
	 * 
	 * @param type of the identifier
	 */
	public Sym(String type) {
		this.type = type;
	}

	/**
	 * getter: this Sym's type.
	 * 
	 * @return identifier declaration type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * string representation: this Sym's type
	 * 
	 * @return identifier declaration type
	 */
	@Override
	public String toString() {
		return this.type;
	}

}
