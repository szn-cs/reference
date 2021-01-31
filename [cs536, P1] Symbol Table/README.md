_Safi Nassar - assignment p1_

# Usage:

-   `javac -d ./bytecode *.java && (cd ./bytecode && java P1)`

### Prerequisites:

-   OpenJDK 11

# Tests documentation:

## `public class P1`

P1 class: tests the SymTable & related classes - Sym

Note: test methods tend to preserve names of corresponding methods being verified & are prefixed with "test\_".

Note: "print" method is used althroughout the unit tests, which excludes it from being tested.

## `public static void main(String[] args)`

test driver: calls unit test methods & annotates each test with pass/fail status

-   Parameters: `args` — input arguments if any

## `public static boolean test_Sym()`

checks the correctness of the constructor & related getter/setter methods implemented in the Sym class

## `public static boolean test_exception()`

checks the correctness of custom checked exception classes

## `public static boolean test_SymTable()`

checks the correctness of the constructor implemented in the SymTable class

## `public static boolean test_addDecl()`

checks the correctness of the addDecl method in the SymTable class

depends on removeScope method

## `public static boolean test_addScope()`

checks the correctness of the addScope implemented in the SymTable class

## `public static boolean test_lookupLocal()`

checks the correctness of the lookupLocal implemented in the SymTable class

## `public static boolean test_lookupGlobal()`

checks the correctness of the lookupGlobal implemented in the SymTable class

## `public static boolean test_removeScope()`

checks the correctness of the removeScope implemented in the SymTable class

---

# Compiler for a C-- language

-   C-- is a simple programming language that uses Pascal identifiers.

## Terminology:

-   identifier: lexical token naming language entities (e.g. variable or function names).
-   symbol: memory instance storing information about a corresponding identifier token.

---

# To-do list:

## Assignment submission:

-   Create pdf from markdown: `pandoc README.md -o <lastname.firstname.Pn.pdf>`
    -   [generate markdown from javadoc](https://delight-im.github.io/Javadoc-to-Markdown) and remove redundant comments
        or
    -   generate javadoc to extract method headers: `find . -type f -name "*.java" | xargs javadoc -d ../javadoc`
-   Add headers for each file
-   Verify code format
-   Verify code execution on CSL machines
