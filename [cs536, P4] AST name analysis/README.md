_Safi Nassar - assignment p4_

# Usage:

-   `make clean && make testclean && make`
-   `make test`

Executing the test will generate output file of the formated program.

### Prerequisites & dependencies:

-   OpenJDK 11
-   Java Cup
-   JLex

# Tests documentation:


---

# Name analyzer for C-- programs:
- Programs are represented as ASTs. 
- Name analyzer functionality: 
  1. Build scope symbol tables (using SymTable class)
  2. Detect static semantic name declaration & usage errors
  3. Add AST IdNode links to corresponding symbol-table entry: for nodes representing a use of name.

## C-- names & scoping rules: 
  - Allows same name declarations in non-overlapping/nested scopes.
  - Uses of undeclared names - names must be declared before they are used.
  - Duplicate/Multiply declared names

### variable declaration
  - Bad declaration: 
    - type void declaration for non function
    - variable of bad struct type - i.e. the name of the struct type doesn't exist or is not struct type.
  - If you find a bad variable declaration (a variable of type void or of a bad struct type), give an error message and add nothing to the symbol table.

### struct
- If a variable or a function with the same name has been declared in the same scope before, then do not add a SymTable entry for the struct. You don't have to process the variables of the struct in this case.
- In case a duplicate filed, create SymTable for the struct and add all variables up to but excluding the second occurrence of x and then continue with the rest of the members.
- If a struct is used without declaration like a.b, then you can report two errors (undeclared ID and dot access of non-struct type) or you can just report undeclared ID.
- a struct and one of its members can have the same name (as struct type declaration is of scope one level outside the struct)
- struct handling issues: 
  - Bad struct access: name analysis `LHS.RHS` should check:
    - left-hand side of the dot-access is not a name already declared to be of a struct type
    - right-hand side of the dot-access is not the name of a field for the appropriate type of struct. 
  - struct definition: 
    - name of the struct type can't be a name that has already been declared.
    - A variable x inside a struct with the same name as another variable inside the struct is illegal (fields of a struct must be unique to that particular struct). 
    - A variable inside a struct with the same name as a variable or a function outside the struct is legal. 
  - struct declaration: on variable declaration that is a struct - check struct type has been previously declared and is actually the name of a struct type.

### function
- Formal parameters are on same scope as function body.
- A function with the same name as another function in the same scope is illegal. You must not add a new SymTable entry in the outer scope for this second occurrence. You should process the formals and the local variables for both the functions.
- A function with the same name as another variable in the same scope is illegal. In this case, do not create a SymTable entry for the function. However, continue processing the body of the function.
- If a function with formal parameter a also has a variable declared as a, then create the SymTable for the function and add the formal parameter but not the local variable and then continue with processing.
- If a function has 2 formal parameters or 2 local variables with the same name, then create the SymTable, add the first parameter/local variable, report the error and then continue with processing.
- The name of the function is in a scope that is one level outside the scope of the function itself. Thus, a function and one of its formals/local variables can have the same name.
- During name analysis, if a function name is multiply declared you should still process the formals and the body of the function; don't add a new entry to the current symbol table for the function, but do add a new hashtable to the front of the SymTable's list for the names declared in the body (i.e., the parameters and other local variables of the function).

### if/else/while
- if/else and while statements have their own scope. So, names can be reused inside these statements.
- The if part and the else part have different scopes. So, the same name can be declared in both of them.

## Tasks
- [ ] Modify the TSym class (by including some new fields and methods and/or by defining some subclasses).
- [ ] Modify the IdNode class in ast.java (by including a new TSym field and by modifying its unparse method). Free to make any changes to ast.java
- [ ] Modify P4.java (an extension of P3.java).
- [ ] Modify the ErrMsg class.
- [ ] Write two test inputs: nameErrors.cminusminus and test.cminusminus to test your new code.
- [ ] TODO: name analyzer will use symbol table's globalLookup method to set IdNode's symbol table link.
- [ ] name analyzer implemented by writing appropriate methods for the different subclasses of ASTnode. Exactly what methods you write is up to you (as long as they do name analysis as specified).
  - [ ] Some nodes's methods need to add a new hashtable (scope enter) or remove hashtable (scope exit) from symbol table.
  - [ ] Some node's methods will process declarations - checking for name errors, and adding appropriate symbol table enteries.
  - [ ] Some node's methods process statements in the program - checking declarations for each used name, and adding links for used names IdNodes of those that were declared.
- [ ] Handling errors: Your compiler should quit after the name analyzer has finished if any errors have been detected so far (either by the scanner/parser or the name analyzer). To accomplish this, you can add a static boolean field to the ErrMsg class that is initialized to false and is set to true if the fatal method is ever called (warnings should not change the value of this field). Your main program can check the value of this field and only call the unparser if it is false.
- [ ] Traversal of AST should be similar to unparsing traversal implemented using `unparse` method. Only involved nodes should be traversed, and their parent classes should have an abstract methods to implement on all their subclasses (e.g. TypeNode subclasses should not be).
  - [ ] declare an abstract name-analysis method for the DeclNode class, because the method for the DeclListNode class will call that method for each node in the list.



## Submission:

- [ ] lastname.firstname.lastname.firstname.P4.zip
+---+ deps/
+---+ ast.java
+---+ cminusminus.cup
+---+ cminusminus.jlex
+---+ DuplicateSymException.java
+---+ EmptySymTableException.java
+---+ ErrMsg.java
+---+ Makefile
+---+ P4.java
+---+ TSym.java
+---+ SymTable.java
+---+ nameErrors.cminusminus
+---+ test.cminusminus
+---+ lastname.firstname.lastname.firstname.P4.pdf
