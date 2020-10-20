// a simple 8 byte allocation
#include <assert.h>
#include <stdlib.h>
#include "myHeap.h"

const int MEM_PAGE_SIZE = 2^10 * 4 ; // 1024*4 bytes = 4 kibi (Ki) 

int main() {
    assert(myInit(MEM_PAGE_SIZE) == 0);
    dispMem(); 
    void* ptr = myAlloc(8);
    dispMem(); 
    assert(ptr != NULL);
    exit(0);
}
