package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PrintFile {
    public static void main(String[] args) {
        if ( args.length >= 1) {
            File file = new File( args[0]);
            Scanner inFile = null;
            try {
                inFile = new Scanner( file );
                int lineNumber = 1;
                while ( inFile.hasNextLine()) {
                    String line = inFile.nextLine();
                    System.out.printf("%3d %s\n", lineNumber++, line);
                }                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if ( inFile != null) {
                    inFile.close();
                }
            }
        }
    }

}


