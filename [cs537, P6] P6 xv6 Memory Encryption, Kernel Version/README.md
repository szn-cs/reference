# xv6 Memory Encryption - Kernel version
- Topics involved virtual memory system in xv6, page table entries, detect current state page, trap handler to be able to handle the page fault, to implement the clock algorithm of most refenced page.
- 

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
- [ ] May start with P5 implementation.

## submission: 
- [ ] partners.txt: `cslogin1 wisclogin1 Lastname1 Firstname1`
- [ ] `~cs537-1/handin/<login>/p6/ontime/src/<xv6 files>`