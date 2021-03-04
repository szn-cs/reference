

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