package pingball;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The thread that takes information off of the server's queue and processes it.
 * It will call upon a PingballClientThread to send a message to the player. 
 * 
 * Thread Safety Argument:
 *      mainQueue: a thread safe type 
 *                  shared amongst PingballServer, BlockingQueueThread, PingballClientThread, and MergeHandlerThread
 *      players, neighbors: uses existing java atomic types
 *                          shared among PingballServer, BlockingQueueThread
 *                          
 * @author sdrammis
 * @author avasoleimany
 *
 */
public class BlockingQueueThread implements Runnable{
    /**
     * Invariants: 
     *      mainQueue: references the only queue on the server
     *      players:
     *          player name maps to player thread with that board name
     *          size is the number of players currently playing
     *      neighbors:
     *          key - all the values of the keys in players map
     *                  at most the size of number of players online
     *          value - map with maximum size 4
     *                  where the keys are ("L", "R", "T", "B") with at most one of each; left, right, top, bottom respectively
     * @author sdrammis 
     */
    
    private BlockingQueue<String> mainQueue; //the server's queue
    private ConcurrentHashMap<String, PingballClientThread> players; //players that are actively connected to the server
    private ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> neighbors; //adjacency of the players in players
 
    /**
     * Create the thread. The thread will take messages of the queue and process them.
     * It will not add any messages on to the queue.
     * 
     * @param mainQueue the server's queue
     * @param players that are actively connected to the server
     * @param neighbors adjacency of the players in players (whose boards are next to whose and sharing walls)
     * 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public BlockingQueueThread(BlockingQueue<String> mainQueue, ConcurrentHashMap<String, PingballClientThread> players, ConcurrentHashMap<PingballClientThread, ConcurrentHashMap<String, PingballClientThread>> neighbors) {
        this.mainQueue = mainQueue;
        this.players = players;
        this.neighbors = neighbors;
    }

    /**
     * Get information off of the blockingQueue and handle it
     * 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    @Override
    public void run() {
        while (true) {
            try {
                // take things off of the queue
                String message = (String) this.mainQueue.take();
                handleRequest(message); // handle what we take from the queue
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    /**
     * Handle the different requests that come on and off the queue
     * @param string the message on the queue
     * messages are of the form:
     *        BALL ballName xVal yVal xVel yVel playerName
     *            xVal, yVal specify the position of the ball 
     *            xVel, yVel specify the velocity of the ball
     *            playerName is the player to receive the ball
     *         MERGE (v|h) player1 player2
     *            both players will then be sent a modified message of the merge, this is done by 
     *            calling horizontalMerge or verticalMerge. 
     *         WALL playerName (T|B|R|L)
     *            playerName player for which we want to change the wall
     *            (T|B|R|L) is the wall for which we want to toggle the state, visible/solid
     * 
     * @author sdrammis, asolei
     * Implemented by: asolei
     */
    public void handleRequest(String string) {

        String[] tokens = string.split(" ");
        // get the first token of the message which specifies the message type
        String flag = tokens[0];
        switch (flag){
            //if a MERGE message handle it and update the ADJ hash map by calling either horizontalMerge or verticalMerge
            case "MERGE":
                String mergeType = tokens[1];
                // perform vertical merge which will send modified message to both players
                if (mergeType.equals("v")){
                    String player1 = tokens[2];
                    String player2 = tokens[3];
                    verticalMerge(player1, player2);
                } 
                // perform horizontal merge which will send modified message to both players
                else if (mergeType.equals("h")){
                    String player1 = tokens[2];
                    String player2 = tokens[3];
                    horizontalMerge(player1, player2);
                } break;
            //if a BALL call the pingballClientThread.passMessage(string) 
            // send a message to the proper pingballClient that gives the information of the Ball
            case "BALL":
                String ballName = tokens[1];
                String xCoord = tokens[2];
                String yCoord = tokens[3];
                String xVel = tokens[4];
                String yVel = tokens[5];
                String client = tokens[6];
                String ballMessage = "BALL " + ballName + " " + xCoord + " " + yCoord + " " + xVel + " " + yVel;
                // the client thread corresponding to the specified player
                PingballClientThread playerSend = players.get(client);
                // send the client the message
                playerSend.passMessage(ballMessage);
                break;
            //if a WALL then we need to send a message to the proper pingballClient to make one of their wall's solid
            //this will be of the form WALL client (T|B|R|L)
            case "WALL":
                String affectedClient = tokens[1];
                String wall = tokens[2];
                String wallMessage = "WALL " + wall;
                // remove neighbor in server map
                wall(wall, affectedClient);
                // the client thread corresponding to the specified player
                PingballClientThread player = players.get(affectedClient);
                // send the client the message
                player.passMessage(wallMessage);
                break;
            default:
                break;
        }

    }
    
    /**
     * Called if a Client disconnects. Update the server's knowledge of neighbors, and udpate 
     *    the affected bordering wall of the affectedClient to make them solid. 
     * @param wall affected wall 
     * @param affectedClient whose wall needs to be fixed
     * @author asolei
     * Implemented by: sdrammis
     */
    private void wall(String wall, String affectedClient){
        // get the neighbor whose wall we need to make solid
        PingballClientThread client = this.players.get(affectedClient);
        this.neighbors.get(client).remove(wall);
    }
    
