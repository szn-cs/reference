import java.io.*;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a C-- program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have. All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
// Subclass Kids
// -------- ----
// ProgramNode DeclListNode
// DeclListNode linked list of DeclNode
// DeclNode:
// VarDeclNode TypeNode, IdNode, int
// FnDeclNode TypeNode, IdNode, FormalsListNode, FnBodyNode
// FormalDeclNode TypeNode, IdNode
// StructDeclNode IdNode, DeclListNode
//
// FormalsListNode linked list of FormalDeclNode
// FnBodyNode DeclListNode, StmtListNode
// StmtListNode linked list of StmtNode
// ExpListNode linked list of ExpNode
//
// TypeNode:
// IntNode -- none --
// BoolNode -- none --
// VoidNode -- none --
// StructNode IdNode
//
// StmtNode:
// AssignStmtNode AssignNode
// PostIncStmtNode ExpNode
// PostDecStmtNode ExpNode
// ReadStmtNode ExpNode
// WriteStmtNode ExpNode
// IfStmtNode ExpNode, DeclListNode, StmtListNode
// IfElseStmtNode ExpNode, DeclListNode, StmtListNode,
// DeclListNode, StmtListNode
// WhileStmtNode ExpNode, DeclListNode, StmtListNode
// RepeatStmtNode ExpNode, DeclListNode, StmtListNode
// CallStmtNode CallExpNode
// ReturnStmtNode ExpNode
//
// ExpNode:
// IntLitNode -- none --
// StrLitNode -- none --
// TrueNode -- none --
// FalseNode -- none --
// IdNode -- none --
// DotAccessNode ExpNode, IdNode
// AssignNode ExpNode, ExpNode
// CallExpNode IdNode, ExpListNode
// UnaryExpNode ExpNode
// UnaryMinusNode
// NotNode
// BinaryExpNode ExpNode ExpNode
// PlusNode
// MinusNode
// TimesNode
// DivideNode
// AndNode
// OrNode
// EqualsNode
// NotEqualsNode
// LessNode
// GreaterNode
// LessEqNode
// GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
// IntNode, BoolNode, VoidNode, IntLitNode, StrLitNode,
// TrueNode, FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
// DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
// ProgramNode, VarDeclNode, FnDeclNode, FormalDeclNode,
// StructDeclNode, FnBodyNode, StructNode, AssignStmtNode,
// PostIncStmtNode, PostDecStmtNode, ReadStmtNode, WriteStmtNode
// IfStmtNode, IfElseStmtNode, WhileStmtNode, CallStmtNode
// ReturnStmtNode, DotAccessNode, AssignExpNode, CallExpNode,
// UnaryExpNode, BinaryExpNode, UnaryMinusNode, NotNode,
// PlusNode, MinusNode, TimesNode, DivideNode,
// AndNode, OrNode, EqualsNode, NotEqualsNode,
// LessNode, GreaterNode, LessEqNode, GreaterEqNode
//
// **********************************************************************

// for Statement AST nodes with variable declarations
abstract interface Declaration {
    // get list of all declarations recursively
    abstract List<DeclNode> getDeclarationList();
}


// for AST nodes with codeGen method
abstract interface CodeGeneration {
    abstract void codeGen();
}


// used to retrieve nested statements
abstract interface Statement {
    // get list of all statements recursively
    abstract List<StmtNode> getStatementList();
}


// for expressions that can be used as conditions
abstract interface Condition {
    abstract void genJumpCode(String trueLabel, String doneLabel);
}

// **********************************************************************
// %%%ASTnode class (base class for all other kinds of nodes)
// **********************************************************************


abstract class ASTnode {
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    // this method can be used by the unparse methods to do indenting
    protected void addIndentation(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++)
            p.print(" ");
    }
}

// **********************************************************************
// ProgramNode, DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************


class ProgramNode extends ASTnode implements CodeGeneration {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }

    /**
     * nameAnalysis Creates an empty symbol table for the outermost scope, then
     * processes all of the globals, struct defintions, and functions in the
     * program.
     */
    public void nameAnalysis() {
        SymTable symTab = new SymTable();
        myDeclList.nameAnalysis(symTab);

        // augment with scope acccess field (global / local)
        myDeclList.setAccessScope(TSym.AccessScope.GLOBAL);

        // check if main function exist
        TSym mainSymbol = null;
        try {
            mainSymbol = symTab.lookupLocal("main");
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in ProgramNode.nameAnalysis");
        }
        if (mainSymbol == null || !mainSymbol.getType().isFnType())
            ErrMsg.fatal(new int[] {0, 0}, 1);
    }

    /**
     * typeCheck
     */
    public void typeCheck() {
        myDeclList.typeCheck();
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }

    public void codeGen() {
        if (myDeclList == null) return;
        myDeclList.codeGen();
    }

    // 1 kid
    private DeclListNode myDeclList;
}


