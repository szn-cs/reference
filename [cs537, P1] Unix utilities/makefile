CC = gcc 
CFLAGS = -Wall -Werror

compile: my-look my-rev

my-look: my-look.c
	$(CC) $(CFLAGS) my-look.c -o my-look

my-rev: my-rev.c
	$(CC) $(CFLAGS) my-rev.c -o my-rev

clean: 
	rm *.o my-look my-rev