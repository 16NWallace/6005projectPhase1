package pingballClient.boardObjects;




import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.*;

import physics.*;
import physics.Geometry.DoublePair;


/** An Absorber is a rectangular gadget on the board. When the ball contacts it, it is not reflected. The Absorber
 * captures the ball, and stores it in the bottom right corner of the absorber. If a ball hits the absorber while
 * it has a stored ball, the held ball is released straight upwards from the bottom right corner, and the ball that hit 
 * the absorber becomes stored. 
 * 
 * An Absorber can be self-triggering, meaning that when a ball collides with the absorber, it can be redirected
 * and the released straight upwards from the bottom right-hand corner of the Absorber.
 *
 *Rep invariant: rectangular-shape, 0<width<=20, 0<height<=20. All corners of the absorber have x,y such
 * that 0<=x<=20, 0<=y<=20 so that the entire shape is contained within the board.
 * The only objects that are valid for triggerForAction are Absorber and Flipper.
 * hasStoredBall must be false if blockingQueue is empty.
 * 
 * Thread safety argument:
 * An absorber object is confined to one board, and each board is manipulated by only one player thread, so absorber
 * is thread-safe by confinement.
 * 
 * @author asolei, nwallace
 */

public class Absorber implements Gadget {
    
    //Could add an integer for time delay that decrements every time getTimeUntilCollision is called
    
    private final double topLeftX;
    private final double topLeftY;
    private final double width;
    private final double height;
    private final List<LineSegment> sides;
    private final List<Circle> corners;
    private final String name;
    private List<Gadget> triggerForAction;

    private final List<Vect> coords;
    
    
    private final Rectangle2D.Double absorberArea;
    //Coordinate from which a ball is released from the absorber
    private final DoublePair launchSite;
    private static BlockingQueue<Ball> ballsInAbs;
    private boolean selfTrigger;
    private double delayTime;
    
    
    
    /** Constructs an absorber from its name, dimensions, and the coordinate for its top left corner. Also
     * specifies whether or not this absorber triggers any actions, including itself.
     * 
     * @param width double width<=20; specifies width of the board
     * @param height double height<=20; specifies height of the board
     * @param topLeftCorner DoublePair x,y-coordinate for the top left corner of the rectangular Absorber
     * @param name String name for the Absorber
     * @param selfTrigger true if the Absorber is self-triggering
     * @param action list of Gadgets which the Absorber triggers. 
     * @author asolei, nwallace
     */
    public Absorber(double width, double height, DoublePair topLeftCorner, String name, boolean selfTrigger, ArrayList<Gadget> gadgets) {
        this.topLeftX = topLeftCorner.d1;
        this.topLeftY= topLeftCorner.d2;
        this.width = width;
        this.height = height;
        this.name = name;
        this.selfTrigger = selfTrigger;

        Absorber.ballsInAbs = new ArrayBlockingQueue<Ball>(100);
        
        this.launchSite = new DoublePair((this.topLeftX + this.width - 0.26), (this.topLeftY - 0.26)); // ball leaves absorber here
        
        delayTime = height/50.0;
        
        // get the coordinates of the absorber as Vectors
        Vect topLeft = new Vect(topLeftCorner.d1, topLeftCorner.d2);
        Vect topRight = new Vect(topLeftCorner.d1+width, topLeftCorner.d2);
        Vect bottomLeft = new Vect(topLeftCorner.d1, topLeftCorner.d2+height);
        Vect bottomRight = new Vect(topLeftCorner.d1+width, topLeftCorner.d2+height);
        this.coords = new ArrayList<Vect>(Arrays.asList(topLeft, topRight, bottomLeft, bottomRight));

        
        this.sides = new ArrayList<LineSegment>();
        this.corners = new ArrayList<Circle>();
        
        // initialize list of Gadgets the absorber triggers
        this.triggerForAction = gadgets;
        
        this.absorberArea = new Rectangle2D.Double(topLeftX, topLeftY, width, height);
        
        //Creates line segments for the edges of the rectangle and circles
        //   for the corners by going around clockwise between the corner coordinates
        for (int i=0; i<4; i++){
            LineSegment side;
            if (i!=3){
            side = new LineSegment(coords.get(i), coords.get(i+1));
            } else {
                side = new LineSegment(coords.get(i), coords.get(0));
            }
            Circle corner = new Circle(coords.get(i).x(), coords.get(i).y(), 0);
            corners.add(corner);
            sides.add(side);
        }

        checkRep();
    }
    
    /**
     * Calculate the time until a given Ball will collide with the absorber
     * @param ball the ball for which we want to know when it will collide with the absorber
     * @return time until collision. If the Ball will never collide with the absborber,
     *      returns POSITIVE_INFINITY. Otherwise returns the time until the Ball will hit 
     *      the absorber, in seconds. 
     * @author asolei 
     * Implemented by: sdrammis, nwallace
     */
    @Override
    public double getTimeUntilCollision(Ball ball) {
        double minTime = Double.POSITIVE_INFINITY;
        for (LineSegment side : sides) {
            double timeUntilCollision = Geometry.timeUntilWallCollision(side, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision < minTime) {
                minTime = timeUntilCollision;
            }
        }
        
        for (Circle corner : corners) {
            double timeUntilCollision = Geometry.timeUntilCircleCollision(corner, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision < minTime) {
                minTime = timeUntilCollision;
            }
        }
        return minTime;
        
    }
    