class DeclListNode extends ASTnode implements CodeGeneration {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }

    /**
     * nameAnalysis Given a symbol table symTab, process all of the decls in the
     * list.
     */
    public void nameAnalysis(SymTable symTab) {
        nameAnalysis(symTab, symTab);
    }

    // set global/local attribute for each declaration node in this list
    public void setAccessScope(TSym.AccessScope s) {
        for (DeclNode node : myDecls)
            node.myId.sym().setScope(s);
    }

    /**
     * nameAnalysis Given a symbol table symTab and a global symbol table
     * globalTab (for processing struct names in variable decls), process all of
     * the decls in the list.
     */
    public void nameAnalysis(SymTable symTab, SymTable globalTab) {
        for (DeclNode node : myDecls) {
            if (node instanceof VarDeclNode) {
                ((VarDeclNode) node).nameAnalysis(symTab, globalTab);
            } else {
                node.nameAnalysis(symTab);
            }
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck() {
        for (DeclNode node : myDecls) {
            node.typeCheck();
        }
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode) it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println(
                    "unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }

    public void codeGen() {
        if (myDecls == null) return;

        for (DeclNode node : myDecls) {
            // skip struct
            if (!(node instanceof VarDeclNode) && !(node instanceof FnDeclNode))
                continue;
            ((CodeGeneration) node).codeGen();
        }
    }

    // list of kids (DeclNodes)
    public List<DeclNode> myDecls;
}


class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: for each formal decl in the
     * list process the formal decl if there was no error, add type of formal
     * decl to list
     */
    public List<Type> nameAnalysis(SymTable symTab) {
        List<Type> typeList = new LinkedList<Type>();
        for (FormalDeclNode node : myFormals) {
            TSym sym = node.nameAnalysis(symTab);
            if (sym != null) typeList.add(sym.getType());
        }
        return typeList;
    }

    /**
     * Return the number of formals in this list.
     */
    public int length() {
        return myFormals.size();
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) { // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    // list of kids (FormalDeclNodes)
    public List<FormalDeclNode> myFormals;
}


class FnBodyNode extends ASTnode {
    public int offset; // $fp offset of locals
    public int localCount = 0; // total number of local declarations

    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - process the declaration
     * list - process the statement list
     */
    public void nameAnalysis(SymTable symTab) {
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);

        ArrayList<DeclNode> declarationList = new ArrayList<>();

        // immediate declarations offset calculation
        declarationList.addAll(myDeclList.myDecls);
        // nested declarations offset calculation
        for (StmtNode s : myStmtList.myStmts) {
            if (!(s instanceof Declaration)) continue; // skip non declarations
            declarationList.addAll(((Declaration) s).getDeclarationList());
        }

        // calculate locals offset
        for (DeclNode node : declarationList) {
            if (!(node instanceof VarDeclNode)) continue; // ignore struct
            TSym s = node.myId.sym(); // local
            if (s == null) continue;

            s.setOffset(offset);
            offset -= 4;
            localCount++;
        }

    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        myStmtList.typeCheck(retType);
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }

    // 2 kids
    private DeclListNode myDeclList;
    public StmtListNode myStmtList;
}


class StmtListNode extends ASTnode implements CodeGeneration {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    /**
     * nameAnalysis Given a symbol table symTab, process each statement in the
     * list.
     */
    public void nameAnalysis(SymTable symTab) {
        for (StmtNode node : myStmts) {
            node.nameAnalysis(symTab);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        for (StmtNode node : myStmts) {
            node.typeCheck(retType);
        }
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }

    public void codeGen() {
        if (myStmts == null) return;

        for (StmtNode node : myStmts)
            // make sure no nulls
            if (node != null) node.codeGen();
    }

    // list of kids (StmtNodes)
    public List<StmtNode> myStmts;
}


class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public int size() {
        return myExps.size();
    }

    /**
     * nameAnalysis Given a symbol table symTab, process each exp in the list.
     */
    public void nameAnalysis(SymTable symTab) {
        for (ExpNode node : myExps) {
            node.nameAnalysis(symTab);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(List<Type> typeList) {
        int k = 0;
        try {
            for (ExpNode node : myExps) {
                Type actualType = node.typeCheck(); // actual type of arg

                if (!actualType.isErrorType()) { // if this is not an error
                    Type formalType = typeList.get(k); // get the formal type
                    if (!formalType.equals(actualType)) {
                        ErrMsg.fatal(node.lineNum(), node.charNum(),
                                "Type of actual does not match type of formal");
                    }
                }
                k++;
            }
        } catch (NoSuchElementException e) {
            System.err.println(
                    "unexpected NoSuchElementException in ExpListNode.typeCheck");
            System.exit(-1);
        }
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) { // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    public void codeGen() {
        if (myExps == null) return;

        for (ExpNode node : myExps)
            // make sure no nulls
            if (node != null) node.codeGen();
    }

    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************


abstract class DeclNode extends ASTnode {
    /**
     * Note: a formal decl needs to return a sym
     */
    abstract public TSym nameAnalysis(SymTable symTab);

    // default version of typeCheck for non-function decls
    public void typeCheck() {}

    public void unparseScopeAccess(PrintWriter p, int indent) {
        p.print((myId.sym().isGlobal() ? "global" : "local") + " ");
    }

    public IdNode myId; // id name
}


class VarDeclNode extends DeclNode implements CodeGeneration {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    /**
     * nameAnalysis (overloaded) Given a symbol table symTab, do: if this name
     * is declared void, then error else if the declaration is of a struct type,
     * lookup type name (globally) if type name doesn't exist, then error if no
     * errors so far, if name has already been declared in this scope, then
     * error else add name to local symbol table
     *
     * symTab is local symbol table (say, for struct field decls) globalTab is
     * global symbol table (for struct type names) symTab and globalTab can be
     * the same
     */
    public TSym nameAnalysis(SymTable symTab) {
        return nameAnalysis(symTab, symTab);
    }

    public TSym nameAnalysis(SymTable symTab, SymTable globalTab) {
        boolean badDecl = false;
        String name = myId.name();
        TSym sym = null;
        IdNode structId = null;

        if (myType instanceof VoidNode) { // check for void type
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Non-function declared void");
            badDecl = true;
        }

        else if (myType instanceof StructNode) {
            structId = ((StructNode) myType).idNode();

            try {
                sym = globalTab.lookupGlobal(structId.name());
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException "
                        + " in VarDeclNode.nameAnalysis");
            }

            // if the name for the struct type is not found,
            // or is not a struct type
            if (sym == null || !(sym instanceof StructDefSym)) {
                ErrMsg.fatal(structId.lineNum(), structId.charNum(),
                        "Invalid name of struct type");
                badDecl = true;
            } else {
                structId.link(sym);
            }
        }

        TSym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in VarDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Multiply declared identifier");
            badDecl = true;
        }

        if (!badDecl) { // insert into symbol table
            try {
                if (myType instanceof StructNode) {
                    sym = new StructSym(structId);
                } else {
                    sym = new TSym(myType.type());
                }
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException "
                        + " in VarDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException "
                        + " in VarDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException "
                        + " in VarDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        return sym;
    }

    public void unparse(PrintWriter p, int indent) {
        TSym s = myId.sym();

        addIndentation(p, indent);
        super.unparseScopeAccess(p, indent);
        if (s.isLocal()) p.print("[offset: " + s.getOffset() + "] ");

        myType.unparse(p, 0);
        p.print(" ");
        p.print(myId.name());
        p.println(";");
    }

    public void codeGen() {
        TSym s = myId.sym();

        if (!s.isGlobal()) {
            System.err.println("Error: codeGen shouldn't be handling locals");
            ErrMsg.setErr();
            return;
        }

        // global variable:
        G.generate(".data");
        G.generateWithComment(".align 2", "align on a word boundary");
        String l = "_" + myId.name();
        String i = String.valueOf(getSize());
        G.generateLabeled(l, ".space ", i, null);
    }

    public int getSize() {
        if (mySize > -1) return mySize; // struct

        // only int and bool size supported (no double definition)
        if (myType.type().isBoolType() || myType.type().isIntType()) return 4;

        System.err.println("Error: unsupported variable type encountered");
        ErrMsg.setErr();
        return -1;
    }

    // 3 kids
    private TypeNode myType;
    private int mySize; // use value NOT_STRUCT if this is not a struct type

    public static int NOT_STRUCT = -1;
}


class FnDeclNode extends DeclNode implements CodeGeneration, Statement {
    public FnDeclNode(TypeNode type, IdNode id, FormalsListNode formalList,
            FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public List<StmtNode> getStatementList() {
        ArrayList<StmtNode> l = new ArrayList<>(myBody.myStmtList.myStmts);
        for (StmtNode node : myBody.myStmtList.myStmts)
            if (node instanceof Statement)
                l.addAll(((Statement) node).getStatementList());
        return l;
    }


    /**
     * nameAnalysis Given a symbol table symTab, do: if this name has already
     * been declared in this scope, then error else add name to local symbol
     * table in any case, do the following: enter new scope process the formals
     * if this function is not multiply declared, update symbol table entry with
     * types of formals process the body of the function exit scope
     */
    public TSym nameAnalysis(SymTable symTab) {
        String name = myId.name();
        FnSym sym = null;
        TSym symCheckMul = null;
        /** offset calculation */
        int controlSize = 8; // return address & control link size
        int parameterSize = 0, localSize; // total params size and local size

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in FnDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Multiply declared identifier");
        }

        else { // add function name to local symbol table
            try {
                sym = new FnSym(myType.type(), myFormalsList.length());
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException "
                        + " in FnDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException "
                        + " in FnDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException "
                        + " in FnDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        symTab.addScope(); // add a new scope for locals and params

        // process the formals
        List<Type> typeList = myFormalsList.nameAnalysis(symTab);
        if (sym != null) sym.addFormals(typeList);

        // formals $fp offset calculation
        {
            int offset = 0; // initial offset
            for (FormalDeclNode node : myFormalsList.myFormals) {
                TSym s = node.myId.sym(); // formal
                if (s == null) continue;

                s.setOffset(offset);
                parameterSize += 4; // int/bool bytes increment
                offset = -parameterSize;
            }
        }

        // locals $fp offset
        myBody.offset = -(parameterSize + controlSize); // set initial offset
        myBody.nameAnalysis(symTab); // process the function body
        localSize = 4 * myBody.localCount; // calculate size of locals

        try {
            symTab.removeScope(); // exit scope
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in FnDeclNode.nameAnalysis");
            System.exit(-1);
        }

        // set function symbol details
        sym.setParameterSize(parameterSize);
        sym.setLocalSize(localSize);

        return null;
    }

    /**
     * typeCheck
     */
    public void typeCheck() {
        myBody.typeCheck(myType.type());
    }

    public void unparse(PrintWriter p, int indent) {
        FnSym s = (FnSym) myId.sym();

        addIndentation(p, indent);

        super.unparseScopeAccess(p, indent);
        p.print("[params: " + s.getParameterSize() + ", locals: "
                + s.getLocalSize() + "] ");

        myType.unparse(p, 0);
        p.print(" ");
        p.print(myId.name());
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent + 4);
        p.println("}\n");
    }

    public void codeGen() {
        FnSym s = (FnSym) myId.sym();
        String funcLabel = // function label ("main" or _<name>)
                (myId.name().equals("main") ? "" : "_") + myId.name();
        int localSize = s.getLocalSize();
        int formalSize = s.getParameterSize();
        int controlSize = 8; // return address & control link
        int offsetRA = -(formalSize); // return address offset
        int offsetControlLink = -(formalSize + 4); // old $fp offset
        String epilogueLabel = G.nextLabel(); // return statement label

        // preamble
        G.sectionComment("⨍\t" + myId.name(), G.Comment.BLOCK);
        {
            G.generate(".text");
            if (funcLabel.equals("main")) G.generate(".globl main");
            G.generateLabeled(funcLabel, "", null);
        }

        // entry (AR)
        G.sectionComment("Entry", G.Comment.LINE);
        {
            G.genPush(G.RA); // push return address
            G.genPush(G.FP); // push control link
            // set $fp: return from top of stack to AR base (addition)
            G.generate("addu", G.FP, G.SP, formalSize + controlSize);
            // push space for local variables: subtract locals size
            G.generate("subu", G.SP, G.SP, localSize);
        }

        // body (statements)
        {
            G.sectionComment("Body", G.Comment.LINE);
            // set epiloguwLabel for return statement instaces of this function
            List<StmtNode> l = getStatementList();
            for (StmtNode node : l)
                if (node instanceof ReturnStmtNode)
                    ((ReturnStmtNode) node).epilogueLabel = epilogueLabel;

            // code generation for statements only (not declarations)
            if (myBody.myStmtList != null) myBody.myStmtList.codeGen();
        }

        // exit (restore stack & return to caller)
        G.sectionComment("Exit", G.Comment.LINE);
        {
            G.generateLabeled(epilogueLabel, "", "epilogue");
            // load return address
            G.generateIndexed("lw", G.RA, G.FP, offsetRA);
            // save current $fp for updating $sp later
            G.generate("move", G.T0, G.FP);
            // restore old $fp
            G.generateIndexed("lw", G.FP, G.FP, offsetControlLink);
            // restore stack pointer
            G.generate("move", G.SP, G.T0);
            // return i.e. jump to return address
            if (!funcLabel.equals("main")) { // required for SPIM
                G.generate("li", G.V0, 10);
                G.generate("syscall");
            } else {
                G.generate("jr", G.RA);
            }
        }
    }

    // 4 kids
    private TypeNode myType;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}


class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: if this formal is declared
     * void, then error else if this formal is already in the local symble
     * table, then issue multiply declared error message and return null else
     * add a new entry to the symbol table and return that TSym
     */
    public TSym nameAnalysis(SymTable symTab) {
        String name = myId.name();
        boolean badDecl = false;
        TSym sym = null;

        if (myType instanceof VoidNode) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Non-function declared void");
            badDecl = true;
        }

        TSym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in FormalDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Multiply declared identifier");
            badDecl = true;
        }

        if (!badDecl) { // insert into symbol table
            try {
                sym = new TSym(myType.type());
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException "
                        + " in FormalDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException "
                        + " in FormalDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException "
                        + " in FormalDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        return sym;
    }

    public void unparse(PrintWriter p, int indent) {
        TSym s = myId.sym();

        super.unparseScopeAccess(p, indent);
        if (s.isLocal()) p.print("[offset: " + s.getOffset() + "] ");

        myType.unparse(p, 0);
        p.print(" ");
        p.print(myId.name());
    }

    // 2 kids
    private TypeNode myType;
}


// ❌ NOT REQUIRED
class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: if this name is already in
     * the symbol table, then multiply declared error (don't add to symbol
     * table) create a new symbol table for this struct definition process the
     * decl list if no errors add a new entry to symbol table for this struct
     */
    public TSym nameAnalysis(SymTable symTab) {
        String name = myId.name();
        boolean badDecl = false;

        TSym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in StructDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Multiply declared identifier");
            badDecl = true;
        }


        if (!badDecl) {
            try { // add entry to symbol table
                SymTable structSymTab = new SymTable();
                myDeclList.nameAnalysis(structSymTab, symTab);
                StructDefSym sym = new StructDefSym(structSymTab);
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException "
                        + " in StructDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException "
                        + " in StructDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException "
                        + " in StructDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        return null;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("struct ");
        p.print(myId.name());
        p.println("{");
        myDeclList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("};\n");

    }

    // 2 kids
    private DeclListNode myDeclList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************


abstract class TypeNode extends ASTnode {
    /* all subclasses must provide a type method */
    abstract public Type type();
}


class IntNode extends TypeNode {
    public IntNode() {}

    /**
     * type
     */
    public Type type() {
        return new IntType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }
}


class BoolNode extends TypeNode {
    public BoolNode() {}

    /**
     * type
     */
    public Type type() {
        return new BoolType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }
}


class VoidNode extends TypeNode {
    public VoidNode() {}

    /**
     * type
     */
    public Type type() {
        return new VoidType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }
}


// ❌ NOT REQUIRED
class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public IdNode idNode() {
        return myId;
    }

    /**
     * type
     */
    public Type type() {
        return new StructType(myId);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        p.print(myId.name());
    }

    // 1 kid
    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************


abstract class StmtNode extends ASTnode implements CodeGeneration {
    abstract public void nameAnalysis(SymTable symTab);

    abstract public void typeCheck(Type retType);
}


class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myAssign.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        myAssign.typeCheck();
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

    public void codeGen() {
        myAssign.codeGen();
        // ignore value when assignment used as a statement
        G.genPop(G.T0); // pop vaue pushed by AssignNode
    }

    // 1 kid
    private AssignNode myAssign;
}


class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (!type.isErrorType() && !type.isIntType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Arithmetic operator applied to non-numeric operand");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }

    public void codeGen() {
        assert myExp instanceof IdNode : "unexpected post increment type";
        IdNode myExp = (IdNode) this.myExp;

        myExp.codeGen(); // T0: value onto stack
        myExp.genAddr(); // T1: address onto stack

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform instruction (T0 = T0 + 1)
        G.generate("add", G.T0, G.T0, 1);
        G.generateIndexed("sw", G.T0, G.T1, 0); // store into address location
    }

    // 1 kid
    private ExpNode myExp;
}


class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (!type.isErrorType() && !type.isIntType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Arithmetic operator applied to non-numeric operand");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

    public void codeGen() {
        assert myExp instanceof IdNode : "unexpected post increment type";
        IdNode myExp = (IdNode) this.myExp;

        myExp.codeGen(); // T0: value onto stack
        myExp.genAddr(); // T1: address onto stack

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform instruction (T0 = T0 + 1)
        G.generate("sub", G.T0, G.T0, 1);
        G.generateIndexed("sw", G.T0, G.T1, 0); // store into address location
    }

    // 1 kid
    private ExpNode myExp;
}


class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (type.isFnType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to read a function");
        }

        if (type.isStructDefType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to read a struct name");
        }

        if (type.isStructType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to read a struct variable");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    public void codeGen() {
        assert myExp instanceof IdNode : "unsupported read input node type";
        IdNode myExp = (IdNode) this.myExp;
        TSym s = myExp.sym();
        assert s.getType().isIntType()
                || s.getType().isBoolType() : "unsupported read variable type";

        // read value
        G.generateWithComment("li", G.V0, 5, "read integer");
        G.generate("syscall");

        // store value to IdNode's address
        myExp.genAddr(); // push variable address to stack
        G.genPop(G.T0);
        G.generateIndexed("sw", G.V0, G.T0, 0);
    }

    // 1 kid (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}


class WriteStmtNode extends StmtNode {
    private Type expressionType; // type of expression being written

    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (type.isFnType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to write a function");
        }

        if (type.isStructDefType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to write a struct name");
        }

        if (type.isStructType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to write a struct variable");
        }

        if (type.isVoidType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Attempt to write void");
        }

        expressionType = type;
    }

    public void codeGen() {
        // generate the appropriate code for each type
        assert (expressionType.isBoolType() || expressionType.isIntType()
                || expressionType.isStringType()) : "unsupported write type";

        myExp.codeGen(); // evaluate leaving value on the stack
        G.genPop(G.A0); // pop the top-of-stack value
        // set syscall register register
        if (expressionType.isStringType())
            G.generate("li", G.V0, 4);
        else
            G.generate("li", G.V0, 1);
        G.generateWithComment("syscall", "write");
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    // 1 kid
    private ExpNode myExp;
}


class IfStmtNode extends StmtNode implements Declaration, Statement {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public List<DeclNode> getDeclarationList() {
        // add immediate declarations
        ArrayList<DeclNode> l = new ArrayList<>(myDeclList.myDecls);
        // add nested declarations
        for (StmtNode s : myStmtList.myStmts) {
            if (!(s instanceof Declaration)) continue; // skip non declarations
            l.addAll(((Declaration) s).getDeclarationList());
        }

        return l;
    }

    public List<StmtNode> getStatementList() {
        ArrayList<StmtNode> l = new ArrayList<>();

        // add immediate declarations
        l.addAll(myStmtList.myStmts);
        // add nested declarations
        for (StmtNode s : myStmtList.myStmts) {
            if (!(s instanceof Statement)) continue; // skip non declarations
            l.addAll(((Statement) s).getStatementList());
        }

        return l;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - process the condition -
     * enter a new scope - process the decls and stmts - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in IfStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (!type.isErrorType() && !type.isBoolType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Non-bool expression used as an if condition");
        }

        myStmtList.typeCheck(retType);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent + 4);
        myStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void codeGen() {
        // NOTE: using the control-flow method for branching statement
        assert myExp instanceof Condition : "unexpected condition node type";
        Condition myExp = (Condition) this.myExp; // cast to appropriate type

        String trueLabel = G.nextLabel();
        String doneLabel = G.nextLabel();

        myExp.genJumpCode(trueLabel, doneLabel); // evaluate condition & jump
        G.genLabel(trueLabel, "case: true"); // true case
        if (myStmtList != null) myStmtList.codeGen();
        G.genLabel(doneLabel, "case: false"); // false case
    }

    // e kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}


class IfElseStmtNode extends StmtNode implements Declaration, Statement {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1, StmtListNode slist1,
            DeclListNode dlist2, StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public List<DeclNode> getDeclarationList() {
        ArrayList<DeclNode> l = new ArrayList<>();
        // add immediate and nested declarations in order
        l.addAll(myThenDeclList.myDecls);
        for (StmtNode s : myThenStmtList.myStmts) {
            if (!(s instanceof Declaration)) continue; // skip non declarations
            l.addAll(((Declaration) s).getDeclarationList());
        }
        l.addAll(myElseDeclList.myDecls);
        for (StmtNode s : myElseStmtList.myStmts) {
            if (!(s instanceof Declaration)) continue; // skip non declarations
            l.addAll(((Declaration) s).getDeclarationList());
        }

        return l;
    }

    public List<StmtNode> getStatementList() {
        ArrayList<StmtNode> l = new ArrayList<>();

        // add immediate declarations
        l.addAll(myThenStmtList.myStmts);
        // add nested declarations
        for (StmtNode s : myThenStmtList.myStmts) {
            if (!(s instanceof Statement)) continue; // skip non declarations
            l.addAll(((Statement) s).getStatementList());
        }

        // add immediate declarations
        l.addAll(myElseStmtList.myStmts);
        // add nested declarations
        for (StmtNode s : myElseStmtList.myStmts) {
            if (!(s instanceof Statement)) continue; // skip non declarations
            l.addAll(((Statement) s).getStatementList());
        }

        return l;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - process the condition -
     * enter a new scope - process the decls and stmts of then - exit the scope
     * - enter a new scope - process the decls and stmts of else - exit the
     * scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myThenDeclList.nameAnalysis(symTab);
        myThenStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in IfElseStmtNode.nameAnalysis");
            System.exit(-1);
        }
        symTab.addScope();
        myElseDeclList.nameAnalysis(symTab);
        myElseStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in IfElseStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (!type.isErrorType() && !type.isBoolType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Non-bool expression used as an if condition");
        }

        myThenStmtList.typeCheck(retType);
        myElseStmtList.typeCheck(retType);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent + 4);
        myThenStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
        addIndentation(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent + 4);
        myElseStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void codeGen() {
        // NOTE: using the control-flow method for branching statement
        assert myExp instanceof Condition : "unexpected condition node type";
        Condition myExp = (Condition) this.myExp; // cast to appropriate type

        String trueLabel = G.nextLabel();
        String falseLabel = G.nextLabel();
        String doneLabel = G.nextLabel();

        myExp.genJumpCode(trueLabel, falseLabel); // evaluate condition & jump

        // case: true
        G.genLabel(trueLabel, "case: true");
        if (myThenStmtList != null) myThenStmtList.codeGen();
        G.generateWithComment("b", doneLabel, "jump: done");

        // case: false
        G.genLabel(falseLabel, "case: false");
        if (myElseStmtList != null) myElseStmtList.codeGen();

        G.genLabel(doneLabel, "done branching"); // done
    }

    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private DeclListNode myElseDeclList;
    private StmtListNode myElseStmtList;
}


class WhileStmtNode extends StmtNode implements Declaration, Statement {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public List<DeclNode> getDeclarationList() {
        // add immediate declarations
        ArrayList<DeclNode> l = new ArrayList<>(myDeclList.myDecls);
        // add nested declarations
        for (StmtNode s : myStmtList.myStmts) {
            if (!(s instanceof Declaration)) continue; // skip non declarations
            l.addAll(((Declaration) s).getDeclarationList());
        }
        return l;
    }

    public List<StmtNode> getStatementList() {
        // add immediate declarations
        ArrayList<StmtNode> l = new ArrayList<>(myStmtList.myStmts);
        // add nested declarations
        for (StmtNode s : myStmtList.myStmts) {
            if (!(s instanceof Statement)) continue; // skip non declarations
            l.addAll(((Statement) s).getStatementList());
        }
        return l;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - process the condition -
     * enter a new scope - process the decls and stmts - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in WhileStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (!type.isErrorType() && !type.isBoolType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Non-bool expression used as a while condition");
        }

        myStmtList.typeCheck(retType);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent + 4);
        myStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void codeGen() {
        // NOTE: using the control-flow method for branching statement
        assert myExp instanceof Condition : "unexpected condition node type";
        Condition myExp = (Condition) this.myExp; // cast to appropriate type

        String whileLabel = G.nextLabel();
        String trueLabel = G.nextLabel();
        String doneLabel = G.nextLabel();

        G.genLabel(whileLabel, "while block:"); // start of while block

        myExp.genJumpCode(trueLabel, doneLabel); // evaluate condition & jump

        // true case
        G.genLabel(trueLabel, "case: true");
        if (myStmtList != null) myStmtList.codeGen();
        G.generateWithComment("b", whileLabel, "jump back: while");

        // false case (done with while)
        G.genLabel(doneLabel, "case: false");
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}


// ❌ NOT REQUIRED
class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - process the condition -
     * enter a new scope - process the decls and stmts - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in RepeatStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        Type type = myExp.typeCheck();

        if (!type.isErrorType() && !type.isIntType()) {
            ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                    "Non-integer expression used as a repeat clause");
        }

        myStmtList.typeCheck(retType);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent + 4);
        myStmtList.unparse(p, indent + 4);
        addIndentation(p, indent);
        p.println("}");
    }

    public void codeGen() {
        // ignore repeat statemnt (as per spec)
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}


class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myCall.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        myCall.typeCheck();
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

    public void codeGen() {
        // generate jump-and-link insrtuction using label of function
        myCall.codeGen();
        // ignore value when function invocation used as a statement
        G.genPop(G.T0); // pop vaue pushed by CallExpNode
    }


    // 1 kid
    private CallExpNode myCall;
}


class ReturnStmtNode extends StmtNode {
    public String epilogueLabel; // corresponding function epilogue

    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child, if it has one
     */
    public void nameAnalysis(SymTable symTab) {
        if (myExp != null) {
            myExp.nameAnalysis(symTab);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type retType) {
        if (myExp != null) { // return value given
            Type type = myExp.typeCheck();

            if (retType.isVoidType()) {
                ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                        "Return with a value in a void function");
            }

            else if (!retType.isErrorType() && !type.isErrorType()
                    && !retType.equals(type)) {
                ErrMsg.fatal(myExp.lineNum(), myExp.charNum(),
                        "Bad return value");
            }
        }

        else { // no return value given -- ok if this is a void function
            if (!retType.isVoidType()) {
                ErrMsg.fatal(0, 0, "Missing return value");
            }
        }

    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

    public void codeGen() {
        assert epilogueLabel != null : "Error: epilogueLabel for return statement must be set";

        if (myExp != null) {
            myExp.codeGen(); // pushes evaluation into stack

            // pop the value from stack into appropriate register (V0 or F0)
            // NOTE: double values are unsupported.s
            G.genPop(G.V0);
        }

        G.generateWithComment("b", epilogueLabel, "jump: epilogue");
    }

    // 1 kid
    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************


abstract class ExpNode extends ASTnode implements CodeGeneration {
    /**
     * Default version for nodes with no names
     */
    public void nameAnalysis(SymTable symTab) {}

    abstract public Type typeCheck();

    abstract public int lineNum();

    abstract public int charNum();
}


class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new IntType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }

    public void codeGen() {
        // push value onto stack
        G.generate("li", G.T0, myIntVal);
        G.genPush(G.T0);
    }

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}


class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new StringType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }

    /**
     * generate a label for the string literal or retieve a existing equivalent
     * 
     * @return true if new label generated, otherwise false for existing equiv.
     */
    private boolean generateLabel() {
        if (!G.stringMap.containsKey(myStrVal)) {
            G.stringMap.put(myStrVal, G.nextLabel());
            return true;
        }
        return false;
    }

    public void codeGen() {
        boolean isNewString = generateLabel();
        String stringLabel = G.stringMap.get(myStrVal);

        // store string in static data area
        if (isNewString) {
            G.generate(".data");
            G.generateLabeled(stringLabel, ".asciiz ", myStrVal, "string");
            G.generate(".text"); // reset memory segment
        }

        // push address of string literal onto stack
        G.generate("la", G.T0, stringLabel);
        G.genPush(G.T0);
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}


