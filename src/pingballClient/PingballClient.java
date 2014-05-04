package pingballClient;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import physics.Vect;
import pingballClient.parser.*;
import pingballClient.boardObjects.*;

/**
 * The Client class that they will run to play.
 * This will be ran from the command line. 
 * Players can either chose to connect with the server or play locally.
 *      When connected with the server, 
 *      the client will message pass with its associated player thread to communicate with the server
 * Client will need this class as well as all objects used in board in order to play.
 * 
 * Thread Safety Argument:
 *      name, socket: immutable object
 *      board: protected by its lock
 *      input, output: are immutable objects
 * @author sdrammis
 */
public class PingballClient {
    /**
     * Invariants:
     *      name: the name of the board
     *      board: a valid board object
     *      socket: has a valid port number 0 <= port <= 65535 -- does not need to be checked
     *      input, output: are disconnected sockets if the player is not connected to the server
     *                      otherwise connected to the host 
     * @author sdrammis
     */
    private final String name; //the name of the current board/player
    private final Board board; //the board object the player is playing with
    private final Socket socket; //the player's socket
    private final boolean local; //whether or not we're playing locally
    
    //how the player communicates to it's PlayerThread
    private final BufferedReader input; //receives messages from the pingballClientThread
    private final PrintWriter output; //sends messages from the pingballClientThread
        
    // blockingQueue that handles all incoming messages
    // WALL, MERGE, and BALL
    protected final BlockingQueue<String> incomingMessages = new ArrayBlockingQueue<String>(100);
    
    /**
     * Create a Client using given arguments.
     * 
     * Usage: PingballServer [--host HOST] [--port PORT] FILE
     * 
     * HOST is an optional hostname or IP address of the server to connect to. If no HOST is provided, 
     * then the client starts in single-machine play mode.
     * 
     * PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port where the server
     * should listen for incoming connections. The default port is 10987. 
     * 
     * FILE is a required argument specifying a file pathname of the Pingball board that this client should run.
     *      The path name to the file MUST NOT contain any white space
     * The file format is specified by Board.g4 in the pingball.parser package.
     * 
     * @author sdrammis
     * Implemented by: asolei
     */
    public static void main(String[] args) {
        boolean isLocal = true;
        String address = null; 
        int port = 10987; // default port
        File file = null;
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while ( ! arguments.isEmpty()){
                String flag = arguments.remove();
                try {
                    if (flag.equals("--host")){
                       address = arguments.remove(); 
                       isLocal = false;
                    } else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > 65535){
                            throw new IllegalArgumentException("port " + port + " out of range.");
                        }
                    } else if (arguments.isEmpty()){
                        file = new File(flag);
                        if ( ! file.isFile()) {
                            throw new IllegalArgumentException("invalid file: \"" + file + "\"");
                        }
                    } else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee){
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae){
            System.err.println(iae.getMessage());
            System.err.println("usage: PingballClient [--host HOST] [--port PORT] FILE");
            return;
        }
        
        PingballClient client;
        try {
            client = new PingballClient(isLocal, address, port, file);
            startGame(client);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
    }
    /**
     * Construct a new PingballClient which manages a Board. A player can choose to play locally or connect
     *    to the server. 
     * @param local whether or not playing locally
     * @param hostAddress null if playing locally, specifies IP address if trying to play in server-client mode
     * @param port null if playing locally, specifies PingballServer port if trying to play in server-client mode
     * @param boardFile the File from which the Board managed by PingballClient will be constructed
     * @throws Exception if board file is illegal or host address isn't found
     * 
     * @author asolei
     * Implemented by: asolei, sdrammis
     */
    // NOTE -- IMPORTANT -- this is only public for the sake of testing, should be private
    public PingballClient(boolean local, String hostAddress, int port, File boardFile) throws Exception {
        
        // set whether local or server-client play
        this.local = local;
        
        // ensure file was passed in
        if (boardFile == null){
            throw new IllegalArgumentException("Must initialize play with input file.");
        }
        // host argument passed in 
        if (!local){
            try {
                this.socket = new Socket(hostAddress, port);  
                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e){
                throw new IOException("Host address not found.");
            } 
        } 
        // host argument was not passed in 
        else {
            this.socket = new Socket();  
            this.output = null;
            this.input = null;
        }
        
        // try to construct the Board from the input File
        try {
            Board playBoard = BoardFactory.parse(boardFile);
            this.name = playBoard.getName();
            this.board = playBoard;
        } catch (Exception e){
            throw e;
        }
    }
    
