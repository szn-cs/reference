CC = gcc 
CFLAGS = -Wall -Werror

compile: my-look my-rev

my-look: my-look.c
	$(CC) $(CFLAGS) my-look.c -o ./object/my-look

my-rev: my-rev.c
	$(CC) $(CFLAGS) my-rev.c -o ./object/my-rev

clean: 
	rm *.o ./object/*

test-all:
	./test/run-tests.sh -c

test-help:
	./test/run-tests.sh -h

test-list: 
	./test/list-tests.sh