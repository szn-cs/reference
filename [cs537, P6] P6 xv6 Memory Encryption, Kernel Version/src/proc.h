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

/* 📝 clock workset related structures */
typedef struct clockNode {
    enum valid { VALID, INVALID } valid;  // is node a valid element
    pte_t *pte;
    uint uva;  // kept for debugging.
    // NOTE: referenced bit = PTE_A of pte
    // PTE_A (0x020 i.e. sixth bit): reference bit that gets set by x86
    // hardware to 1 every time a page is accessed.
} node_t;

// A clock struture is composed of a fixed circular buffer and an index of the
// current tail (clock hand).
struct clock {
    // (statically) allocate a clock queue for each process - storing all the
    // virtual pages that are currently decrypted    node_t queue[CLOCKSIZE]; //
    // list of clock nodes
    node_t queue[CLOCKSIZE];
    int capacity;  // maximum number of items in the buffer
    int size;      // number of items in the buffer
    int hand;      // index points to oldest page node in the queue
};

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
    // 📝
    struct clock workingSet;  // clock algorithm queue
};

// Process memory is laid out contiguously, low addresses first:
//   text
//   original data and bss
//   fixed-size stack
//   expandable heap
