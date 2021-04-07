import java.util.*;
import static java.util.Map.entry;

/**
 * ErrMsg
 *
 * This class is used to generate warning and fatal error messages.
 */
class ErrMsg {
    private static boolean err = false;

    /**
     * Generates a fatal error message.
     * 
     * @param lineNum line number for error location
     * @param charNum character number (i.e., column) for error location
     * @param msg     associated message for error
     */
    static void fatal(int lineNum, int charNum, String msg) {
        err = true;
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

    /**
     * Returns the err flag.
     */
    static boolean getErr() {
        return err;
    }


    // tracks if a fatal error occured during the execution of the program
    // public static boolean errorOccured = false;

    public static final HashMap<Integer, String> message =
            new HashMap<>(Map.ofEntries(
                    /**
                     * Writing a function; e.g., "cout << f", where f is a
                     * function name.
                     * 
                     * 1st character of the function name.
                     */
                    entry(1, "Attempt to write a function"),

                    /**
                     * Writing a struct name; e.g., "cout << P", where P is the
                     * name of a struct type.
                     * 
                     * 1st character of the struct name.
                     */
                    entry(2, "Attempt to write a struct name"),

                    /**
                     * Writing a struct variable; e.g., "cout << p", where p is
                     * a variable declared to be of a struct type.
                     * 
                     * 1st character of the struct variable.
                     */
                    entry(3, "Attempt to write a struct variable"),

                    /**
                     * Writing a void value (note: this can only happen if there
                     * is an attempt to write the return value from a void
                     * function); e.g., "cout << f()", where f is a void
                     * function.
                     * 
                     * 1st character of the function name.
                     */
                    entry(4, "Attempt to write void"),

                    /**
                     * Reading a function: e.g., "cin >> f", where f is a
                     * function name.
                     * 
                     * 1st character of the function name.
                     */
                    entry(5, "Attempt to read a function"),

                    /**
                     * Reading a struct name; e.g., "cin >> P", where P is the
                     * name of a struct type.
                     * 
                     * 1st character of the struct name.
                     */
                    entry(6, "Attempt to read a struct name"),

                    /**
                     * Reading a struct variable; e.g., "cin >> p", where p is a
                     * variable declared to be of a struct type.
                     * 
                     * 1st character of the struct variable.
                     */
                    entry(7, "Attempt to read a struct variable"),

                    /**
                     * Calling something other than a function; e.g., "x();",
                     * where x is not a function name. Note: In this case, you
                     * should not type-check the actual parameters.
                     * 
                     * 1st character of the variable name.
                     */
                    entry(8, "Attempt to call a non-function"),

                    /**
                     * Calling a function with the wrong number of arguments.
                     * Note: In this case, you should not type-check the actual
                     * parameters.
                     * 
                     * 1st character of the function name.
                     */
                    entry(9, "Function call with wrong number of args"),

                    /**
                     * Calling a function with an argument of the wrong type.
                     * Note: you should only check for this error if the number
                     * of arguments is correct. If there are several arguments
                     * with the wrong type, you must give an error message for
                     * each such argument.
                     * 
                     * 1st character of the first identifier or literal in the
                     * actual parameter.
                     */
                    entry(10, "Type of actual does not match type of formal"),

                    /**
                     * Returning from a non-void function with a plain return
                     * statement (i.e., one that does not return a value).
                     * 
                     * position to report: 0,0
                     */
                    entry(11, "Missing return value"),

                    /**
                     * Returning a value from a void function.
                     * 
                     * 1st character of the first identifier or literal in the
                     * returned expression.
                     */
                    entry(12, "Return with a value in a void function"),

                    /**
                     * Returning a value of the wrong type from a non-void
                     * function.
                     * 
                     * 1st character of the first identifier or literal in the
                     * returned expression.
                     */
                    entry(13, "Bad return value"),

                    /**
                     * Applying an arithmetic operator (+, -, *, /) to an
                     * operand with type other than int. Note: this includes the
                     * ++ and -- operators.
                     * 
                     * 1st character of the first identifier or literal in an
                     * operand that is an expression of the wrong type.
                     */
                    entry(14,
                            "Arithmetic operator applied to non-numeric operand"),

                    /**
                     * Applying a relational operator (<, >, <=, >=) to an
                     * operand with type other than int.
                     * 
                     * 1st character of the first identifier or literal in an
                     * operand that is an expression of the wrong type.
                     */
                    entry(15,
                            "Relational operator applied to non-numeric operand"),

                    /**
                     * Applying a logical operator (!, &&, ||) to an operand
                     * with type other than bool.
                     * 
                     * 1st character of the first identifier or literal in an
                     * operand that is an expression of the wrong type.
                     */
                    entry(16, "Logical operator applied to non-bool operand"),

                    /**
                     * Using a non-bool expression as the condition of an if.
                     * 
                     * 1st character of the first identifier or literal in the
                     * condition.
                     */
                    entry(17, "Non-bool expression used as an if condition"),

                    /**
                     * Using a non-bool expression as the condition of a while.
                     * 
                     * 1st character of the first identifier or literal in the
                     * condition.
                     */
                    entry(18, "Non-bool expression used as a while condition"),

                    /**
                     * Using a non-integer expression as the times clause of a
                     * repeat.
                     * 
                     * 1st character of the first identifier or literal in the
                     * condition.
                     */
                    entry(19, "Non-integer expression used as a repeat clause"),

                    /**
                     * Applying an equality operator (==, !=) to operands of two
                     * different types (e.g., "j == true", where j is of type
                     * int), or assigning a value of one type to a variable of
                     * another type (e.g., "j = true", where j is of type int).
                     * 
                     * 1st character of the first identifier or literal in the
                     * left-hand operand.
                     */
                    entry(20, "Type mismatch"),

                    /**
                     * Applying an equality operator (==, !=) to void function
                     * operands (e.g., "f() == g()", where f and g are functions
                     * whose return type is void).
                     * 
                     * 1st character of the first function name.
                     */
                    entry(21, "Equality operator applied to void functions"),

                    /**
                     * Comparing two functions for equality, e.g., "f == g" or
                     * "f != g", where f and g are function names.
                     * 
                     * 1st character of the first function name.
                     */
                    entry(22, "Equality operator applied to functions"),

                    /**
                     * Comparing two struct names for equality, e.g., "A == B"
                     * or "A != B", where A and B are the names of struct types.
                     * 
                     * 1st character of the first struct name.
                     */
                    entry(23, "Equality operator applied to struct names"),

                    /**
                     * Comparing two struct variables for equality, e.g., "a ==
                     * b" or "a != b", where a and a are variables declared to
                     * be of struct types.
                     * 
                     * 1st character of the first struct variable.
                     */
                    entry(24, "Equality operator applied to struct variables"),

                    /**
                     * Assigning a function to a function; e.g., "f = g;", where
                     * f and g are function names.
                     * 
                     * 1st character of the first function name.
                     */
                    entry(25, "Function assignment"),

                    /**
                     * Assigning a struct name to a struct name; e.g., "A = B;",
                     * where A and B are the names of struct types.
                     * 
                     * 1st character of the first struct name.
                     */
                    entry(26, "Struct name assignment"),

                    /**
                     * Assigning a struct variable to a struct variable; e.g.,
                     * "a = b;", where a and b are variables declared to be of
                     * struct types.
                     * 
                     * 1st character of the first struct variable.
                     */
                    entry(27, "Struct variable assignment")

            ));

}
