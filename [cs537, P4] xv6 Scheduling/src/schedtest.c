#include "types.h"
#include "stat.h"
#include "user.h"
#include "pstat.h"

extern struct pstat pstat;
static int loop_childProcess(int, char *);

static int cpNumber = 0;  // counter of child processes spawned

/**
 * @brief runs two copies of loop as child processes with custom time-slice
 * and sleep time for each child
 *
 * Test example: expected output should match RR compensation policy
 * ```
 *   prompt> schedtest 2 3 5 5 100
 *   3 5
 * ```
 *
 * @param argc argument count
 * @param argv argument vector
 */
int main(int argc, char **argv) {
    // parsed arguments
    int sliceA = 0, sliceB = 0, sleepParent = 0;
    char *sleepA, *sleepB;         // should be passed to exec function
    int cpA_PID = 0, cpB_PID = 0;  // child process IDs for A and B

    // verify commandline arguments
    if (argc != 6) {
        printf(1,
               "Usage: schedtest <sliceA> <sleepA> <sliceB> <sleepB> "
               "<sleepParent>\n");
        exit();
    }

    // parse arguments
    sliceA = atoi(argv[1]);
    sliceB = atoi(argv[3]);
    sleepA = argv[2];
    sleepB = argv[4];
    sleepParent = atoi(argv[5]);

    // spawn processes executing loop user-level program
    cpA_PID = loop_childProcess(sliceA, sleepA);  // A first
    cpB_PID = loop_childProcess(sliceB, sleepB);  // B second

    /* NOTE: assuming sleepParent much larger than
     * sliceA+2*sleepA+sliceB+2*sleepB
     * i.e. no need to worry about parent interfering with scheduling queue */
    sleep(sleepParent);

    getpinfo(&pstat);
    // print compensation ticks
    printf(1, "%d %d\n",  // compticks of processes in the pstat
           pstat.compticks[cpA_PID], pstat.compticks[cpB_PID]);

    for (int i = 1; i <= cpNumber; i++) wait();  // wait for child processes
    exit();
}

/**
 * @brief execute loop user program in a child process with specific time slice
 *
 * @param slice time-slice for the forked process
 * @param sleep number of ticks to sleep (argument for loop user program)
 * @return child process PID
 */
int loop_childProcess(int slice, char *sleep) {
    cpNumber++;  // increment child process counter

    // fork with specific time-slice
    int childPID = 0;
    if ((childPID = fork2(slice)) != 0) return childPID;

    /* ↓ child execution */
    char *argv[] = {"loop", sleep, 0};  // child process argument list
    exec("loop", argv);  // replace memory executable w/ user-program

    printf(1, "❌ schedtest: exec sleep failed\n");
    exit();
}
