import java.util.*;

/*
 * Symbol types/kinds some new fields (e.g., a kind field) and/or by declaring
 * some subclasses (e.g., a subclass for functions that has extra fields for the
 * return type and the list of parameter types). You will probably also want to
 * add new methods that return the values of the new fields and it may be
 * helpful to change the toString method so that you can print the contents of a
 * TSym for debugging purposes.
 */


// variable declaration symbol
public class TSym {
    private String type; // bool, int, void, struct

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


/**
 * variable declaration symbol - used by bool/int or struct variables
 */
class VarSym extends TSym {
    // int or bool declaration variables
    public VarSym(String type) {
        super(type);
        this.size = -1;
    }

    // struct variable declaration
    public VarSym(String type, String structTag, int size,
            StructSym structDeclSym) {
        super(type);
        this.size = size;
        this.structTag = structTag;
        this.structDeclSym = structDeclSym;
    }

    StructSym getStructDeclSym() {
        return this.structDeclSym;
    }

    /**
     * For names of global variables, parameters, and local variables of a
     * non-struct type , the information should be int or bool.
     * 
     * For a global or local variable that is of a struct type, the information
     * should be the name of the struct type.
     */
    @Override
    public String toString() {
        String t = this.getType();
        if (t == "struct")
            return structTag;
        else
            return t;
    }

    // These fields are specific for struct variables
    private int size;
    private String structTag; // the id name of the struct type
    // the matching struct declaration symbol, used later to verify fields
    // correct usage
    private StructSym structDeclSym;
}


class FnSym extends TSym {
    private List<String> paramTypeList;
    private String returnType;

    public FnSym(List<String> paramTypeList, String returnType) {
        super(null);
        // kind = function
        this.paramTypeList = paramTypeList;
        this.returnType = returnType;
    }

    /**
     * function information form: `param1Type, param2Type, ..., paramNType ->
     * returnType`
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < this.paramTypeList.size(); i++) {
            s += this.paramTypeList.get(i);
            if (i != this.paramTypeList.size() - 1)
                s += ",";
            s += " ";
        }

        s += "-> ";

        s += returnType;

        return s;
    }

}


/**
 * Struct declaration with body
 */
class StructSym extends TSym {
    private String structTag; // the id name of the struct type
    private SymTable fieldSymbolTable;

    public StructSym(String structTag, SymTable fieldSymbolTable) {
        super("struct");
        this.structTag = structTag;
        this.fieldSymbolTable = fieldSymbolTable;
    }

    public SymTable getField() {
        return this.fieldSymbolTable;
    }


    // toString - this struct declaration class is not inteded to be linked in
    // used referenced.
}
