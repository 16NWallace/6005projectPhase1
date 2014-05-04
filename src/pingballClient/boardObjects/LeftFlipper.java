    package pingballClient.boardObjects;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Geometry.DoublePair;
import physics.Vect;

/**Rep invariant: the entire 2Lx2L square that the flipper can occupy is within the 20Lx20L grid. Orientation must be an
 * integer equal to 0, 90, 180 or 270. All gadgets that are triggered by this flipper must be instance of LeftFlipper, RightFlipper or
 * Absorber. Assert that 0<=rotatedState<=Math.PI/2
 * 
 * Thread safety: Although flippers are mutable, this mutation is protected by confinement. A flipper object can
 * only be instantiated once within a board because all gadgets in a board are unique. The board is manipulated by a 
 * single thread in PingballClient, so left flipper is thread-safe by confinement since it cannot be accessed by
 * other threads.
 * 
 * @author nwallace
 */

public class LeftFlipper implements Gadget {

    //topLeftX and topLeftY will be the topLeftCorner of the original state, even if the flipper is rotated
    private final double topLeftX;
    private final double topLeftY;
    
    //Line segment represents the rectangular shape of the flipper
    private LineSegment flipper;
    //Circles of 0-radius placed on ends of LineSegment for proper reflection, the first endpoint in the list is the center
    //rotation
    private final List<Circle> endPoints;
 
    private final String name;
    //List of gadgets that this flipper is a trigger for
    private final List<Gadget> triggerForAction;
    
    private final double REFLECT_COEFF;
    private final double ANGULAR_VELOCITY;
    
    private boolean selfTrigger;
    
    // the orientation is 0|90|180|270
    private final int orientation;
    
    // rotatedState describes the angular deviation in radians from the original state. it is a double between 0 and PI/2, with 
    //    0 corresponding to the original state and PI/2 corresponding to the fully flipped state. in terms of this
    //    parameter whether we are going from original to fully flipped or vice versa doesn't matter
    private double rotatedState;
    
    //Whether or not the flipper has been flipped completely - false if the flipper is how it was initialized;
    //true if the flipper has been flipped completely an odd number of times
    private boolean fullyFlipped;
    
    // Keeps track of whether or not we can rotate the flipper 
    private boolean canRotate;
    
    //center of rotation to rotate line segments around to get sides in flipped state, one of the endpoints
    private Vect cor;

    /** Constructs a rectangular left flipper in the specified orientation.
     * 
     * @param name
     * @param topLeftCorner DoublePair coordinate of topLeftCorner, center of rotation
     * @param orientation must be int 0|90|180|270
     * @param selfTrigger true if the trigger is self-triggering, meaning that it rotates when it collides with a ball
     * @param action List of Gadgets whose actions the constructed object will cause when triggered
     * @author asolei, nwallace
     * 
     * Implemented by: nwallace
     */
    public LeftFlipper(String name, DoublePair topLeftCorner, int orientation, boolean selfTrigger, List<Gadget> action) {
        this.topLeftX = topLeftCorner.d1;
        this.topLeftY= topLeftCorner.d2;
        
        
        this.name = name;
        this.orientation = orientation;
        this.fullyFlipped = false;
        this.canRotate = false;
        this.selfTrigger = selfTrigger;
        this.rotatedState = 0.0;
        
        REFLECT_COEFF = 0.95;
        ANGULAR_VELOCITY = 6*Math.PI;
        this.rotatedState = 0;
        

        // initialize the list of Gadgets the Flipper triggers
        this.triggerForAction = action;
        
        //Initialize appropriate representation
        switch(orientation){

           case 0:
               this.flipper = new LineSegment(topLeftX, topLeftY, topLeftX, topLeftY+2);
               this.endPoints = Collections.synchronizedList(new ArrayList<Circle>(Arrays.asList(
                                new Circle(topLeftX, topLeftY,0), new Circle(topLeftX, topLeftY+2, 0))));
               break;
           case 90:
               this.flipper = new LineSegment(topLeftX+2, topLeftY, topLeftX, topLeftY);
               this.endPoints = Collections.synchronizedList(new ArrayList<Circle>(Arrays.asList(
                       new Circle(topLeftX+2, topLeftY,0), new Circle(topLeftX, topLeftY, 0))));
               break;
           case 180:
               this.flipper = new LineSegment(topLeftX+2, topLeftY+2, topLeftX+2, topLeftY);
               this.endPoints = Collections.synchronizedList(new ArrayList<Circle>(Arrays.asList(
                       new Circle(topLeftX+2, topLeftY+2,0), new Circle(topLeftX+2, topLeftY, 0))));
               break;
           case 270:
               this.flipper = new LineSegment(topLeftX, topLeftY+2, topLeftX+2, topLeftY+2);
               this.endPoints = Collections.synchronizedList(new ArrayList<Circle>(Arrays.asList(
                       new Circle(topLeftX, topLeftY+2,0), new Circle(topLeftX+2, topLeftY+2, 0))));
               break;
          
            default:
                this.flipper = new LineSegment(topLeftX, topLeftY, topLeftX, topLeftY+2);
                this.endPoints = Collections.synchronizedList(new ArrayList<Circle>(Arrays.asList(
                        new Circle(topLeftX, topLeftY,0), new Circle(topLeftX, topLeftY+2, 0))));
                break; 
        
        }
        this.cor = endPoints.get(0).getCenter();
        checkRep();
    }

