
#include "param.h"
#include "types.h"
#include "stat.h"
#include "user.h"
#include "ptentry.h"

#define PGSIZE 4096

int main(void) {
    const uint PAGES_NUM = 100;

    // Allocate one pages of page3ace
    char *page3 = sbrk(PGSIZE * sizeof(char));  // new page 1
    // sbrk(PAGES_NUM * PGSIZE);  // +100

    // char *page2 = page3 - PGSIZE;
    // char *page1 = page3 - 2 * PGSIZE;
    // char *page0 = 0x0;
    // printf(1, "0: %d,\t1: %d,\t2: %d,\t3: %d\n", page0, page1, page2, page3);

    // using pages
    // uint text_pages = (uint)page1 / PGSIZE;  // 1
    // for (int i = 0; i < text_pages; i++) page0[i * PGSIZE] = page0[i *
    // PGSIZE]; page2[0] = page2[0] + 1;
    page3[0] = page3[0];

    int expected_pages_num = 3;

    struct pt_entry pt_entry[PAGES_NUM];
    int retval = getpgtable(pt_entry, 100, 1);
    if (retval != expected_pages_num) {
        printf(1,
               "XV6_TEST_OUTPUT: getpgtable returned incorrect value:"
               "expected"
               "%d, got %d\n",
               expected_pages_num, retval);
        exit();
    }
    printf(1, "XV6_TEST_OUTPUT PASS!\n");
    exit();
}

// ///////////////////////////////////////////////
// #include "param.h"
// #include "types.h"
// #include "stat.h"
// #include "user.h"
// #include "ptentry.h"

// #define PGSIZE 4096

// void print(struct pt_entry *pt_entry) {
//     printf(1,
//            "pdx: 0x%x, ptx: 0x%x, "
//            "present: %d, writable: %d, encrypted: %d user: %d ref: %d \n",
//            pt_entry->pdx, pt_entry->ptx, pt_entry->present,
//            pt_entry->writable, pt_entry->encrypted, pt_entry->user,
//            pt_entry->ref);
// }

// int main(void) {
//     printf(1, "before sbrk");
//     char *ptr = sbrk(PGSIZE);  // Allocate one page
//     printf(1, "after sbrk");

//     struct pt_entry pt_entry;
//     printf(1, "after pt_entry");

//     // Get the page table information for newly allocated page
//     // and the page should be encrypted at this point
//     getpgtable(&pt_entry, 1, 0);
//     print(&pt_entry);

//     printf(1, "before ptr[0]");
//     ptr[0] = 0x0;
//     printf(1, "after ptr[0]");

//     // The page should now be decrypted and put into the clock queue
//     getpgtable(&pt_entry, 1, 1);
//     ptr[0] = 0x0;
//     print(&pt_entry);
//     print(&pt_entry);

//     ptr = sbrk(PGSIZE);  // Allocate one page
//     ptr[1] = 0x0;
//     getpgtable(&pt_entry, 1, 0);
//     print(&pt_entry);

//     ptr = sbrk(PGSIZE);  // Allocate one page
//     ptr[2] = 0x0;
//     getpgtable(&pt_entry, 1, 0);
//     print(&pt_entry);

//     ptr = sbrk(PGSIZE);  // Allocate one page
//     ptr[3] = 0x0;
//     getpgtable(&pt_entry, 1, 0);
//     print(&pt_entry);

//     ptr = sbrk(PGSIZE);  // Allocate one page
//     ptr[4] = 0x0;
//     getpgtable(&pt_entry, 1, 0);
//     print(&pt_entry);

//     ptr = sbrk(PGSIZE);  // Allocate one page
//     ptr[4] = 0x0;
//     getpgtable(&pt_entry, 1, 0);
//     print(&pt_entry);

//     exit();
// }
