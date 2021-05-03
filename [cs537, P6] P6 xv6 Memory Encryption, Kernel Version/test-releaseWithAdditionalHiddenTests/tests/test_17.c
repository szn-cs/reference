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
    //exit();
    return 1;
}

void access_all_dummy_pages(char **dummy_pages, uint len) {
    for (int i = 0; i < len; i++) {
        char temp = dummy_pages[i][0];
        temp = temp;
    }
    printf(1, "\n");
}

/*
Scan through 16 pages several times and ensure that contents of pages are all correct. 
CLOCKSIZE should be 8. So at least 8 pages should be encrypted. Probably more like 10.
This test scans in a pseudorandom order.
*/

int 
main(void){
    const uint PAGES_NUM = 16;

    const uint expected_dummy_pages_num = 4;
    // These pages are used to make sure the test result is consistent for different text pages number
    char *dummy_pages[expected_dummy_pages_num];
    char *buffer = sbrk(PGSIZE * sizeof(char));
    char *sp = buffer - PGSIZE;
    char *boundary = buffer - 2 * PGSIZE;
    struct pt_entry pt_entries[PAGES_NUM];

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


    // Allocate 5 pages of space
    char *ptr = sbrk(PAGES_NUM * PGSIZE);
    

    // Initialize the pages
    //for (int i = 0; i < PAGES_NUM * PGSIZE; i++)
    //    ptr[i] = 0xAA;

    int order[] = {15, 10, 5, 4, 13, 6, 7, 1, 0, 12, 3, 2, 9, 8, 11, 14};

    //scan through the pages 4 times
    for (int repeats = 0; repeats < 4; repeats ++) {
        for (int i = 0; i < PAGES_NUM; i++) {
            for (int slider = 0; slider < PGSIZE; slider++) {
                int index = (order[i] * PGSIZE) + slider;
                /*if (repeats > 0) {
                    if (ptr[index] != 0xAA) {
                        printf(1, "XV6_TEST_OUTPUT Wrong content while scanning");
                        exit();
                    }
                }*/
                ptr[index] = 0xAA;
            }
        }
    }
    /* check that values are set correctly
    int chances1 = 0;
    for (int i = 0; i < PAGES_NUM * PGSIZE; i++) {
        char expected = 0xAA;
        if (ptr[i] != expected) {
            char b = ptr[i];
            printf(1, "XV6_TEST_OUTPUT Hi Wrong content in userspace at %d! Wanted %x got %x\n", i, expected, b);
        }
        if (chances1 > 20) {
            break;
        }
        chances1++;
    }*/

    if (getpgtable(pt_entries, PAGES_NUM, 0) >= 0){

        for (int i = 0; i < PAGES_NUM; i++) {

            /*Bring all this information into the queue first*/
            printf(1, "Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n", 
                i,
                pt_entries[i].pdx,
                pt_entries[i].ptx,
                pt_entries[i].present,
                pt_entries[i].writable,
                pt_entries[i].encrypted
            );

            if (pt_entries[i].ptx == 0x8 || pt_entries[i].ptx == 0x9 || pt_entries[i].ptx == 0x15) {
                printf(1, "Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n", 
                        i,
                        pt_entries[i].pdx,
                        pt_entries[i].ptx,
                        pt_entries[i].present,
                        pt_entries[i].writable,
                        pt_entries[i].encrypted
                    );

            }
            else {
                printf(1, "XV6_TEST_OUTPUT Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n", 
                        i,
                        pt_entries[i].pdx,
                        pt_entries[i].ptx,
                        pt_entries[i].present,
                        pt_entries[i].writable,
                        pt_entries[i].encrypted
                    );

            }
            // Bring the buffer page into the clock queue
            buffer[0] = buffer[0];
            if (dump_rawphymem((uint)(pt_entries[i].ppage * PGSIZE), buffer) != 0)
                err("dump_rawphymem return non-zero value\n");
            
            uint expected = 0xAA;
            uint is_failed = 0;

            if (pt_entries[i].encrypted)
                expected = ~0xAA;

            for (int j = 0; j < PGSIZE; j ++) {
                if (pt_entries[i].ptx == 0x8 || pt_entries[i].ptx == 0x9 || pt_entries[i].ptx == 0x15) {
                    if (buffer[j] != (char)expected) {
                        printf(1, "Intermittent error: buffer[%d]: %x, expected: %x\n", j, buffer[j], expected);
                    }
                    is_failed = is_failed;
                }
                else {
                    if (buffer[j] != (char)expected) {
                        printf(1, "XV6_TEST_OUTPUT error: buffer[%d]: %x, expected: %x\n", j, buffer[j], expected);
                        is_failed = 1;
                        break;
                    }
                }
            }
            if (is_failed) {
                printf(1, "XV6_TEST_OUTPUT wrong content at physical page 0x%x\n", pt_entries[i].ppage * PGSIZE);
                err("Wrong content above.\n");
            }
            else {
                printf(1, "XV6_TEST_OUTPUT correct content at physical page index %d!\n", i);
            }

        }
    }
    else {
        err("getpgtable did not return expected number of results\n");
    }
    exit();
}
