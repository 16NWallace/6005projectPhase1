package pingballClient.boardObjects;

import physics.*;
import physics.Geometry.*;


/**
 * ADT for a pingball Ball. 
 * @author asolei
 * 
 * Thread Safety: a ball instance is always contained within a board. Each board is only manipulated by one
 * thread, so although the ball is mutable, it is confined and therefore thread-safe. The mutability is protected
 * because all of the getter methods that access the representation return copies.
 * 
 * Rep invariant:
 *      name, ball, velocity, position, speed, DELTA all not null
 *      radius is 0.25L
 * @author nwallace
 */

public class Ball {

    private final String name;
    private Circle ball;
    private Vect velocity;
    private DoublePair position;
    private double speed;
    //DELTA = frame update rate
    private final double DELTA = .001;
    private final double mass = 1.0;
    //flags whether or not the ball is stored in an absorber gadget
    private boolean inAbsorber;
    
    /**
     * Construct a new ball with radius 0.25 centered at x, y, with specified velocity. 
     * @param x coordinate of the center of the ball. 
     * @param y coordinate of the center of the ball. 
     * @param velocity direction the ball is moving. Specifies speed of the ball.  
     * @author asolei
     */

    public Ball(String name, double x, double y, Vect velocity){
        this.name = name;
        this.ball = new Circle(x, y, 0.25);
        // calculate the speed be getting the magnitude of the vector
        this.speed = Math.sqrt(Math.pow(velocity.x(), 2) + Math.pow(velocity.y(), 2));
        this.velocity = velocity;
        this.position = new DoublePair(x, y);
        this.inAbsorber = false;
    }
    
    /**
     * Set the velocity of the ball to the specified Vector
     * @param vector velocity
     * @author asolei
     * Implemented by: asolei
     */
    public void setVelocity(Vect velocity){
        this.velocity = velocity;
        this.speed = Math.sqrt(Math.pow(velocity.x(), 2) + Math.pow(velocity.y(), 2));
    }
    
    /**
     * Set the position of the ball to the specified coordinates
     * @param x newX coord
     * @param y newY coord
     * @author asolei
     * Implemented by: asolei
     */
    public void setCoord(double x, double y){
        this.position = new DoublePair(x, y);
        this.ball = new Circle(x, y, 0.25);
    }
    
    /**
     * Set whether or not the Ball is in the absorber
     * @param what to set it to
     */
    public void setInAbsorber(boolean set){
        this.inAbsorber = set;
    }
    
    /**
     * Get inAbsorber
     * @return inAbsorber
     */
    public boolean getInAbsorber(){
        return this.inAbsorber;
    }
    
    /**
     * Get the name of the ball. 
     * Returns a new copy.
     * @return name 
     * @author asolei
     */
    public String getName(){
        return new String(name);
    }
    
    /**
     * Get the x coordinate of the center of the ball. 
     * Returns a new copy.
     * @return x coordinate
     * @author asolei
     */
    public double getX(){
        return new Double(position.d1);
    }
    
    /**
     * Get the y coordinate of the center of the ball. 
     * Returns a new copy.
     * @return y coordinate
     * @author asolei
     */
    public double getY(){
        return new Double(position.d2);
    }
    
    /**
     * Get the x direction of the velocity vector of the ball. 
     * @return x direction of velocity 
     * @author asolei
     */
    public Vect getXdirection(){
        return velocity.X_HAT;
    }
    
    /**
     * Get the y direction of the velocity vector of the ball. 
     * @return y direction of velocity
     * @author asolei
     */
    public Vect getYdirection(){
        return velocity.Y_HAT;
    }
    
    /**
     * Get the Circle object associated with a ball. 
     * @return Circle copy representing the ball
     * @author asolei
     */
    public Circle getBall() {
        return new Circle(this.ball.getCenter(), this.ball.getRadius());
    }
    
    /**
     * Get the velocity vector of the ball. 
     * Returns a new copy.
     * @return direction the ball is moving in 
     * @author asolei
     */
    public Vect getVelocity() {
        return new Vect(velocity.x(), velocity.y());
    }
    
    /**
     * Get the speed of the ball. 
     * @return speed of ball
     * @author asolei
     */
    public double getSpeed(){
        return new Double(this.speed);
    }
    
    /**
     * Get the position of the ball as a vector. 
     * Returns a new Vect
     * @return position vector of the ball
     * @author asolei
     */
    public Vect getPosition() {
        return new Vect(position.d1, position.d2);
    }

    /**
     * Get the mass of the ball
     * @return the mass
     * @author asolei
     */
    public double getMass() {
        return new Double(this.mass);
    }
    
    /**
     * Check the time until this ball will collide with another ball
     * @return the time until collision
     * @author sdrammis
     */
    public double getTimeUntilCollision(Ball ball) {
        return Geometry.timeUntilBallBallCollision(this.getBall(), this.getVelocity(), ball.getBall(), ball.getVelocity());
    }
    
    /**
     * Get this ball position from reflecting off of the ball param
     * @param ball that we are reflecting off of
     * @return a new ball with new velocity from reflecting off pall param
     * @author sdrammis
     */
    public void reflect(Ball otherBall) {
        VectPair newVects = Geometry.reflectBalls(this.getPosition(), this.getMass(), this.getVelocity(),
                                                    otherBall.getPosition(), otherBall.getMass(), otherBall.getVelocity());
        this.setVelocity(newVects.v1);
        otherBall.setVelocity(newVects.v2);
    }
    
    /**
     * Adds the velocity vectors of this and some other ball
     * @param ball other ball
     * @return vector sum of velocity vectors of this and the other ball
     * @author asolei
     */
    public Vect add(Ball ball){
        Vect thisVel = this.getVelocity();
        Vect otherVel = ball.getVelocity();
        return thisVel.plus(otherVel);
        
    }
    
    /**
     * Determine if a given object is observationally equal to this. The condition for observational
     *   equality is: the other object must be a Ball, must have the same name, x and y coordinates,
     *   the Circle objects corresponding to the ball must be equal, the same mass, the same velocity vector,
     *   and the same speed. 
     * @param obj 
     * @return whether or not observationally equal
     * @author asolei, sdrammis
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ball) {
            Ball that = (Ball) obj;
            return this.getName().equals(that.getName()) &&
                   this.getX() == that.getX() &&
                   this.getY() == that.getY() &&
                   this.getMass() == that.getMass() &&
                   this.getBall().equals(that.getBall()) &&
                   this.getVelocity().equals(that.getVelocity()) &&
                   this.getSpeed() == that.getSpeed();
        }
        return false;
        
    }
    
    /**
     * Compute hashCode for this, generated by considering the name of the Ball
     * @return int hashCode
     * @author asolei
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
        
    }
    
    /**
     * Return String that represents a Ball as specified in the project prompt
     * @return *
     * @author asolei
     */
    @Override
    public String toString(){
        return "*";
    }
    
    /**Rep invariant:
     *      radius is 0.25L, represented by a Circle
     *      name, ball, velocity, position, speed, DELTA all not null
     * 
     * @author nwallace
     */
    protected void checkRep(){
        boolean validRadius = (this.ball.getRadius()==0.25);
        assert(validRadius);
    }
}
