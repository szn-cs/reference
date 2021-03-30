import java.util.*;
import static java.util.Map.entry;

/**
 * ErrMsg
 *
 * - Name analyser should report the specified position of the error, and it
 * should give exactly the specified error message (each on a separate line).
 * Note that the names themselves should not be printed as part of the error
 * messages.
 * 
 * This class is used to generate warning and fatal error messages.
 */
class ErrMsg {
    // tracks if a fatal error occured during the execution of the program
    public static boolean errorOccured = false;

    public static final HashMap<Integer, String> message =
            new HashMap<>(Map.ofEntries(
                    /**
                     * More than one declaration of an identifier in a given
                     * scope (note: includes identifier associated with a struct
                     * definition).
                     * 
                     * Posiion to report: The first character of the ID in the
                     * duplicate declaration
                     */
                    entry(1, "Multiply declared identifier"),
                    /**
                     * Use of an undeclared identifier
                     * 
                     * reported position: The first character of the undeclared
                     * identifier
                     * 
                     */
                    entry(2, "Undeclared identifier"),
                    /**
                     * Bad struct access (LHS of dot-access is not of a struct
                     * type)
                     * 
                     * reported position: The first character of the ID
                     * corresponding to the LHS of the dot-access.
                     * 
                     */
                    entry(3, "Dot-access of non-struct type"),
                    /**
                     * Bad struct access (RHS of dot-access is not a field of
                     * the appropriate a struct)
                     * 
                     * reported position: The first character of the ID
                     * corresponding to the RHS of the dot-access.
                     * 
                     */
                    entry(4, "Invalid struct field name"),
                    /**
                     * Bad declaration (variable or parameter of type void)
                     * 
                     * reported position: The first character of the ID in the
                     * bad declaration.
                     * 
                     */
                    entry(5, "Non-function declared void"),
                    /**
                     * Bad declaration (attempt to declare variable of a bad
                     * struct type)
                     * 
                     * reported position: The first character of the ID
                     * corresponding to the struct type in the bad declaration.
                     * 
                     */
                    entry(6, "Invalid name of struct type")

            ));


    /**
     * Generates a fatal error message.
     * 
     * @param lineNum line number for error location
     * @param charNum character number (i.e., column) for error location
     * @param msg     associated message for error
     */
    static void fatal(int lineNum, int charNum, String msg) {
        errorOccured = true; // set error flag to true
        System.err.println(lineNum + ":" + charNum + " ***ERROR*** " + msg);
    }

    static void fatal(int[] position, int msgNum) {
        fatal(position[0], position[1], message.get(msgNum));
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
