#include "param.h"

#ifndef SRC_PSTAT_H_
#define SRC_PSTAT_H_

// process stats used for implementing RR compensation scheduler
struct pstat {
  // whether this slot of the process table is in use (1 or 0)
  int inuse[NPROC];
  // PID of each process
  int pid[NPROC];
  // number of base ticks this process can run in a timeslice.
  // - After a process consumes its time-slice, it should be moved to the back
  // of the queue
  int timeslice[NPROC];

  /* ↓ accumulative during the lifecycle of the process. Gets reset when process
   * dies */

  // number of compensation ticks this process has used
  int compticks[NPROC];
  // total number of timer ticks this process has been scheduled.  incremented
  // exactly once for exactly one process when a timer tick occurs.
  int schedticks[NPROC];
  // number of ticks during which this process was blocked
  int sleepticks[NPROC];
  /* total num times this process has been scheduled.

   Incremented whenever:
    - process is scheduled.
    - also in the case where it finishes its time-slice and gets scheduled
   immediately again.
   - when a process is scheduled after waking (even if its previous time-slice
   was not used up entirely).

   Not incremented:
    - it should not be incremented for each timer-tick within that time-slice.
    - it should not be incremented separately for compensation ticks
   */
  int switches[NPROC];
};

struct pstat pstat;  // global pstat variable holding  processes statistics

#endif  // SRC_PSTAT_H_
