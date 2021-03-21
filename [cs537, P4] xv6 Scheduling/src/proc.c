#include "types.h"
#include "defs.h"
#include "param.h"
#include "memlayout.h"
#include "mmu.h"
#include "x86.h"
#include "proc.h"
#include "spinlock.h"
#include "pstat.h"

struct {
    struct spinlock lock;
    struct proc proc[NPROC];
} ptable;

/** Typical circular Queue implementation in C: current scheduling circular
 queue data structure (e.g. linked-list or fixed-sized array) (modified from
 programiz.com/dsa/circular-queue)

 - add the init user process to the queue.
 - new allocated process: its scheduler-related PCB should be initialized, and
   added to the tail of the queue. On exit it must be removed from the queue.
 - peek at current head of scheduler queue, instead of iterating over ptable. */

// size of scheduler queue (which should match the maximum number of processes)
#define SIZE NPROC

struct proc *queue[SIZE];
int front = -1, rear = -1;

/**
 * @brief Check if the queue is full
 *
 * @return int 1 if full, otherwise 0
 */
int isFull() {
    if ((front == rear + 1) || (front == 0 && rear == SIZE - 1)) return 1;
    return 0;
}

/**
 * @brief Check if the queue is empty
 *
 * @return int 1 for an empty queue, otherwise 0;
 */
int isEmpty() {
    if (front == -1) return 1;
    return 0;
}

/**
 * @brief Adding an element to the scheduler queue
 *
 * Fails to enqueue if front == rear + 1
 *
 * @param element the process to enqueue into the circular queue
 */
void enQueue(struct proc *element) {
    if (isFull()) {
        // cprintf("\n Queue is full!! \n");

    } else {
        if (front == -1) front = 0;
        rear = (rear + 1) % SIZE;
        queue[rear % SIZE] = element;
        // cprintf("\n Inserted -> %d", element->pid);
    }
}

/**
 * @brief Removing an element from the scheduler queue
 *
 * @return struct proc* the removed process pointer
 */
struct proc *deQueue() {
    struct proc *element;
    if (isEmpty()) {
        // cprintf("\n Queue is empty !! \n");
        return 0;
    } else {
        element = queue[front % SIZE];  // front of the queue
        if (front == rear)              // if only a single entry was present
            front = -1, rear = -1;
        else
            front = (front + 1) % SIZE;  // increment front, removing
                                         // essentially the front of the queue

        // cprintf("\n Deleted element -> %d \n", element);
        return element;
    }
}

/**
 * @brief get the front process entry in the queue
 *
 * @return struct proc* process at front of the queue
 */
struct proc *peek() {
    struct proc *element;
    if (isEmpty()) return 0;
    element = queue[front % SIZE];
    return element;
}

/**
 * @brief Remove an entry by PID
 *
 * NOTE: an entry can be in the middle of the circular list, or lapsed beyond
 * complete turn, therefore careful comparisons are made.
 *
 * @param pid process id to search and remove from the queue
 * @return struct proc* the process that was removed, or null if none.
 */
struct proc *deQueueByPID(int pid) {
    struct proc *p;   // the process to remove
    int index;        // index of element matching pid
    if (isEmpty()) {  // if queue is empty
        // cprintf("\n Queue is empty !! \n");
        return 0;
    } else {
        // search for pid
        for (index = front; index <= rear; index++)
            if ((p = queue[index % SIZE])->pid == pid) goto remove;
        // in case no match return null
        return 0;
    }

remove:
    if (front == rear) {  // only a single entry was present
        front = -1, rear = -1;
    } else {
        // Q has only one element, so we reset the queue after dequeing it.
        // shift to fill the space of removed element
        for (; index < rear; index++)
            queue[index % SIZE] = queue[(index + 1) % SIZE];
        rear--;  // reflect removal of element
    }

    // cprintf("\n Deleted element -> %d \n", queue[index % SIZE]);
    return p;
}

/**
 * @brief helper function to display the queue for debugging
 */
