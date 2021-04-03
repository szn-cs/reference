_Safi Nassar - assignment p5_

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

# Type checking for C--: 
	• primitive types: int, bool, string, void
	• Type constructors: struct, functions
	• Limited coercion: bool cannot be used as int, & vice-versa.
	• Type errors: 
		○ Arithmetic operators & relational operators must have int operands. 
		○ Equality operators (==, !=) & Assignment operator (=) must have same type operands, and cannot be applied to function names, struct names or variables.
		○ logical operators must have bool operands.
		○ input/output operators (cin >> x, cout << x) x cannot be function name, struct name or variable
		○ if condition must be a bool.
		○ function invocation:
			§ on non-functions is not allowed 
			§ not allowed: wrong number of args, wrong types of args, struct or functions not allowed as args.
			§ must return value of correct type. and not returning value at all or returning the wrong type in a non-void function. 
	• Type checking implementation: 
		○ add typeCheck method to AST nodes - walk the AST checking types of sub-expressions.
		○ for base nodes (e.g. IntLitNode) just return their type.
		○ for IdNode: lookup the type of the declaration in the symbol table (linked field), and propagate up the type.
		○ Algorithm:  get LHS & RHS types, check types compatibility for operator, then set the kind of node be a value, then set type of node to be the type of the operation's result.
		○ function calls: get type of each actual argument, match against the formal argument (check symbol), propagate the return type.
	• to prevent duplicate error reporting: introduce an internal error type: 
		○ when type incompatibility is discovered then report the error, and propagate the type up the recursive calling. 
        ○ when an error type is encountered as an operand: don't re-report an error, only propagate the error type up the calling chain.

## Tasks


## Submission:
  - [ ] Create pdf from markdown: `pandoc README.md -o <lastname.firstname.Pn.pdf>`
      - [ ] [generate markdown from javadoc](https://delight-im.github.io/Javadoc-to-Markdown) and remove redundant comments
        or
      - [ ] generate javadoc to extract method headers: `find . -type f -name "*.java" | xargs javadoc -d ../javadoc`
  - [ ] Add headers for each file
  - [ ] Verify code format
  - [ ] Verify code execution on CSL machines
- [ ] lastname.firstname.lastname.firstname.P5.zip
