/*
 * TODO:
 * 
 * - Name analyser should report the specified position of the error, and it
 * should give exactly the specified error message (each on a separate line).
 * Note that the names themselves should not be printed as part of the error
 * messages.
 * 
 * 
 * 
 * - Error messages should have the same format as in the scanner and parser
 * (i.e., they should be issued using a call to ErrMsg.fatal).
 * 
 * - If a declaration is both "bad" (e.g., a non-function declared void) and is
 * a declaration of a name that has already been declared in the same scope, you
 * should give two error messages (first the "bad" declaration error, then the
 * "multiply declared" error).
 * 
 * 
 * 
 */

/**
 * ErrMsg
 *
 * This class is used to generate warning and fatal error messages.
 */
class ErrMsg {
    public static final List<String> message = List.of(
            /**
             * More than one declaration of an identifier in a given scope
             * (note: includes identifier associated with a struct definition).
             * 
             * Posiion to report: The first character of the ID in the duplicate
             * declaration
             */
            "Multiply declared identifier",
            /**
             * Use of an undeclared identifier
             * 
             * reported position: The first character of the undeclared
             * identifier
             * 
             */
            "Undeclared identifier",
            /**
             * Bad struct access (LHS of dot-access is not of a struct type)
             * 
             * reported position: The first character of the ID corresponding to
             * the LHS of the dot-access.
             * 
             */
            "Dot-access of non-struct type",
            /**
             * Bad struct access (RHS of dot-access is not a field of the
             * appropriate a struct)
             * 
             * reported position: The first character of the ID corresponding to
             * the RHS of the dot-access.
             * 
             */
            "Invalid struct field name",
            /**
             * Bad declaration (variable or parameter of type void)
             * 
             * reported position: The first character of the ID in the bad
             * declaration.
             * 
             */
            "Non-function declared void",
            /**
             * Bad declaration (attempt to declare variable of a bad struct
             * type)
             * 
             * reported position: The first character of the ID corresponding to
             * the struct type in the bad declaration.
             * 
             */
            "Invalid name of struct type");


    /**
     * Generates a fatal error message.
     * 
     * @param lineNum line number for error location
     * @param charNum character number (i.e., column) for error location
     * @param msg     associated message for error
     */
    static void fatal(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " ***ERROR*** " + msg);
    }

    /**
     * Generates a warning message.
     * 
     * @param lineNum line number for warning location
     * @param charNum character number (i.e., column) for warning location
     * @param msg     associated message for warning
     */
    static void warn(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " ***WARNING*** " + msg);
    }
}
