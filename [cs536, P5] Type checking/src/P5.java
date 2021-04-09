import java.io.*;
import java_cup.runtime.*;


abstract class functionMachine {
    public FileReader inFile;
    public PrintWriter outFile;
    public static PrintStream outStream = System.err;

    public functionMachine() {
    }

    public functionMachine(String in, String out) {
        try {
            setInfile(in);
            setOutfile(out);
        } catch (BadInfileException e) {
            pukeAndDie(e.getMessage());
        } catch (BadOutfileException e) {
            pukeAndDie(e.getMessage());
        }
    }

    /**
     * Source code file path
     * 
     * @param filename path to source file
     */
    public void setInfile(String filename) throws BadInfileException {
        try {
            inFile = new FileReader(filename);
        } catch (FileNotFoundException ex) {
            throw new BadInfileException(ex, filename);
        }
    }

    /**
     * Text file output
     * 
     * @param filename path to destination file
     */
    public void setOutfile(String filename) throws BadOutfileException {
        try {
            outFile = new PrintWriter(filename);
        } catch (FileNotFoundException ex) {
            throw new BadOutfileException(ex, filename);
        }
    }

    /**
     * Perform cleanup at the end of parsing. This should be called after both
     * good and bad input so that the files are all in a consistent state
     */
    public void cleanup() {
        if (inFile != null) {
            try {
                inFile.close();
            } catch (IOException e) {
                // At this point, users already know they screwed
                // up. No need to rub it in.
            }
        }
        if (outFile != null) {
            // If there is any output that needs to be
            // written to the stream, force it out.
            outFile.flush();
            outFile.close();
        }
    }


    public class BadInfileException extends Exception {
        private static final long serialVersionUID = 1L;
        private String message;

        public BadInfileException(Exception cause, String filename) {
            super(cause);
            this.message = "Could not open " + filename + " for reading";
        }

        @Override
        public String getMessage() {
            return message;
        }
    }


    public class BadOutfileException extends Exception {
        private static final long serialVersionUID = 1L;
        private String message;

        public BadOutfileException(Exception cause, String filename) {
            super(cause);
            this.message = "Could not open " + filename + " for reading";
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    /**
     * Private error handling method. Convenience method for
     * 
     * @link pukeAndDie(String, int) with a default error code
     * @param error message to print on exit
     */
    public void pukeAndDie(String error) {
        pukeAndDie(error, -1);
    }

    /**
     * Private error handling method. Prints an error message
     * 
     * @link pukeAndDie(String, int) with a default error code
     * @param error message to print on exit
     */
    public void pukeAndDie(String error, int retCode) {
        outStream.println(error);
        cleanup();
        System.exit(-1);
    }

}


class Compiler extends functionMachine {
    public static final int RESULT_CORRECT = 0;
    public static final int RESULT_SYNTAX_ERROR = 1;
    public static final int RESULT_NAME_ERROR = 3;
    public static final int RESULT_TYPE_ERROR = 2;
    public static final int RESULT_OTHER_ERROR = -1;

    /**
     * Compiler constructor for client programs and testers. Note that users
     * MUST invoke {@link setInfile} and {@link setOutfile}
     */
    public Compiler() {
        super();
    }

    /**
     * If we are directly invoking P5 from the command line, this is the command
     * line to use. It shouldn't be invoked from outside the class (hence the
     * private constructor) because it
     * 
     * @param args command line args array for [<infile> <outfile>]
     */
    public Compiler(String in, String out) {
        super(in, out);
    }

    /**
     * the parser (lexical and syntax analysis) will return a Symbol whose value
     * field is the translation of the root nonterminal (i.e., of the
     * nonterminal "program")
     * 
     * @return root of the CFG
     */
    private Symbol parseCFG() {
        try {
            parser P = new parser(new Yylex(inFile));
            return P.parse();
        } catch (Exception e) {
            return null;
        }
    }

    public int process() {
        Symbol cfgRoot = parseCFG(); // lexical & syntax analysis

        ProgramNode astRoot = (ProgramNode) cfgRoot.value;
        if (ErrMsg.getErr())
            return Compiler.RESULT_SYNTAX_ERROR;

        astRoot.nameAnalysis(); // semantic name analysis
        if (ErrMsg.getErr())
            return Compiler.RESULT_NAME_ERROR;

        /** semantic type analysis */
        try {
            Traverser.traverse(astRoot); // traverse & visit each node
        } catch (Exception e) {
            System.err.println("Exception occured during type analyze: " + e);
            return Compiler.RESULT_OTHER_ERROR;
        }
        if (ErrMsg.getErr())
            return Compiler.RESULT_TYPE_ERROR;

        astRoot.unparse(outFile, 0); // intermediate code generator
        return Compiler.RESULT_CORRECT;
    }

    /**
     * run compilation and print result status
     */
    public void run() {
        int resultCode = process();
        if (resultCode == RESULT_CORRECT) {
            cleanup();
            return;
        }

        switch (resultCode) {
            case RESULT_SYNTAX_ERROR:
                pukeAndDie("Syntax error", resultCode);
            case RESULT_NAME_ERROR:
                pukeAndDie("Name checking error", resultCode);
            case RESULT_TYPE_ERROR:
                pukeAndDie("Type checking error", resultCode);
            default:
                pukeAndDie("Type checking error", RESULT_OTHER_ERROR);
        }
    }

}


/**
 * Main program to test the parser.
 *
 * There should be 2 command-line arguments: 1. the file to be parsed 2. the
 * output file into which the AST built by the parser should be unparsed The
 * program opens the two files, creates a scanner and a parser, and calls the
 * parser. If the parse is successful, the AST is unparsed.
 */
public class P5 extends Compiler {
    /**
     * P5 constructor for client programs and testers. Note that users MUST
     * invoke {@link setInfile} and {@link setOutfile}
     */
    public P5() {
    }

    public P5(String in, String out) {
        super(in, out);
    }

    public static void main(String[] args) {
        // Parse arguments
        if (args.length < 2) {
            outStream.println("please supply name of file to be parsed"
                    + "and name of file for unparsed version.");
            System.exit(-1);
        }

        Compiler compiler = new P5(args[0], args[1]);
        compiler.run();
    }
}