    /**
     * Initiates the game loop after sending an initial message to the server to ensure that there 
     *    is not another PingballClient in the game with the same name as this.
     * Carries out the "watching" of the game. 
     * @param client the PingballClient
     * Implemented by: sdrammis
     */
    private static void startGame(final PingballClient client) {        
        //send the name to the server, and check if a board/client with this name exists
        if ( !client.local ) { handleName(client);}
        
        Thread playThread = new Thread(new Runnable() {
            public void run(){
                int printClock = 5;
                while (true) {
                    double time = 1; // miliseconds
                    // handle incomming messages
                    client.udpateFromMessages(); //this adds the new balls to the board
                    // get the min time until collision if < 1ms or make time 1ms
                    double minTimeUntilCollision = client.board.getMinTimeUntilCollision();
                    // sleep for that min time
                    if (minTimeUntilCollision < time) { time = minTimeUntilCollision; }
                    try {
//                        long mili = (long) time;
//                        int nano = (int) (time - mili)*100000;
//                        Thread.sleep(mili, nano);
                        Thread.sleep((long) time); 
                    } catch (InterruptedException e) {
                        // gets here if time until collision is WAY too small
                        e.printStackTrace();
                    }
                    // update everything for the ammount of the min time (move for time amount)
                    client.board.newBallPositions(time);
                    client.board.newGadgetPositions(time);
                    // handle collisions (recalc velocities)
                    // get the balls to send over
                    ConcurrentHashMap<String, List<Ball>> ballsToPass = client.board.handleCollisions(time);
                    // send the balls to server
                    client.sendBallsToNeighboringBoards(ballsToPass);
                    // print if the clock is at 0
                    if (printClock == 0) {
                        System.out.print(client.board);
                        printClock = 100;
                    } else {
                        printClock -= 1;
                    }
                    // reset the time (go back to the top of the loop)
                    continue;
                }
            }
        });
        playThread.start();
        
        if (!client.local) {
            //create a thread to listen for messages, parse them, and handle them
            Thread listenThread = new Thread(new Runnable() {
                public void run(){
                    String line;
                    try {
                        while ((line = client.input.readLine()) != null) {
                            // add messages to the queue
                            client.incomingMessages.add(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            listenThread.start();
        }
   
    }
    
    /**
     * Server requests name from Client via PingballClientThread. PingballClient sends a message with the name to the server, 
     *    and if this Board name already exists the client will be notified and then the Client's socket
     *    will be closed. If the name doesn't exist we will send a message in order to start the game. 
     * @param _client
     * Implemented by: sdrammis
     */
    private static void handleName(PingballClient _client) {
       String line;
       try {  
            while ((line = _client.input.readLine()) != null) {
                //see if the clientThread is requesting a name
                if (line.equals("NAME")) {
                    _client.output.println("NAME " + _client.name);
                } else if (line.equals("Board name already exists.")) {
                    //tell player the message
                    System.out.println(line);
                    _client.socket.close();
                } else if (line.equals("START")) {
                    break;
                }
            }
        } catch (IOException e) {
            // will get here if playing locally, won't communicate with server
            // socket will be not connected so input.readLine() will throw an exception
            // we ignore this exception and allow players to continue with play
            e.printStackTrace();
        }
    }
    
  /**
   * Get incoming messages and adjust the class accordingly.
   * 
   * @author sdrammis
   * Implemented by: asolei
   */
    
  //public for testing -- SHOULD BE PRIVATE
  public void udpateFromMessages() {
      // add new balls and update walls (that have come in during the play method)
      synchronized (this.incomingMessages) { //gets the lock so that incomming balls won't be added during this time
          while (!this.incomingMessages.isEmpty()){
              String currentMessage = this.incomingMessages.remove();
              String[] tokens = currentMessage.split(" ");
              String type = tokens[0];
              switch (type){
                  // merge message
                  // MERGE T|B|R|L neighborName
                  case "MERGE":
                      String direction = tokens[1];
                      String neighbor = tokens[2];
                      this.board.merge(direction, neighbor);
                      break;
                  // wall message
                  // WALL T|B|R|L 
                  case "WALL":
                      String wall = tokens[1];
                      this.board.removeInvisibleWall(wall);
                      break;
                  // ball message
                  // BALL ballName xCoord yCoord xVel yVel
                  case "BALL":
                      String ballName = tokens[1];
                      double xCoord = Double.parseDouble(tokens[2]);
                      double yCoord = Double.parseDouble(tokens[3]);
                      double xVel = Double.parseDouble(tokens[4]);
                      double yVel = Double.parseDouble(tokens[5]);
                      Vect velocity = new Vect(xVel, yVel);
                      this.board.addBall(new Ball(ballName, xCoord, yCoord, velocity));
                      break;
                  default:
                      break;
              }
          }
      } 
  }
  
  /**
   * Sends BALL messages to the neighboring boards appropriately. 
   * @param ballsToMove
   * @author asolei, sdrammis
   * Implemented by: asolei, sdrammis
   */
  private void sendBallsToNeighboringBoards(ConcurrentHashMap<String, List<Ball>> ballsToMove) {
      // HANDLE THE BALL PASSING 
      // iterate through ballsToMove and pass a Ball message to PingballClientThread. 
      for (String direction : ballsToMove.keySet()){
          List<Ball> ballsOnWall = ballsToMove.get(direction);
          for (Ball ball : ballsOnWall){
              String ballName = ball.getName();
              String xCoordInNewBoard = Double.toString(ball.getX());
              String yCoordInNewBoard = Double.toString(ball.getY());
              String xVel = Double.toHexString(ball.getVelocity().x());
              String yVel = Double.toHexString(ball.getVelocity().y());
              this.output.println(ballName + " " + xCoordInNewBoard + " " + yCoordInNewBoard + " " + xVel + " " + yVel 
                    + " " + this.board.getNeighbor(direction)); 
          }
      }
  }
  
  /**
   * Ensure the rep holds
   */
  public void checkRep() {
      //check that the name is valid
      String regex = "s[A-Za-z_][A-Za-z_0-9]*";
      assert this.name.matches(regex);
  }
  
  // FOLLOWING METHODS ARE TO BE USED ONLY FOR TESTING
  public String getName() {
      return this.name;
  }
  
  public Board getBoard() {
      return this.board;
  }
    
}
