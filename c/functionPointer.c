void add(int x, int y) {
    printf("add function");
};

void subtract(int x, int y) {
    printf("sub function");
};

void multiply(int x, int y) {
    printf("mul function");
};

// an array to function pointers (function jump table)
void (*func_pointer[])(int, int) = {
    // initialize function jump table
    subtract,
    multiply,
    add

};