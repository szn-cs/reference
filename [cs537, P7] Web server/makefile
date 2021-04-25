.PHONY:
all:
	cd ./src && make all

.PHONY:
server:
	cd ./src && make server

client:
	cd ./src && make client

.PHONY: clean
clean:
	cd src && make clean

.PHONY:
runServer: 
	cd src && ./server 8080 1 1 x

.PHONY:
runClient: 
	cd src && ./client localhost 8080 /

.PHONY:
test-all:
	(cd src && ./test/run-tests.sh -c -v)

.PHONY:
test-help:
	./src/test/run-tests.sh -h

.PHONY:
test-list: 
	./src/test/list-tests.sh

.PHONY: 
clean-test: 
	(cd src && rm -r tester tests tests-out)

# .PHONY:
# valgrind: 
# 	valgrind -v --show-reachable=yes --leak-check=full ./<executable> -f dictionary odd

.PHONY:
# watch files and execute make with `make watch LABEL=<label to execute>`
# required:
# -	"cli-watch" npm package
watch:
	watch -f ./**/*.c -r "make test-all && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"

