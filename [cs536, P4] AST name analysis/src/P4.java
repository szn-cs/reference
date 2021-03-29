import java.io.*;
import java.util.*;
import java_cup.runtime.*;

/**
 * Main program to test the C-- parser.
 *
 * There should be 2 command-line arguments: 1. the file to be parsed 2. the
 * output file into which the AST built by the parser should be unparsed
 */

public class P4 {
    public static void main(String[] args) throws IOException // may be thrown
                                                              // by the scanner
    {
        // check for command-line args
        if (args.length != 2) {
            System.err.println("please supply name of file to be parsed "
                    + "and name of file for unparsed version.");
            System.exit(-1);
        }

        // open input file
        FileReader inFile = null;
        try {
            inFile = new FileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("File " + args[0] + " not found.");
            System.exit(-1);
        }

        // open output file
        PrintWriter outFile = null;
        try {
            outFile = new PrintWriter(args[1]);
        } catch (FileNotFoundException ex) {
            System.err.println(
                    "File " + args[1] + " could not be opened for writing.");
            System.exit(-1);
        }


        Symbol root = null; // the parser will return a Symbol whose value
        // field is the translation of the root nonterminal
        // (i.e., of the nonterminal "program")
        ASTnode rootNode;

        /** Lexical & Syntax analysis */
        try {
            parser P = new parser(new Yylex(inFile));
            root = P.parse(); // do the parse
            System.out.println("program parsed correctly.");
        } catch (Exception ex) {
            System.err.println("Exception occured during parse: " + ex);
            System.exit(-1); // exit on syntax error
        }

        rootNode = (ASTnode) root.value;

        /** Static semantic name analysis */
        try {
            traverse(rootNode); // traverse & visit each node
            System.out.println("program name analyzed correctly.");
        } catch (Exception ex) {
            System.err.println("Exception occured during name analyze: " + ex);
            System.exit(-1); // exit on name analysis error
        }

        // TODO: After that, if there are no errors so far (either scanning,
        // parsing, or name-analysis errors)

        /** Unparser code generator */
        rootNode.unparse(outFile, 0);
        outFile.close();

        return;
    }

    /**
     * Traverse root node
     * 
     * @param node
     */
    public static void traverse(ASTnode node) throws EmptySymTableException {
        // tracks current scopes state during traveral of the AST (list of
        // symbol hashtables each corresponding to a scope level)
        SymTable l = new SymTable(0);
        traverse(node, l);
    }

    /**
     * Traverse a list of ASTnode trees
     */
    public static void traverse(List<? extends ASTnode> nodeList, SymTable l)
            throws EmptySymTableException {
        for (ASTnode n : nodeList)
            if (n instanceof Visitable)
                traverse(n, l);
    }

    /**
     * Traverse ASTnode tree
     */
    public static void traverse(ASTnode n, SymTable l)
            throws EmptySymTableException {
        // visit each node and manipulate the state of the scope chain and
        // symbols hashtables as needed.
        try {
            ((Visitable) n).visit(l);
        } catch (Exception e) {
            System.out.println(e.toString());
        }


        // traverse children
        if (n instanceof Iterable) {
            Iterator<IterationConfig> iterator = ((Iterable) n).getChildren();

            while (iterator.hasNext()) {
                IterationConfig config = iterator.next();

                if (config.newScope)
                    l.addScope(); // create new scope for children nodes

                if (!config.list.isEmpty())
                    traverse(config.list, l);

                if (config.newScope)
                    l.removeScope(); // discard scope
            }
        }

        System.out.println(n.toString() + " visited & children");
        l.print();
    }

}
