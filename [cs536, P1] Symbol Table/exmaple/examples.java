// **********************************************************************
// This program illustrates how to
//    o open a file for reading and for writing
//    o read Strings from a file
//    o use the LinkList class
// When this program is run, it opens a file named "data"
// then reads names from that file, adding each name to a LinkedList.
// On end-of-file, it iterates through the LinkedList and writes all of the
// names to a file named "output".
// **********************************************************************

import java.io.*;
import java.util.*;

class examples {
    public static void main(String[] args) {
	String name;
	StreamTokenizer inFile = null;
	PrintWriter outFile = null;
	LinkedList list = new LinkedList();

	// Open the input file.
	// Give an error message and quit if the open fails.
	try {
	    inFile = IO.openInputFile("data");
	} catch (FileNotFoundException ex) {
	    System.err.println("The file `data' could not be opened for reading.");
	    System.exit(-1);
	}

	// Open the output file.
	// Give an error message and quit if the open fails.
	try {
	    outFile = IO.openOutputFile("output");
	} catch (IOException ex) {
	    System.err.println("The file `output' could not be opened for writing");
	    System.exit(-1);
	}

	// Read names from the input file until eof;
	// add names to LinkedList list.
	// Note: the readWord method may throw an IOException.
	try {
	    name = IO.readWord(inFile);
	    while (name != null) {
		list.addLast(name);
		name = IO.readWord(inFile);
	    }
	} catch (IOException ex) {
	    System.err.println("unexpected IOException");
	    System.exit(-1);
	}

	// Iterate through the LinkedList, writing each name to the output
	// file, one name per line.
	// Note:
	//  1. The "next" method may throw an exception,
	//     so the code that iterates through the list is inside a
	//     try block.
	//  2. The type returned by the "next" method is Object;
	//     we know that in this case it is a String (since that's
	//     what we put in the LinkedList), so we can cast the value
	//     returned by "next" to String.
	Iterator it = list.iterator();
	try {
	    while (it.hasNext()) {
		String S = ((String)it.next());
		outFile.println(S);
	    }
	} catch (NoSuchElementException ex) {
	    System.err.println("unexpected NoSuchElementException thrown");
	    System.exit(-1);
	}
	outFile.close();
    }
}