void display() {
    int i;
    if (isEmpty())
        cprintf(" \n Empty Queue\n");
    else {
        cprintf("\n Front -> %d ", front);
        cprintf("\n queue -> ");
        for (i = front; i != rear; i = (i + 1) % SIZE) {
            cprintf("%d ", queue[i]->pid);
        }
        cprintf("%d ", queue[i]->pid);
        cprintf("\n Rear -> %d \n", rear);
    }
}

/**
 * @brief get process by pid
 *
 * @param pid process id number
 * @return pointer to process structure, otherwise if not found null
 */
struct proc *getProcess(int pid) {
    if (!(pid > 0)) goto fail;  // pid must positive

    for (struct proc *p = ptable.proc; p < &ptable.proc[NPROC]; p++)
        if (p->state != UNUSED && p->pid == pid) return p;

fail:
    return 0;
}

static struct proc *initproc;

int nextpid = 1;
extern void forkret(void);
extern void trapret(void);

static void wakeup1(void *chan);

void pinit(void) { initlock(&ptable.lock, "ptable"); }

// Must be called with interrupts disabled
int cpuid() { return mycpu() - cpus; }

// Must be called with interrupts disabled to avoid the caller being
// rescheduled between reading lapicid and running through the loop.
struct cpu *mycpu(void) {
    int apicid, i;

    if (readeflags() & FL_IF) panic("mycpu called with interrupts enabled\n");

    apicid = lapicid();
    // APIC IDs are not guaranteed to be contiguous. Maybe we should have
    // a reverse map, or reserve a register to store &cpus[i].
    for (i = 0; i < ncpu; ++i) {
        if (cpus[i].apicid == apicid) return &cpus[i];
    }
    panic("unknown apicid\n");
}

// Disable interrupts so that we are not rescheduled
// while reading proc from the cpu structure
struct proc *myproc(void) {
    struct cpu *c;
    struct proc *p;
    pushcli();
    c = mycpu();
    p = c->proc;
    popcli();
    return p;
}

// PAGEBREAK: 32
// Look in the process table for an UNUSED proc.
// If found, change state to EMBRYO and initialize
// state required to run in the kernel.
// Otherwise return 0.
static struct proc *allocproc(void) {
    struct proc *p;
    char *sp;

    acquire(&ptable.lock);

    for (p = ptable.proc; p < &ptable.proc[NPROC]; p++)
        if (p->state == UNUSED) goto found;

    release(&ptable.lock);
    return 0;

found:
    p->state = EMBRYO;
    p->pid = nextpid++;

    release(&ptable.lock);

    // Allocate kernel stack.
    if ((p->kstack = kalloc()) == 0) {
        p->state = UNUSED;
        return 0;
    }
    sp = p->kstack + KSTACKSIZE;

    // Leave room for trap frame.
    sp -= sizeof *p->tf;
    p->tf = (struct trapframe *)sp;

    // Set up new context to start executing at forkret,
    // which returns to trapret.
    sp -= 4;
    *(uint *)sp = (uint)trapret;

    sp -= sizeof *p->context;
    p->context = (struct context *)sp;
    memset(p->context, 0, sizeof *p->context);
    p->context->eip = (uint)forkret;

    // custom fields initialization
    p->sleepDuration = 0;
    p->timeSleptAt = 0;
    // inherits from parent, or for first process defaults to 1 timer tick
    p->timeslice = (p->parent) ? p->parent->timeslice : 1;
    p->compticks = 0;

    p->statistics.compticks = 0;
    p->statistics.schedticks = 0;
    p->statistics.sleepticks = 0;
    p->statistics.switches = 0;

    enQueue(p);  // add to scheduler queue

    return p;
}

