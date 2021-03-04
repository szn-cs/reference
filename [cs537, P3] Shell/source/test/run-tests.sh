#!/bin/bash

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
TESTS_PATH="${DIR}"
utility="./mysh"

# if [ "$TESTS_PATH" != $(pwd) ]; then
#     rm -rf ./tests
#     rm -rf ./tester
# fi

rm -rf "${TESTS_PATH}/tests-out"

# cp -r $TESTS_PATH/tests ./tests
# cp -r $TESTS_PATH/tester ./tester
# cp $TESTS_PATH/CPPLINT.cfg ./

chmod +x "${TESTS_PATH}/tester/run-tests.sh"

echo
echo "*** Start testing $utility..."

echo
echo -e "\e[33m*** Compiler output for $utility\e[0m"
rm -f ./mysh
make compile

if [ ! -f $utility ]; then
    echo "*** ERROR: $utility not built by make command"
    exit
fi

echo
echo -e "\e[33m*** Testing output\e[0m"
"${TESTS_PATH}/tester/run-tests.sh" -c -v -d "./test/tests"

echo
echo -e "\e[33m*** Linting output\e[0m"
python ../lint/cpplint.py --root=${pwd} --extensions=c,h *.c *.h

echo
echo -e "\e[33m*** Valgrind output for $utility \e[0m"
valgrind --show-reachable=yes --leak-check=full $utility tests/19.in >/dev/null
