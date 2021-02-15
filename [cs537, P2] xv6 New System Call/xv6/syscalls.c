#include "types.h"
#include "stat.h"
#include "user.h"

int main(int argc, char **argv) {
  int i;

  if (argc < 2) {
    printf(2, "usage: syscalls pid...\n");
    exit();
  }

  for (i = 1; i < argc; i++) customecho(atoi(argv[i]));

  exit();
}

/**
 * invokes system calls successful and/or unsuccessful
 *
 *  For example, if you run the program as "syscalls 20 5" the output should be
 * exactly "20 5\n".
 *
 * @param N total number of system calls to invoke
 * @param g number of system calls which should be successful
 */
void syscalls(int N, int g) {
  // process identifier - A valid pid indicates any pid that is in-use
  // which includes the zombie processes. A process is in the zombie
  // state after it exits and before it is waited for by its parent
  // process.
  //   int pid;

  // validate parameter:
  // at least one system call must be invoked, as syscall getpid() must be
  // called to know the pid of the current running process.
  //   assert(N > 0 && g > 0);
  //    handle errors (e.g., an incorrect number of arguments or the number of
  //    good system calls is greater than the total number of calls however you
  //    choose) but we won't test it.

  // invoke system call:
  // You can choose to make any system calls that you know will be successful or
  // unsuccessful; you can perform those system calls in any order you choose.
  // You should also be careful when calling some library functions as they may
  // also invoke syscalls (e.g. malloc() might call sbrk()). Try to avoid them
  // before printing the final result.

  // print system calls state
  //   printf("%d %d\n", getnumsyscalls(pid), getnumsyscallsgood(pid));
}