_Safi Nassar - assignment p6_

# Usage:

-   `make clean && make testclean && make`
-   `make test`
-   `diff testErr.expected testErr.out`
-   `diff test.expected test.out`

Executing the test will generate output file of the formated program.

### Prerequisites & dependencies:

-   OpenJDK 11
-   Java Cup
-   JLex
-   LLVM
-   SPIM interpreter: for testing generated MIPS code http://pages.cs.wisc.edu/~larus/spim.html

# Tests documentation:
- use the Linux utility diff to compare your file of error messages with the expected files.

---

# MIPS architecture assembly code generation for C--: 
- Not required code generation for: 
  - structs or anything struct-related (like dot-accesses)
  - repeat statement
- Use Codegen.java class for generating template code.
- 


# Tasks: 
- [ ] write codeGen method for various types of AST nodes.
- [ ] main program: 
  - [ ] compiler phase errors. 
  - [ ] code generator should wirte code to file named by 2nd cli arg.
  - [ ] remove unparse call and reporting.
  - [x] initialize Codegen class's PrintWriter p with output file.
- [ ] 
- [ ] 

## Submission:
  - [ ] Create pdf from markdown: `pandoc README.md -o <lastname.firstname.Pn.pdf>`
      - [ ] [generate markdown from javadoc](https://delight-im.github.io/Javadoc-to-Markdown) and remove redundant comments
        or
      - [ ] generate javadoc to extract method headers: `find . -type f -name "*.java" | xargs javadoc -d ../javadoc`
  - [ ] Add headers for each file
  - [ ] Verify code format
  - [ ] Verify code execution on CSL machines
- [ ] lastname.firstname.lastname.firstname.P6.zip
+---+ deps/
+---+ ast.java
+---+ cminusminus.cup
+---+ cminusminus.jlex
+---+ Codegen.java
+---+ DuplicateSymException.java
+---+ EmptySymTableException.java
+---+ ErrMsg.java
+---+ Makefile
+---+ P6.java
+---+ TSym.java
+---+ SymTable.java
+---+ Type.java
+---+ lastname.firstname.lastname.firstname.P6.pdf
