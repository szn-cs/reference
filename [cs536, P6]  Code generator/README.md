_Safi Nassar - assignment p6_

# Usage:

-   `make clean && make testclean && make`
-   `make test`
-   `diff test.expected.s test.s`

Executing the test will generate output file of the formated program.

### Prerequisites & dependencies:

-   OpenJDK 11
-   Java Cup
-   JLex
-   LLVM
-   SPIM interpreter: for testing generated MIPS code http://pages.cs.wisc.edu/~larus/spim.html https://shawnzhong.github.io/JsSpim/

# Tests documentation:
- use the Linux utility diff to compare your file of error messages with the expected files.
- use SPIM tool to verify assembly correct execution.

---

# MIPS architecture assembly code generation for C--: 
- [x] Not required code generation for: 
  - structs or anything struct-related (like dot-accesses)
  - repeat statement
- Use Codegen.java class for generating template code.
- 


# Tasks: 
- [ ] write codeGen method for various types of AST nodes.
- [x] main program: 
  - [x] compiler phase errors. 
  - [x] code generator should wirte code to file named by 2nd cli arg.
  - [x] remove unparse call and reporting.
  - [x] initialize Codegen class's PrintWriter p with output file.
- [ ] To allow SPIM simulator to recognize main function:
  - [ ] add `__start:` to main preamble on line after `main:`
  - [ ] function exit for main: instead of `jr $ra` issue a syscall to exit with: 
        ```
        li $v0, 10
        syscall

        ```
- [x] Add to name analyzer or type checker wheather the program contians a function named main.
- [x] Add global / local differentiation
- [ ] Add a new "offset" field to the TSym class (or to the appropriate subclass(es) of TSym). Change the name analyzer to compute offsets for each function's parameters and local variables (i.e., where in the function's Activation Record they will be stored at runtime) and to fill in the new offset field. 



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