// PAGEBREAK: 32
// Set up first user process.
void userinit(void) {
    struct proc *p;
    extern char _binary_initcode_start[], _binary_initcode_size[];

    p = allocproc();

    initproc = p;
    if ((p->pgdir = setupkvm()) == 0) panic("userinit: out of memory?");
    inituvm(p->pgdir, _binary_initcode_start, (int)_binary_initcode_size);
    p->sz = PGSIZE;
    memset(p->tf, 0, sizeof(*p->tf));
    p->tf->cs = (SEG_UCODE << 3) | DPL_USER;
    p->tf->ds = (SEG_UDATA << 3) | DPL_USER;
    p->tf->es = p->tf->ds;
    p->tf->ss = p->tf->ds;
    p->tf->eflags = FL_IF;
    p->tf->esp = PGSIZE;
    p->tf->eip = 0;  // beginning of initcode.S

    safestrcpy(p->name, "initcode", sizeof(p->name));
    p->cwd = namei("/");

    // this assignment to p->state lets other cores
    // run this process. the acquire forces the above
    // writes to be visible, and the lock is also needed
    // because the assignment might not be atomic.
    acquire(&ptable.lock);

    p->state = RUNNABLE;

    release(&ptable.lock);
}

// Grow current process's memory by n bytes.
// Return 0 on success, -1 on failure.
int growproc(int n) {
    uint sz;
    struct proc *curproc = myproc();

    sz = curproc->sz;
    if (n > 0) {
        if ((sz = allocuvm(curproc->pgdir, sz, sz + n)) == 0) return -1;
    } else if (n < 0) {
        if ((sz = deallocuvm(curproc->pgdir, sz, sz + n)) == 0) return -1;
    }
    curproc->sz = sz;
    switchuvm(curproc);
    return 0;
}

// Create a new process copying p as the parent.
// Sets up stack to return as if from system call.
// Caller must set state of returned proc to RUNNABLE.
int fork(void) {
    int i, pid;
    struct proc *np;
    struct proc *curproc = myproc();

    // Allocate process.
    if ((np = allocproc()) == 0) {
        return -1;
    }

    // Copy process state from proc.
    if ((np->pgdir = copyuvm(curproc->pgdir, curproc->sz)) == 0) {
        kfree(np->kstack);
        np->kstack = 0;
        np->state = UNUSED;
        return -1;
    }
    np->sz = curproc->sz;
    np->parent = curproc;
    *np->tf = *curproc->tf;

    // Clear %eax so that fork returns 0 in the child.
    np->tf->eax = 0;

    for (i = 0; i < NOFILE; i++)
        if (curproc->ofile[i]) np->ofile[i] = filedup(curproc->ofile[i]);
    np->cwd = idup(curproc->cwd);

    safestrcpy(np->name, curproc->name, sizeof(curproc->name));

    pid = np->pid;

    acquire(&ptable.lock);

    np->state = RUNNABLE;

    release(&ptable.lock);

    return pid;
}

// Exit the current process.  Does not return.
// An exited process remains in the zombie state
// until its parent calls wait() to find out it exited.
void exit(void) {
    struct proc *curproc = myproc();
    struct proc *p;
    int fd;

    if (curproc == initproc) panic("init exiting");

    // Close all open files.
    for (fd = 0; fd < NOFILE; fd++) {
        if (curproc->ofile[fd]) {
            fileclose(curproc->ofile[fd]);
            curproc->ofile[fd] = 0;
        }
    }

    begin_op();
    iput(curproc->cwd);
    end_op();
    curproc->cwd = 0;

    acquire(&ptable.lock);

    // Parent might be sleeping in wait().
    wakeup1(curproc->parent);

    // Pass abandoned children to init.
    for (p = ptable.proc; p < &ptable.proc[NPROC]; p++) {
        if (p->parent == curproc) {
            p->parent = initproc;
            if (p->state == ZOMBIE) wakeup1(initproc);
        }
    }

    // Jump into the scheduler, never to return.
    curproc->state = ZOMBIE;
    sched();
    panic("zombie exit");
}

