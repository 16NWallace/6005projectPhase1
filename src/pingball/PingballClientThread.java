package pingball;


import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;



/**
 * A thread that enables communication between a player and the server.
 * The thread will only add messages to the blocking queue.
 * The thread will NEVER take and process messages from the queue, this is handled in BlockingQueueThread
 * 
 * Thread Safety Argument:
 *      players, neighbors: uses existing java atomic types
 *          shared amongst PingballServer, BlockingQueueThread
 *      input, output: immutable objects
 *      mainQueue: a thread safe type 
 *                  shared amongst PingballServer, BlockingQueueThread, PingballClientThread, and MergeHandlerThread
 *      pingballClientName: will be changed from null to the client name and then not mutated again
 *                          confinement, is confined within the thread
 * @author sdrammis
 */
public class PingballClientThread implements Runnable {
    
    /**
     * Invariants:
     *      players:
     *          player name maps to player thread with that board name
     *          size is the number of players currently playing
     *      input, output: valid connections
     *      mainQueue: refrences the only queue on the server
     * @author sdrammis
     */
    private final Socket socket;
    private final BufferedReader input; //get the messages from the Client
    private final PrintWriter output; //send messages to the Client
    private BlockingQueue<String> mainQueue; //the blocking queue that the thread will add mesages too
    private ConcurrentHashMap<String, PingballClientThread> players; //the list of players in the current game
    private String pingballClientName = null; //the name of the board/player that this thread is associated with
    
    //the neighbors of each player
    //player maps to a hashmap of its neighbors where keys are "N", "S", "E", "W"
    private ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> neighbors;
    
    /**
     * Player is created when the socket from the server connects.
     * 
     * @param socket created when player connects to the server
     * @param mainQueue the server's queue that the thread will be adding messages to
     * @throws IOException if unable to get input and output stream
     * 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public PingballClientThread(Socket socket, BlockingQueue<String> mainQueue, ConcurrentHashMap<String, PingballClientThread> players, ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> neighbors) throws IOException{
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.mainQueue = mainQueue;
        this.players = players;
        this.neighbors = neighbors;
    }
    
    /**
     * Listen for messages from the player and adds them to the main queue.
     * 
     * @author sdrammis
     * Implemented by: asolei, sdrammis
     */
    @Override
    public void run() {
        //request the boards name from the player
        this.output.println("NAME"); //this will only be sent once per thread
        
        //only messages to be handled are ball passign messages
        String line;
        //for every message on the input, add it to the queue (input messages come from the client)
        try { 
            while (((line = input.readLine()) != null)) {
                String[] tokens = line.split(" ");
                //if line has the NAME token at the beginning, we add the player to the group of players
                if (tokens[0].equals("NAME")) {
                    //check if the player/board name is in players
                    if (players.containsKey(tokens[1])) {
                        //if the name already exists send a message to the player telling them to change the name
                        this.output.println("Board name already exists.");
                    } else {
                        //set the clientName variable to the name of the client/board the thread is associated with
                        this.pingballClientName = tokens[1];
                        //add the name to the players
                        this.players.put(tokens[1], this); //adding "playerName" instead of pingballClientName to avoid reorderings of thread
                        this.output.println("START");
                    }
                } else {
                    //otherwise it must be a ball message so pass it to the main queue
                    this.mainQueue.add("BALL " + line);
                }
            }
        } catch (IOException e) {
            // exception occurs when the Client has closed the program
            e.printStackTrace();
        } finally {
            // remove the player from the maps
            this.neighbors.remove(this);
            this.players.remove(this.pingballClientName);
            //iterate through all players and see if the player that has left was next to any of the players
            for (PingballClientThread player : this.neighbors.keySet()) {
                for (String direction : this.neighbors.get(player).keySet()) {
                    if (this.neighbors.get(player).get(direction) == this) {
                        //remove it as a neighbor in the map
                        this.neighbors.get(player).remove(direction);
                        //send a message to the other player to set wall back to solid
                        this.mainQueue.add("WALL " + player.getClientName() + " " + direction); //WALL playerName (T|B|R|L)
                    }
                }
            }
        }
    } 
 

    /**
     * Passes a given message from the server to the player.
     * Either pass a new ball to the board, or pass message to connect boards.
     * Called by BlockingQueueThread
     * 
     * Messages are of the form:
     *      BALL ballName xVal yVal xVel yVel
     *      MERGE (T|B|R|L) neighborName
     *      WALL (T|B|R|L) 
     *          (T|B|R|L) is the wall to be made solid/visible
     * @param string the specific message
     * @author asolei, sdrammis
     */
    public void passMessage(String string) {
        output.println(string); //printWriter has automatic line flushing
    }
    
    /**
     * Get the client's name that this thread communicates with
     * 
     * @return string of the client/board/player's name
     * 
     * @author sdrammis
     */
    public String getClientName() {
        return this.pingballClientName;
    }
    
    /**
     * Return String representation of the PingballClientThread
     * @author asolei
     */
    public String toString() {
        return this.pingballClientName;
    }
    
    /**
     * Determine equality of two PingballClientThreads by testing equality of their names
     * @author asolei
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PingballClientThread) {
            return ((PingballClientThread) obj).getClientName() == this.getClientName();
        }
        return false;
    }
    
    /**
     * Compute the hashCode of a PingballClientThread by hashing the name
     * @author asolei
     */
    @Override
    public int hashCode() {
        return this.getClientName().hashCode();
    }
    
    /**
     * Ensure that rep holds
     */
    public void checkRep() {
        for (String player : this.players.keySet()) {
            String clientName = this.players.get(player).getClientName();
            if (player != clientName) { assert false; }
        }
        for (PingballClientThread player : this.neighbors.keySet()) {
            if (this.neighbors.get(player).size() > 4) { assert false; }
                for (String direction : this.neighbors.get(player).keySet()) {
                    if (!direction.matches("[NSEW]")) { assert false; }
            }
        }
        assert true;
    }
    
    //FOLLOWING METHODS ARE FOR TESTING ONLY
    public void setName(String name) {
        this.pingballClientName = name;
    }
    
    /**
     * Testing for this class was done by running the server and adding print statments to the code.
     * The main test was to ensure that the thread was in fact getting the name of the player from the 
     * client.
     * Tests were also done to ensure that balls messages were being handled.
     * Lastly tests were done for clients disconnecting.
     */
}
