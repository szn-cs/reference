#include "param.h"
#include "types.h"
#include "stat.h"
#include "user.h"
#include "ptentry.h"

#define PGSIZE 4096
#define KERNBASE 0x80000000 

static int 
err(char *msg, ...) {
    printf(1, "XV6_TEST_OUTPUT %s\n", msg);
    exit();
}

void access_all_dummy_pages(char **dummy_pages, uint len) {
    for (int i = 0; i < len; i++) {
        char temp = dummy_pages[i][0];
        temp = temp;
    }
    printf(1, "\n");
}

/*
This test is basically unchanged from the P5 version. it's mainly a test of getpgtable
*/

int 
main(void){
    const uint PAGES_NUM = 5;

    const uint expected_dummy_pages_num = 4;
    // These pages are used to make sure the test result is consistent for different text pages number
    char *dummy_pages[expected_dummy_pages_num];
    
    char *buffer = sbrk(PGSIZE * sizeof(char));
    //more pages before buffer? Why? idk
    //while (buffer != (char*)0x9000) {
     //   buffer = sbrk(PGSIZE * sizeof(char));
    //}
    char *sp = buffer - PGSIZE;
    char *boundary = buffer - 2 * PGSIZE;

    //pt_entries is much larger than number of allocated pages
    struct pt_entry pt_entries[PAGES_NUM +  30];

    uint text_pages = (uint) boundary / PGSIZE;
    if (text_pages > expected_dummy_pages_num - 1)
        err("XV6_TEST_OUTPUT: program size exceeds the maximum allowed size. Please let us know if this case happens\n");
    
    for (int i = 0; i < text_pages; i++)
        dummy_pages[i] = (char *)(i * PGSIZE);
    dummy_pages[text_pages] = sp;

    for (int i = text_pages + 1; i < expected_dummy_pages_num; i++)
        dummy_pages[i] = sbrk(PGSIZE * sizeof(char));
    

    // After this call, all the dummy pages including text pages and stack pages
    // should be resident in the clock queue.
    access_all_dummy_pages(dummy_pages, expected_dummy_pages_num);

    // Bring the buffer page into the clock queue
    buffer[0] = buffer[0];
    // Allocate 5 pages of space
    char *ptr = sbrk(PAGES_NUM * PGSIZE);


    // Initialize the pages
    for (int i = 0; i < PAGES_NUM * PGSIZE; i++)
        ptr[i] = 0xAA;

     // Successfully call mencrypt on all pages
    //if (mencrypt((char *)ptr, PAGES_NUM) != 0)
    //    err("mencrypt should return 0 when [vaddr, vaddr + len * PGSIZE) resides in valid region\n");

    int expected_num_results = ((uint)(ptr+(PAGES_NUM *PGSIZE)))/PGSIZE;
    //some margin for code section?
    expected_num_results += 2;
    if (getpgtable(pt_entries, PAGES_NUM+30, 0) > expected_num_results){
        err("getpgtable returned too many results\n");

    }
    else {
        err("Passed!\n");
    }
    exit();
}
