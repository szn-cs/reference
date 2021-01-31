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
 * EmptySymTableException checked exception defintion: empty symbol-table data
 * structure.
 * 
 * <p>
 * used by SymTable class
 * </p>
 * 
 * @author Safi
 */
public class EmptySymTableException extends Exception {
	/**
	 * constructor: create exception without a message
	 */
	public EmptySymTableException() {
		super();
	}

	/**
	 * constructor: create exception with custom error message
	 * 
	 * @param message error message
	 */
	public EmptySymTableException(String message) {
		super(message);
	}
}