// Wait for a child process to exit and return its pid.
// Return -1 if this process has no children.
int wait(void) {
    struct proc *p;
    int havekids, pid;
    struct proc *curproc = myproc();

    acquire(&ptable.lock);
    for (;;) {
        // Scan through table looking for exited children.
        havekids = 0;
        for (p = ptable.proc; p < &ptable.proc[NPROC]; p++) {
            if (p->parent != curproc) continue;
            havekids = 1;
            if (p->state == ZOMBIE) {
                // Found one.
                pid = p->pid;
                kfree(p->kstack);
                p->kstack = 0;
                freevm(p->pgdir);
                p->pid = 0;
                p->parent = 0;
                p->name[0] = 0;
                p->killed = 0;
                p->state = UNUSED;
                release(&ptable.lock);

                deQueueByPID(pid);  // remove it from the scheduler queue too

                return pid;
            }
        }

        // No point waiting if we don't have any children.
        if (!havekids || curproc->killed) {
            release(&ptable.lock);
            return -1;
        }

        // Wait for children to exit.  (See wakeup1 call in proc_exit.)
        sleep(curproc, &ptable.lock);  // DOC: wait-sleep
    }
}

/**
 * @brief helper function: pick next process to run from queue, according to the
 * schedluing policy.
 *
 * scheduler robin-robins over the queue should correctly give each process the
 * correct number of ticks per cycle.
 *
 * @param p last process to run (previous active process)
 * @return number of ticks on success, otherwise -1
 */
int pickNextProcess_compensationRR(struct proc **external_p) {
    struct proc *p = *external_p;

    // get next process using ptable or start from first process
    // for (p = (p == 0) ? ptable.proc : p + 1; p < &ptable.proc[NPROC]; p++) {
    //     if (p->state == RUNNABLE) goto success;
    // }

    // get next process using a circular queue
    for (int index = rear; index >= 0; index--) {
        p = deQueue();  // extract
        enQueue(p);     // move to tail
        if (p->state == RUNNABLE) goto success;
    }

    return -1;  // iteration over queue ended
success:
    *external_p = p;                     // modify external pointer
    p->statistics.switches++;            // stats
    return p->timeslice + p->compticks;  // number of ticks to run
}

// PAGEBREAK: 42
// Per-CPU process scheduler.
// Each CPU calls scheduler() after setting itself up.
// Scheduler never returns.  It loops, doing:
//  - choose a process to run
//  - swtch to start running that process
//  - eventually that process transfers control
//      via swtch back to the scheduler.
void scheduler(void) {
    struct proc *p;
    struct cpu *c = mycpu();
    int runDuration = 0;  // number of ticks to run for the picked process
    c->proc = 0;

    /* each iteration is equivalent to a timer tick */
runProcess:
    sti();  // Enable interrupts on this processor.
    acquire(&ptable.lock);

    p = 0;  // reset process pointer
    runDuration = 0;
    // choose proper process depending on scheduling policy & compensation ticks
    // If its timeslice (+ compensation ticks if applicable) has not been used
    // up for this scheduling cycle, keep scheduling it. Otherwise, move to the
    // next process in the queue and put the previous head to the tail.
    while ((p && p->state == RUNNABLE && --runDuration > 0) ||
           (runDuration = pickNextProcess_compensationRR(&p)) != -1) {
        // cprintf("[%d] \n", p->pid); // debug
        // display();

        // keep track of used compensation ticks
        if (runDuration <= p->compticks) {
            --p->compticks;  // current iteration is using a compensation ticket
            ++p->statistics.compticks;  // stats
        }

        // Switch to chosen process.  It is the process's job
        // to release ptable.lock and then reacquire it
        // before jumping back to us.
        c->proc = p;
        switchuvm(p);
        p->state = RUNNING;

        int previous_timeslice = p->timeslice;  // store old time slice

        swtch(&(c->scheduler), p->context);  // switch to process

        ++p->statistics.schedticks;  // stats

        /* in case a new slice value is assigned during time slice execution
            period, the run duration should be recalculated according to the new
            slice value, and the next process should be scheduled when timer
            interrupt fires. */
        if (previous_timeslice != p->timeslice)  // if process slice was updated
            // update recalculate run duration
            runDuration += p->timeslice - previous_timeslice;

        switchkvm();  // kernel switch

        // Process is done running for now.
        // It should have changed its p->state before coming back.
        c->proc = 0;
    }

    // new round
    release(&ptable.lock);
    goto runProcess;
}

