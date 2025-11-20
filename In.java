/******************************************************************************
 *
 *  A library from the Algorithms optional textbook
 *  for reading data of various types from stdin, files, and URLs.
 *
 *  You need to implement your own method in your CW3 program to read the input file.
 *
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The In class provides methods for reading input from various sources such as stdin, files, and URLs.
 */
class In {
    private BufferedReader br;

    /**
     * Constructor for reading input from a file.
     * @param fileName The name of the file to read.
     */
    public In(String fileName) {
        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            br = new BufferedReader(fileReader);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Reads a line of input from the source.
     * @return The line of input read as a String.
     */
    public String readLine() {
        String line = null;
        try {
            line = br.readLine();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return line;
    }

    /**
     * Closes the input source.
     */
    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