    /** Getter method for the name of the flipper
     * 
     * @return name String name of the LeftFlipper 
     * @author nwallace
     */

    @Override
    public String getName() {
        return name;
    }
    
    /** Find the minimum time in which the ball will collide with any part of the flipper(edge or corner), and returns that
     * time. This time can be mapped to the appropriate point of contact with the flipper in the reflectBall() method.
     * @param Ball ball ball for which the soonest collision with the flipper is occurring
     * @return double time until the ball collides with the flipper
     * @author nwallace
     * 
     * Implemented by: nwallace
     */

    @Override
    public double getTimeUntilCollision(Ball ball) {
        
        double collisionTimeSide = Geometry.timeUntilWallCollision(flipper, ball.getBall(), ball.getVelocity());
        double collisionTimeEndPoint0 = Geometry.timeUntilCircleCollision(endPoints.get(0), ball.getBall(), ball.getVelocity());
        double collisionTimeEndPoint1 = Geometry.timeUntilCircleCollision(endPoints.get(1), ball.getBall(), ball.getVelocity());
        double minTime = Collections.min(Arrays.asList(collisionTimeSide, collisionTimeEndPoint0, collisionTimeEndPoint1));

        return minTime;

    }
    
    /**Updates the velocity vector of the ball when it collides with a flipper, either on the side
     * or corner of the flipper.
     * @param Ball ball ball that will be checked for collision and updated if it collides
     * @param time the time interval for which we want to consider updating
     * @return Ball with updated velocity vector after colliding
     * @throws Exception -- not thrown by this gadget
     * @author nwallace
     * 
     * Implemented by: nwallace, asolei
     */
    @Override

    public void reflectBall(Ball ball, double time) {
        double minCollisionTime = getTimeUntilCollision(ball);
        
       
        double collisionTimeSide = Geometry.timeUntilWallCollision(flipper, ball.getBall(), ball.getVelocity());
        double collisionTimeEndPoint0 = Geometry.timeUntilCircleCollision(endPoints.get(0), ball.getBall(), ball.getVelocity());
        double collisionTimeEndPoint1 = Geometry.timeUntilCircleCollision(endPoints.get(1), ball.getBall(), ball.getVelocity());
        
        Vect newVel=null;
        
        //determine which component that ball collided with, and reflect the ball off of that component appropriately
        if (minCollisionTime==collisionTimeSide){
            newVel = Geometry.reflectRotatingWall(flipper, cor, ANGULAR_VELOCITY, ball.getBall(), ball.getVelocity(), REFLECT_COEFF);
        } else if (minCollisionTime==collisionTimeEndPoint0){
            newVel = Geometry.reflectRotatingCircle(endPoints.get(0), cor, ANGULAR_VELOCITY, ball.getBall(), ball.getVelocity(), REFLECT_COEFF);
        } else if (minCollisionTime==collisionTimeEndPoint1){
            newVel = Geometry.reflectRotatingCircle(endPoints.get(1), cor, ANGULAR_VELOCITY, ball.getBall(), ball.getVelocity(), REFLECT_COEFF);
        } 
        
        ball.setVelocity(newVel);
        
        //when this flipper reflects a ball, it triggers the actions of all other gadgets that it is connected to
        for (Gadget action: triggerForAction){
            action.respondToTrigger(minCollisionTime);
        }
        
        //Makes the flipper respond if it is self-triggering
        if(selfTrigger){
            this.respondToTrigger(time);
        }
    }

