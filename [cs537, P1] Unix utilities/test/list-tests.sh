#!/bin/bash

# this file's directory path
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# TESTS_PATH="/u/c/s/cs537-1/tests/p1"
TESTS_PATH="$DIR"

for utility in my-look my-rev pipe; do
    echo "*** Tests for $utility:"

    # for testnum in $( ls "$TESTS_PATH/p1-testing/$utility/tests/*.desc -v" ); do
    for testdesc in $( ls "${TESTS_PATH}/${utility}" -v | grep ".desc" ); do

        # echo -n $testdesc
        echo -n "  $testdesc - "
        echo $(head -1 "$TESTS_PATH/$utility/$testdesc")
    done
done
