package pingballClient.boardObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import physics.*;
import physics.Geometry.DoublePair;

/**Rep invariant: all side lengths are equal to 1 or math.sqrt(2).
 * The only objects that are valid for triggerForAction are Absorber and Flipper.
 * Orientation must be an int {0,90,180,270}
 * 0<=(topLeftX and topLeftY)<=19 so that the entire triangle is contained within the board.
 * 
 * Thread safety argument: all triangles are kept in a board, and each board is only touched by one thread, so this
 * datatype is threadsafe by confinement and encapsulation.
 * 
 * @author nwallace
 */
public class TriangleBumper implements Gadget {
    
    private final double topLeftX;
    private final double topLeftY;
    
    private final String name;
    private final double REFLECT_COEFF;
    
    private final List<Circle> corners;
    private final List<Vect> coords;
    
    private final List<Gadget> triggerForAction;
    private final int orientation;
    
    private final List<LineSegment> sides;

    
    /**Initializes a right isosceles triangular bumper from a corner and a specified orientation.
     * Location of right-angle corner per orientation:
     * 0:=NW
     * 90:=NE
     * 180:SE
     * 270:=SW
     * 
     * @param corner DoublePair that represents x,y-coordinate of the right-angle corner of the triangle bumper
     * @param name String name for the triangle bumper
     * @param orientation integer that must be in {0, 90, 180, 270}
     * @param action List of Gadgets whose actions this object will trigger when it is triggered
     * @author nwallace, asolei
     *  Implemented by: nwallace
     */
    public TriangleBumper(String name, DoublePair corner, int orientation, List<Gadget> action){
        this.topLeftX = corner.d1;
        this.topLeftY = corner.d2;
        this.name = name;
        this.corners = Collections.synchronizedList(new ArrayList<Circle>());
        this.sides = Collections.synchronizedList(new ArrayList<LineSegment>());

        this.orientation = orientation;
        
        REFLECT_COEFF = 1.0;
        
        // Initialize appropriate representation
        switch (orientation){
            case 0:
                int[][] cornerConstruct0 = {{0,0},{1,0},{0,1}};
                for (int[] newCorner: cornerConstruct0){
                    corners.add(new Circle(topLeftX+newCorner[0], topLeftY+newCorner[1], 0));
                }
                sides.add(new LineSegment(topLeftX, topLeftY, topLeftX+1, topLeftY));
                sides.add(new LineSegment(topLeftX+1, topLeftY, topLeftX, topLeftY+1));
                sides.add(new LineSegment(topLeftX, topLeftY+1, topLeftX, topLeftY));
                break;
            case 90:
                int[][] cornerConstruct90 = {{0,0},{1,0},{1,1}};
                for (int[] newCorner: cornerConstruct90){
                    corners.add(new Circle(topLeftX+newCorner[0], topLeftY+newCorner[1], 0));
                }
                sides.add(new LineSegment(topLeftX, topLeftY, topLeftX+1, topLeftY));
                sides.add(new LineSegment(topLeftX+1, topLeftY, topLeftX+1, topLeftY+1));
                sides.add(new LineSegment(topLeftX+1, topLeftY+1, topLeftX, topLeftY));
                break;
            case 180:
                int[][] cornerConstruct180 = {{1,0},{1,1},{0,1}};
                for (int[] newCorner: cornerConstruct180){
                    corners.add(new Circle(corner.d1+newCorner[0], corner.d2+newCorner[1], 0));
                }
                sides.add(new LineSegment(topLeftX+1, topLeftY, topLeftX+1, topLeftY+1));
                sides.add(new LineSegment(topLeftX+1, topLeftY+1, topLeftX, topLeftY+1));
                sides.add(new LineSegment(topLeftX, topLeftY+1, topLeftX+1, topLeftY));
                break;
            case 270:
                int[][] cornerConstruct270 = {{0,0},{1,1},{0,1}};
                for (int[] newCorner: cornerConstruct270){
                    corners.add(new Circle(corner.d1+newCorner[0], corner.d2+newCorner[1], 0));
                }
                sides.add(new LineSegment(topLeftX, topLeftY, topLeftX+1, topLeftY+1));
                sides.add(new LineSegment(topLeftX+1, topLeftY+1, topLeftX, topLeftY+1));
                sides.add(new LineSegment(topLeftX, topLeftY+1, topLeftX, topLeftY));
                break;
            // should never get here by File parsing and rep invariant
            default: 
                break;
        }
            
        coords = new ArrayList<Vect>();
        for (Circle cornerCircle: corners){
            coords.add(cornerCircle.getCenter());
        }
        
        // if the bumper is not linked to anything, prevent a null pointer exception by initializing triggerForAction
        //      to an empty array
        this.triggerForAction = action;
        
        checkRep();
    }
    
    /** Find the minimum time in which the ball will collide with any part of the bumper(edge or corner), and returns that
     * time. This time can be mapped to the appropriate point of contact with the bumper in the reflectBall() method.
     * @param Ball ball ball for which the soonest collision with the flipper is occurring
     * @return double time until the ball collides with the flipper
     * 
     * Implemented by: sdrammis, nwallace
     */
    @Override
    public double getTimeUntilCollision(Ball ball) {
        double minTime = Double.POSITIVE_INFINITY;
        // check the sides of the triangle. if time for a particular side is less than minTime, reset minTime
        for (LineSegment side : sides) {
            double timeUntilCollision = Geometry.timeUntilWallCollision(side, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision <= minTime) {
                minTime = timeUntilCollision;
            }
        }
        
        // check the corners. if time for a particular corner is less than minTime, reset minTime
        for (Circle corner : corners) {
            double timeUntilCollision = Geometry.timeUntilCircleCollision(corner, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision <= minTime) {
                minTime = timeUntilCollision;
            }
        }
        
        return minTime;
    }
    
