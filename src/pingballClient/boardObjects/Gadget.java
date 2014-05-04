package pingballClient.boardObjects;

import java.util.ArrayList;
import java.util.List;

import pingballClient.Board;

/**The classes that implement the Gadget interface represent all special elements in the board. A Gadget
 * object interacts with the board when the Board.update() method is called. The reflectBall() method for
 * each Gadget will be called while iterating through the list of balls in the board to handle the collision.
 * Gadgets can also be linked such that when one Gadget is triggered, it can cause the action of another object,
 * and this connection is handled in the respondToTrigger() method of each Gadget, which performs the appropriate 
 * action.
 * 
 * Thread safety argument:
 * All gadget-implementing objects are confined to one board. Each board is managed by a single thread, so all gadgets
 * are thread-safe by confinement.
 * 
 * @author nwallace
 *
 */

public interface Gadget {
    
    /**Returns the minimum collision time between a ball and a gadget. 
     * 
     * @param ball 
     * @return double minimum collision time between the ball and gadget
     * @author nwallace
     */
    public double getTimeUntilCollision(Ball ball);
    
    
    /**Returns a Ball with a new velocity vector after it collides with a gadget
     * 
     * @param Ball ball that is being checked for collision with item
     * @return Ball with updated velocity vector
     * @throws Exception 
     * @author nwallace
     */
    public void reflectBall(Ball ball, double time);
    
    /**If two gadgets are linked such that when one is triggered, it causes the action of another trigger,
     * this method is called by the trigger gadget to cause the response in its action counterpart.
     * @param ball that remotely or self-triggers the event. 
     * @return 
     * @throws Exception 
     * @author nwallace
     */
    public void respondToTrigger(double time);
    
    /**
     * Move a Gadget, if it can move, for the specified time interval. 
     * @param time for which we want the Gadget to move. 
     * @author asolei
     */
    public void move(double time);
    
    /** Return the name of the Gadget. 
     * @return String name
     * @author nwallace
     */
    public String getName();
    
    /**
     * Get the x position of the gadget from the upper left corner
     * @return x coordinate
     * @author nwallace
     */
    public double getX();
    
    /**
     * Get the y position of the gadget from the upper left corner
     * @return y coordinate
     * @author nwallace
     */
    public double getY();
    
    /**
     * Return Gadgets that this triggers. 
     * @return
     */
    public List<Gadget> getTriggers();
    
    /**
     * Set a Gadget to be self-triggering.
     * 
     */
    public void setSelf();
    
    /**
     * Set the action-gadgets of a Gadget
     * @param list of gadgets that we want to make this trigger
     */
    public void addGadgets(List<Gadget> toAdd);
    
    /**
     * Return the String representation of a Gadget. 
     * @return String representation
     * @author nwallace
     */
    @Override
    public String toString();
    
    /**
     * Check the representation invariant of a Gadget.
     * @author asolei
     */
    public void checkRep();


}
