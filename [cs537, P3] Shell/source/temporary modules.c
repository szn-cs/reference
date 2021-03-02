/**
 * send the output of a program to a file rather than to the screen
 *
 */
void redirection() {
    // redirect standard output to a file with ">" character
    // - example `/bin/ls -la /tmp > output` should not print anything to the
    // screen, instead output rereouted to file output.
    // - Note that standard error output should not be changed
    // -  If the output file exists before you run your program, you should
    // simply overwrite it (after truncating it, which sets the file's size to
    // zero bytes).
    // fork(), setup file descriptors, execv()
    // approach 1- close stdout, then open new file (which will take its place)
    // approach 2- use dup2(): 2 different file descriptors can point to the
    // same open file.

    // FOrmat of redirection:  a command (along with its arguments, if present),
    // followed by any number of white spaces (including none), the redirection
    // symbol >,  again any number of white space (including none), followed by
    // a filename.

    // errors: Multiple redirection operators (e.g.
    // /bin/ls > > file.txt ), starting with a redirection sign (e.g. > file.txt
    // ), multiple files to the right of the redirection sign (e.g. /bin/ls >
    // file1.txt file2.txt ), or not specifying an output file (e.g. /bin/ls >
    // )are all errors.
    fprintf(stdout, "%s", ERROR_REDIRECTION);
    fflush(stdout);
    // do not execute command and continue to the next line.

    // errors: If if the output file cannot be opened for some reason (e.g.,
    // the user doesn't have write permission or the name is an existing
    // directory)
    char *filename = "";
    fprintf(stdout, ERROR_IO(filename));
    fflush(stdout);
    // do not execute command and continue to the next line.

    // Note: Do not worry about redirection for built-in commands (alias,
    // unalias, and exit); we will not test these cases.
}

/**
 * alias shortcuts for commands
 *
 * example: `mysh> alias ls /bin/ls`
 */
void alias() {
    // A built-in command means that the shell interprets this command directly;
    // the shell does not exec() the built-in command and run it as a separate
    // process; instead, the built-in command impacts how the shell itself runs.
    // data structure to store aliases: doubly-linked list

    // 3 ways to invoke:
    // 1. `alias <alias-name> <... replacement strigns>` if alias name already
    // exist, replace its value.
    // 2. `alias` should display all the aliases that have been set up (one per
    // line).
    char *aliasName = "", *replacementValue = "";
    fprintf(stdout, "%s %s", aliasName,
            replacementValue /* each token separated by one space*/);
    fflush(stdout);
    // 3. `alias <alias name>` if word matches alias name, print it (alias and
    // replacement value). if word doesn't match ignore and continue.
}

void unalias() {
    // `unalias <alias name>` should remove alias from the list datastructure.
    // if alias dones't exist ignore and continue.

    // errors: if no argument or too many arguments to unalias, print error and
    // continue
    fprintf(stdout, "%s", ERROR_UNALIAS);
    fflush(stdout);

    // Note: You do not need to worry about aliases to other aliases, aliases
    // that involve redirection, or redirection of aliases.

    // error: cannot be used as alias-names: alias, unalias, and exit. Should
    // print error and continue
    fprintf(stderr, "%s", ERROR_ALIAS);
    fflush(stderr);

    // Note:  additional arguments (e.g. ll -a where ll is an alias-name) is
    // undefined behavior. We will not test this. All alias calls will consist
    // of only the alias-name.
}