package pingball;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Testing Strategy:
 * NOTE -- MergeHandler listens for merge messages from the server
 * TEST REGEX
 *      valid message
 *      invalid merge message - make sure checkRep() asserts false
 * TEST GET TOKENS AND CREATION OF MERGE MESSAGE
 *      valid
 *      
 * Additional tests were performed by sending merge messages from the server's command line.
 * The messages that were shown to be valid by the regex were printed. 
 * @author sdrammis
 *
 */
public class MergeHandlerThreadTest {

    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testValidMessageHReg() {
        String regex ="(v|h)\\s[A-Za-z_][A-Za-z_0-9]*\\s[A-Za-z_][A-Za-z_0-9]*";
        assertTrue("v player1 player2".matches(regex));
    }
    
    @Test
    public void testInvalidMessageVReg() {
        String regex ="(v|h)\\s[A-Za-z_][A-Za-z_0-9]*\\s[A-Za-z_][A-Za-z_0-9]*";
        assertFalse("v player1 player2 player".matches(regex));
    }
    
    @Test
    public void testConvertToMerge() {
        String message = "v player1 player2";
        String[] tokens = message.split(" ");
        assertTrue(tokens[1].equals("player1") && tokens[2].equals("player2"));
    }

}