class TrueNode extends ExpNode implements Condition {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new BoolType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }

    public void codeGen() {
        G.generate("li", G.T0, G.TRUE);
        G.genPush(G.T0);
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        G.generate("b", trueLabel);
    }

    private int myLineNum;
    private int myCharNum;
}


class FalseNode extends ExpNode implements Condition {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new BoolType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }

    public void codeGen() {
        G.generate("li", G.T0, G.FALSE);
        G.genPush(G.T0);
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        G.generate("b", falseLabel);
    }

    private int myLineNum;
    private int myCharNum;
}


class IdNode extends ExpNode implements Condition {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    /**
     * Link the given symbol to this ID.
     */
    public void link(TSym sym) {
        mySym = sym;
    }

    /**
     * Return the name of this ID.
     */
    public String name() {
        return myStrVal;
    }

    /**
     * Return the symbol associated with this ID.
     */
    public TSym sym() {
        return mySym;
    }

    /**
     * Return the line number for this ID.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this ID.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - check for use of
     * undeclared name - if ok, link to symbol table entry
     */
    public void nameAnalysis(SymTable symTab) {
        TSym sym = null;

        try {
            sym = symTab.lookupGlobal(myStrVal);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException "
                    + " in IdNode.nameAnalysis");
            System.exit(-1);
        }

