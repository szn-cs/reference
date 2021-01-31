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

/**
 * DuplicateSymException checked exception defintion: duplicate entries in the
 * symbol HashMap
 * 
 * <p>
 * used by SymTable class
 * </p>
 * 
 * @author Safi
 */
public class DuplicateSymException extends Exception {
	/**
	 * constructor: create exception without a message
	 */
	public DuplicateSymException() {
		super();
	}

	/**
	 * constructor: create exception with custom error message
	 * 
	 * @param message error message
	 */
	public DuplicateSymException(String message) {
		super(message);
	}
}
