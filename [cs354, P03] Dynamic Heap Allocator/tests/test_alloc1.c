// a simple 8 byte allocation
#include <assert.h>
#include <stdlib.h>
#include "myHeap.h"

const int MEM_PAGE_SIZE = 2^10 * 4 ; // 1024*4 bytes = 4 kibi (Ki) 

int main() {
    assert(myInit(MEM_PAGE_SIZE) == 0);
    void* ptr = myAlloc(8);
    assert(ptr != NULL);
    dispMem(); 
    exit(0);
}
