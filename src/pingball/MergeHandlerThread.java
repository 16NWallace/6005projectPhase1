package pingball;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The thread listens for input from the command line (specifically commands to merge the boards, since these are the only valid commands for now).
 * Then the thread parses the command and adds it to the server's queue.
 * 
 * Thread Safety:
 *      bufferedReader: immutable, only thread that accesses the command line
 *      mainQueue: a thread safe type 
 *                  shared amongst PingballServer, BlockingQueueThread, PingballClientThread, and MergeHandlerThread     
 * 
 * @author sdrammis
 *
 */
public class MergeHandlerThread implements Runnable{
    
    /**
     * Invariants:
     *      bufferedReader: not null
     *      mainQueue: refrences the only queue on the server
     * @author sdrammis                    
     */
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); //read in from the command line
    private BlockingQueue<String> mainQueue; //server's queue
    private ConcurrentHashMap<String, PingballClientThread> players; //players that are actively connected to the server

    
    /**
     * Create the thread to handle requests from the command line.
     * 
     * @param mainQueue server's queue
     * @param players 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public MergeHandlerThread(BlockingQueue<String> mainQueue, ConcurrentHashMap<String, PingballClientThread> players) {
        this.mainQueue = mainQueue;
        this.players = players;
    }

    /**
     * Handle the input from the terminal and add to message to the blockingQueue
     * Implemented by: asolei, sdrammis
     */
    @Override
    public void run() {
        String line;
        try {
            while ((line = input.readLine()) != null) {
                // check if the merge message is valid 
                String regex ="(v|h)\\s[A-Za-z_][A-Za-z_0-9]*\\s[A-Za-z_][A-Za-z_0-9]*";
                if ( line.matches(regex)){
                    // valid input. check if the names correspond to players in the game
                    String[] tokens = line.split(" ");
                    // the names are parts at index 1, 2
                    if (players.containsKey(tokens[1]) && players.containsKey(tokens[2])){
                        mainQueue.add("MERGE " + line);
                        System.out.println(line);
                    }  
                }
            }
        } catch (IOException e) {
            // server is off?
            e.printStackTrace();
        }
        
    }

    
    /**Invariants:
     *      bufferedReader: not null
     *      mainQueue: refrences the only queue on the server
     * Check the representation invariant of MergeHandlerThread
     * 
     * @author asolei
     * Implemented by: sdrammis
     */
    public void checkRep() {
        for (String player : this.players.keySet()) {
            String clientName = this.players.get(player).getClientName();
            if (player != clientName) { assert false; }
        }
    }
    

    // EVERYTHING BELOW IF FOR TESTING PURPOSES ONLY -- srdammis
    public boolean testRegex(String line) {
        String regex ="(v|h)\\s[A-Za-z_][A-Za-z_0-9]*\\s[A-Za-z_][A-Za-z_0-9]*";
        if ( line.matches(regex)){
            return true;
        }
        return false;
    }

}
