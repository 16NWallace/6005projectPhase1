package pingballClient;

import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.*;

import physics.*;
import pingballClient.boardObjects.*;

/**
 * The class that will handle the setup of a board. All mutations must be protected by a lock. 
 * 
 * Thread Safety Argument:
 *      All walls (topWall, bottomWall, leftWall, rightWall) and the walls List
 *      and all corners (topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner) and the corners List
 *      are confined within the thread.
 *      
 *      The instance variables: gravity, mu, mu2 are immutable 
 *      
 *      The gadgets array is immutable. No gadgets can be removed or added during play.
 *      Gadget objects within the gadgets array are mutable and protected by confinement.
 *      
 *      The balls List is a thread safe datatype and protected by the board's lock.
 *          balls are added to the list without a lock on the list
 *          and the list is iterated through under the lock on the list
 *      
 *      neighbors
 *          protected by the lock of the board
 *      
 * @author sdrammis
 *
 */

public class Board {
    /**
     * rep invariant:
     *      topWall coords: (-0.05,-0.05) --- (20.05,-0.05)
     *      botomWall coords: (-0.05,20.05) --- (20.05,20.05)
     *      leftWall coords: (-0.05,-0.05) --- (-0.05,20.05)
     *      rightWal coords: (20.05,-0.05) --- (20.05,20.05)
     *      ball and all 1L gadget coords: anywhere in the square (0,0)---(20,0)---(20,20)---(0,20)
     *      all 2L gadget coords: anywhere in the square (0,0)---(18,0)---(18,18)---(0,18)
     *          coordinates are measured from the top left
     *      
     *      board: each entry is one of the following characters,
     *          #, *, 0, /, \, |, -, =
     *      
     *      gravity, mu, mu2: not null
     *      
     *      neighbors: 
     *          maximum size is 4. with the keys ("N", "S", "E", "W") at most one of each
     *          
     *      gadgets:
     *          doesn't not contain any OuterWall objects
     * @author sdrammis         
     */
    
    //create the walls
    private final OuterWall topWall = new OuterWall(new Vect(-0.01,-0.01), new Vect(20.01, -0.01));
    private final OuterWall bottomWall = new OuterWall(new Vect(-0.01,20.01), new Vect(20.01, 20.01));
    private final OuterWall leftWall = new OuterWall(new Vect(-0.01,-0.01), new Vect(-0.01, 20.01));
    private final OuterWall rightWall = new OuterWall(new Vect(20.01,-0.01), new Vect(20.01, 20.01));
    final List<OuterWall> walls = new ArrayList<OuterWall>(Arrays.asList(topWall, bottomWall, leftWall, rightWall));
    
    //add in the corners
    private final Circle topLeftCorner = new Circle(-0.01, -0.01, 0);
    private final Circle topRightCorner = new Circle(20.01, -0.01, 0);
    private final Circle bottomLeftCorner = new Circle(-0.01, 20.01, 0);
    private final Circle bottomRightCorner = new Circle(20.01, 20.01, 0);
    final List<Circle> corners = new ArrayList<Circle>(Arrays.asList(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner));

    //the string representation of the board
    private String[][] board = new String[20][20]; //row by column    
    
    // gravity and friction
    private final double gravity;
    private final double mu;
    private final double mu2;
    
    // gadgets in the board
    private final List<Gadget> gadgets; //should not contain any OuterWalls
    
    // balls in the board
    private List<Ball> balls;
    
    // the board's name
    private final String name;
    
    // keeps track of all the neighboring board names
    // keys are "R", "L", "T", "B" for the walls of the board respectively 
    private HashMap<String, String> neighbors = new HashMap<String, String>();
        
    // how often we update the velocities and positions of the Balls in the board
    private static double DELTA = .001;
    private static int BOARD_SIZE = 20;
    
