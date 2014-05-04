package pingballClient;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.Vect;
import pingballClient.boardObjects.Ball;

/**
 * ------------------- updateFromMessages -------------------
 *      MERGE message
 *      WALL message to remove invisible wall
 *      BALL message to add new ball to balls
 *      
 * 
 * @author sdrammis
 * Implemented by: sdrammis
 * @category no_didit
 */
public class PingballClientTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testMergeMessage() throws Exception {
        File file = new File("./src/sampleBoard1.pb.txt");        
        PingballClient pbc = new PingballClient(true, "localhost", 4444, file);
        pbc.incomingMessages.add("MERGE T player");
        pbc.udpateFromMessages();
        assertEquals(pbc.getBoard().getNeighbor("T"), "player");
    }
    
    @Test
    public void testWallMessage() throws Exception {
        File file = new File("./src/sampleBoard1.pb.txt");        
        PingballClient pbc = new PingballClient(true, "localhost", 4444, file);
        pbc.incomingMessages.add("MERGE T player");
        pbc.udpateFromMessages();
        pbc.incomingMessages.add("WALL T");
        pbc.udpateFromMessages();
        assertFalse(pbc.getBoard().getNeighbors().containsKey("T"));
    }
    
    @Test
    public void testBallMessage() throws Exception {
        File file = new File("./src/sampleBoard1.pb.txt");        
        PingballClient pbc = new PingballClient(true, "localhost", 4444, file);
        pbc.incomingMessages.add("BALL name 5 5 5 5");
        pbc.udpateFromMessages();
        for (Ball ball : pbc.getBoard().getBallCopy()) {
            if (ball.getName().equals("name") &&
                    ball.getX() == 5.0 &&
                    ball.getY() == 5.0 &&
                    ball.getVelocity() == new Vect(5.0, 5.0)) {
                assert true;
            }
        }
        assert false;
    }

}
