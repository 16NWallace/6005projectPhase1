package pingball;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import pingball.BlockingQueueThread;
import pingball.MergeHandlerThread;
import pingball.PingballClientThread;


/**
 * The server for pingball. The main thread.
 * Listens for new players and connects them to the game.
 * 
 * 
 * Thread Safety:
 *      every client that connects to the server is given their own thread
 *          clients cannot directly contact eachother
 *      message passing between clients is handled by a blocking queue on the server thread
 *          this handles the passing of balls from one board to another and merge messages
 *          
 *      players, neighbors: uses existing java atomic types
 *          shared amongst PingballServer, BlockingQueueThread
 *      serverSocket: immutable
 *      mainQueue: a thread safe data type
 *                   shared amongst PingballServer, BlockingQueueThread, PingballClientThread, and MergeHandlerThread
 *      blockingQueueThread, mergeHandlerThread: confinement, confined within this class
 *                              only accessable from the PingballServer thread
 *                                  
 *      @author sdrammis
 */
public class PingballServer {
    /**
     * Rep invariant:
     *      players:
     *          player name maps to player thread with that board name
     *          size is the number of players currently playing
     *      serverSocket:
     *          has valid socket port, 0 <= port <= 65535 -- precondition on client
     *      neighbors:
     *          key - all the values of the keys in players map
     *                  at most the size of number of players online
     *          value - map with maximum size 4
     *                  where the keys are ("L", "R", "T", "B") with at most one of each; left, right, top, bottom respectively
     *      mainQueue, blockingQueueThread, mergeHandlerThread:
     *          only one instance of each
     * 
     * @author sdrammis
     */
    
    
    // synchronized list of current players playing;
    // maps: playerName -> PlayerThread with the name
    private ConcurrentHashMap<String, PingballClientThread> players = new ConcurrentHashMap<String, PingballClientThread>();
    
    private final ServerSocket serverSocket; //the socket that the server is listening on
    
    //the neighbors of each player
    //player maps to a hashmap of its neighbors where keys are "N", "S", "E", "W"
    private ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> neighbors = new ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>>();
     
    //queue that handles both the movement of balls and merging of players
    private BlockingQueue<String> mainQueue;
    
    private BlockingQueueThread blockingQueueThread; //the thread that will take messages out of the queue
    
    private MergeHandlerThread mergeHandlerThread; //the thread that will listen for merge messages and add them to the queue

    /**
     * Make a PingballServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * @throws IOException if cannot create a listening socket with the privded port
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public PingballServer(int port) throws IOException {
        serverSocket = new ServerSocket(port); //create the socket for listening on the passed in port
        
        mainQueue = new ArrayBlockingQueue<String>(100); //create the main blocking queue with a capacity
        
        blockingQueueThread = new BlockingQueueThread(mainQueue, players, neighbors);
        mergeHandlerThread = new MergeHandlerThread(mainQueue, players);
    }
    
    /**
     * Run the server, listening for client connection and handling them.
     * Never returns unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public void serve() throws IOException{
        new Thread(blockingQueueThread).start();
        new Thread(mergeHandlerThread).start();

        while(true) {
            Socket socket = null; //socket for a client           
            socket = serverSocket.accept();
            
            //make a new player thread and add them to the game
            PingballClientThread playerThread = new PingballClientThread(socket, mainQueue, players, neighbors); 
            //start the thread
            new Thread(playerThread).start();
        }        
    }
    
    /**
     * Start a PingballServer using the given arguments.
     * 
     * Usage: PingballServer [--port PORT]
     * 
     * PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     * should be listening on for incoming connections. E.g. "PingballServer --port 1234"
     * starts the server listening on port 1234
     * The default port is 10987
     * 
     * @author sdrammis
     * Implemented by: asolei
     * 
     */
    public static void main(String[] args) {
        //try and run the server
        int port = 10987; //default port
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while (! arguments.isEmpty()){
                String flag = arguments.remove();
                try {
                    if (flag.equals("--port")){
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > 65535){
                            throw new IllegalArgumentException("port " + port + " out of range.");
                        }
                    } else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee){
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe){
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae){
            System.err.println(iae.getMessage());
            System.err.println("usage: PingballServer [--port PORT]");
        }
        try {
            runPingballServer(port); //try and run the server            
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * Start a PingballServer running on the specified port. 
     * 
     * @param port The network port on which the server should listen.
     * @throws IOException if the server can't be started
     * 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public static void runPingballServer(int port) throws IOException {
        PingballServer server = new PingballServer(port);
        server.serve();
    }

    /**
     * Ensure the rep holds
     * Implemented by: sdrammis
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
    
    
    // ========================================== METHODS FOR TESTING ONLY ========================================== //
    //NOTE -- THIS IS FOR TESTING PURPOSES ONLY
    public BlockingQueue<String> getQueue() {
        return this.mainQueue;
    }
    
    public ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> getNeighbors() {
        return this.neighbors;
    }
    
    public ConcurrentHashMap<String, PingballClientThread> getPlayers() {
        return this.players;
    }
    
    public BlockingQueueThread getBQT() {
        return this.blockingQueueThread;
    }
    
    public void resetNeighbors() {
        this.neighbors = new ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>>();
    }

}