        if (sym == null) {
            ErrMsg.fatal(myLineNum, myCharNum, "Undeclared identifier");
        } else {
            link(sym);
        }
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        if (mySym != null) {
            return mySym.getType();
        } else {
            System.err.println("ID with null sym field in IdNode.typeCheck");
            System.exit(-1);
        }
        return null;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
        if (mySym != null) {
            p.print("(" + mySym + ")");
        }
    }

    // fetch identifier's value onto stack
    public void codeGen() {
        // fetch current value (either from static data area or current AR)
        if (mySym.isGlobal()) {
            String funcLabel = // function label ("main" or _<name>)
                    (this.name().equals("main") ? "" : "_") + this.name();
            G.generate("lw", G.T0, funcLabel);

        } else
            G.generateIndexed("lw", G.T0, G.FP, mySym.getOffset(),
                    "variable: " + name());

        G.genPush(G.T0); // push value onto stack
    }

    // fetch identifier's address onto stack
    public void genAddr() {
        if (mySym.isGlobal()) {
            String funcLabel = // function label ("main" or _<name>)
                    (this.name().equals("main") ? "" : "_") + this.name();

            G.generate("la", G.T0, funcLabel);
        } else
            G.generateIndexed("la", G.T0, G.FP, mySym.getOffset(),
                    "variable: " + name());

        G.genPush(G.T0); // push value onto stack
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        assert mySym.getType()
                .isBoolType() : "unexpected variable type for condition";

        // retrieve variable value
        genAddr(); // get address onto stack
        G.genPop(G.T0);
        G.generateIndexed("lw", G.T0, G.T0, 0, "load value"); // get value

        G.generate("beq", G.T0, G.ZERO, falseLabel);
        G.generate("b", trueLabel);
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
    private TSym mySym;
}


