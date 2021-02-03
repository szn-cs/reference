#!/bin/bash

# this file's directory path
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

# TESTS_PATH="/u/c/s/cs537-1/tests/p1"
TESTS_PATH="${DIR}"

TMP_DIR="tests-tmp"
OUT_DIR="tests-out"

rm -rf $TMP_DIR
rm -rf $OUT_DIR

echo "$DIR"
echo "$TMP_DIR"
echo "$OUT_DIR"

mkdir -p "${TMP_DIR}" && mkdir -p "${OUT_DIR}" && cp -a "${TESTS_PATH}/." "${TMP_DIR}"

chmod +x "${TMP_DIR}/tester/run-tests.sh"

for utility in my-look my-rev; do
    echo
    echo "*** Start testing the $utility utility..."
    if [ ! -f $utility.c ]; then
        echo "*** ERROR: $utility.c does not exist"
    else
        echo
        echo "*** Linting output for $utility"
        python ./lint/cpplint.py --root="$pwd" --extensions=c,h $utility.c

        echo
        echo "*** Compiler output for $utility"
        gcc -o $utility ./object/$utility.c -Wall -Werror

        echo
        echo "*** Valgrind output for $utility"
        if [ $utility = "my-look" ]; then
            valgrind --show-reachable=yes ./$utility -f my-look.c '#' >/dev/null
        else
            valgrind --show-reachable=yes ./$utility -f my-rev.c >/dev/null
        fi

        echo
        echo "*** Testing output for $utility"
        ./$TMP_DIR/tester/run-tests.sh -d $TMP_DIR/$utility -o $OUT_DIR/$utility $*
    fi
done

echo
echo "*** Start testing pipe..."
if [ ! -f my-look ] | [ ! -f my-rev ]; then
    echo "*** ERROR: my-look or my-rev does not exist"
else
    echo
    echo "*** Testing output for pipe"
    ./$TMP_DIR/tester/run-tests.sh -d $TMP_DIR/pipe -o $OUT_DIR/pipe $*
fi
