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
	watch -f ./**/nameErrors.cminusminus -r "(cd src; java -cp ./deps:. P4 nameErrors.cminusminus test.out) && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