// ❌ NOT REQUIRED
class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;
        myId = id;
        mySym = null;
    }

    /**
     * Return the symbol associated with this dot-access node.
     */
    public TSym sym() {
        return mySym;
    }

    /**
     * Return the line number for this dot-access node. The line number is the
     * one corresponding to the RHS of the dot-access.
     */
    public int lineNum() {
        return myId.lineNum();
    }

    /**
     * Return the char number for this dot-access node. The char number is the
     * one corresponding to the RHS of the dot-access.
     */
    public int charNum() {
        return myId.charNum();
    }

    /**
     * nameAnalysis Given a symbol table symTab, do: - process the LHS of the
     * dot-access - process the RHS of the dot-access - if the RHS is of a
     * struct type, set the sym for this node so that a dot-access "higher up"
     * in the AST can get access to the symbol table for the appropriate struct
     * definition
     */
    public void nameAnalysis(SymTable symTab) {
        badAccess = false;
        SymTable structSymTab = null; // to lookup RHS of dot-access
        TSym sym = null;

        myLoc.nameAnalysis(symTab); // do name analysis on LHS

        // if myLoc is really an ID, then sym will be a link to the ID's symbol
        if (myLoc instanceof IdNode) {
            IdNode id = (IdNode) myLoc;
            sym = id.sym();

            // check ID has been declared to be of a struct type

            if (sym == null) { // ID was undeclared
                badAccess = true;
            } else if (sym instanceof StructSym) {
                // get symbol table for struct type
                TSym tempSym = ((StructSym) sym).getStructType().sym();
                structSymTab = ((StructDefSym) tempSym).getSymTable();
            } else { // LHS is not a struct type
                ErrMsg.fatal(id.lineNum(), id.charNum(),
                        "Dot-access of non-struct type");
                badAccess = true;
            }
        }

        // if myLoc is really a dot-access (i.e., myLoc was of the form
        // LHSloc.RHSid), then sym will either be
        // null - indicating RHSid is not of a struct type, or
        // a link to the TSym for the struct type RHSid was declared to be
        else if (myLoc instanceof DotAccessExpNode) {
            DotAccessExpNode loc = (DotAccessExpNode) myLoc;

            if (loc.badAccess) { // if errors in processing myLoc
                badAccess = true; // don't continue proccessing this dot-access
            } else { // no errors in processing myLoc
                sym = loc.sym();

                if (sym == null) { // no struct in which to look up RHS
                    ErrMsg.fatal(loc.lineNum(), loc.charNum(),
                            "Dot-access of non-struct type");
                    badAccess = true;
                } else { // get the struct's symbol table in which to lookup RHS
                    if (sym instanceof StructDefSym) {
                        structSymTab = ((StructDefSym) sym).getSymTable();
                    } else {
                        System.err.println(
                                "Unexpected Sym type in DotAccessExpNode");
                        System.exit(-1);
                    }
                }
            }

        }

        else { // don't know what kind of thing myLoc is
            System.err.println("Unexpected node type in LHS of dot-access");
            System.exit(-1);
        }

        // do name analysis on RHS of dot-access in the struct's symbol table
        if (!badAccess) {

            try {
                sym = structSymTab.lookupGlobal(myId.name()); // lookup
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException "
                        + " in DotAccessExpNode.nameAnalysis");
            }

            if (sym == null) { // not found - RHS is not a valid field name
                ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                        "Invalid struct field name");
                badAccess = true;
            }

            else {
                myId.link(sym); // link the symbol
                // if RHS is itself as struct type, link the symbol for its
                // struct
                // type to this dot-access node (to allow chained dot-access)
                if (sym instanceof StructSym) {
                    mySym = ((StructSym) sym).getStructType().sym();
                }
            }
        }
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return myId.typeCheck();
    }

    public void unparse(PrintWriter p, int indent) {
        myLoc.unparse(p, 0);
        p.print(".");
        myId.unparse(p, 0);
    }

    public void codeGen() {
        // ignore struct related statemnts (as per spec)
    }

    // 2 kids
    private ExpNode myLoc;
    private IdNode myId;
    private TSym mySym; // link to TSym for struct type
    private boolean badAccess; // to prevent multiple, cascading errors
}


