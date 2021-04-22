.PHONE:
all:
	cd ./src && make all

.PHONE:
server:
	cd ./src && make server

client:
	cd ./src && make client

.PHONY: clean
clean:
	cd src && make clean

.PHONY:
test-all:
	./test/run-tests.sh -c -v

.PHONY:
test-help:
	./test/run-tests.sh -h

.PHONY:
test-list: 
	./test/list-tests.sh

# .PHONY:
# valgrind: 
# 	valgrind -v --show-reachable=yes --leak-check=full ./<executable> -f dictionary odd

.PHONY:
# watch files and execute make with `make watch LABEL=<label to execute>`
# required:
# -	"cli-watch" npm package
watch:
	watch -f ./**/*.c -r "make test-all && echo '\n\n■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n\n'"

