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
TEST_DIRECTORY = ./test
# TESTS = $(shell find . -iname "*.in" -exec basename {} .in ';')
.PHONY: test
test:
	java -cp $(CLASSPATH) P2	
	diff $(TEST_DIRECTORY)/reservedWord.in $(TEST_DIRECTORY)/reservedWord.out
	diff $(TEST_DIRECTORY)/characterSymbol.in $(TEST_DIRECTORY)/characterSymbol.out
	diff $(TEST_DIRECTORY)/identifier.in $(TEST_DIRECTORY)/identifier.out
	diff $(TEST_DIRECTORY)/stringLiteral.in $(TEST_DIRECTORY)/stringLiteral.out
	diff $(TEST_DIRECTORY)/integerLiteral.in $(TEST_DIRECTORY)/integerLiteral.out
	# diff $(TEST_DIRECTORY)/allTokens.in $(TEST_DIRECTORY)/allTokens.out

###
# clean up
###
.PHONY: clean
clean:
	rm -f *~ *.class cminusminus.jlex.java

.PHONY: cleantest
cleantest:
	rm -f $(TEST_DIRECTORY)/*.out 

# watch files and execute make with `make watch LABEL=<label to execute>`
.PHONY: watch
watch:
	# requires:
	# -	"cli-watch" npm package
	watch -f "./**/P2.java;./**/*.in;./**/*.jlex"  -r "make clean && make && make test && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
	