    /**A LeftFlipper sweeps about its center of rotation, within the bounding box, when triggered 
     *      (counterclockwise if original state, clockwise if already flipped).
     * Mutator method that updates the flipper after this rotation. If the flipper is already in
     * its rotated state, then it rotates in the opposite direction towards its original state.
     * @param Ball ball the ball that is triggering this event
     * @param time the time interval for which we are considering updating
     * @return the ball that triggers the event 
     * @author asolei, nwallace
     * Implemented by: nwallace, asolei
     */
    @Override
    public void respondToTrigger(double time) {
        this.canRotate = true;

    }
    
    /**
     * If the Flipper canRotate, rotate it for the specified time. 
     * @param time for which we want the flipper to rotate
     * @author asolei
     * Implemented by: asolei
     */
    @Override
    public void move(double time) {
        if (canRotate){
            this.rotateFlipper(time);
        } else {
            checkRep();
        }
        
    }
    
    /**Helper method to be used in reflectBall and respondToTrigger that sweeps the flipper in the appropriate direction.
     * Counterclockwise from original state, clockwise from fully flipped back to original state.
     * 
     * @param time the timespan for which we want to move the flipper
     * @author asolei
     * 
     * Implemented by: asolei
     */
    private void rotateFlipper(double time){   
        

        // if not fully flipped, we rotate counterclockwise
        if (! fullyFlipped ){
            double rotateValue = -1 * this.ANGULAR_VELOCITY * time;
            // check if we are going to make a full rotation or leave the bounding box
            if (rotateValue <= (-Math.PI/2)){
                // rotate the flipper's line segment 90 degrees, and set fullyFlipped to true
                Angle angle90Counter = new Angle(-Math.PI/2);
                LineSegment newFlipper = Geometry.rotateAround(this.flipper, getCOR(), angle90Counter);
                // rotate the endpoint
                Circle oldEndpoint = this.endPoints.remove(1);
                Circle newEndpoint = Geometry.rotateAround(oldEndpoint, getCOR(), angle90Counter);
                // mutate the flipper. we have stopped rotating
                this.flipper = newFlipper;
                this.endPoints.add(newEndpoint);
                this.fullyFlipped = true;
                this.canRotate = false;
                this.rotatedState = 0.0;
            } 
            // otherwise we are going to make a partial rotation
            else {
                Angle rotateCounter = new Angle(rotateValue);
                // rotate the flipper's line segment the specified amount about the center point
                LineSegment newFlipper = Geometry.rotateAround(this.flipper, getCOR(), rotateCounter);
                // rotate the endpoint
                Circle oldEndpoint = this.endPoints.remove(1);
                Circle newEndpoint = Geometry.rotateAround(oldEndpoint, getCOR(), rotateCounter);
                // mutate the flipper
                this.flipper = newFlipper;
                this.endPoints.add(newEndpoint);
                // check how far it will be rotating from it's current state, if that's greater than 90 deg
                //      it will be fullyFlipped, and set the rotated state to 0. this also means that we 
                //      have stopped rotating
                if ((this.rotatedState + (-1 * rotateValue)) >= Math.PI/2){
                    this.fullyFlipped = true;
                    this.canRotate = false;
                    this.rotatedState = 0.0;
                } // otherwise it's still not fully flipped, add the rotation value to rotatedState
                else {
                    this.rotatedState += -1 * rotateValue;
                }
            }
        } // if it is fully flipped we rotate clockwise
        else if (fullyFlipped){
            double rotateValue = this.ANGULAR_VELOCITY * time;
            // check if we are going to make a full rotation or leave the bounding box
            if (rotateValue >= (Math.PI/2)){
                // rotate the flipper's line segment 90 degrees, and set fullyFlipped to false
                Angle angle90Clock = new Angle(Math.PI/2);
                LineSegment newFlipper = Geometry.rotateAround(this.flipper, getCOR(), angle90Clock);
                // rotate the endpoint
                Circle oldEndpoint = this.endPoints.remove(1);
                Circle newEndpoint = Geometry.rotateAround(oldEndpoint, getCOR(), angle90Clock);
                // mutate the flipper. we have also stopped rotating at this point.
                this.flipper = newFlipper;
                this.endPoints.add(newEndpoint);
                this.fullyFlipped = false;
                this.canRotate = false;
                this.rotatedState = 0.0;
            }
            // otherwise we are going to make a partial rotation clockwise
            else {
                Angle rotateClock = new Angle(rotateValue);
                // rotate the flipper's line segment the specified amount about the center point
                LineSegment newFlipper = Geometry.rotateAround(this.flipper, getCOR(), rotateClock);
                // rotate the endpoint
                Circle oldEndpoint = this.endPoints.remove(1);
                Circle newEndpoint = Geometry.rotateAround(oldEndpoint, getCOR(), rotateClock);
                // mutate the flipper
                this.flipper = newFlipper;
                this.endPoints.add(newEndpoint);
                // check how far it will be rotating from it's current state, if that's greater than 90 deg
                //      it will go bact to not fullyFlipped. set the rotated state to 0. this also means that we 
                //      have stopped rotating
                if ((this.rotatedState + (rotateValue)) >= Math.PI/2){
                    this.fullyFlipped = false;
                    this.canRotate = false;
                    this.rotatedState = 0.0;
                } // otherwise add the rotation value to rotatedState
                else {
                    this.rotatedState += rotateValue;
                }
            }
        }

    }


