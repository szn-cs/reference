# Compensated Round-Robin (CRR) scheduler
- Modifying RR CPU scheduler in xv6, implementing different time-slice lengths with compensation algorithm for time processes relinquish the CPU.
  - add variable length time intervals support for different processes. user program can setslice for its intervals
  - compensation: incentivize processes to sleep by rewarding them with ticks. Compensating processes for the amount of time they were blocked by shceduling those processes for a longer time-slice when they awake.
  - Improving the sleep/wakeup mechanism so that processes are unblocked only after their sleep interval has expired, instead of on every 10 ms timer tick.
- new process inherit time-slice of its parent process. The first user process starts with time-slice of 1 timer tick.

# xv6 specifics: 
- main.c: ƒ main → ƒ mpmain → proc.c: ƒ scheduler;  & sysproc.c
- involved functions: userinit, allocproc, trap, sleep, etc.
- stock xv6 scheduler: basic RR
- schedules new process (calls `sched()`): on call to yield, sleep, exit, or every 10 ms timer tick
interval.
- stock implementation of sleep syscall: forces sleeping process to wake on every timer tick to check if it has slept for the requested # of timer ticks or not. 

- xv6 help command: `ctrl+a h`
- user program defined through makefile UPROGS entry
- System calls defined in 5 files: syscall.h, syscall.c, sysproc.c, usys.S, user.h
- run programs in xv6 concurrently in background: `<user program> &; <user program> &;`

# Todo list:
- [ ] xv6 book chapter 5, particularly "sleep and wake" section
- [ ] [xv6 scheduler explanation ](https://www.youtube.com/watch?v=eYfeOT1QYmg)
- [x] Makefile cpu 1 & compilation flags -Og
- implement system calls: 
  - [ ] setslice, getslice, fork2, getpinfo
- test implementation by writing user-level programs: 
  - [x] loop
  - [x] schedtest
- Tests to consider: 
  - [ ] ensure correct scheduler policy is implemented before proceeding to provided tests. Ensuring correct ticks for each process per cycle, and having processes sleep and wakeup.
  - [ ] handle large range of jobs: run usertests and forktests
  - [ ] example: A slice = 2; B slice = 3; A sleeps for 3; should be compensated in next time-slice interval for 2 + 3 ticks. and then reverts back to its original time-slice.
  - [ ] example: proper sleep accounting: process gets compensated for the amount of time it was actually sleeping, not the time it wasn't scheduled. A slice = 2; B slice = 3; A sleeps for 1; will be compensated 2 + 1 ticks;
  - [ ] No accumulation of compensation ticks between sleeps. A slice = 2; B slice = 3; A accumulates 3 compenstation ticks, but uses only 1 and then sleeps for 1 tick; It will lose the other 2 ticks and will count only 1 compensation from the latest sleep. A compensation = 2 + 1;
  - [ ] No ready processes but sleeping/blocking process - should still acquire compensation ticks when they are blocked. A slice = 2; A runs alone and then sleeps for 3 ticks; when it runs again it will use the compensation ticks.
  - [ ] Multiple processes blocked during same time would be acquiring compensation ticks each.
  - [ ] Fix sleep syscall implementation in `wakeup1`@proc.c - avoid falsely waking up sleeping process until it is the right time.

## submission: 
- Files: src folder submission at `<...>/p4/slip1/src`, parterners.txt @ p4 
- partners.txt even if worked alone: `cslogin wiscNetId lastname firstname`