/*
 * It is up to you how you store information in each symbol-table entry (each
 * TSym). To implement the changes to the unparser methods you will need to know
 * each name's type. For function names, this includes the return type and the
 * number of parameters and their types. You can modify the TSym class by adding
 * some new fields (e.g., a kind field) and/or by declaring some subclasses
 * (e.g., a subclass for functions that has extra fields for the return type and
 * the list of parameter types). You will probably also want to add new methods
 * that return the values of the new fields and it may be helpful to change the
 * toString method so that you can print the contents of a TSym for debugging
 * purposes.
 */
public class TSym {
    private String type;

    public TSym(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}