// Enter scheduler.  Must hold only ptable.lock
// and have changed proc->state. Saves and restores
// intena because intena is a property of this
// kernel thread, not this CPU. It should
// be proc->intena and proc->ncli, but that would
// break in the few places where a lock is held but
// there's no process.
// switches between current context back to the context for scheduler
void sched(void) {
    int intena;
    struct proc *p = myproc();

    if (!holding(&ptable.lock)) panic("sched ptable.lock");
    if (mycpu()->ncli != 1) panic("sched locks");
    if (p->state == RUNNING) panic("sched running");
    if (readeflags() & FL_IF) panic("sched interruptible");
    intena = mycpu()->intena;
    swtch(&p->context, mycpu()->scheduler);
    mycpu()->intena = intena;
}

// Give up the CPU for one scheduling round.
void yield(void) {
    acquire(&ptable.lock);  // DOC: yieldlock
    myproc()->state = RUNNABLE;
    sched();
    release(&ptable.lock);
}

// A fork child's very first scheduling by scheduler()
// will swtch here.  "Return" to user space.
void forkret(void) {
    static int first = 1;
    // Still holding ptable.lock from scheduler.
    release(&ptable.lock);

    if (first) {
        // Some initialization functions must be run in the context
        // of a regular process (e.g., they call sleep), and thus cannot
        // be run from main().
        first = 0;
        iinit(ROOTDEV);
        initlog(ROOTDEV);
    }

    // Return to "caller", actually trapret (see allocproc).
}

// Atomically release lock and sleep on chan.
// Reacquires lock when awakened.
void sleep(void *chan, struct spinlock *lk) {
    struct proc *p = myproc();

    if (p == 0) panic("sleep");

    if (lk == 0) panic("sleep without lk");

    // Must acquire ptable.lock in order to
    // change p->state and then call sched.
    // Once we hold ptable.lock, we can be
    // guaranteed that we won't miss any wakeup
    // (wakeup runs with ptable.lock locked),
    // so it's okay to release lk.
    if (lk != &ptable.lock) {   // DOC: sleeplock0
        acquire(&ptable.lock);  // DOC: sleeplock1
        release(lk);
    }

    // Go to sleep.
    p->chan = chan;  // mark the process's wait channel - waiting mechanism for
                     // a change in a data structure it points to.
    p->state = SLEEPING;  // set state to sleeping

    sched();

    // finished sleeping (context switched back to this process kernel stack)
    p->compticks = p->sleepDuration;
    p->statistics.sleepticks += p->sleepDuration;  // stats
    p->sleepDuration = 0;                          // reset

    // Tidy up.
    p->chan = 0;

    // Reacquire original lock.
    if (lk != &ptable.lock) {  // DOC: sleeplock2
        release(&ptable.lock);
        acquire(lk);
    }
}

// PAGEBREAK!
// Wake up all processes sleeping on chan.
// The ptable lock must be held.
static void wakeup1(void *chan) {
    struct proc *p;

    for (p = ptable.proc; p < &ptable.proc[NPROC]; p++) {
        // wake up when it is right time - calculate wakeup time - sleep should
        // end after required # of ticks passed
        if (p->state == SLEEPING && p->chan == chan &&
            ticks >= (p->timeSleptAt + p->sleepDuration)) {
            p->state = RUNNABLE;
            // reset related fields
            p->timeSleptAt = 0;
            // p->sleepDuration = 0; // do not reset. Required for setting
            // compensation ticks
        }
    }
}

// Wake up all processes sleeping on chan (e.g. on ticks global variable or on a
// parent process).
void wakeup(void *chan) {
    acquire(&ptable.lock);
    wakeup1(chan);
    release(&ptable.lock);
}

// Kill the process with the given pid.
// Process won't exit until it returns
// to user space (see trap in trap.c).
int kill(int pid) {
    struct proc *p;

    acquire(&ptable.lock);
    for (p = ptable.proc; p < &ptable.proc[NPROC]; p++) {
        if (p->pid == pid) {
            p->killed = 1;
            // Wake process from sleep if necessary.
            if (p->state == SLEEPING) p->state = RUNNABLE;
            release(&ptable.lock);
            return 0;
        }
    }
    release(&ptable.lock);
    return -1;
}