class AssignNode extends ExpNode implements Condition {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }

    /**
     * Return the line number for this assignment node. The line number is the
     * one corresponding to the left operand.
     */
    public int lineNum() {
        return myLhs.lineNum();
    }

    /**
     * Return the char number for this assignment node. The char number is the
     * one corresponding to the left operand.
     */
    public int charNum() {
        return myLhs.charNum();
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's two children
     */
    public void nameAnalysis(SymTable symTab) {
        myLhs.nameAnalysis(symTab);
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type typeLhs = myLhs.typeCheck();
        Type typeExp = myExp.typeCheck();
        Type retType = typeLhs;

        if (typeLhs.isFnType() && typeExp.isFnType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Function assignment");
            retType = new ErrorType();
        }

        if (typeLhs.isStructDefType() && typeExp.isStructDefType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Struct name assignment");
            retType = new ErrorType();
        }

        if (typeLhs.isStructType() && typeExp.isStructType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Struct variable assignment");
            retType = new ErrorType();
        }

        if (!typeLhs.equals(typeExp) && !typeLhs.isErrorType()
                && !typeExp.isErrorType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Type mismatch");
            retType = new ErrorType();
        }

        if (typeLhs.isErrorType() || typeExp.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1) p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1) p.print(")");
    }

    public void codeGen() {
        assert myLhs instanceof IdNode : "expected LHS to be of type IdNode";
        IdNode myLhs = (IdNode) this.myLhs;

        myExp.codeGen(); // evaluate RHS value onto stack
        myLhs.genAddr(); // push LHS address onto stack

        // Store the value into the address
        G.genPop(G.T0); // address`
        G.genPop(G.T1); // value
        G.generateIndexed("sw", G.T1, G.T0, 0, "assign to address");

        G.genPush(G.T1); // keep a copy of value onto stack
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        this.codeGen(); // evaluate expression onto stack

        G.genPop(G.T0);
        G.generate("beq", G.T0, G.FALSE, falseLabel);
        G.generate("b", trueLabel);
    }

    // 2 kids
    private ExpNode myLhs;
    private ExpNode myExp;
}


