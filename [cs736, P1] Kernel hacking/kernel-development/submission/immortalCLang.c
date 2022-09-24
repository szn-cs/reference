#include "linux/kernel.h"
#include "linux/unistd.h"

extern int fsync_counter; 

asmlinkage
int sys_immortalCLang( int i ) {

  printk("[system call] counter = %d. \n", fsync_counter);

  return 0;
}
