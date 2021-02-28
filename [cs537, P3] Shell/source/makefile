CC = gcc 

# strict compilcation
CFLAGS = -Wall -Werror -g 


compile: mysh 
	echo "compilation completed"

mysh: mysh.c sample_module.c sample_module.h
	mkdir -p ./object/
	$(CC) $(CFLAGS) -o ./object/$@  $< sample_module.c

.PHONY: clean
clean:
	mkdir -p ./object
	rm *.o ./object/* 

test-all:
	./test/run-tests.sh -c

test-help:
	./test/run-tests.sh -h

test-list: 
	./test/list-tests.sh

valgrind: 
	valgrind -v --show-reachable=yes --leak-check=full ./my-look -f dictionary odd

# watch files and execute make with `make watch LABEL=<label to execute>`
watch:
	# required:
	# -	"cli-watch" npm package
	watch -f ./**/*.c -r "make test-all && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"
	

