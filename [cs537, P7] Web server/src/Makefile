# (NOTE: for debugging and development I use utilities wrapped in a repository with a separate makefile configuration, which won't be submitted with the assignment.)

SERVER_OBJS = server.o request.o helper.o
CLIENT_OBJS = client.o helper.o

CC = gcc
CFLAGS = -g -Werror -Wall -Wno-format-overflow -Wno-restrict 
LIBS = -lpthread -lrt 

.SUFFIXES: .c .o 

all: server client output.cgi stat_process

# create just the server program
server: $(SERVER_OBJS)
	$(CC) $(CFLAGS) -o server $(SERVER_OBJS) $(LIBS)

client: $(CLIENT_OBJS)
	$(CC) $(CFLAGS) -o client $(CLIENT_OBJS) $(LIBS)

# compile statistics process executables
stat_process: stat_process.o
	$(CC) $(CFLAGS) -o stat_process stat_process.o $(LIBS)

output.cgi: output.c
	$(CC) $(CFLAGS) -o output.cgi output.c

.c.o:
	$(CC) $(CFLAGS) -o $@ -c $<

.PHONY:
clean:
	-rm -f $(SERVER_OBJS) $(CLIENT_OBJS) stat_process.o stat_process server client output.cgi
