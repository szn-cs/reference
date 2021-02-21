_Safi Nassar - assignment p2_

# Usage:

-   `javac -d ./bytecode *.java && (cd ./bytecode && java P2)`

### Prerequisites:

-   OpenJDK 11

# Tests documentation:

---

# Compiler for a C-- language: JLex scanner for C-- language

-   C-- is a simple programming language that uses Pascal identifiers, a small subset of the C++ language.
-   Assumption: the compiler lexical and syntax analysis phases would be executed in parallel, where the scanner would stream tokens to the parser for evaluation. The compiler would insert & remove current scopes from the list while the analysis process is being executed, and priority is given to the current scope (latest pushed to the list).
-   Source Language: C-- (made up language) → Host language: Java → Target language: MIPS

## Terminology:

-   .

# Assignment:

Tasks:

-   Writing the scanner in abstract level - the scanner specification (cminusminus.jlex).
-   Writing the main program (P2.java).

## Submission:

-   Create pdf from markdown: `pandoc README.md -o <lastname.firstname.Pn.pdf>`
    -   [generate markdown from javadoc](https://delight-im.github.io/Javadoc-to-Markdown) and remove redundant comments
        or
    -   generate javadoc to extract method headers: `find . -type f -name "*.java" | xargs javadoc -d ../javadoc`
-   Add headers for each file
-   Verify code format
-   Verify code execution on CSL machines

## To-do list:

-
