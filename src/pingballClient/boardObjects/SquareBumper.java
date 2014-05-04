package pingballClient.boardObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import physics.Geometry.DoublePair;
import physics.*;
import warmup.BallWarmup;

/**Rep invariant: all sides must be of length one, and there is a Circle at the end of each side for
 * the corners, 0<=(topLeftX and topLeftY)<=19 so that the entire square is contained within the board
 * 
 * Thread safety argument: all gadgets are kept in a board, and each board is only touched by one thread, so this
 * datatype is threadsafe by confinement and encapsulation.
 * 
 * @author nwallace
 */

public class SquareBumper implements Gadget {

    private final List<LineSegment> sides;
    private final List<Circle> corners;
    private final String name;
    private final double REFLECT_COEFF;
    private List<Gadget> triggerForAction;
    private final List<Vect> coords;
    
    private final double topLeftX;
    private final double topLeftY;

    
    /**Constructs a 1Lx1L square bumper from an xy-coordinate for the top left corner and a String for them name.
     * 
     * @param topLeftCorner DoublePair x,y 
     * @param name String name for the bumper
     * @param action List of Gadgets that contains other gadgets whose actions this object will trigger when it is triggered
     * @author nwallace, asolei
     */
    public SquareBumper(String name, DoublePair topLeftCorner,  List<Gadget> action){
        this.name = name;
        this.topLeftX = topLeftCorner.d1;
        this.topLeftY = topLeftCorner.d2;
        
        Vect topLeft = new Vect(topLeftCorner.d1, topLeftCorner.d2);
        Vect topRight = new Vect(topLeftCorner.d1+1, topLeftCorner.d2);
        Vect bottomLeft = new Vect(topLeftCorner.d1, topLeftCorner.d2+1);
        Vect bottomRight = new Vect(topLeftCorner.d1+1, topLeftCorner.d2+1);
        coords = new ArrayList<Vect>(Arrays.asList(topLeft, topRight, bottomRight, bottomLeft));
        
        REFLECT_COEFF = 1.0;
        
        this.sides = new ArrayList<LineSegment>();
        this.corners = new ArrayList<Circle>();
        
        // if the bumper is not linked to anything, prevent a nullPointerException by instantiating an empty array
        this.triggerForAction = action;
        
        //Creates line segments for the edges of the square by going around clockwise between the corners
        for (int i=0; i<4; i++){
            LineSegment side;
            if (i!=3){
                side = new LineSegment(coords.get(i), coords.get(i+1));
                sides.add(side);
            } else {
                side = new LineSegment(coords.get(i), coords.get(0));
                sides.add(side);
            }
            Circle corner = new Circle(coords.get(i).x(), coords.get(i).y(), 0);
        }
        checkRep();
        
    }
    
    /**Rep invariant: all sides must be of length one, and there is a Circle at the end of each side for
     * the corners, 0<=(topLeftX and topLeftY)<=19
     * @author nwallace
     * Implemented by: nwallace
     */
    public void checkRep(){
        boolean allSidesSame=true;
        boolean lineSegmentEndCoords = true;
        double sideLength = 1.0;
        // check sides
        for (LineSegment side: sides){
            if (side.length()!=sideLength){
                allSidesSame=false;
                break;
            } else if(!coords.contains(side.p1())&&coords.contains(side.p2())){
                lineSegmentEndCoords = false;
                break;
            }
        }
        // check corners
        boolean validCorners = true;
        for (Circle corner: corners){
            if (!coords.contains(corner.getCenter())&&coords.contains(corner.getCenter())){
               validCorners = false;
               break;
            }
        }

        boolean topLeftInBoard = (0<=topLeftX)&&(0<=topLeftY)&&(topLeftX<=19)&&(topLeftY<=19);
        
        assert(allSidesSame&&lineSegmentEndCoords&&topLeftInBoard&&validCorners);
    }
    
    /** Find the minimum time in which the ball will collide with any part of the bumper(edge or corner), and returns that
     * time. This time can be mapped to the appropriate point of contact with the bumper in the reflectBall() method.
     * @param Ball ball ball for which the soonest collision with the bumper is occurring
     * @return double time until the ball collides with the bumper
     * @author sdrammis
     * Implemented by: nwallace
     */
    @Override
    public double getTimeUntilCollision(Ball ball) {
        double minTime = Double.POSITIVE_INFINITY;
        // consider the sides. if time for a particular side is less than minTime, reset minTime
        for (LineSegment side : sides) {
            double timeUntilCollision = Geometry.timeUntilWallCollision(side, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision <= minTime) {
                minTime = timeUntilCollision;
            }
        }
        
        // consider the corners. if time for a particular corner is less than minTime, reset minTime
        for (Circle corner : corners) {
            double timeUntilCollision = Geometry.timeUntilCircleCollision(corner, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision <= minTime) {
                minTime = timeUntilCollision;
            }
        }

        return minTime;
    }

