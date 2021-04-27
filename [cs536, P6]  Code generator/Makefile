JC = javac
FLAGS = -g
CP = ./deps:.

compile: 
	cd ./src && make

test: 
	cd ./src; make test

clean: 
	cd ./src; make clean; make cleantest

.PHONY:
# watch files and execute make with `make watch LABEL=<label to execute>`
# required:
# -	"cli-watch" npm package
watch:
	watch -f "./**/test.cminusminus" -r "(cd src; java -cp ./deps:. P6 test.cminusminus test.s;) && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"


