package warmup;

import physics.*;
import physics.Geometry.DoublePair;
import java.lang.Math;

/**Thread safety argument: updatePosition() method will be synchronized so that collisions with invisible walls
 * allow for transfer of the ball between adjacent boards
 * @author nwallace
 *
 */

public class BallWarmup {
    private Circle ball;
    private final Vect velocity;
    private DoublePair position;
    private final double speed;
    
    public BallWarmup(double x, double y, Vect velocity){
        this.ball = new Circle(x, y, 0.25);
        this.speed = 0.5;
        this.velocity = velocity;
        this.position = new DoublePair(x, y);
    }
    
    public double getX(){
        return position.d1;
    }
    
    public double getY(){
        return position.d2;
    }
    
    public Vect getXdirection(){
        return velocity.X_HAT;
    }
    
    public Vect getYdirection(){
        return velocity.Y_HAT;
    }
    
    public Circle getBall() {
        return this.ball;
    }
    
    public Vect getVelocity() {
        return velocity;
    }
    
    public Vect getPosition() {
        return new Vect(position.d1, position.d2);
    }
    
    public BallWarmup updateBallPosition(){
        // updates the ball position after 1 frame of movement
        final double FRAME_RATE = 20.0;
        return new BallWarmup(position.d1 + (speed/FRAME_RATE)*velocity.x(), 
                position.d2 + (speed/FRAME_RATE)*velocity.y(), velocity); 
    }
    
    @Override
    public String toString(){
        return "*";
    }
    
    
    

}
