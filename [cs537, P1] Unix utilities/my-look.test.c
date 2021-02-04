// custom test: 

/*  cases for argument parsing
- ./bin -V -h -f filename required
- ./bin -V -h required
- ./bin -V required
- ./bin -V
- ./bin -V required -h
- ./bin required -h



./my-look -> INVALID
./my-look word -> STDIN
./my-look -V -> -V
./my-look -h -> -h
./my-look -V -h -> -V
./my-look -f file.txt ->INVALID
./my-look -f file.txt -V -> -V
./my-look -f file.txt word -> OK
./my-look word -f file.txt -> INVALID




*/