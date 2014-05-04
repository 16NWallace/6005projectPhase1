package pingballClient.parser;

import java.io.*;
import java.util.*;
/**
 * 
 * @author asolei
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        BoardFactory.parse(new File(arguments.remove()));

    }

}