// PAGEBREAK: 36
// Print a process listing to console.  For debugging.
// Runs when user types ^P on console.
// No lock to avoid wedging a stuck machine further.
void procdump(void) {
    static char *states[] = {[UNUSED] "unused",   [EMBRYO] "embryo",
                             [SLEEPING] "sleep ",  // blocked
                             [RUNNABLE] "runble",  // ready
                             [RUNNING] "run   ",  [ZOMBIE] "zombie"};
    int i;
    struct proc *p;
    char *state;
    uint pc[10];

    for (p = ptable.proc; p < &ptable.proc[NPROC]; p++) {
        if (p->state == UNUSED) continue;
        if (p->state >= 0 && p->state < NELEM(states) && states[p->state])
            state = states[p->state];
        else
            state = "???";
        cprintf("%d %s %s", p->pid, state, p->name);
        if (p->state == SLEEPING) {
            getcallerpcs((uint *)p->context->ebp + 2, pc);
            for (i = 0; i < 10 && pc[i] != 0; i++) cprintf(" %p", pc[i]);
        }
        cprintf("\n");
    }
}

/* ↓ custom implementations of system call funcs (accessible in kernel mode) */

/**
 * @brief sets time-slice of the specified pid
 *
 * @param pid target process to modify
 * @param slice new time-slice of target process
 * @return 0 on success, otherwise -1
 */
int setslice(int pid, int slice) {
    struct proc *p;
    // get process & validate parameters
    if (pid < 0 || !(p = getProcess(pid)) || !(slice > 0)) goto fail;

    // if pid is the currently running process, then its time-slice should be
    // immediately changed and applied to this scheduling interval.
    // if (p->pid == myproc()->pid)
    p->timeslice = slice;

    // The time-slice of a process could be increased, decreased, or not
    // changed;
    return 0;
fail:
    return -1;
}

/**
 * @brief get time-slice of a specific process
 *
 * @param pid
 * @return int time-slice of a process
 */
int getslice(int pid) {
    struct proc *p;
    // validate parameters
    if (!(p = getProcess(pid))) goto fail;

    return p->timeslice;

fail:
    return -1;
}

/**
 * @brief spawns process (equivalent to official fork) with specific time-slice
 *
 * example usage: `fork2(getslice(getpid()))` would inherit slice from parent
 *
 * @param slice spawned process time-slice length
 * @return int child process pid, or 0 for returning in spawned child process,
 * otherwise -1
 */
int fork2(int slice) {
    // validate parameters
    if (slice < 0) goto fail;

    // always returns the pid of the child in kernel mode
    int childPID = fork();
    // set time slice for child
    if (setslice(childPID, slice) == -1) goto fail;

    return childPID;
fail:
    return -1;
}

/**
 * @brief extracts information for each process; used to test scheduler
 * implementation
 *
 * @param pstat statistical information of all processes
 * @return int 0 on success, otherwise -1
 */
int getpinfo(struct pstat *pstat) {
    int index;       // index shared between ptable and pstat enteries
    struct proc *p;  // process at current index
    // validate parameters
    if (pstat == 0) goto fail;

    // loop through process list and update pstat corresponding enteries
    for (index = 0, p = ptable.proc; p < &ptable.proc[NPROC]; p++, index++) {
        if (p->state == UNUSED) {
            pstat->inuse[index] = 0;
            continue;
        } else {
            pstat->inuse[index] = 1;
        }

        pstat->pid[index] = p->pid;
        pstat->timeslice[index] = p->timeslice;
        pstat->compticks[index] = p->statistics.compticks;
        pstat->schedticks[index] = p->statistics.schedticks;
        pstat->sleepticks[index] = p->statistics.sleepticks;
        pstat->switches[index] = p->statistics.switches;
    }

    return 0;
fail:
    return -1;
}
