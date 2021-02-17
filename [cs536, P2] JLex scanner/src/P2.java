///////////////////////////////////////////////////////////////////////////////
// Title: P2 assignment
// Files: P2, 
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
 * P2 class: tests the ________ & related classes ________
 * 
 * <p>
 * Note: test methods tend to preserve names of corresponding methods being
 * verified & are prefixed with "test_".
 * </p>
 * 
 * @author Safi
 */
public class P2 {
	/**
	 * test driver: calls unit test methods & annotates each test with pass/fail
	 * status
	 * 
	 * @param args input arguments if any
	 */
	public static void main(String[] args) {
		boolean status = true; // test state
		String m = "———————————\nUnit Tests:\n"; // output message accumolator
		// unit test status info
		LinkedHashMap<String, Boolean> u = new LinkedHashMap<>();

		// run unit tests & record status
		// custom exception definitions
		u.put("test_exception", test_exception() ? true : false);
		// Sym class & related functionality
		u.put("test_Sym", test_Sym() ? true : false);
		// SymTable class
		u.put("test_SymTable", test_SymTable() ? true : false);
		u.put("test_addDecl", test_addDecl() ? true : false);
		u.put("test_addScope", test_addScope() ? true : false);
		u.put("test_lookupLocal", test_lookupLocal() ? true : false);
		u.put("test_lookupGlobal", test_lookupGlobal() ? true : false);
		u.put("test_removeScope", test_removeScope() ? true : false);

		// loop through unite test results & annotate output
		for (Map.Entry<String, Boolean> entry : u.entrySet()) {
			if (entry.getValue())
				m += "✔️";
			else {
				status = false;
				m += "❌";
			}
			m += "  → " + entry.getKey() + "\n";
		}

		// print test results
		System.out.println(m + "\n⇒ " + (status ? "✔️  PASS" : "❌  FAIL")
				+ " test suite status.");
	}

	/** TODO:
	 * checks the correctness of the removeScope method implemented in the
	 * SymTable class
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