    /**
     * Construct a new 20 by 20 board
     * 
     * @param gadgets the gadgets to be placed on the board
     * @param balls the balls for the board
     * @param name the board's name as given by the user not null
     * @param gravity the amount of gravity in the board
     * @param mu friction1
     * @param mu2 friction2
     * 
     * @author sdrammis
     */
    public Board(List<Gadget> gadgets, List<Ball> balls, String name, double gravity, double mu, double mu2) {
        //create the gadgets array
        this.gadgets = gadgets;
        
        //initialize the rest of the inputs
        this.balls = Collections.synchronizedList(balls);
        this.gravity = gravity;
        this.mu = mu;
        this.mu2 = mu2; 
        this.name = name;

        
        //initialize the inner board
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                board[y][x] = " ";
            }
        }
    }
    
    /**
     * Create string representation of the top or bottom wall
     * @param invisible whether the specified wall is invisible
     * @param topOrBottom "T" or "B"
     * @return String representation of the specified wall
     * @author asolei
     * Implemented by: asolei
     */
    private String topBottomString(boolean invisible, String topOrBottom){
        String wallString = new String();
        if (invisible) {
            String neighbor = neighbors.get(topOrBottom);
            if (neighbor.length() < 20) {
                int remainder = 22 - neighbor.length();
                if ((remainder % 2) == 0){
                    int dots = remainder / 2;
                    for (int i=0; i < dots; i++){
                        wallString += ".";
                    } wallString += neighbor;
                    for (int i=0; i < dots; i++){
                        wallString += ".";
                    } wallString += "\n";
                } else {
                    int dotsFront = (remainder - 1)/2;
                    int dotsBack = (remainder + 1)/2;
                    for (int i=0; i < dotsFront; i++){
                        wallString += ".";
                    } wallString += neighbor;
                    for (int i=0; i < dotsBack; i++){
                        wallString += ".";
                    } wallString += "\n";
                }
            } else {
                wallString += "." + neighbor.substring(0, 20) + ".\n";
            }
        } else {
            for (int i = -1; i < 21; i++) {
                wallString += ".";
            } wallString += "\n";
        } return wallString;
    }
    
    /**
     * Create string representation of the left or right wall
     * @param invisible whether the specified wall is invisible
     * @param leftOrRight "L" or "R"
     * @return String representation of the specified wall
     * @author asolei
     * Implemented by: asolei
     */
    private String[] leftRightString(boolean invisible, String leftOrRight){
        ArrayList<String> wallList = new ArrayList<String>();
        if (invisible){
            String neighbor = neighbors.get(leftOrRight);
            if (neighbor.length() < 20){
                int remainder = 20 - neighbor.length();
                if ((remainder % 2) == 0){
                    int dots = remainder / 2;
                    for (int i=0; i<dots; i++){
                        wallList.add(".");
                    } for (int i=0; i<neighbor.length(); i++){
                        wallList.add(Character.toString(neighbor.charAt(i)));
                    } for (int i=0; i<dots; i++){
                        wallList.add(".");
                    }
                } else {
                    int dotsAbove = (remainder - 1)/2;
                    int dotsBelow = (remainder + 1)/2;
                    for (int i=0; i<dotsAbove; i++){
                        wallList.add(".");
                    } for (int i=0; i<neighbor.length(); i++){
                        wallList.add(Character.toString(neighbor.charAt(i)));
                    } for (int i=0; i<dotsBelow; i++){
                        wallList.add(".");
                    }
                }
            } else {
                String neighborSubstring = neighbor.substring(0, 20);
                for (int i=0; i<20; i++){
                    wallList.add(Character.toString(neighborSubstring.charAt(i)));
                }
            }
        } else {
            for (int i=0; i<20; i++){
                wallList.add(".");
            }
        } String[] wallArray = wallList.toArray(new String[wallList.size()]);
        assert (wallArray.length == 20);
        return wallArray;
    }
    
    /**
     * Adjusts the display of the Board according to the movement of Balls and Flippers within the Board. 
     * 
     * @author asolei
     * Implemented by: sdrammis
     */
    private void updateBoardDisplay() {
        //initialize the inner board
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                board[y][x] = " ";
            }
        }
        // add in the balls
        for (Ball ball : this.balls) {
            if ( ball.getInAbsorber() ) { continue; }
            int y = (int) Math.floor(ball.getY());
            int x = (int) Math.floor(ball.getX());
            //if the ball reaches the end or an area before a wall, print it inside the board still
            if ( y >= BOARD_SIZE ) { y = BOARD_SIZE - 1; }
            if ( x >= BOARD_SIZE ) { x = BOARD_SIZE - 1; }
            this.board[y][x] = "*";
        }
        
        // add in the gadgets
        for (Gadget gadget : this.gadgets) {
            String gadgetToString = gadget.toString();
            
            //first check if the gadget is a absorber (ie contains ABS)
            //if so add the absorber to the board
            if (gadgetToString.contains("ABS")) { //ABS width height -- is what toString is for absorber
                String[] absSplit = gadgetToString.split("\\s+");
                int width = Integer.parseInt(absSplit[1]);
                int height = Integer.parseInt(absSplit[2]);

                int y = (int) gadget.getY();
                int x = (int) gadget.getX();
                for (int r = y; r < y + height; r ++) {
                    for (int c = x; c < x + width; c ++) {
                        this.board[r][c] = "=";
                    }
                }
            } else {

                switch (gadgetToString) {
                    case "FLIPPER | R":
                        removeFlipper(gadget);
                        this.board[(int) gadget.getY()][(int) gadget.getX() + 1] = "|";
                        this.board[(int) gadget.getY() + 1][(int) gadget.getX() + 1] = "|";
                        break;
                    case "FLIPPER | L":
                        removeFlipper(gadget);
                        this.board[(int) gadget.getY()][(int) gadget.getX()] = "|";
                        this.board[(int) gadget.getY() + 1][(int) gadget.getX()] = "|";
                        break;
                    case "FLIPPER - T":
                        removeFlipper(gadget);
                        this.board[(int) gadget.getY()][(int) gadget.getX()] = "-";
                        this.board[(int) gadget.getY()][(int) gadget.getX() + 1] = "-";
                        break;
                    case "FLIPPER - B":  
                        removeFlipper(gadget);
                        this.board[(int) gadget.getY() + 1][(int) gadget.getX()] = "-";
                        this.board[(int) gadget.getY() + 1][(int) gadget.getX() + 1] = "-";
                        break;
                    default: //not a flipper or absorber so use normal toString()
                        this.board[(int) gadget.getY()][(int) gadget.getX()] = gadget.toString();
                        break;
                }
            }
        }
    }
    
    /**
     * Create string representation of a board
     * 
     * @return a string representation of the board
     * @author asolei
     * Implemented by: asolei
     */
    @Override
    public String toString() {
        updateBoardDisplay();
        String boardToString = new String();
        
        // make the top wall
        boardToString += topBottomString(topWall.isInvisible(), "T");

        // create the inner board space, and the left and right wall
        // get the left wall String array
        String[] leftWallArray = leftRightString(leftWall.isInvisible(), "L");
        // get the right wall String array
        String[] rightWallArray = leftRightString(rightWall.isInvisible(), "R");
        
        for (int x = 0; x < 20; x++) {
            boardToString += leftWallArray[x];
            for (int y = 0; y < 20; y++) {
                boardToString += board[x][y];
            }
            boardToString += rightWallArray[x];
            boardToString += "\n";
        }
        
        // make the bottom wall
        boardToString += topBottomString(bottomWall.isInvisible(), "B");
        
        return boardToString;
    }
    
    

    
    /**
     * Determine where every ball will move in the next time step. We actually move the ball.
     * @param time time interval for which we want to simulate movement of the balls
     * @author asolei
     * Implemented by: sdrammis
     */
    public void newBallPositions(double time){  
        for (Ball ball : this.balls) {
            if (ball.getInAbsorber()) { continue; }
            updateBall(ball, time); 
        }
    }
    
    /**
     * Update the velocity and position of a ball according to the friction values and gravity of the board. 
     * @param time an interval of time. If this value is less than DELTA, 
     *      we will simulate movement for this value. If this value is greater than DELTA we will simulate movement
     *      for DELTA. 
     * @param ball ball that we want to move
     * @author asolei
     * Implemented by: asolei
     */
    private void updateBall(Ball ball, double time){
        double updateTime = DELTA;
        if (time < DELTA){
            updateTime = time;
        }
        double newXVect = (ball.getVelocity().x() * (1 - (mu*updateTime) - (mu2 * ball.getSpeed() * updateTime))); 
        double newYVect = (ball.getVelocity().y() * (1 - (mu*updateTime) - (mu2 * ball.getSpeed() * updateTime)) + (gravity * updateTime)); 
        Vect newVel = new Vect(newXVect, newYVect);
        double newX = ball.getX() + updateTime * newXVect;
        double newY = ball.getY() + updateTime * newYVect;
        ball.setVelocity(newVel);
        ball.setCoord(newX, newY);
    }
    
    /**
     * Removes the display of a Flipper from it's position in the Board display
     * @param gadget a Flipper
     * @author asolei
     * Implemented by: sdrammis
     */
    private void removeFlipper(Gadget gadget) {
        this.board[(int) gadget.getY()][(int) gadget.getX()] = " ";
        this.board[(int) gadget.getY()][(int) gadget.getX() + 1] = " ";
        this.board[(int) gadget.getY() + 1][(int) gadget.getX()] = " ";
        this.board[(int) gadget.getY() + 1][(int) gadget.getX() + 1] = " ";
    }
    
    /**
     * Add a new ball when it enters the board
     * @param ball entering ball
     * @author asolei
     * Implemented by: asolei
     */
    public synchronized void addBall(Ball ball) {
        balls.add(ball);
    }
    
    /**
     * Get the name of the neighboring Board joined at the specified wall of this
     * @param wall of this
     * @return name of the neighboring Board joined at the specified wall
     * @author sdrammis
     */
    public String getNeighbor(String wall){
        return new String(neighbors.get(wall));
        
    }
    /**
     * Add a neighbor to the board. Mutates the neighbor of a board.
     * 
     * @param direction of the wall of the board to change
     *          either: "T", "B", "R", "L"
     * @param neighborBoardName name of the new neighbor board as given by the player
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public synchronized void merge(String direction, String neighborBoardName) {
        for (OuterWall wall : this.walls) {
            if (wall.getName().equals(direction)) {
                wall.changeInvisible(true);
            }
        }
        this.neighbors.put(direction, neighborBoardName);
    }
    
    
    /**
     * Either add a neighbor to or remove a neighbor from a wall
     * 
     * @param direction the direction of the ball to remove: "T" || "B" || "L" || "R"
     *          top, bottom, left, right respectively
     * @author sdrammis
     * Implemented by: sdrammis
     */
    public synchronized void removeInvisibleWall(String direction) {
        this.neighbors.remove(direction);
        switch (direction) {
            case "T":
                this.topWall.changeInvisible(false);
                break;
            case "B":
                this.bottomWall.changeInvisible(false);
                break;
            case "L":
                this.leftWall.changeInvisible(false);
                break;
            case "R":
                this.rightWall.changeInvisible(false);
                break;
        }
    }
    
    /**
     * Get name of the board
     * @return the name of the board
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Determines whether or not a given Object obj is observationally equal to this
     * Criterion for observational equality:
     *      instance of Board
     *      same name
     *      same gravity
     *      same friction values
     *      the set of Gadgets in both Boards are equal
     * @return whether or not obj is observationally equal to this
     * @author asolei
     * Implemented by: asolei
     */
    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof Board)){
            return false;
        } Board other = (Board) obj;
        boolean sameName = this.getName().equals(other.getName());
        boolean sameGravity = this.gravity==other.gravity;
        boolean sameMu1 = this.mu==other.mu;
        boolean sameMu2 = this.mu2==other.mu2;

        
        Set<Gadget> thisGadgets = new HashSet<Gadget>(this.gadgets);
        Set<Gadget> otherGadgets = new HashSet<Gadget>(other.gadgets);
        if (thisGadgets.size() != otherGadgets.size()){
            return false;
        } 
        boolean sameGadgets = true;
        for (Gadget thisGadget : thisGadgets){
            if (! otherGadgets.contains(thisGadget)) {
                sameGadgets = false;
            }
        }
        return (sameName&&sameGravity&&sameMu1&&sameMu2&&sameGadgets);
    }
    
    /**
     * Compute the hashCode() of this by hashing the Board's name
     * @author asolei
     * Implemented by: asolei
     */
    @Override
    public int hashCode(){
        return this.name.hashCode();
    }

    /**
     * Calculate the minimum time until any collision occurs on a board
     * @return the minimum time for any sort of collision to occur on the board
     * Implemented by: sdrammis
     */
    public double getMinTimeUntilCollision() {
        double minTimeUntilCollision = Double.POSITIVE_INFINITY; //initialize minTime

        // now we check all of the collisions
        for (Ball ball : this.balls) {   
            if (ball.getInAbsorber()) { continue; }
            //check against all of the walls
            for (OuterWall wall : this.walls) {
                double timeUntilCollision = wall.getTimeUntilCollision(ball);
                if (timeUntilCollision < minTimeUntilCollision) { 
                    minTimeUntilCollision = timeUntilCollision;
                }
            }
            //check all of the gadgets
            for (Gadget gadget : this.gadgets) {
                double timeUntilCollision = gadget.getTimeUntilCollision(ball);
                if (timeUntilCollision < minTimeUntilCollision) { 
                    minTimeUntilCollision = timeUntilCollision;
                }
            }
            //check collisions will all other balls
            for (Ball otherBall : this.balls) {
                if ( !ball.equals(otherBall) ) {
                    double timeUntilCollision = ball.getTimeUntilCollision(otherBall);
                    if (timeUntilCollision < minTimeUntilCollision) { 
                        minTimeUntilCollision = timeUntilCollision;
                    }
                }
            }
        }
        return minTimeUntilCollision;
    }
    
    /**
     * Move all the Gadgets in the board according to the parameter time
     * @param time
     * @author asolei
     */
    public void newGadgetPositions(double time) {
        for (Gadget gadget : this.gadgets){
            gadget.move(time);
        }
    }
    
    /**
     * Handle all the collisions within the Board. Updates the positions of all the Balls in the Board, and determines
     *    which Balls are colliding with invisible walls and where they must be moved to. 
     * @param time
     * @return map which maps a wall name to a List of Balls that are colliding with it if the wall is invisible.
     *    These Balls have a position which reflects where they will enter in the new Board they are moving to. 
     * Implemented by: sdrammis
     */
    public ConcurrentHashMap<String, List<Ball>> handleCollisions(double time) {
        ConcurrentHashMap<String, List<Ball>> ballsToPass = new ConcurrentHashMap<String, List<Ball>>();
        
        // itterate through the balls, if we a ball and something it collides with, handle it
        // and move to the next ball
        ballLoop:
        for (Ball ball : this.balls) {
            if (ball.getInAbsorber()) { 
                continue; 
            }
            
            for (OuterWall wall : this.walls) {
                ballsToPass.put(wall.getName(), new ArrayList<Ball>());
                // if it is invisible add it to the balls to send 
                if (wall.getTimeUntilCollision(ball) <=  0.000001 && wall.isInvisible()) {
                    //add it to the balls to pass
                    wall.reflectBall(ball, time); //get the new pos of the ball
                    ballsToPass.get(wall.getName()).add(ball); //this gives the new position for the ball on the board
                    continue ballLoop;
                } else if (wall.getTimeUntilCollision(ball) <=  0.000001) { //otherwise just reflect
                    wall.reflectBall(ball, time);
                    continue ballLoop;
                }
            }
        
            for (Gadget gadget : this.gadgets) {
                if (gadget.getTimeUntilCollision(ball) <=  0.000001) {
                        gadget.reflectBall(ball, time);
                        continue ballLoop;
                }
            }
            
            for (Ball otherBall : this.balls) {
                if (otherBall != ball) {
                    if (otherBall.getTimeUntilCollision(ball) <= 0.000001) {
                        otherBall.reflect(ball);
                        continue ballLoop;
                    } 
                }
            }
        }

        for (String wall : ballsToPass.keySet()) {
            this.balls.removeAll(ballsToPass.get(wall));
        }
        
        return ballsToPass;
    }
    
    /**
     * rep invariant: see top of class   
     * @author sdrammis   
     * Implemented by: nwallace  
     */
    public void checkRep() {
        List<Double> validCoords = new ArrayList<Double>(Arrays.asList(new Double(-0.01), new Double(20.01)));
        List<OuterWall> walls = new ArrayList<OuterWall>(Arrays.asList(topWall, bottomWall, leftWall, rightWall));
        boolean validCorners = true;
        for (OuterWall wall: walls){
            if(!validCoords.contains(new Double(wall.getWall().p1().x()))||!validCoords.contains(new Double(wall.getWall().p1().y()))
             ||!validCoords.contains(new Double(wall.getWall().p2().x()))||!validCoords.contains(new Double(wall.getWall().p2().y()))){  
                validCorners = false;
                break;
            }
        }
        
        Rectangle2D.Double boardArea = new Rectangle2D.Double(0, 0, 20, 20);
        boolean noOuterWallGadgets = true;
        for (Gadget gadget: gadgets){
            if(gadget.getClass()==OuterWall.class){
                noOuterWallGadgets = false;
                break;
            }
        }
        
        boolean allValidEntries = true;
        List<String> validVal = new ArrayList<String>(Arrays.asList("#", "*", "0", "/", "\\", "|", "-", "=", " "));
        for(String[] row: board){
            for(String val: row){
                if(!validVal.contains(val)){
                    allValidEntries = false;
                    break;
                }
            }
        }
        
        boolean neighborSize = (neighbors.size()<=4);
        
        // TEST THAT CAUGHT THE MULTIPLE ABSORBERS/GADGETS THAT LOOKED THE SAME 
//        Gadget absorberA = null;
//        Gadget absorberB = null;
//        for (Gadget g: this.gadgets){
//            if (g instanceof SquareBumper){
//                absorberA = ((SquareBumper) g).getTriggers().get(0);
//                System.out.println(absorberA);
//            }
//            if (g instanceof Absorber){
//                absorberB = (Absorber) g;
//                System.out.println(absorberB);
//            }
//        } assert (absorberA == absorberB);
//        System.out.println("validCorners " + validCorners);
//        System.out.println("noOuterWallGadgets " + noOuterWallGadgets);
//        System.out.println("allValidEntries " + allValidEntries);
//        System.out.println("neighborSize " + neighborSize);
        assert(validCorners&&noOuterWallGadgets&&allValidEntries&&neighborSize);
    }
    
    // ONLY TO BE USED FOR TESTING
    
    /**
     * Method for testing listener to make sure Balls are being constructed properly
     * @return a set of Balls in the Board where each Ball is constructed by defensive copying
     * @author asolei
     * Implemented by: asolei
     */
    public HashSet<Ball> getBallCopy(){
        HashSet<Ball> copySet = new HashSet<Ball>();
        for (Ball ball : this.balls){
            copySet.add(new Ball(ball.getName(), ball.getX(), ball.getY(), ball.getVelocity()));
        } return copySet;
    }
    
    public HashMap<String, String> getNeighbors() {
        return this.neighbors;
    }
    
    public List<OuterWall> getWalls() {
        return this.walls;
    }
}
