#include "types.h"
#include "x86.h"
#include "defs.h"
#include "date.h"
#include "param.h"
#include "memlayout.h"
#include "mmu.h"
#include "proc.h"
#include "ptentry.h"

int sys_fork(void) { return fork(); }

int sys_exit(void) {
    exit();
    return 0;  // not reached
}

int sys_wait(void) { return wait(); }

int sys_kill(void) {
    int pid;

    if (argint(0, &pid) < 0) return -1;
    return kill(pid);
}

int sys_getpid(void) { return myproc()->pid; }

int sys_sbrk(void) {
    int addr;
    int n;

    if (argint(0, &n) < 0) return -1;
    addr = myproc()->sz;
    if (growproc(n) < 0) return -1;
    return addr;
}

int sys_sleep(void) {
    int n;
    uint ticks0;

    if (argint(0, &n) < 0) return -1;
    acquire(&tickslock);
    ticks0 = ticks;
    while (ticks - ticks0 < n) {
        if (myproc()->killed) {
            release(&tickslock);
            return -1;
        }
        sleep(&ticks, &tickslock);
    }
    release(&tickslock);
    return 0;
}

// return how many clock tick interrupts have occurred
// since start.
int sys_uptime(void) {
    uint xticks;

    acquire(&tickslock);
    xticks = ticks;
    release(&tickslock);
    return xticks;
}

// 📝
int sys_mencrypt(void) {
    char *virtual_addr;
    int len;

    argptr(0, (void *)&virtual_addr, sizeof(char *));
    argint(1, &len);

    return mencrypt(virtual_addr, len);
}

int sys_getpgtable(void) {
    struct pt_entry *entries;
    int num;

    argptr(0, (void *)&entries, sizeof(struct pt_entry *));
    argint(1, &num);

    return getpgtable(entries, num);
}

int sys_dump_rawphymem(void) {
    uint physical_addr;
    char *buffer;

    // Note that argptr() will do a boundary check, which would cause an error
    // for the pointer physical_addr. Therefore, when you grab the value of
    // physical_addr from the stack, use argint() instead of argptr().
    argint(0, &physical_addr);
    argptr(1, (void *)&buffer, sizeof(char *));

    return dump_rawphymem(physical_addr, buffer);
}
