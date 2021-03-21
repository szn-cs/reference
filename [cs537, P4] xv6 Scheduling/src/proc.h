// Per-CPU state
struct cpu {
  uchar apicid;               // Local APIC ID
  struct context *scheduler;  // swtch() here to enter scheduler
  struct taskstate ts;        // Used by x86 to find stack for interrupt
  struct segdesc gdt[NSEGS];  // x86 global descriptor table
  volatile uint started;      // Has the CPU started?
  int ncli;                   // Depth of pushcli nesting.
  int intena;                 // Were interrupts enabled before pushcli?
  struct proc *proc;          // The process running on this cpu or null
};

extern struct cpu cpus[NCPU];
extern int ncpu;

// PAGEBREAK: 17
// Saved registers for kernel context switches.
// Don't need to save all the segment registers (%cs, etc),
// because they are constant across kernel contexts.
// Don't need to save %eax, %ecx, %edx, because the
// x86 convention is that the caller has saved them.
// Contexts are stored at the bottom of the stack they
// describe; the stack pointer is the address of the context.
// The layout of the context matches the layout of the stack in swtch.S
// at the "Switch stacks" comment. Switch doesn't save eip explicitly,
// but it is on the stack and allocproc() manipulates it.
struct context {
  uint edi;
  uint esi;
  uint ebx;
  uint ebp;
  uint eip;
};

enum procstate { UNUSED, EMBRYO, SLEEPING, RUNNABLE, RUNNING, ZOMBIE };

// Per-process state
struct proc {
  uint sz;                     // Size of process memory (bytes)
  pde_t *pgdir;                // Page table
  char *kstack;                // Bottom of kernel stack for this process
  enum procstate state;        // Process state
  int pid;                     // Process ID
  struct proc *parent;         // Parent process
  struct trapframe *tf;        // Trap frame for current syscall
  struct context *context;     // swtch() here to run process
  void *chan;                  // If non-zero, sleeping on chan
  int killed;                  // If non-zero, have been killed
  struct file *ofile[NOFILE];  // Open files
  struct inode *cwd;           // Current directory
  char name[16];               // Process name (debugging)

  // custom - used for implementing RR compensation scheduler

  /* number of base ticks this process can run in a timeslice.
   - After a process consumes its time-slice, it should be moved to the back
   of the queue */
  int timeslice;
  // wakeup from sleep when # of ticks reached
  int wakeupTime;

  /* ↓ accumulative during the lifecycle of the process. Gets reset when
   * process dies */

  // number of compensation ticks this process has used
  int compticks;
  /* total number of timer ticks this process has been scheduled.
   incremented exactly once for exactly one process when a timer tick
   occurs. */
  int schedticks;
  // number of ticks during which this process was blocked
  int sleepticks;
  /* total num times this process has been scheduled.

    Incremented whenever:
    - process is scheduled.
    - also in the case where it finishes its time-slice and gets scheduled
    immediately again.
    - when a process is scheduled after waking (even if its previous
    time-slice was not used up entirely).

    Not incremented:
    - it should not be incremented for each timer-tick within that
    time-slice.
    - it should not be incremented separately for compensation ticks
    */
  int switches;
};

// Process memory is laid out contiguously, low addresses first:
//   text
//   original data and bss
//   fixed-size stack
//   expandable heap
