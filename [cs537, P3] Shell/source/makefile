CC = gcc 

# strict compilcation
FLAG = -Wall -Werror -g

compile: mysh

mysh: mysh.c mysh.h message.h utility.o alias.o
	$(CC) $(FLAG) -o ./$@ $< utility.o alias.o

alias.o: alias.c alias.h
	$(CC) $(FLAG) -c $<

utility.o: utility.c utility.h message.h
	$(CC) $(FLAG) -c $<

# chmod +x ./object/mysh
.PHONY:
run:
	./mysh 

# batch.txt

.PHONY: clean
clean:
	rm *.o mysh

.PHONY:
test-all:
	./test/run-tests.sh -c

.PHONY:
test-help:
	./test/run-tests.sh -h

.PHONY:
test-list: 
	./test/list-tests.sh

.PHONY:
valgrind: 
	valgrind -v --show-reachable=yes --leak-check=full ./mysh -f dictionary odd

.PHONY:
# watch files and execute make with `make watch LABEL=<label to execute>`
# required:
# -	"cli-watch" npm package
watch:
	watch -f ./**/*.c -r "make test-all && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
	

