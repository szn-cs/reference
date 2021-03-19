# Compensated Round-Robin (CRR) scheduler
- Modifying RR CPU scheduler in xv6, implementing different time-slice lengths with compensation algorithm for time processes relinquish the CPU.
  - add variable length time intervals support. user program can setslice for its intervals
  - compensation: incentivize processes to sleep by rewarding them with ticks

# xv6 specifics: 
- main.c: ƒ main → ƒ mpmain → proc.c: ƒ scheduler
- stock xv6 scheduler: basic RR
- schedules new process on: call to yield, sleep, exit, or every 10 ms timer tick
interval.
- xv6 help command: `ctrl+a h`
- System calls defined in 5 files: syscall.h, syscall.c, sysproc.c, usys.S, user.h

# Todo list:
- [ ] xv6 book chapter 5, particularly "sleep and wake" section
- [ ] [xv6 scheduler explanation ](https://www.youtube.com/watch?v=eYfeOT1QYmg)
- [x] Makefile cpu 1 & compilation flags -Og
- implement system calls: 
  - [ ] setslice, getslice, fork2, getpinfo
- test implementation by writing user-level programs: 
  - [x] loop
  - [ ] schedtest
- Tests to consider: 
  - [ ] ensure correct scheduler policy is implemented before proceeding to provided tests. Ensuring correct ticks for each process per cycle, and having processes sleep and wakeup.
  - [ ] handle large range of jobs: run usertests and forktests


