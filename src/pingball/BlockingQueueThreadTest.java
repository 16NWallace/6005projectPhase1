package pingball;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


import pingballClient.PingballClient;

/**
 * BlockingQueueThread Tests
 * ----------------------- handleMessage -----------------------
 *      calls upon vertical merge:
 *          test for vertical MERGE with 2 players not in hash map
 *          test for vertical MERGE with 2 players already with neighbors
 *      test WALL for all four players (occurs when another player disconnects) 
 *      calls upon horizontal merge:
 *          test for horizontal MERGE with 2 players not in hash map
 *          test for horizontal MERGE with 2 players already with neighbors
 * -------------------------------------------------------------------
 * 
 * Additional tests were done by merging clients with the server live and printing the hash maps to ensure that
 * the merges produced the propper adjacencies.
 * As well as merging players, tests were done where clients would dissconnect from the server. Again, the maps were printed
 * to ensure that the player was removed from the players map and the neighbors map (note the player is also removed
 * from being a neighbor of any key in the map) -- this is what produces a WALL message
 * A test for ball passing was done which showed that the BlockingQueueThread was handling ball messages appropriately. 
 *  
 * @author sdrammis
 * @category no_didit
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlockingQueueThreadTest {

    private static PingballServer pbS;
    private static ConcurrentHashMap<String, PingballClientThread> players;
    private static ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> neighbors;
    private static ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> expected;
    private static BlockingQueueThread bqt;
        
    private static PingballClient p1;
    private static PingballClient p2;
    private static PingballClient p3;
    private static PingballClient p4;
    
    private static PingballClientThread p1T;
    private static PingballClientThread p2T;
    private static PingballClientThread p3T;
    private static PingballClientThread p4T;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
      pbS = new PingballServer(4444);  //create a pingball server
      players = pbS.getPlayers();
      neighbors = pbS.getNeighbors();
      bqt = pbS.getBQT();
      
      //expected hash map  
      expected = new ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>>();
      
      p1 = new PingballClient(false, "127.0.0.1", 4444, new File("p1Board.pb.txt"));  
      p2 = new PingballClient(false, "127.0.0.1", 4444, new File("p2Board.pb.txt")); 
      p3 = new PingballClient(false, "127.0.0.1", 4444, new File("p3Board.pb.txt"));  
      p4 = new PingballClient(false, "127.0.0.1", 4444, new File("p4Board.pb.txt")); 
      
      p1T = new PingballClientThread(new Socket("127.0.0.1", 4444), pbS.getQueue(), pbS.getPlayers(), pbS.getNeighbors());
          p1T.setName("player1");
      p2T = new PingballClientThread(new Socket("127.0.0.1", 4444), pbS.getQueue(), pbS.getPlayers(), pbS.getNeighbors());
          p2T.setName("player2");
      p3T = new PingballClientThread(new Socket("127.0.0.1", 4444), pbS.getQueue(), pbS.getPlayers(), pbS.getNeighbors());
          p3T.setName("player3");
      p4T = new PingballClientThread(new Socket("127.0.0.1", 4444), pbS.getQueue(), pbS.getPlayers(), pbS.getNeighbors());
          p4T.setName("player4");
      
      players.put("player1", p1T);
      players.put("player2", p2T);
      players.put("player3", p3T);
      players.put("player4", p4T);
      
      
    }
    
    // test the handle request of a vertical merge with no neighbors
    @Test
    public void testA() {      
        bqt.handleRequest("MERGE v player1 player2 \n");

        ConcurrentHashMap<String, PingballClientThread> e1 = new ConcurrentHashMap<String, PingballClientThread>();
        e1.put("B", players.get("player2"));
        ConcurrentHashMap<String, PingballClientThread> e2 = new ConcurrentHashMap<String, PingballClientThread>();
        e2.put("T", players.get("player1"));
        expected.put(players.get("player1"), e1);
        expected.put(players.get("player2"), e2);
        
        assertEquals(pbS.getNeighbors(), expected);
    }
    
    
    // test the handle request of a horizontal merge with no neighbors
    @Test
    public void testB() throws IOException {
        bqt.handleRequest("MERGE h player3 player4 \n");
        
        ConcurrentHashMap<String, PingballClientThread> e3 = new ConcurrentHashMap<String, PingballClientThread>();
        e3.put("R", players.get("player4"));
        ConcurrentHashMap<String, PingballClientThread> e4 = new ConcurrentHashMap<String, PingballClientThread>();
        e4.put("L", players.get("player3"));
        expected.put(players.get("player3"), e3);
        expected.put(players.get("player4"), e4);
        
        assertEquals(pbS.getNeighbors(), expected);
    }
    
    // send a wall message
    @Test
    public void testC() {
        bqt.handleRequest("WALL player1 B \n");
        bqt.handleRequest("WALL player2 T \n");
        bqt.handleRequest("WALL player3 R \n");
        bqt.handleRequest("WALL player4 L \n");
        
        expected.get(p1T).clear();
        expected.get(p2T).clear();
        expected.get(p3T).clear();
        expected.get(p4T).clear();

        assertEquals(expected, pbS.getNeighbors());
    }
    
    // test the handle request of a vertical merge where each player has a neighbor already
    @Test
    public void testD() {
        //add neighbors 
        bqt.handleRequest("MERGE v player1 player2 \n");
        bqt.handleRequest("MERGE v player3 player4 \n");

        //handle the merge now where each wall has a neighbor already
        bqt.handleRequest("MERGE v player1 player4 \n");
    
        ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> expected;
        //expected hash map  
        expected = new ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>>();
        ConcurrentHashMap<String, PingballClientThread> e1 = new ConcurrentHashMap<String, PingballClientThread>();
        e1.put("B", players.get("player4"));
        ConcurrentHashMap<String, PingballClientThread> e4 = new ConcurrentHashMap<String, PingballClientThread>();
        e4.put("T", players.get("player1"));
        expected.put(players.get("player1"), e1);
        expected.put(players.get("player4"), e4);
        expected.put(players.get("player2"), new ConcurrentHashMap<String, PingballClientThread>());
        expected.put(players.get("player3"), new ConcurrentHashMap<String, PingballClientThread>());
        
        assertEquals(pbS.getNeighbors(), expected);
    }
    
    // send a wall message
    @Test
    public void testE() {
        bqt.handleRequest("WALL player1 B \n");
        bqt.handleRequest("WALL player4 T \n");
        
        expected.get(p1T).clear();
        expected.get(p2T).clear();
        expected.get(p3T).clear();
        expected.get(p4T).clear();

        assertEquals(expected, pbS.getNeighbors());
    }
    
    // test the handle request of a horizontal merge where each player has a neighbor already
    @Test
    public void testF() {
        neighbors = new ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>>();
        //add neighbors 
        bqt.handleRequest("MERGE h player1 player2 \n");
        bqt.handleRequest("MERGE h player3 player4 \n");
        
        //handle the merge now where each wall has a neighbor already
        bqt.handleRequest("MERGE h player1 player4 \n");
        
        ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> expected;
        //expected hash map  
        expected = new ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>>();
        ConcurrentHashMap<String, PingballClientThread> e1 = new ConcurrentHashMap<String, PingballClientThread>();
        e1.put("R", players.get("player4"));
        ConcurrentHashMap<String, PingballClientThread> e4 = new ConcurrentHashMap<String, PingballClientThread>();
        e4.put("L", players.get("player1"));
        expected.put(players.get("player1"), e1);
        expected.put(players.get("player4"), e4);
        expected.put(players.get("player2"), new ConcurrentHashMap<String, PingballClientThread>());
        expected.put(players.get("player3"), new ConcurrentHashMap<String, PingballClientThread>());
                
        assertEquals(pbS.getNeighbors(), expected);

    }
    
}
