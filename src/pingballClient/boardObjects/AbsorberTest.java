package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import physics.Circle;
import physics.Geometry;
import physics.Geometry.DoublePair;
import physics.LineSegment;
import physics.Vect;

/**
 * Absorber testing strategy:
 *      equals
 *          absorbers that are equal
 *          --> test hashCode
 *              do not trigger anything
 *              trigger some things
 *          absorbers that are not equal
 *              different name
 *              different width/height
 *              different topLeft corner
 *              different self-triggering
 *              different things they trigger
 *      toString
 *          test a given absorber
 *      respondToTrigger
 *          emptyQueue of held balls
 *          hold ball, allow time to pass, release ball
 *          hold ball, do not allow enough time to pass, ball not released
 *      reflectBall
 *          selfTriggering
 *              release ball
 *          not selfTriggering
 *              single reflect, hold ball
 *              multiple reflects, hold all balls
 *      getTimeUntilCollision
 *          collide with side
 *          collide with corner
 *      test constructor
 *          parameters that satisfy rep invariant
 *          parameters that do not satisfy rep invariant
 * @author asolei        
 *             
 */
public class AbsorberTest {
       
    // test toString
    
    @Test
    public void testAbsorberToString(){
        Absorber absorber7 = new Absorber(5, 6, new DoublePair(5,5), "abs", false, new ArrayList<Gadget>());
        String absString = absorber7.toString();
        assertTrue(absString.equals("ABS 5 6"));
    }
    

    // test reflectBall
    
    @Test
    public void testReflectHoldBall(){
        Absorber absorber7 = new Absorber(5, 1, new DoublePair(0,5), "abs", false, new ArrayList<Gadget>());
        Ball ballToMove = new Ball("ball", 2, 2, new Vect(0,-1));
        absorber7.reflectBall(ballToMove,0.001);
        assertTrue(ballToMove.equals(new Ball("ball", 22, 0, new Vect(0,0))));  
   }   

    
//    @Test (expected=Exception.class)
//    public void testRespondToTriggerNotSelfHasBallNotRelease() throws Exception{
//        Absorber absorber7 = new Absorber(5, 1, new DoublePair(0,5), "abs", false);
//        try {
//            absorber7.reflectBall(new Ball("ball", 2, 2, new Vect(0,0)), 0.001);
//        } catch (Exception e){
//            for (int i=0; i < 5; i++){
//                absorber7.getTimeUntilCollision(new Ball("ball", 2, 2, new Vect(0,0)));
//            }
//            absorber7.respondToTrigger(new Ball("ball", 2, 2, new Vect(0,0)), 0.001);
//        }   
//    }
    
    // test reflectBall
    
    @Test 
    public void testReflectSelfImmediate() throws Exception{
        Absorber absorber7 = new Absorber(5, 5, new DoublePair(0,5), "abs", true, new ArrayList<Gadget>());
        Ball ballToReflect = new Ball("ball", 2, 2, new Vect(0,0));
        absorber7.reflectBall(ballToReflect,1);
        assertTrue(ballToReflect.equals(new Ball("ball", 4.74, 4.74, new Vect(0, -50))));
    }
    
//    @Test (expected=Exception.class)
//    public void testReflectNotSelf() throws Exception{
//        Absorber absorber7 = new Absorber(5, 1, new DoublePair(0,5), "abs", false);
//        absorber7.reflectBall(new Ball("ball", 2, 2, new Vect(0,0)), 0.001);
//    }
    
//    @Test (expected=Exception.class)
//    public void testReflectMultipleNotSelf() throws Exception{
//        Absorber absorber7 = new Absorber(5, 1, new DoublePair(0,5), "abs", false);
//        try {
//            absorber7.reflectBall(new Ball("ball", 2, 2, new Vect(0,0)), 0.001);
//        } catch (Exception e){
//            absorber7.reflectBall(new Ball("ball", 2, 2, new Vect(0,0)), 0.001);
//        }
//    }
    
    // test timeUntilCollision
    
    @Test
    public void testTimeUntilCollisionSide() {
        Absorber absorber7 = new Absorber(5, 1, new DoublePair(0,0), "abs", false, new ArrayList<Gadget>());
        Ball ball = new Ball("ball", 2.5, 1.3, new Vect(0,-10));
        double collisionTime = absorber7.getTimeUntilCollision(ball);
        assertTrue(collisionTime==Geometry.timeUntilWallCollision(new LineSegment(new Vect(0, 1), new Vect(5, 1)), 
                ball.getBall(), ball.getVelocity()));
    }
    
    @Test
    public void testTimeUntilCollisionCorner(){
        Absorber absorber7 = new Absorber(4, 1, new DoublePair(0,0), "abs", false, new ArrayList<Gadget>());
        Ball ball = new Ball("ball", 4, 1.3, new Vect(0,-10));
        double collisionTime = absorber7.getTimeUntilCollision(ball);
        assertTrue(collisionTime==Geometry.timeUntilCircleCollision(new Circle(4, 1, 0), 
                ball.getBall(), ball.getVelocity()));
    }
    
            
}
