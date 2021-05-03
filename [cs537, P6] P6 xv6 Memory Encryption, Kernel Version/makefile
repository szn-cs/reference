CC = gcc 

# strict compilcation
# -Werror
FLAG = -Wall  -g

# exit qemu using ctrl+A X

# compile xv6
.PHONE:
qemu-nox:
	cd ./src && make qemu-nox

.PHONE:
qemu-nox-gdb: 
	cd ./src && make qemu-nox-gdb

.PHONY: clean
clean:
	cd src && make clean
	rm -r ./tests ./tests-out;

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
	

