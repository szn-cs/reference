# xv6 Memory Encryption (Kernel version) - Kernel Memory Encryption Pager
- Topics involved virtual memory system in xv6, page table entries, detect current state page, trap handler to be able to handle the page fault, to implement the clock algorithm of most refenced page.
- kernel manage page encryption and decryption.
  - [ ] The system will keep a fixed number (represted as N @param.h) of recently-accessed pages for each process stored in cleartext. Minimizing the number of page decryptions/encryptions by keeping each process's working set in cleartext (i.e. PTE_E is 0).
  - [ ] new user pages (including the program text, data, and stack pages) start as encrypted and are ONLY decrypted when accessed.
  - [ ] when a child process is created, its initial memory state (including whether or not a page is encrypted) and clock queue, should match that of its parent.
  - [ ] If a decrypted page is deallocated by the user, it should be removed from the clock queue.
  - [ ] If a process calls exec(), it starts with fresh memory, and thus the working set should be emptied.  All user-level pages should be encrypted.

# xv6 specifics: 
- [ ] [xv6 book page tables Chapters 2 and 3](https://pdos.csail.mit.edu/6.828/2018/xv6/book-rev11.pdf), traps, and interrupts of xv6

- [Control timer clock frequency](https://stackoverflow.com/questions/59276602/how-to-modify-process-preemption-policies-like-rr-time-slices-in-xv6) `lapicw(TICR, 10000000)` @ lapic.c
- xv6 help command: `ctrl+a h`
- user program defined through makefile UPROGS entry
- run programs in xv6 concurrently in background: `<user program> &; <user program> &;`

# Todo list:
- [x] set CPUS = 1 and change the compilation flag from O2 to O0 (not Og).
- [x] Download `~cs537-1/tests/p6/run-tests.sh`
- [x] Download `~cs537-1/projects/xv6.tar.gz`
- [x] Download `~cs537-1/projects/memory-kernel/ptentry.h`
- [ ] `userProgram` Example user program to test the implementation

- [ ] Implement the decryption mechanism when an encrypted page is accessed (already done in P5) and init all the user pages as the encrypted state.
    All the user pages are allocated through the function allocuvm() in vm.c, but directly encrypting all the pages in allocuvm() might not work out as you might expect. The reason is that another system call exec will call allocuvm to init those text, data, and stack pages and copy program content (e.g. program text) into it. If you do the encryption inside the allocuvm(), then you probably need to modify other functions like loaduvm() and copyout() to make sure that those pages are decrypted before the content is copied into it. Considering the difficulty you will encounter, we encourage you to do the initial memory encryption in two parts:
      1. Encrypt all the newly-allocated heap pages in growproc(). These pages are allocated by the user through syscall sbrk() which will call growproc().
      2. Encrypt all those pages set up by the exec function at the end of the exec function. These pages include program text, data, and stack pages. These pages are not allocated through growproc() and thus not handle by the first case.

- [ ] Add the clock queue mechanism. Make sure you encrypt pages when they get kicked from the queue and add pages to the queue when you decrypt them. (implement clock algorithm for page eviction) 
    In this step, you will mainly extend your memory decryption implementation. Before you do that, you will need to determine what's kind of data structure you wish to use as a queue. This data structure should be able to append elements to the tail, check the head element, and remove elements in the middle of the queue. 
        Either use: Statically-allocated linked list (e.g. P4), or Statically allocated circular array (ring buffer) as a queue, doubly linked list for easy removal. A ring buffer can be implemented using an array that representing the queue and an index pointer pointing to the head of the queue. Each method has its pros and cons that you need to deal with. So choose whatever way make you feel comfortable to implement. See TA's discussion material for more information.

    Remembering that we should remove the pages from the queue if it's deallocated. deallocuvm might be one of the candidate places to remove the pages from the queue. But you should be aware that you can't directly call myproc() to get the clock queue in deallocuvm since this function could be called by the parent process to deallocate pages for the child process. 

- [ ] Modify the corresponding code to handle the `fork()` behavior and deallocation of pages. This is mainly just queue management. 
    As we mentioned above, the child process should inherit the page table entry flags and clock queue from the parent process. You probably want to check out the fork() function in proc.c and copyuvm() in vm.c. The modification mainly involves copying extra flags and clock queue. If you are using a linked list as the queue, you should do a deep copy instead of just copying the pointer.

- [ ] statistics: implement 2 syscalls.
- [ ] checkout ring structure implementation in [C interfaces & implementations book](http://www.r-5.org/files/books/computers/languages/c/mod/David_R_Hanson-C_Interfaces_and_Implementations-EN.pdf) chapter 12.

## submission: 
- [ ] partners.txt: `cslogin1 wisclogin1 Lastname1 Firstname1`
- [ ] `~cs537-1/handin/<login>/p6/ontime/src/<xv6 files>`