    /**
     * Generates a String 'Message' which specifies the LeftFlipper's state within it's bounding box.
     * 
     * @return String message. 
     *      "FLIPPER | L" if vertical in left half of bounding box. 
     *      "FLIPPER | R" if vertical in right half of bounding box. 
     *      "FLIPPER - T" if horizontal in top half of bounding box. 
     *      "FLIPPER - B" if horizontal in bottom half of bounding box. 
     * @author asolei
     * 
     * Implemented by: asolei, nwallace
     */
    @Override
    public String toString() {
        String verticalL = "FLIPPER | L";
        String verticalR = "FLIPPER | R";
        String horizontalT = "FLIPPER - T";
        String horizontalB = "FLIPPER - B";
        
        //Determine value of closeToFlipped using rotatedState, which effectively represents how far the flipper has 
        //  moved through the bounding box
        boolean closeToFlipped = false;
        if (fullyFlipped){
            closeToFlipped = true;
        }
        if (! closeToFlipped){
            if (rotatedState < Math.PI/4){
                closeToFlipped = false;
            } else {
                closeToFlipped = true;
            }
        }
        
        //Return the appropriate vertical or horizontal representation of the flipper
        switch (orientation){
            case 0:
                if (closeToFlipped){
                    return horizontalT;
                } else {
                    return verticalL;
                }
            case 90:
                if (closeToFlipped){
                    return verticalR;
                } else {
                    return horizontalT;
                }
            case 180:
                if (closeToFlipped){
                    return horizontalB;
                } else {
                    return verticalR;
                }
            case 270:
                if (closeToFlipped){
                    return verticalL;
                } else {
                    return horizontalB;
                }
            // should never get here by rep invariant
            default:
                return "";
        }
    }
    
    /**
     * Get the xcoord of the top-left corner of the bounding box
     * @return xcoord
     * @author asolei
     */
    @Override
    public double getX() {
        return this.topLeftX;
    }
    
    /**
     * Get the ycoord of the top-left corner of the bounding box
     * @return ycoord
     * @author asolei
     */
    @Override
    public double getY() {
        return this.topLeftY;
    }
    
    /**
     * Get the center of rotation of the LeftFlipper within it's bounding box. 
     * @return Vect center of rotation 
     * @author asolei
     */
    public Vect getCOR(){
        return this.cor;
    }
    
    /**Rep invariant: the entire 2Lx2L square that the flipper can occupy is within the 20Lx20L grid. Orientation must be an
     * integer equal to 0, 90, 180 or 270. All gadgets that are triggered by this flipper must be instance of LeftFlipper, RightFlipper or
     * Absorber. Assert that 0<=rotatedState<=Math.PI/2
     * @author nwallace
     * 
     * Implemented by: nwallace
     */
    public void checkRep(){
        Rectangle2D.Double border = new Rectangle2D.Double(0,0,20,20);
        boolean containsCorner = (border.contains(topLeftX, topLeftY,2,2));
        boolean validOrientation = ((orientation==0)||(orientation==90)||(orientation==180)||(orientation==270));

        boolean validRotatedState = (0<=rotatedState)&&(rotatedState<=Math.PI/2);
        assert(containsCorner&&validOrientation&&validRotatedState);
    }
    
    /**
     * Get the List of Gadgets the Flipper triggers
     * @return the list of Gadgets the Flipper triggers
     * @author asolei
     */
    @Override
    public List<Gadget> getTriggers() {
        return this.triggerForAction;
    }
    
    /**
     * Set the flipper to be self-triggering. 
     * @author asolei
     */
    @Override
    public void setSelf() {
        this.selfTrigger = true;
        
    }
    
    /**
     * Add some Gadgets to the List of Gadgets the Flipper triggers
     * @param toAdd what we wannt to add
     * @author asolei
     */
    @Override
    public void addGadgets(List<Gadget> toAdd) {
        this.triggerForAction.addAll(toAdd);
        
    }

}
