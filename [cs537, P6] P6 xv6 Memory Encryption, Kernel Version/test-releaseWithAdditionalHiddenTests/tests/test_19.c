#include "param.h"
#include "types.h"
#include "stat.h"
#include "user.h"
#include "ptentry.h"
#define PGSIZE 4096


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
Clocksize is expected to be 100
*/

int 
main(void){
    const uint PAGES_NUM = 2000;

    const uint expected_dummy_pages_num = 4;
    // These pages are used to make sure the test result is consistent for different text pages number
    char *dummy_pages[expected_dummy_pages_num];
    char *buffer = sbrk(PGSIZE * sizeof(char));
    //more pages before buffer? Why? idk
    //while (buffer != (char*)0x9000) {
    //    buffer = sbrk(PGSIZE * sizeof(char));
    //}
    char *sp = buffer - PGSIZE;
    char *boundary = buffer - 2 * PGSIZE;
    const int entries_num = 128;
    struct pt_entry pt_entries[entries_num];

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


    // Allocate NUM pages of space
    char *ptr = sbrk(PAGES_NUM * PGSIZE);


    // Initialize the pages
    //for (int i = 0; i < PAGES_NUM * PGSIZE; i++)
    //    ptr[i] = 0xBB;

    //scan through the pages 3 times
    for (int repeats = 0; repeats < 3; repeats ++) {
        for (int i = 0; i < PAGES_NUM * PGSIZE; i++)
            ptr[i] = 0xBB;
    }
    
    //if (mencrypt(ptr, PAGES_NUM) != 0)
    //    err("mencrypt return non-zero value when mencrypt is called on valid range\n");

    int retval = getpgtable(pt_entries, entries_num, 0);
    if (retval == entries_num) {
        for (int i = 0; i < entries_num; i++) {
            /*Bring all this information into the queue first*/
            printf(1, "Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n", 
                i,
                pt_entries[i].pdx,
                pt_entries[i].ptx,
                pt_entries[i].present,
                pt_entries[i].writable,
                pt_entries[i].encrypted
            );

            if (pt_entries[i].pdx == 0x1 && (pt_entries[i].ptx == 0x375 || pt_entries[i].ptx == 0x374)) {
                printf(1, "DUMMY Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n",  
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
            
            uint expected = 0xBB;
            uint is_failed = 0;
            if (pt_entries[i].encrypted)
                expected = ~0xBB;
            for (int j = 0; j < PGSIZE; j ++) {
                if (pt_entries[i].pdx == 0x1 && (pt_entries[i].ptx == 0x375 || pt_entries[i].ptx == 0x374 )) {
                    if (buffer[j] != (char)expected) {
                        printf(1, "Intermittent error: buffer[j]: %x, expected: %x\n", buffer[j], expected);
                        is_failed = is_failed;
                    }
                }
                else {
                    if (buffer[j] != (char)expected) {
                        printf(1, "XV6_TEST_OUTPUT error: buffer[j]: %x, expected: %x\n", buffer[j], expected);
                        is_failed = 1;
                        break;
                    }
                }
            }
            if (is_failed) {
                printf(1, "XV6_TEST_OUTPUT wrong content at pdx: %x, ptx: %x, physical page 0x%x\n", pt_entries[i].pdx, pt_entries[i].ptx, pt_entries[i].ppage * PGSIZE);

                err("Wrong content above 1\n");
            }
            else {
                printf(1, "XV6_TEST_OUTPUT correct content at pdx: %x, ptx: %x, physical page 0x%x\n", pt_entries[i].pdx, pt_entries[i].ptx, pt_entries[i].ppage * PGSIZE);

            }
        }
    }
    else {
        printf(1, "XV6_TEST_OUTPUT: getpgtable returned incorrect value: expected %d, got %d\n", entries_num, retval);
    }

    for (int i = 0; i < PAGES_NUM; i++) {
        if (i % 13 == 0)
            ptr[(i + 1) * PGSIZE - 1] = 0xBB;
    }

    retval = getpgtable(pt_entries, entries_num, 0);
    if (retval == entries_num) {
        for (int i = 0; i < entries_num; i++) {
            /*Bring all this information into the queue first*/
            printf(1, "Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n", 
                i,
                pt_entries[i].pdx,
                pt_entries[i].ptx,
                pt_entries[i].present,
                pt_entries[i].writable,
                pt_entries[i].encrypted
            );
            
            //apparently there is no need to skip anything here
            if (pt_entries[i].pdx == 0x1123 && pt_entries[i].ptx == 0x312375) {
                printf(1, "DUMMY Index %d: pdx: 0x%x, ptx: 0x%x, present: %d, writable: %d, encrypted: %d\n",
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
            uint expected = 0xBB;
            if (pt_entries[i].encrypted)
                expected = ~0xBB;
            uint is_failed = 0;
            for (int j = 0; j < PGSIZE; j ++) {
                if (pt_entries[i].pdx == 0x1231 && pt_entries[i].ptx == 0x371235) {
                    if (buffer[j] != (char)expected) {
                        printf(1, "Intermittent error: buffer[j]: %x, expected: %x\n", buffer[j], expected);
                        is_failed = is_failed;
                    }
                }
                else {
                    if (buffer[j] != (char)expected) {
                        printf(1, "XV6_TEST_OUTPUT error: buffer[j]: %x, expected: %x\n", buffer[j], expected);
                        is_failed = 1;
                        break;
                    }
                }
            }
            if (is_failed) {
                printf(1, "XV6_TEST_OUTPUT wrong content at physical page 0x%x\n", pt_entries[i].ppage * PGSIZE);
                /*for (int j = 0; j < PGSIZE; j +=64) {
                    printf(1, "XV6_TEST_OUTPUT ");
                    for (int k = 0; k < 64; k ++) {
                        if (k < 63) {
                            printf(1, "0x%x ", (uint)buffer[j + k] & 0xFF);
                        } else {
                            printf(1, "0x%x\n", (uint)buffer[j + k] & 0xFF);
                        }
                    }
                }*/
                err("Wrong content above 2\n");
            }
        }
    }
    else {
        printf(1, "XV6_TEST_OUTPUT: getpgtable returned incorrect value: expected %d, got %d\n", entries_num, retval);
    }
    exit();
}