class CallExpNode extends ExpNode implements Condition {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    /**
     * Return the line number for this call node. The line number is the one
     * corresponding to the function name.
     */
    public int lineNum() {
        return myId.lineNum();
    }

    /**
     * Return the char number for this call node. The char number is the one
     * corresponding to the function name.
     */
    public int charNum() {
        return myId.charNum();
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's two children
     */
    public void nameAnalysis(SymTable symTab) {
        myId.nameAnalysis(symTab);
        myExpList.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        if (!myId.typeCheck().isFnType()) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Attempt to call a non-function");
            return new ErrorType();
        }

        FnSym fnSym = (FnSym) (myId.sym());

        if (fnSym == null) {
            System.err.println("null sym for Id in CallExpNode.typeCheck");
            System.exit(-1);
        }

        if (myExpList.size() != fnSym.getNumParams()) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                    "Function call with wrong number of args");
            return fnSym.getReturnType();
        }

        myExpList.typeCheck(fnSym.getParamTypes());
        return fnSym.getReturnType();
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }

    public void codeGen() {
        final String functionLabel =
                (!myId.name().equals("main") ? "" : "_") + myId.name();

        // evaluate arguments & push to stack
        if (myExpList != null) myExpList.codeGen();
        // jump-and-link using appropriate label for target function
        G.generateWithComment("jal", functionLabel, "call");
        // handle return value (pushing value will not cause issue for void)
        G.genPush(G.V0);
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        this.codeGen(); // evaluate expression onto stack

        G.genPop(G.T0);
        G.generate("beq", G.T0, G.FALSE, falseLabel);
        G.generate("b", trueLabel);
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList; // possibly null
}


abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }

    /**
     * Return the line number for this unary expression node. The line number is
     * the one corresponding to the operand.
     */
    public int lineNum() {
        return myExp.lineNum();
    }

    /**
     * Return the char number for this unary expression node. The char number is
     * the one corresponding to the operand.
     */
    public int charNum() {
        return myExp.charNum();
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    // one child
    protected ExpNode myExp;
}


abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }

    /**
     * Return the line number for this binary expression node. The line number
     * is the one corresponding to the left operand.
     */
    public int lineNum() {
        return myExp1.lineNum();
    }

    /**
     * Return the char number for this binary expression node. The char number
     * is the one corresponding to the left operand.
     */
    public int charNum() {
        return myExp1.charNum();
    }

    /**
     * nameAnalysis Given a symbol table symTab, perform name analysis on this
     * node's two children
     */
    public void nameAnalysis(SymTable symTab) {
        myExp1.nameAnalysis(symTab);
        myExp2.nameAnalysis(symTab);
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************


class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type = myExp.typeCheck();
        Type retType = new IntType();

        if (!type.isErrorType() && !type.isIntType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                    "Arithmetic operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (type.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp.codeGen();

        // perform operation
        G.genPop(G.T0);
        G.generateWithComment("sub", G.T0, G.ZERO, G.T0, "negate value");
        G.genPush(G.T0); // push result onto stack
    }
}


class NotNode extends UnaryExpNode implements Condition {
    public NotNode(ExpNode exp) {
        super(exp);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type = myExp.typeCheck();
        Type retType = new BoolType();

        if (!type.isErrorType() && !type.isBoolType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                    "Logical operator applied to non-bool operand");
            retType = new ErrorType();
        }

