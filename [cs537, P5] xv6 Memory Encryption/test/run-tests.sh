#!/bin/bash

if ! [[ -d src ]]; then
    echo "The src/ dir does not exist."
    echo "Your xv6 code should be in the src/ directory"
    echo "to enable the automatic tester to work."
    exit 1
fi

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
TEST_DIR="${DIR}/"

TMP_DIR=tmp_cs537_sp21

mkdir $TMP_DIR
cp -r src "$TMP_DIR"
cp -r "$TEST_DIR/tests" "$TMP_DIR/"
cp "$TEST_DIR/test-memory.sh" "$TMP_DIR/"
cp -r "$TEST_DIR/tester" .
chmod +x tester/run-tests.sh
chmod +x tester/run-xv6-command.exp
chmod +x tester/xv6-edit-makefile.sh
chmod +x "$TMP_DIR/src/sign.pl"

cd $TMP_DIR
#chmod +x test-getnumsyscalls.sh $*
./test-memory.sh $*
cd ../

cp -r "$TMP_DIR/tests/" .
cp -r "$TMP_DIR/tests-out" .

rm -rf $TMP_DIR
rm -rf tester/
