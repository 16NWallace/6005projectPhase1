package pingballClient.boardObjects;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import physics.Geometry.DoublePair;
import physics.*;

/**Rep invariant: The only objects that are valid for triggerForAction are Absorber and Flipper.
 * Asserts that this object is a circle. Asserts that the entire circle is within the area of the board.
 * 
 * Thread safety argument: all gadgets are kept in a board, and each board is only touched by one thread, so this
 * datatype is threadsafe by confinement and encapsulation.
 * 
 * @author nwallace
 */

public class CircleBumper implements Gadget {
    //top left corner of the 1Lx1L square that contains the circle bumper
    private final double topLeftX;
    private final double topLeftY;
    private final boolean selfTriggering;
    
    private final Vect center;
    private final String name;
    private final Circle bumper;
    
    private final double REFLECT_COEFF=1.0;
   //List of gadgets that this flipper is a trigger for
    private List<Gadget> triggerForAction;
    
    /**Constructs a circular bumper of diameter 1 L from its center point and a specified name string
     * 
     * @param name String that gives the bumper an identifier
     * @param center DoublePair of x,y-coordinate where the bumper will be centered
     * @param action a List that contains other Gadgets whose actions this object will cause when it is triggered
     * @author nwallace, asolei
     */
    public CircleBumper(String name, DoublePair topLeftCorner,  List<Gadget> actions){
        this.selfTriggering = false;
        this.topLeftX = topLeftCorner.d1;
        this.topLeftY = topLeftCorner.d2;
        this.center = new Vect(topLeftX+0.5, topLeftY+0.5);
        this.name = name;
        this.bumper = new Circle(center.x(), center.y(), 0.5);
        
        // initialize list of Gadgets the bumper triggers
        this.triggerForAction = actions;
        checkRep();   
    }
    
    /** Determines how long it will take for a ball to collide with the circular bumper. 
     * @param Ball ball
     * @return double minimum time until collision with the circle bumper. POSITIVE_INFINITY if the Ball
     *    will never collide with this
     * @author nwallace
     * Implemented by: nwallace
     */
    @Override
    public double getTimeUntilCollision(Ball ball) {
        // determine time until collision
        double collisionTime = Geometry.timeUntilCircleCollision(bumper, ball.getBall(), ball.getVelocity());
        return collisionTime;
    }

    
    /**Updates the position and velocity of the ball. This method is called when the this.getTimeUntilCollision(Ball ball) 
     * is the minimum within the time bounds of one time-step, as determined when the board is updated.
     * 
     * @param Ball ball that is being checked for collision 
     * @return Ball ball with updated position and velocity based on whether the ball collides with the bumper
     * @throws Exception 
     * @author asolei, nwallace
     * Implemented by: nwallace
     */
    @Override


    public void reflectBall(Ball ball, double time){

        Vect newVel = Geometry.reflectCircle(this.bumper.getCenter(), ball.getPosition(), ball.getVelocity(), REFLECT_COEFF);
        ball.setVelocity(newVel);
        
        // make sure the Gadgets that this triggers respond to the reflection
        for (Gadget action: triggerForAction){
            action.respondToTrigger(time);
        }
        
    }

    /**A circle bumper has no 'action', it is never triggered by another object. This method should never be called
     * on a circle object, so it checks the representation of the circle.
     * @author nwallace
     */
    @Override
    public void respondToTrigger(double time){
    }
    
    /**
     * Returns the String representation of a CircleBumper 
     * @return "0"
     * @author asolei
     */
    @Override
    public String toString(){
        return "0";
    }

    /**Getter method for the name assigned to the bumper.
     * 
     * @return String name that was assigned to the object upon creation
     * @author nwallace
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Getter method for the xcoord of the CircleBumper bounding box
     * @return xcoord
     * @author asolei
     */
    @Override
    public double getX() {
        return this.topLeftX;
    }
    
    /**
     * Getter method for the ycoord of the CircleBumper bounding box
     * @return ycoord
     * @author asolei
     */
    @Override
    public double getY() {
        return this.topLeftY;
    }
    
    /**
     * Getter method for the Circle corresponding to the CircleBumper 
     * @return Circle
     * @author asolei
     */
    public Circle getBumper(){
        return this.bumper;
    }
    
    /**
     * Getter method for a Vector representing the center of the CircleBumper
     * @return vector representing center of object
     * @author asolei
     */
    public Vect getCenter(){
        return this.center;
    }
    
    /**
     * A CircleBumper cannot move. Assert the rep invariant
     * @author asolei
     */
    @Override
    public void move(double time) {
        checkRep();       
    }

    /**Rep invariant: Asserts that this object is a circle. Asserts that the entire circle is within the area 
     * of the board.
     * @author nwallace
     */
    public void checkRep(){
        boolean circleType = (this.bumper instanceof Circle);
        
        Rectangle2D.Double border = new Rectangle2D.Double(0,0,20,20);
        boolean allCornersInBorder =(border.contains(topLeftX, topLeftY, 2, 2));
        assert(circleType&&allCornersInBorder);
    }
    
    /**
     * Return the List of Gadgets the bumper triggers
     * @return list of Gadgets the bumper triggers
     * @author asolei
     */
    @Override
    public List<Gadget> getTriggers() {
        return this.triggerForAction;
    }
    
    /**
     * Does nothing
     * @author asolei
     */
    @Override
    public void setSelf(){}
    
    /**
     * Add some Gadgets to the List of Gadgets the bumper triggers
     * @param toAdd Gadgets to be added
     * @author asolei
     */
    @Override
    public void addGadgets(List<Gadget> toAdd) {
        this.triggerForAction.addAll(toAdd);
        
    }

}
