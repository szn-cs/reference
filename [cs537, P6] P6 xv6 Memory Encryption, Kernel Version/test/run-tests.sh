#!/bin/bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
TEST_DIR="${DIR}/"

# TEST_DIR=~cs537-1/tests/p6
TMP_DIR=537_sp21_test_tmp
TEST_SCRIPT=test-memory-kernel.sh
clean_up() {
    ARG=$?

    if [ ! $ARG -eq 0 ]; then
        echo "*** clean_up"
    fi

    if [ -d $TMP_DIR ]; then
        rm -rf $TMP_DIR
    fi

    if [ -d "tester" ]; then
        rm -rf tester/
    fi
    exit $ARG
}
trap clean_up EXIT

if [ -d $TMP_DIR ]; then
    rm -rf $TMP_DIR
fi

mkdir $TMP_DIR
cp -r src $TMP_DIR
cp -r "$TEST_DIR/tests" $TMP_DIR/
cp "$TEST_DIR/$TEST_SCRIPT" $TMP_DIR/
cp -r "$TEST_DIR/tester" .
chmod +x tester/run-tests.sh
chmod +x tester/run-xv6-command.exp
chmod +x tester/xv6-edit-makefile.sh
chmod +x $TMP_DIR/src/sign.pl

cd $TMP_DIR
chmod +x $TEST_SCRIPT
./$TEST_SCRIPT $*
cd ../

cp -r $TMP_DIR/tests/ .
cp -r $TMP_DIR/tests-out .

rm -rf $TMP_DIR
rm -rf tester/