    /**An Absorber captures the ball on collision, and stores it in its bottom right corner. If the Absorber is 
     *      self-triggering, then the collision with the Ball will cause the Absorber to respond to the self-trigger 
     *      event by releasing a Ball if it is already holding one. The collision of the ball with the Absorber
     *      also triggers the actions of the Gadgets linked to this 
     * @param ball the Ball that is colliding with the absorber
     * @param time the span of time for which we want to update the Absorber
     * @return Ball if the absorber is self-triggering and already contains a ball 
     * @throws Exception if the absorber is self-triggering and does not contain a ball, or if the Absorber is not
     *    linked to any other Gadgets
     * @author asolei
     * Implemented by: sdrammis, asolei, nwallace
     */
    @Override

    public void reflectBall(final Ball ball, double time)  {

        if (this.selfTrigger) {
            ball.setCoord(this.launchSite.d1, this.launchSite.d2);
            ball.setVelocity(new Vect(0, -50));
        } else {
            ball.setInAbsorber(true);
            ball.setCoord(22, 0);
            ball.setVelocity(new Vect(0,0));
            
            Thread delay = new Thread( new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long) delayTime);
                    } catch (InterruptedException e) { e.printStackTrace(); }
                    Absorber.ballsInAbs.add(ball);
                }
            });
            delay.start();
            
        } for (Gadget action: triggerForAction){
            action.respondToTrigger(time);
        }

    }
    
    /**An Absorber shoots out a stored ball when triggered, if it has one stored, and if enough 
     *  time has passed since the Absorber was initially triggered. 
     * An Absorber can be self-triggered, meaning that when a ball contacts the absorber, it is placed in the bottom right-hand corner 
     *  and shot straight upwards at 50L/sec (for default values of gravity and friction). 
     * 
     * @param ball the Ball that triggers the response
     * @return the Ball released by the Absorber, if applicable
     * @throws Exception if no Ball is launched by the Absorber when this method is called
     * @author asolei
     * Implemented by: sdrammis, asolei, nwallace
     */
    @Override
    public void respondToTrigger(double time) {
        if (! Absorber.ballsInAbs.isEmpty() ){
            Ball launchBall = Absorber.ballsInAbs.poll();
            launchBall.setInAbsorber(false);
            launchBall.setCoord(this.launchSite.d1, this.launchSite.d2);
            launchBall.setVelocity(new Vect(0, -50));
        }
    }

    /**Rep invariant: rectangular-shape, 0<width<=20, 0<height<=20. All corners of the absorber have x,y such
     * that 0<=x<=20, 0<=y<=20 so that the entire shape is contained within the board.
     * The only objects that are valid for triggerForAction are Absorber and Flipper.
     * hasStoredBall must be false if blockingQueue is empty.
     * 
     * @author nwallace
     * Implemented by: nwallace
     */
    @Override
    public void checkRep(){
        Rectangle2D.Double border = new Rectangle2D.Double(0,0,20,20);
        boolean containsShape = (border.contains(topLeftX, topLeftY, width, height));
        boolean validCorners = true;
        for (Vect coord: coords){
            if(!(border.contains(coord.x(), coord.y()))){
              validCorners = false;
              break;
            }
        }
        assert(containsShape&&validCorners);
    }
    
    /**Returns a 'Message' which will be used to print the Absorber in the printed 
     *    Board output. 
     * @return string message, which is of the form 
     *     ABS [width] [height]
     * @author asolei
      * Implemented by: sdrammis, nwallace
     */
    @Override 
    public String toString(){
        return "ABS " + (int) width + " " + (int) height;  
    }
    
    /**
     * Returns the name of the Absorber
     * @return name
     * @author asolei
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Returns the x-coord of the topLeft corner of the Absorber
     * @return x-coord
     * @author asolei
     */
    @Override
    public double getX() {
        return this.topLeftX;
    }
    
    /**
     * Returns the y-coord of the topLeft corner of the Absorber
     * @return y-coord
     * @author asolei
     */
    @Override
    public double getY() {
        return this.topLeftY;
    }
    
    /**
     * An absorber cannot move. Assert the rep invariant. 
     * @param time
     * @author asolei
     */
    @Override
    public void move(double time) {
        checkRep();
        
    }
    
    /**
     * Get the list of Gadgets the Absorber triggers.
     * @return gadgets the Absorber triggers
     * @author asolei
     */
    @Override
    public List<Gadget> getTriggers() {
        return this.triggerForAction;
    }
    
    /**
     * Set the Absorber to be self-triggering. 
     * @author asolei
     */
    @Override
    public void setSelf() {
        this.selfTrigger = true;
        
    }
    
    /**
     * Add some Gadgets to the list of Gadgets the Absorber triggers
     * @param toAdd the Gadgets we want to add
     * @author asolei
     */
    @Override
    public void addGadgets(List<Gadget> toAdd) {
        this.triggerForAction.addAll(toAdd);
        
    }
}
