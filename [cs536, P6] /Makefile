JC = javac
FLAGS = -g
CP = ./deps:.

compile: 
	cd ./src && make

test: 
	cd ./src; make test

diff: 
	cd ./src; diff test.expected test.out
	cd ./src; diff testErr.expected testErr.out

clean: 
	cd ./src; make clean; make cleantest

.PHONY:
# watch files and execute make with `make watch LABEL=<label to execute>`
# required:
# -	"cli-watch" npm package
# watch:
# 	watch -f "./**/typeErrors.cminusminus;./**/test.cminusminus" -r "(cd src; java -cp ./deps:. P5 test.cminusminus test.out) && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
# watch:
# 	watch -f "./**/typeErrors.cminusminus;./**/test.cminusminus" -r "(cd src; java -cp ./deps:. P5 typeErrors.cminusminus testErr.out) && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
watch:
	watch -f "./**/typeErrors.cminusminus;./**/test.cminusminus" -r "(cd src; java -cp ./deps:. P5 typeErrors.cminusminus testErr.out; java -cp ./deps:. P5 test.cminusminus test.out) && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"


