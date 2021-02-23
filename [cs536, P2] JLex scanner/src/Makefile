###
# This Makefile can be used to make a scanner for the C-- language
# (Yylex.class) and to make a program that tests the scanner (P2.class).
#
# The default makes both the scanner and the test program.
#
# make clean removes all generated files.
#
# Note: P2.java will not compile unless Yylex.class exists.
#
###

# define the java compiler to be used and the flags
C = javac
CLASSPATH = ./deps:.
FLAGS = -g -cp $(CLASSPATH) 

# compile tester
P2.class: P2.java Yylex.class sym.class
	$(C) $(FLAGS) P2.java

# compile lexer
Yylex.class: cminusminus.jlex.java ErrMsg.class sym.class
	$(C) $(FLAGS) cminusminus.jlex.java

# generate lexer
cminusminus.jlex.java: cminusminus.jlex sym.class
	java -cp $(CLASSPATH) JLex.Main cminusminus.jlex

sym.class: sym.java
	$(C) $(FLAGS) sym.java

ErrMsg.class: ErrMsg.java
	$(C) $(FLAGS) ErrMsg.java


###
# testing - executes java tester class, and compares input and output of each test
###
TEST_DIR = .
# TESTS = $(shell find . -iname "*.in" -exec basename {} .in ';')
# diff $(TEST_DIR)/reservedWord.in $(TEST_DIR)/reservedWord.out
# diff $(TEST_DIR)/characterSymbol.in $(TEST_DIR)/characterSymbol.out
# diff $(TEST_DIR)/identifier.in $(TEST_DIR)/identifier.out
# diff $(TEST_DIR)/stringLiteral.in $(TEST_DIR)/stringLiteral.out
# diff $(TEST_DIR)/integerLiteral.in $(TEST_DIR)/integerLiteral.out
.PHONY: test
test:
	java -cp $(CLASSPATH) P2 2> errors.out
	diff $(TEST_DIR)/allTokens.expected $(TEST_DIR)/allTokens.out
	diff $(TEST_DIR)/errors.expected $(TEST_DIR)/errors.out
	diff $(TEST_DIR)/illegalTokens.expected $(TEST_DIR)/illegalTokens.out

###
# clean up
###
.PHONY: clean
clean:
	rm -f *~ *.class cminusminus.jlex.java

.PHONY: cleantest
cleantest:
	rm -f $(TEST_DIR)/*.out 

# watch files and execute make with `make watch LABEL=<label to execute>`
.PHONY: watch
watch:
	# requires:
	# -	"cli-watch" npm package
	watch -f "./**/P2.java;./**/*.in;./**/*.jlex"  -r "make clean && make && make test && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
	
