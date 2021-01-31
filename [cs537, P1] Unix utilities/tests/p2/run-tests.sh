TEST_DIR=~cs537-1/tests/p2
TMP_DIR=537_sp21_test_tmp

mkdir $TMP_DIR
cp -r $TEST_DIR/tests $TMP_DIR/
cp $TEST_DIR/test-getnumsyscalls.sh $TMP_DIR/
cp -r $TEST_DIR/tester .
cp -r src $TMP_DIR
chmod +x tester/run-tests.sh
chmod +x tester/run-xv6-command.exp
chmod +x tester/xv6-edit-makefile.sh
chmod +x $TMP_DIR/src/sign.pl

cd $TMP_DIR
chmod +x test-getnumsyscalls.sh $*
./test-getnumsyscalls.sh
cd ../

cp -r $TMP_DIR/tests/ .
cp -r $TMP_DIR/tests-out .

rm -rf $TMP_DIR
rm -rf tester/
