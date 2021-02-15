# Related files for the project:

-   syscall.c, sysproc.c, proc.c, usys.S
-   Makefile UPROGS, proc.h, user.h, defs.h, program c file.

# Files relations

-   usys.S defines system calls, with numbers at syscall.h; Shell receives command -> trap.c -> syscall.c -> sysproc.c -> proc.c
