# Compensated Round-Robin (CRR) scheduler
- Modifying RR CPU scheduler in xv6, implementing different time-slice lengths with compensation algorithm for time processes relinquish the CPU.
  - add variable length time intervals support for different processes. user program can setslice for its intervals
  - compensation: incentivize processes to sleep by rewarding them with ticks. Compensating processes for the amount of time they were blocked by shceduling those processes for a longer time-slice when they awake.
  - Improving the sleep/wakeup mechanism so that processes are unblocked only after their sleep interval has expired, instead of on every 10 ms timer tick.
- new process inherit time-slice of its parent process. The first user process starts with time-slice of 1 timer tick.

# xv6 specifics: 
- Control timer clock frequency `lapicw(TICR, 10000000)` @ lapic.c
    https://stackoverflow.com/questions/59276602/how-to-modify-process-preemption-policies-like-rr-time-slices-in-xv6
- Trace xv6 execution:  https://aphasiayc.github.io/2019/xv6-process.html
  - § Initialization: 
    - → ƒ main @ main.c
    - → ƒ userinit @ proc.c: 1st § process creation (shell: exec `ini` @ init.c)
    - → ƒ mpmain @ main.c: initializing CPU & running the first process using § shcedular
  - § Process creation: 
    - ƒ allocproc @ proc.c: process creation
  - § Scheduler:
    - ƒ schedular @ proc.c: pick process, setup cpu, update process state, & start running it
    - ƒ switchuvm: pick process corredponding page table
    - ƒ swtch.S: execute the process kernel thread (i.e. from kernel stack)
  - § trap timer tick or system call: 
    - → ƒ trap @ trap.c: proceeds with § system call or § timer interrupt handling
  - § Timer interrupt handling: increments ticks & updates processes status
    - → ƒ wakeup → ƒ wakup1 @ proc.c: wakes up all that waits for ticks to change (channel)
    - [force relinquish CPU]
    - → ƒ trapasm.S (trapret)
  - § System call handling:
    - → ƒ sysproc.c → ƒ @ proc.c
  - § First process execution: 
    - → ƒ forkret → ƒ trapasm.S (trapret)
  - other involved functions: sleep, exit, fork, etc.
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
  - [x] Switch back to original timer frequency, and test implementation.

## submission: 
- Files: src folder submission at `<...>/p4/slip2/src`, parterners.txt @ p4 
- partners.txt even if worked alone: `cslogin wiscNetId lastname firstname`