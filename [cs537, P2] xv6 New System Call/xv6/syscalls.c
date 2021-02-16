#include "types.h"
#include "stat.h"
#include "user.h"

static int syscalls(int, int);

int main(int argc, char **argv) {
    if (argc != 3) {
        // an incorrect number of arguments
        printf(1, "usage: syscalls <N: total number> <g: successful calls>\n");
        exit();
    }

    syscalls(atoi(argv[1]), atoi(argv[2]));  // call implementation

    exit();
}

/**
 * invokes system calls successful and/or unsuccessful
 *
 * For example, if you run the program as "syscalls 20 5" the output should be
 * exactly "20 5\n".
 *
 * @param N total number of system calls to invoke
 * @param g number of system calls which should be successful
 */
int syscalls(int N, int g) {
    // validate parameter:
    // at least one system call must be invoked, as syscall getpid() must
    // be called to know the pid of the current running process. or
    // number of good system calls is greater than the total number of
    // calls
    if (N <= 0 || g <= 0 || g > N) return 1;

    // process identifier - A valid pid indicates any pid that is in-use
    // which includes the zombie processes. A process is in the zombie
    // state after it exits and before it is waited for by its parent
    // process.
    int pid = getpid();
    N--, g--;  // invoked system call

    // invoke system call:
    // you can perform those system calls in any
    // order you choose. You should also be careful when calling some
    // library functions as they may also invoke syscalls (e.g. malloc()
    // might call sbrk()). Try to avoid them before printing the final
    // result.

    for (int i = 0; i < N - g; i++) kill(-1);  // make unsuccessful calls
    for (; g-- > 0;) uptime();                 // make successful calls

    // print system calls state
    printf(1, "%d %d\n", getnumsyscalls(pid), getnumsyscallsgood(pid));
    return 0;
}
