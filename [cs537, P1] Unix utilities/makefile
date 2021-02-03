CC = gcc 
CFLAGS = -Wall -Werror -g

compile: my-look my-rev

my-look: my-look.c
	$(CC) $(CFLAGS) $@ -o ./object/$<

my-rev: my-rev.c
	$(CC) $(CFLAGS) $@ -o ./object/$<

clean: 
	rm *.o ./object/*

test-all:
	./test/run-tests.sh -c

test-help:
	./test/run-tests.sh -h

test-list: 
	./test/list-tests.sh

valgrind: 
	valgrind --show-reachable=yes --leak-check=full ./my-diff "args"