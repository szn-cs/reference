import java.util.*;

// must be implemented using a List of HashMaps - Think about the operations that will be done on a SymTable to decide whether to use an ArrayList or a LinkedList
public class SymTable {
 // HashMaps must map a String to a Sym
 private List<HashMap<String,Sym>>; 

//  SymTable()	This is the constructor; it should initialize the SymTable's List field to contain a single, empty HashMap.

// void addDecl(String name, Sym sym) throws DuplicateSymException, EmptySymTableException	If this SymTable's list is empty, throw an EmptySymTableException. If either name or sym (or both) is null, throw a IllegalArgumentException. If the first HashMap in the list already contains the given name as a key, throw a DuplicateSymException. Otherwise, add the given name and sym to the first HashMap in the list.

// void addScope()	Add a new, empty HashMap to the front of the list.

// Sym lookupLocal(String name)	If this SymTable's list is empty, throw an EmptySymTableException. Otherwise, if the first HashMap in the list contains name as a key, return the associated Sym; otherwise, return null.

// Sym lookupGlobal(String name)	If this SymTable's list is empty, throw an EmptySymTableException. If any HashMap in the list contains name as a key, return the first associated Sym (i.e., the one from the HashMap that is closest to the front of the list); otherwise, return null.

// void removeScope() throws EmptySymTableException	If this SymTable's list is empty, throw an EmptySymTableException; otherwise, remove the HashMap from the front of the list. To clarify, throw an exception only if before attempting to remove, the list is empty (i.e. there are no HashMaps to remove).

// void print()	This method is for debugging. First, print “\nSym Table\n”. Then, for each HashMap M in the list, print M.toString() followed by a newline. Finally, print one more newline. All output should go to System.out.

}