    /**
     * Merge two boards together horizontally. Update the adjacency information of players.
     * Pass message to the two players appropriately which tells them if the state of a specified wall 
     *      is going to change, and whom the player will be merging with. 
     * 
     * @param left the board whose right wall will be merged
     * @param right the board whose left wall will be merged
     * 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    private void horizontalMerge(String left, String right){
        //join left's right wall with right's left wall
        PingballClientThread playerLeft = this.players.get(left);
        PingballClientThread playerRight = this.players.get(right);
        
        //CHECK IF PLAYERS IN HASH MAP
        //put left player in the hash map if it isn't already in there
        if (!this.neighbors.containsKey(playerLeft)) {
            this.neighbors.put(playerLeft, new ConcurrentHashMap<String, PingballClientThread>());
        }
        //get the adj of the left player
        ConcurrentHashMap<String, PingballClientThread> leftMap = this.neighbors.get(playerLeft);
        
        //put right player in the hash map if it isn't already in there
        if (!this.neighbors.containsKey(playerRight)) {
            this.neighbors.put(playerRight, new ConcurrentHashMap<String, PingballClientThread>());
        }
        //get the adj of the right player
        ConcurrentHashMap<String, PingballClientThread> rightMap = this.neighbors.get(playerRight);

        //update the player on the left's right wall in the map
        if (leftMap.containsKey("R")) {
            // get the old neighbor
            PingballClientThread oldNeighbor = leftMap.get("R");
            ConcurrentHashMap<String, PingballClientThread> neighborMap = this.neighbors.get(oldNeighbor);
            //fix the ADJ
            oldNeighbor.passMessage("WALL L"); //old neighbor needs to make it's wall solid
            neighborMap.remove("L");
            leftMap.replace("R", playerRight);
        } else {
            leftMap.put("R", playerRight);
        }
        // MERGE R neighborName
        //      the top will now be the neighborName
        playerLeft.passMessage("MERGE R " + playerRight.getClientName());
        
        //update the player on the right's left wall in the map
        if (rightMap.containsKey("L")) {
            // get the old neighbor
            PingballClientThread oldNeighbor = rightMap.get("L");
            ConcurrentHashMap<String, PingballClientThread> neighborMap = this.neighbors.get(oldNeighbor);
            // fix ADJ
            oldNeighbor.passMessage("WALL R");
            neighborMap.remove("R");
            rightMap.replace("L", playerLeft);
        } else {
            rightMap.put("L", playerLeft);
        }
        // MERGE L neighborName
        //      the top will now be the neighborName
        playerRight.passMessage("MERGE L " + playerLeft.getClientName());
    }
    
    /**
     * Merge two boards together vertically. Updating the adjacency information of players.
     * Pass message to the two players appropriately which tells them if the state of a specified wall 
     *      is going to change, and whom the player will be merging with. 
     * 
     * @param top the board whose bottom wall will be merged
     * @param bottom the board whose top wall will be merged
     * 
     * @author sdrammis
     * Implemented by: sdrammis
     */
    private void verticalMerge(String top, String bottom){
        //join top's bottom wall with bottom's top wall
        PingballClientThread playerTop = this.players.get(top);
        PingballClientThread playerBottom = this.players.get(bottom);
        //CHECK IF PLAYERS IN HASH MAP
        if (!this.neighbors.containsKey(playerTop)) { //if not in map add to map
            this.neighbors.put(playerTop, new ConcurrentHashMap<String, PingballClientThread>());
        }
        //get the adj of the player
        ConcurrentHashMap<String, PingballClientThread> topMap = this.neighbors.get(playerTop);
        
        if (!this.neighbors.containsKey(playerBottom)) { //if not in map add
            this.neighbors.put(playerBottom, new ConcurrentHashMap<String, PingballClientThread>());
        }
        ConcurrentHashMap<String, PingballClientThread> bottomMap = this.neighbors.get(playerBottom);
        
        //update the player on the top's bottom wall in map
        if (topMap.containsKey("B")) {
            // get the old neighbor
            PingballClientThread oldNeighbor = topMap.get("B");
            ConcurrentHashMap<String, PingballClientThread> neighborMap = this.neighbors.get(oldNeighbor);
            // fix the ADJ 
            oldNeighbor.passMessage("WALL T"); //old neighbor needs to make it's top wall solid
            neighborMap.remove("T");
            topMap.replace("B", playerBottom);
        } else {
            topMap.put("B", playerBottom);
        }
        // MERGE B neighborName
        //      the bottom wall will now be the neighborName
        playerTop.passMessage("MERGE B " + playerBottom.getClientName());
        
        //update the player on the bottom's top wall in map
        if (bottomMap.containsKey("T")) {
            // get old neighbor
            PingballClientThread oldNeighbor = bottomMap.get("T");
            ConcurrentHashMap<String, PingballClientThread> neighborMap = this.neighbors.get(oldNeighbor);
            // fix the ADJ
            oldNeighbor.passMessage("WALL B");
            neighborMap.remove("B");
            bottomMap.replace("T", playerTop);
        } else {
            bottomMap.put("T", playerTop);
        }
        // MERGE T neighborName
        //      the top will now be the neighborName
        playerBottom.passMessage("MERGE T " + playerTop.getClientName());
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

        
}
