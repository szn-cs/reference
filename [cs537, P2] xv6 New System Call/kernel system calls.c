/*
- related files for the project: syscall.c, sysproc.c, proc.h, proc.c, usys.S

- How will you find out the pid that has been passed to your two system calls?
This is the same as what sys_kill() must do.

- How will you track the two counters for each process? You will want to add
both of these new counters to a structure that already exists for each process.
Your new system calls will simply return the value of one of the two counters.

- Make sure you increment the appropriate counter after the system call
(except for those we have said to exclude) has returned and check its return
value to make sure it isn't -1.

- How will you find the data structures for the specified process? You'll need
to look through the ptable.proc data structure to find the process with the
matching pid.

*/

/**
 * returns the number of system calls that the process
 * identified by pid has completed (not just initiated);
 *
 * calls to getnumsyscalls(), getnumsyscallsgood(), fork(), exec(), and sbrk()
 * should not be counted in that total.
 *
 * @param pid process identifier
 * @return number of system calls invoked, or -1 if pid is not a
 * valid pid.
 */
int getnumsyscalls(int pid);

/**
 * returns the number of system calls that the process identified by pid has
 * completed successfully(i.e., with a return code that is not -1);
 *
 * calls to getnumsyscalls(), getnumsyscallsgood(), fork(), exec(), and sbrk()
 * should not be counted in this total.
 *
 * @param pid process identifier
 * @return number of system calls completed successfully, or -1 if pid is not a
 * valid pid
 */
int getnumsyscallsgood(int pid);
