import java.util.*;

/**
 * The TSym class defines a symbol-table entry. Each TSym contains a type (a
 * Type).
 */
public class TSym {
    private Type type;
    // $fp offset of param or local (each scalar variable requires 4 bytes of
    // storage for int & 8 double)
    private int offset = 0;

    // declaration scope: local or global
    public enum AccessScope {
        LOCAL, GLOBAL
    };

    private AccessScope scope = AccessScope.LOCAL; // defaults to local

    public TSym(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public AccessScope getScope() {
        return scope;
    }

    public void setScope(AccessScope scope) {
        this.scope = scope;
    }

    public boolean isGlobal() {
        return (scope == AccessScope.GLOBAL) ? true : false;
    }

    public boolean isLocal() {
        return (scope == AccessScope.LOCAL) ? true : false;
    }

    public String toString() {
        return type.toString();
    }
}


/**
 * The FnSym class is a subclass of the TSym class just for functions. The
 * returnType field holds the return type and there are fields to hold
 * information about the parameters.
 */
class FnSym extends TSym {
    // new fields
    private Type returnType;
    private int numParams;
    private List<Type> paramTypes;
    private int parameterSize; // formals size
    private int localSize; // locals size


    public FnSym(Type type, int numparams) {
        super(new FnType());
        returnType = type;
        numParams = numparams;
    }

    public void addFormals(List<Type> L) {
        paramTypes = L;
    }

    public Type getReturnType() {
        return returnType;
    }

    public int getNumParams() {
        return numParams;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public int getParameterSize() {
        return parameterSize;
    }

    public int getLocalSize() {
        return localSize;
    }

    public void setParameterSize(int parameterSize) {
        this.parameterSize = parameterSize;
    }

    public void setLocalSize(int localSize) {
        this.localSize = localSize;
    }

    public String toString() {
        // make list of formals
        String str = "";
        boolean notfirst = false;
        for (Type type : paramTypes) {
            if (notfirst)
                str += ",";
            else
                notfirst = true;
            str += type.toString();
        }

        str += "->" + returnType.toString();
        return str;
    }
}


/**
 * The StructSym class is a subclass of the TSym class just for variables
 * declared to be a struct type. Each StructSym contains a symbol table to hold
 * information about its fields.
 */
class StructSym extends TSym {
    // new fields
    private IdNode structType; // name of the struct type

    public StructSym(IdNode id) {
        super(new StructType(id));
        structType = id;
    }

    public IdNode getStructType() {
        return structType;
    }
}


/**
 * The StructDefSym class is a subclass of the TSym class just for the
 * definition of a struct type. Each StructDefSym contains a symbol table to
 * hold information about its fields.
 */
class StructDefSym extends TSym {
    // new fields
    private SymTable symTab;

    public StructDefSym(SymTable table) {
        super(new StructDefType());
        symTab = table;
    }

    public SymTable getSymTable() {
        return symTab;
    }
}
