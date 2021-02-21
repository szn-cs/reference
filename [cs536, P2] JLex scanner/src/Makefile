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
# testing
# TODO: add more here to run your tester and compare its results to expected results
###
test:
	java -cp $(CLASSPATH) P2
	diff allTokens.in allTokens.out


###
# clean up
###

clean:
	rm -f *~ *.class cminusminus.jlex.java

cleantest:
	rm -f allTokens.out

# watch files and execute make with `make watch LABEL=<label to execute>`
watch:
	# requires:
	# -	"cli-watch" npm package
	watch -f "./**/P2.java;./**/*.in;./**/*.jlex"  -r "make clean && make && make test && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
	
