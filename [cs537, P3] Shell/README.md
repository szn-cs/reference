# Shell / Command line interpreter:

-   Build by running 'make mysh' or simply 'make'
-   Clean by running 'make clean'

# Implementation notes:

-   use write() to prevent intermingled output (instead of fprintf or
    pritnf). or use printf/fprintf calling fflush() immediately after each
    time.
-   strdup() for preserving input string (careful about memory leaks),
