# xv6 Memory Encryption
- 


# xv6 specifics: 
- 

- [Control timer clock frequency](https://stackoverflow.com/questions/59276602/how-to-modify-process-preemption-policies-like-rr-time-slices-in-xv6) `lapicw(TICR, 10000000)` @ lapic.c
- xv6 help command: `ctrl+a h`
- user program defined through makefile UPROGS entry
- run programs in xv6 concurrently in background: `<user program> &; <user program> &;`

# Todo list:
- [x]  set CPUS = 1 and change the compilation flag from O2 to Og
- [ ]  [xv6 Chapters 2 and 3](https://pdos.csail.mit.edu/6.828/2018/xv6/book-rev11.pdf): page tables, traps, and interrupts of xv6
- [x]  Download ptentry.h file
- [ ]  

## submission: 
- [ ] ~cs537-1/handin/<login>/p5/ontime/src/<xv6 files>
- [ ] partners.txt: `cslogin1 wisclogin1 Lastname1 Firstname1`
- [ ] if slip day: put code in corresponding slip directory.