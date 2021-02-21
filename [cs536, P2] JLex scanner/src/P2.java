///////////////////////////////////////////////////////////////////////////////
// Title: P2 assignment
// Files: P2.java, sym.java, ErrMsg.java, cminusminus.java, & dependencies
// Semester: CS536 Spring 2021
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// CS Login: safi@cs.wisc.edu
// Lecturer's Name: Loris D'Antoni
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java_cup.runtime.*; // defines Symbol

/**
 * configuration class for controling testing bahavior
 */
class config {
    // test directories reltivate path
    final public static String INPUT_DIRECTORY = "./test/in/";
    final public static String OUTPUT_DIRECTORY = "./test/out/";
}


/**
 * P2 class: tests the test the C-- scanner testing all tokens, and related
 * classes. e.g., input that causes errors, character numbers, values associated
 * with tokens
 * 
 * @author Safi
 */
public class P2 {
    /**
     * test driver: calls unit test methods
     * 
     * @param args input arguments if any
     */
    public static void main(String[] args) throws IOException {
        // exception may be thrown by yylex
        // test all tokens
        testAllTokens();
        CharNum.num = 1;
        // another test
        anotherTest();
    }

    /**
     * testAllTokens
     *
     * Open and read from file allTokens.txt For each token read, write the
     * corresponding string to allTokens.out If the input file contains all
     * tokens, one per line, we can verify correctness of the scanner by
     * comparing the input and output files (e.g., using a 'diff' command).
     */
    private static void testAllTokens() throws IOException {
        Path inPath = Paths.get(config.INPUT_DIRECTORY, "allTokens.in");
        Path outPath = Paths.get(config.OUTPUT_DIRECTORY, "allTokens.out");
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader(inPath.toString());
            outFile = new PrintWriter(new FileWriter(outPath.toString()));
        } catch (FileNotFoundException ex) {
            System.err.println("File allTokens.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("allTokens.out cannot be opened.");
            System.exit(-1);
        }

        runScanner(inFile, outFile);
        outFile.close();
    }

    private static void anotherTest() throws IOException {
    }

    /**
     * runs scanner - pipes input strings through scanner to an output file,
     * writing the analyzed tokens
     * 
     * @param in  input stream of strings
     * @param out output stream for writing tokens to
     */
    private static void runScanner(FileReader in, PrintWriter out)
            throws IOException {
        // create and call the scanner
        Yylex scanner = new Yylex(in);
        Symbol token = scanner.next_token();
        while (token.sym != sym.EOF) {
            System.out.printf("→ %s\n", getTokenValue(token));
            out.println(getTokenValue(token));
            token = scanner.next_token();
        }
    }

    /**
     * retrieve the appropriate token value corresponding to a symbol
     * 
     * @param token
     */
    private static String getTokenValue(Symbol token) {
        switch (token.sym) {
            case sym.BOOL:
                return "bool";
            case sym.INT:
                return "int";
            case sym.VOID:
                return "void";
            case sym.TRUE:
                return "true";
            case sym.FALSE:
                return "false";
            case sym.STRUCT:
                return "struct";
            case sym.CIN:
                return "cin";
            case sym.COUT:
                return "cout";
            case sym.IF:
                return "if";
            case sym.ELSE:
                return "else";
            case sym.WHILE:
                return "while";
            case sym.RETURN:
                return "return";
            case sym.ID:
                return ((IdTokenVal) token.value).idVal;
            case sym.INTLITERAL:
                return Integer.toString(((IntLitTokenVal) token.value).intVal);
            case sym.STRINGLITERAL:
                return ((StrLitTokenVal) token.value).strVal;
            case sym.LCURLY:
                return "{";
            case sym.RCURLY:
                return "}";
            case sym.LPAREN:
                return "(";
            case sym.RPAREN:
                return ")";
            case sym.SEMICOLON:
                return ";";
            case sym.COMMA:
                return ",";
            case sym.DOT:
                return ".";
            case sym.WRITE:
                return "<<";
            case sym.READ:
                return ">>";
            case sym.PLUSPLUS:
                return "++";
            case sym.MINUSMINUS:
                return "--";
            case sym.PLUS:
                return "+";
            case sym.MINUS:
                return "-";
            case sym.TIMES:
                return "*";
            case sym.DIVIDE:
                return "/";
            case sym.NOT:
                return "!";
            case sym.AND:
                return "&&";
            case sym.OR:
                return "||";
            case sym.EQUALS:
                return "==";
            case sym.NOTEQUALS:
                return "!=";
            case sym.LESS:
                return "<";
            case sym.GREATER:
                return ">";
            case sym.LESSEQ:
                return "<=";
            case sym.GREATEREQ:
                return ">=";
            case sym.ASSIGN:
                return "=";
            default:
                return "UNKNOWN TOKEN";
        } // end switch
    }
}
