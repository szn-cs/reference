import java.io.*;
import java.util.*;
import static java.util.Map.entry;

/**
 * ErrMsg
 *
 * This class is used to generate warning and fatal error messages.
 */
class ErrMsg {
    private static boolean err = false;

    // message values
    public static final HashMap<Integer, String> message =
            new HashMap<>(Map.ofEntries(
                    /**
                     * ✅ missing main function
                     * 
                     * [0, 0]
                     */
                    entry(1, "No main function")));

    /**
     * Generates a fatal error message.
     * 
     * @param lineNum line number for error location
     * @param charNum character number (i.e., column) for error location
     * @param msg associated message for error
     */
    static void fatal(int lineNum, int charNum, String msg) {
        err = true;
        System.err.println(lineNum + ":" + charNum + " ***ERROR*** " + msg);
    }

    // additional interface accepting an array position parameter
    static void fatal(int[] position, int msgNum) {
        fatal(position[0], position[1], message.get(msgNum));
    }

    /**
     * Generates a warning message.
     * 
     * @param lineNum line number for warning location
     * @param charNum character number (i.e., column) for warning location
     * @param msg associated message for warning
     */
    static void warn(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " ***WARNING*** " + msg);
    }

    /**
     * Returns the err flag.
     */
    static boolean getErr() {
        return err;
    }

    static void setErr() {
        err = true;
    }


}
