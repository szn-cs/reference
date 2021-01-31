#include <stdio.h>

int main() {
    FILE* fp = fopen("main.c", "r");
    if (fp == NULL) {
        printf("cannot open file\n");
        exit(1);
    }

    return 0;
}