CC = gcc 

# strict compilcation
CFLAGS = -Wall -Werror -g
CFLAGSWARN = -Wall -g

compile: mysh.o
	ln -s ./mysh.o ./mysh

mysh.o: mysh.c mysh.h message.h
	$(CC) $(CFLAGS) -o ./$@  $<

compileWarn: mysh.c mysh.h message.h
	$(CC) $(CFLAGSWARN) -o ./mysh.o $<

# chmod +x ./object/mysh
.PHONY:
run:
	./mysh.o 
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
	valgrind -v --show-reachable=yes --leak-check=full ./mysh.o -f dictionary odd

.PHONY:
# watch files and execute make with `make watch LABEL=<label to execute>`
# required:
# -	"cli-watch" npm package
watch:
	watch -f ./**/*.c -r "make test-all && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
	

