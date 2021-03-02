CC = gcc 

# strict compilcation
CFLAGS = -Wall -Werror -g 
CFLAGSWARN = -Wall -g 

compile: mysh.o

mysh.o: mysh.c mysh.h message.h sample_module.c sample_module.h
	$(CC) $(CFLAGS) -o ./$@  $< sample_module.c

compileWarn: mysh.c mysh.h message.h sample_module.c sample_module.h
	$(CC) $(CFLAGSWARN) -o ./mysh.o $< sample_module.c

# chmod +x ./object/mysh
.PHONY:
run:
	./mysh.o batch.txt

.PHONY: clean
clean:
	rm *.o 

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
	

