#ifndef __message_h
#define __message_h

#define PROMPT "mysh> "
#define ERROR_REDIRECTION "Redirection misformatted.\n"
#define ERROR_IO(FILENAME) "%s%s%s", "Cannot write to file ", FILENAME, ".\n"
#define ERROR_UNALIAS "unalias: Incorrect number of arguments.\n"
#define ERROR_ALIAS "alias: Too dangerous to alias that.\n"
#define ERROR_ARGUMENTS "Usage: mysh [batch-file]\n"
#define ERROR_FILE(FILENAME) \
  "%s%s%s", "Error: Cannot open file ", FILENAME, ".\n"
#define ERROR_COMMAND(JOB) "%s%s", JOB, ": Command not found.\n"

#endif  // __message_h