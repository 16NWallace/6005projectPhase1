package pingballClient.boardObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import physics.*;

/** Represents the outer edges of a board that can either be solid (reflective) or invisible if the board 
 * is adjacent to another board on that edge.
 * 
 * Rep invariant:The length of the wall must be 20.0<length<20.1, since walls are placed just outside 
 * of the board region. The wall must be labelled as "T", "B", "L" or "R", for "top", "bottom", "left" or "right". 
 * All endpoint coordinate components of the wall should be equal to -0.01 or 20.01.
 * 
 * @author nwallace
 *
 */

public class OuterWall implements Gadget {
    
    private AtomicBoolean invisible;
    private final LineSegment wall;
    private final double REFLECT_COEFF;
    //Must be "T", "B", "L", "R" for top/bottom/left/right
    private final String name;
    
    
    /**Initializes a wall that will be just outside the grid of the pingball board. These walls can be connected
     * and made invisible when boards are merged.
     * 
     * @param startPoint Vect to represent the beginning of the wall
     * @param endPoint Vect to represent the end of the wall
     * @author nwallace
     */
    public OuterWall(Vect startPoint, Vect endPoint) {
        wall = new LineSegment(startPoint, endPoint);
        REFLECT_COEFF = 1.0;
        // construct an OuterWall based on the startPoint and endPoint vectors
        if ((int) startPoint.x()==0&&(int) endPoint.x()==20&&(int) startPoint.y()==0){
            name = "T";
        }
        else if ((int) startPoint.x()==0&&(int) endPoint.x()==0){
            name = "L";
        }
        else if ((int) startPoint.x()==20&&((int) endPoint.x()==20)){
            name = "R";
        }
        else if ((int) startPoint.x()==0&&(int) endPoint.x()==20&&(int) startPoint.y()==20){
            name = "B";
        } else {
            //should never get here --> checkRep
            name = "";
        }
        // the wall is initialized to solid
        invisible = new AtomicBoolean(false);
        checkRep();
    }
    
    /**
     * Get the time until a ball will collide with a wall.
     * @param ball that we want to check how far away is from the wall
     * @return time the ball is from the wall
     * @author nwallace
     */
    @Override
    public double getTimeUntilCollision(Ball ball) {
        return Geometry.timeUntilWallCollision(this.wall, ball.getBall(), ball.getVelocity());
    }
    
    /**Updates the velocity vector of the ball when it collides with the wall.
     * 
     * @param ball Ball object that will be reflected off of the wall if it collides with the wall while it is solid.
     * If the wall is invisible, it returns a ball with the same velocity vector.
     * @return Ball object with updated velocity vector after collision.
     * @author nwallace
     */
    @Override
    public void reflectBall(Ball ball, double time) {
        if (invisible.get()){ 
            //Return the coordinates of what the ball would be as it enters the edge of the new board
            switch(name){
                case "T": 
                    ball.setCoord(ball.getX(), 19);
                    break;
                case "B":
                    ball.setCoord(ball.getX(), 1);
                    break;
                case "L":
                    ball.setCoord(19, ball.getY());
                    break;
                case "R":
                    ball.setCoord(1,  ball.getY()); 
                    break;
                // should never get here by rep invariant
                default:
                    break;

            }
        } else {
            //get the new velocity from the reflection
            Vect newVel = Geometry.reflectWall(wall, ball.getVelocity(), REFLECT_COEFF);
            //create a new ball with new velocity
            ball.setVelocity(newVel);

        }
    }
    
    
    
    /**Helper method that will be called when the server is merging or separating two boards. 
     * It changes the walls of the boards to be opposite of what they were before: invisible becomes solid,
     * solid becomes invisible.
     * @param invisible what the wall state is changing to upon merging/separating: 
     *      True if the wall is invisible
     *      False if the wall is visible
     * @author nwallace
     */
    public void changeInvisible(boolean invisible){
        this.invisible = new AtomicBoolean(invisible);
    }

    /**OuterWall cannot be a trigger or action for another Gadget, so this method should never be called. 
     * It asserts that the representation of the OuterWall is valid. 
     * @param ball Ball 
     * @param time time interval
     * @return ball return the ball back
     * @author nwallace
     */
    @Override
    public void respondToTrigger(double time){
        checkRep();
    }
    
    /**
     * See if the wall is invisible or not
     * @return true if wall is invisible
     *          false otherwise
     * @author nwallace
     */
    public boolean isInvisible() {
        return this.invisible.get();
    }
    
    /**Rep invariant: asserts that is an instance of LineSegment so that the
     * reflect methods in the physics package can be called on it.The length of the wall must be
     * 20.0<length<20.1, since walls are placed just outside of the board region. The wall must be
     * labelled as "T", "B", "L" or "R". All endpoint coordinate components
     * of the wall should be equal to -0.01 or 20.01
     * @author nwallace
     */
    public void checkRep(){
        boolean correctName = (name.equals("T")||name.equals("B")||name.equals("L")||name.equals("R"));
        List<Vect> coords = new ArrayList<Vect>(Arrays.asList(new Vect(-0.01,-0.01), new Vect(-0.01, 20.01), new Vect(20.01, -0.01), new Vect(20.01, 20.01)));
        boolean validEndpoints = coords.contains(wall.p1())&&coords.contains(wall.p2()); 
        assert(correctName&&validEndpoints);
    }
    
    ///FOR TESTING ONLY///
    /**
     * Get the line segment corresponding the wall
     * @return line segment
     * @author asolei
     */
    public LineSegment getWall() {
        return this.wall;
    }
    
    /**
     * Return the name of the OuterWall
     * @return String name of wall
     * @author asolei
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * String 'Message' representation of a wall
     * @return String message representation of a wall
     * @author sdrammis     
     */
    @Override
    public String toString() {
        String output = "";
        for (int i=0; i<20; i++){
            if (name.equals("L")||name.equals("R")){
                output+=".\n";
            } else if (name.equals("T")||name.equals("B")){
                output+=". ";
            }
        }
        return output;
    }
    
    /**
     * Returns 0
     * @author asolei
     */
    @Override
    public double getX() {
        return 0;
    }
    
    /**
     * Returns 0 
     * @author asolei
     */
    @Override
    public double getY() {
        return 0;
    }
    
    /**
     * An OuterWall cannot move. Assert the rep invariant
     * @author asolei
     */
    @Override
    public void move(double time) {
        checkRep();
    }
    
    /**
     * This method will never be called on an OuterWall. Do nothing
     * @author asolei
     */
    @Override
    public List<Gadget> getTriggers() {
        return null;
    }
    
    /**
     * Do nothing. 
     * @author asolei
     */
    @Override
    public void setSelf() {}
    
    /**
     * This method will never be called on an OuterWall. Do nothing.
     * @author asolei
     */
    @Override
    public void addGadgets(List<Gadget> toAdd) {}


}