    /**Updates the velocity vector of the ball when it collides with a triangle bumper, either on the side
     * or corner of the triangle.
     * @param ball pingball.Ball ball that will be checked for collision and updated if it collides
     * @return pingball.Ball with updated velocity vector after colliding
     * @throws Exception 
     * 
     * @author nwallace
     * Implemented by: nwallace, sdrammis
     */

    @Override

    public void reflectBall(Ball ball, double time)  {
        
        double minCollisionTime = getTimeUntilCollision(ball);
        double minTimeSide = Double.POSITIVE_INFINITY;
        double minTimeCorner = Double.POSITIVE_INFINITY;
        
        LineSegment wallForCollision = null;
        Circle cornerForCollision = null;
        
        // determine if the ball will collide with any of the sides
        for (LineSegment side : sides) {
            double timeUntilCollision = Geometry.timeUntilWallCollision(side, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision <= minTimeSide) {
                minTimeSide = timeUntilCollision;
                wallForCollision = side;
            }
        }
        
        // determine if the ball will collide with any of the corners
        for (Circle corner: corners) {
            double timeUntilCollision = Geometry.timeUntilCircleCollision(corner, ball.getBall(), ball.getVelocity());
            if (timeUntilCollision <= minCollisionTime) {
                minTimeCorner = timeUntilCollision;
                cornerForCollision = corner;
            }
        }
        
        // make sure the triggered Gadgets respond
        for (Gadget action: triggerForAction){

            action.respondToTrigger(minCollisionTime);
         }

        
        Vect newVel;

        // if it will collide with a side before a corner, reflect off the side
        if (minTimeSide<=minTimeCorner) {
            newVel = Geometry.reflectWall(wallForCollision, ball.getVelocity(), REFLECT_COEFF);
        } 
        // otherwise reflect off the corner
        else {
            newVel = Geometry.reflectCircle(cornerForCollision.getCenter(), ball.getPosition(), ball.getVelocity(), REFLECT_COEFF);
        }

        ball.setVelocity(newVel);

    }


    /**TriangleBumper cannot be a trigger for another Gadget, so this method should never be called. 
     * It asserts that the representation of the TriangleBumper is valid.
     * 
     * @author nwallace
     * Implemented by: nwallace
     */
    @Override
    public void respondToTrigger(double time){    
        checkRep(); 
    }
    
    /**
     * Generate a String representation of a TriangleBumper based on its orientation
     * @return "\" if ori is 90 or 270
     *          "/" if ori is 0 or 180
     *          
     * @author asolei
     * Implemented by: nwallace
     */
    @Override
    public String toString(){
        checkRep();
        if (orientation==0||orientation==180){
            return "/";
        } 
        if (orientation==90||orientation==270){
            return "\\";
        }
        //should never get here
        return "";
        
    }
    
    /**
     * Getter for the name of this TriangleBumper
     * @return String name
     * @author asolei
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Getter for the value of the xcoord of the topLeftCorner of the bounding box
     * @return xcoord
     * @author asolei
     */
    @Override
    public double getX() {
        return this.topLeftX;
    }
    
    /**
     * Getter for the value of the ycoord of the topLeftCorner of the bounding box
     * @return ycoord
     * @author asolei
     */
    @Override
    public double getY() {
        return this.topLeftY;
    }
    
    /**
     * Getter for the value of the orientation of the TriangleBumper
     * @return orientation of the TriangleBumper
     * @author asolei
     */
    public int getOri(){
        return new Integer(this.orientation);
    }
    
    /**
     * Return a List of the sides of the TriangleBumper
     * @return list of the sides of the bumper
     * @author asolei
     */
    public List<LineSegment> getSides(){
        return this.sides;
    }
    
    /**
     * A TriangleBumper cannot move. Assert the rep invariant
     * @param time
     * @author asolei
     */
    @Override
    public void move(double time) {
        checkRep();
    }
    
    /**Rep invariant: all side lengths are equal to 1 or math.sqrt(2).
     * The only objects that are valid for triggerForAction are Absorber and Flipper.
     * Orientation must be an int {0,90,180,270}
     * 0<=(topLeftX and topLeftY)<=19
     * 
     * @author nwallace
     * Implemented by: nwallace
     */
    public void checkRep(){
        boolean validOrientation = (orientation==0)||(orientation==90)||(orientation==180)||(orientation==270);
        boolean correctSideLength=true;
        boolean validCoords = true;
        boolean validCorners=true;
        boolean topLeftInBoard = (0<=topLeftX)&&(0<=topLeftY)&&(topLeftX<=19)&&(topLeftY<=19);

        if(validOrientation){
            // check the sides 
            for (LineSegment side: sides){
                if(!(coords.contains(side.p1())&&coords.contains(side.p2()))){
                    validCoords=false;
                    break;
                } 
                else if(!(side.length()==1||side.length()==Math.sqrt(2))){
                    correctSideLength = false;
                    break;
                }
            }
            
            // check the corners
            for (Circle corner: corners){
                if (!(coords.contains(corner.getCenter()))){
                   validCorners = false;
                   break;
                }
            }
            
            

        }
        assert(correctSideLength&&validCoords&&validOrientation&&topLeftInBoard&&validCorners);
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
    public void setSelf() { }
    
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
