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
            return;
            // System.exit(-1); // exit on syntax error
        }

        rootNode = (ASTnode) root.value;

        /** Static semantic name analysis */
        try {
            Traverser.traverse(rootNode); // traverse & visit each node
        } catch (Exception ex) {
            System.err.println("Exception occured during name analyze: " + ex);
            return;
            // System.exit(-1); // exit on name analysis error
        }

        // After that, if there are no errors so far (either scanning, parsing,
        // or name-analysis errors)
        if (ErrMsg.errorOccured) {
            System.err.println(
                    "ErrMsg: Errors detected during name analysis; Skipping unparse execution.");
            return;
            // System.exit(-1); // exit on semantic analysis
        } else {
            System.out.println("program name-analyzed correctly.");
        }


        /** Unparser code generator */
        rootNode.unparse(outFile, 0);
        outFile.close();

        return;
    }

}