        if (type.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp.codeGen();

        // perform operation
        G.genPop(G.T0);
        G.generateWithComment("xori", G.T0, G.T0, 1, "flip bits");
        G.genPush(G.T0); // push result onto stack
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        assert myExp instanceof Condition : "unexpected condition expression type";
        Condition myExp = (Condition) this.myExp;

        myExp.genJumpCode(falseLabel, trueLabel); // reverse logic
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************


abstract class ArithmeticExpNode extends BinaryExpNode {
    public ArithmeticExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new IntType();

        if (!type1.isErrorType() && !type1.isIntType()) {
            ErrMsg.fatal(myExp1.lineNum(), myExp1.charNum(),
                    "Arithmetic operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (!type2.isErrorType() && !type2.isIntType()) {
            ErrMsg.fatal(myExp2.lineNum(), myExp2.charNum(),
                    "Arithmetic operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }
}


abstract class LogicalExpNode extends BinaryExpNode implements Condition {
    public LogicalExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new BoolType();

        if (!type1.isErrorType() && !type1.isBoolType()) {
            ErrMsg.fatal(myExp1.lineNum(), myExp1.charNum(),
                    "Logical operator applied to non-bool operand");
            retType = new ErrorType();
        }

        if (!type2.isErrorType() && !type2.isBoolType()) {
            ErrMsg.fatal(myExp2.lineNum(), myExp2.charNum(),
                    "Logical operator applied to non-bool operand");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }
}


abstract class EqualityExpNode extends BinaryExpNode implements Condition {
    public EqualityExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new BoolType();

        if (type1.isVoidType() && type2.isVoidType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                    "Equality operator applied to void functions");
            retType = new ErrorType();
        }

        if (type1.isFnType() && type2.isFnType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                    "Equality operator applied to functions");
            retType = new ErrorType();
        }

        if (type1.isStructDefType() && type2.isStructDefType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                    "Equality operator applied to struct names");
            retType = new ErrorType();
        }

        if (type1.isStructType() && type2.isStructType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                    "Equality operator applied to struct variables");
            retType = new ErrorType();
        }

        if (!type1.equals(type2) && !type1.isErrorType()
                && !type2.isErrorType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Type mismatch");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        this.codeGen(); // evaluate expression onto stack

        G.genPop(G.T0);
        G.generate("beq", G.T0, G.FALSE, falseLabel);
        G.generate("b", trueLabel);
    }
}


abstract class RelationalExpNode extends BinaryExpNode implements Condition {
    public RelationalExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new BoolType();

        if (!type1.isErrorType() && !type1.isIntType()) {
            ErrMsg.fatal(myExp1.lineNum(), myExp1.charNum(),
                    "Relational operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (!type2.isErrorType() && !type2.isIntType()) {
            ErrMsg.fatal(myExp2.lineNum(), myExp2.charNum(),
                    "Relational operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        this.codeGen(); // evaluate expression onto stack

        G.genPop(G.T0);
        G.generate("beq", G.T0, G.FALSE, falseLabel);
        G.generate("b", trueLabel);
    }
}


class PlusNode extends ArithmeticExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public String getInstruction() {
        return "";
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 + T1)
        G.generateWithComment("add", G.T0, G.T0, G.T1, "arithmetic");
        G.genPush(G.T0); // push result onto stack
    }
}


class MinusNode extends ArithmeticExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 - T1)
        G.generateWithComment("sub", G.T0, G.T0, G.T1, "arithmetic");
        G.genPush(G.T0); // push result onto stack
    }
}


class TimesNode extends ArithmeticExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }


    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 * T1)
        G.generateWithComment("mulo", G.T0, G.T0, G.T1, "arithmetic");
        G.genPush(G.T0); // push result onto stack
    }
}


class DivideNode extends ArithmeticExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 / T1)
        G.generateWithComment("div", G.T0, G.T0, G.T1, "arithmetic");
        G.genPush(G.T0); // push result onto stack
    }
}


// Short-circuited operator
class AndNode extends LogicalExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        String doneLabel = G.nextLabel();

        myExp1.codeGen(); // evaluate left operand

        // branch
        G.generateIndexed("lw", G.T0, G.SP, 4, "LOAD");
        G.generate("beq", G.T0, G.FALSE, doneLabel);

        // case: true (resutl depends on right operand)
        G.genPop(G.T0);
        myExp2.codeGen();
        // G.genPop(G.T1);
        // perform insrtuction (T0 = T0 && T1)
        // G.generate("and", G.T0, G.T0, G.T1);
        // G.genPush(G.T0); // push result onto stack

        // case: false
        // leave value of left operand on stack
        G.genLabel(doneLabel);
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        assert myExp1 instanceof Condition
                && myExp2 instanceof Condition : "unsupported expression in place of condition";
        Condition myExp1 = (Condition) this.myExp1;
        Condition myExp2 = (Condition) this.myExp2;

        String secondEvaluationLabel = G.nextLabel();
        myExp1.genJumpCode(secondEvaluationLabel, falseLabel);
        // evaluate second operand
        G.genLabel(secondEvaluationLabel);
        myExp2.genJumpCode(trueLabel, falseLabel);
    }

}


// Short-circuited operator
class OrNode extends LogicalExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        String doneLabel = G.nextLabel();

        myExp1.codeGen(); // evaluate left operand

        // branch
        G.generateIndexed("lw", G.T0, G.SP, 4, "LOAD");
        G.generate("beq", G.T0, G.TRUE, doneLabel);

        // case: false (resutl depends on right operand)
        G.genPop(G.T0);
        myExp2.codeGen();
        // G.genPop(G.T1);
        // perform insrtuction (T0 = T0 || T1)
        // G.generate("or", G.T0, G.T0, G.T1);
        // G.genPush(G.T0); // push result onto stack

        // case: true
        // leave value of left operand on stack
        G.genLabel(doneLabel);
    }

    public void genJumpCode(String trueLabel, String falseLabel) {
        assert myExp1 instanceof Condition
                && myExp2 instanceof Condition : "unsupported expression in place of condition";
        Condition myExp1 = (Condition) this.myExp1;
        Condition myExp2 = (Condition) this.myExp2;

        String secondEvaluationLabel = G.nextLabel();
        myExp1.genJumpCode(trueLabel, secondEvaluationLabel);
        // evaluate second operand
        G.genLabel(secondEvaluationLabel);
        myExp2.genJumpCode(trueLabel, falseLabel);
    }

}


class EqualsNode extends EqualityExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 == T1)
        G.generateWithComment("seq", G.T0, G.T0, G.T1, "equality");
        G.genPush(G.T0); // push result onto stack
    }
}


class NotEqualsNode extends EqualityExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 != T1)
        G.generateWithComment("sne", G.T0, G.T0, G.T1, "equality");
        G.genPush(G.T0); // push result onto stack
    }
}


class LessNode extends RelationalExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 < T1)
        G.generateWithComment("slt", G.T0, G.T0, G.T1, "relational");
        G.genPush(G.T0); // push result onto stack
    }
}


class GreaterNode extends RelationalExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 > T1)
        G.generateWithComment("sgt", G.T0, G.T0, G.T1, "relational");
        G.genPush(G.T0); // push result onto stack
    }
}


class LessEqNode extends RelationalExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 <= T1)
        G.generateWithComment("sle", G.T0, G.T0, G.T1, "relational");
        G.genPush(G.T0); // push result onto stack
    }
}


class GreaterEqNode extends RelationalExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }

    public void codeGen() {
        // evaluate operands into stack (for 2 operands: right will be on top)
        myExp1.codeGen(); // push T0
        myExp2.codeGen(); // push T1

        G.genPop(G.T1);
        G.genPop(G.T0);
        // perform operation (T0 = T0 >= T1)
        G.generateWithComment("sge", G.T0, G.T0, G.T1, "relational");
        G.genPush(G.T0); // push result onto stack
    }

}