    /**Updates the velocity vector of the ball when it collides with a square bumper, either on the side
     * or corner of the square.
     * @param ball Ball ball that will be checked for collision and updated if it collides
     * @return Ball with updated velocity vector after colliding
     * @throws Exception 
     * @author nwallace, sdrammis
     * Implemented by: nwallace
     */
    @Override

    public void reflectBall(Ball ball, double time){
        Vect startVel = ball.getVelocity();
        double startX = ball.getX();
        double startY = ball.getY();
        
        double minCollisionTime = getTimeUntilCollision(ball);
        double minTimeSide = Double.POSITIVE_INFINITY;
        double minTimeCorner = Double.POSITIVE_INFINITY;
        
        LineSegment wallForCollision = null;
        Circle cornerForCollision = null;
        
        // determine if the ball will collide with any of the sides
        for (LineSegment side : sides) {
            double timeUntilCollision = Geometry.timeUntilWallCollision(side, ball.getBall(), startVel);
            if (timeUntilCollision <= minTimeSide) {
                minTimeSide = timeUntilCollision;
                wallForCollision = side;
            }
        }
        
        // determine if the ball will collide with any of the corners
        for (Circle corner: corners) {
            double timeUntilCollision = Geometry.timeUntilCircleCollision(corner, ball.getBall(), startVel);
            if (timeUntilCollision <= minCollisionTime) {
                minTimeCorner = timeUntilCollision;
                cornerForCollision = corner;
            }
        }

        // make sure the triggered Gadgets respond

        for (Gadget action: triggerForAction){
            action.respondToTrigger(minCollisionTime);
        }
        
        // check if it is going to reflect off a side
        Vect newVel=new Vect(0,0);
        if(minTimeSide<minTimeCorner){
            newVel = Geometry.reflectWall(wallForCollision, ball.getVelocity(), REFLECT_COEFF);
        } 
        // otherwise reflect off the corner
        else {
            newVel = Geometry.reflectCircle(cornerForCollision.getCenter(), ball.getPosition(), ball.getVelocity(), REFLECT_COEFF);
        }
        // return the Balls that result from the reflection
        
        ball.setVelocity(newVel);


    }

    /**SquareBumper cannot be a trigger for another Gadget, so this method should never be called. 
     * It asserts that the representation of the SquareBumper is valid.
     * @author nwallace
     */
    @Override
    public void respondToTrigger(double time) {
        checkRep();
    }
    
    /**
     * Returns String representation of a SquareBumper
     * @return "#"
     * @author asolei
     */
    @Override
    public String toString() {
        return "#";
    }


    /**
     * Getter for the name of the Bumper
     * @return name of the bumper
     * @author asolei
     */
    @Override
    public String getName() {
        return name;
    }


    /**
     * Getter for the xcoord of the top-left corner of the bounding box
     * @return double xcoord
     * @author asolei
     */
    @Override
    public double getX() {
        return this.topLeftX;
    }


    /**
     * Getter for the ycoord of the top-left corner of the bounding box
     * @return double ycoord
     * @author asolei
     */
    @Override
    public double getY() {
        return this.topLeftY;
    }

    /**
     * A SquareBumper cannot move. Assert the rep invariant
     * @param time
     * @author asolei
     */
    @Override
    public void move(double time) {
        checkRep();
    }
    
    /**
     * Get the list of Gadgets the bumper triggers
     * @return list of Gadgets bumper triggers
     * @author asolei
     */
    @Override
    public List<Gadget> getTriggers() {
        return this.triggerForAction;
    }

    /**
     * Do nothing
     * @author asolei
     */
    @Override
    public void setSelf() {}

    /**
     * Add some Gadgets to the list of Gadgets the bumper triggers
     * @param toAdd what we want to add
     * @author asolei
     */
    @Override
    public void addGadgets(List<Gadget> toAdd) {
        this.triggerForAction.addAll(toAdd);
    }

}
