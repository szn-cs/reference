#!/bin/bash

TESTS_PATH="/u/c/s/cs537-1/tests/p7"

if [ "$TESTS_PATH" != $(pwd) ]; then
    rm -rf ./tests
    rm -rf ./tester
fi

rm -rf ./tests-out

cp -rf $TESTS_PATH/tests ./tests
cp -rf $TESTS_PATH/tester ./tester

chmod +x tester/run-tests.sh

echo
echo "*** Start testing"

echo
echo "*** Compiler output"
make clean
make all

for expected in server client stat_process output.cgi ; do
	if [ ! -f $expected ]; then
		echo "*** ERROR: $expected not built by make command"
		exit 1
	fi
done

echo
echo "*** Testing output"

export TEST_PORT=$(( $RANDOM % 5000 + 5000 ))
export TEST_SHM_NAME=cs537-shm-$TEST_PORT-$RANDOM

echo "*** Using test port $TEST_PORT"
echo "*** Using test shm name $TEST_SHM_NAME"
tester/run-tests.sh -c $@

echo "*** Check using condition variable (no busy-waiting)"
echo "*** You MUST pass this test!"
python3 tests/cv_check.py
killall -q -u $USER -s INT server ; rm -rf /dev/shm/$TEST_SHM_NAME
