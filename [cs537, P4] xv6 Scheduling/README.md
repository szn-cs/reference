# Compensated Round-Robin (CRR) scheduler
- Modifying RR CPU scheduler in xv6, implementing different time-slice lengths with compensation algorithm for time processes relinquish the CPU.
  - add variable length time intervals support. user program can setslice for its intervals
  - compensation: incentivize processes to sleep by rewarding them with ticks
- implement system calls setslice, getslice, fork2, getpinfo
- test implementation by writing user-level programs: loop, schedtest.

# xv6 specifics: 
- main.c: ƒ main → ƒ mpmain → proc.c: ƒ scheduler
- stock xv6 scheduler: basic RR
- schedules new process on: call to yield, sleep, exit, or every 10 ms timer tick
interval.

process

# Todo list:
- [ ] xv6 book chapter 5
- [ ] Makefile cpu 1 & compilation flags -Og


