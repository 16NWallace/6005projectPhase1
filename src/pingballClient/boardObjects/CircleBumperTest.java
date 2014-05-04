package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import physics.Geometry.DoublePair;
import physics.*;

/**
 * CircleBumper testing strategy:
 *      constructor
 *          fail checkRep for outside board
 *      getTimeUntilCollision
 *          never collide
 *          collide at an edge
 *          collide from left
 *          collide from right
 *          collide from above
 *          collide from below
 *      reflectBall
 *          no reflection
 *          reflect in opposite direction
 *          reflect 90 degrees
 *      toString
 * @author asolei, nwallace
 */
public class CircleBumperTest {
    double SAMPLE_TIME = .005;
    CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    Ball ball1 = new Ball("ball1",10,10, new Vect(-1,0));
    Ball ball2 = new Ball("ball2",5.5,5.5, new Vect(-1,0));
    Ball ball3 = new Ball("ball3",6,5, new Vect(-1,0));
    Ball ball4 = new Ball("ball4",4,5, new Vect(1,0));
    Ball ball5 = new Ball("ball5",5,4, new Vect(0,1));
    Ball ball6 = new Ball("ball6",5,6, new Vect(0,-1));
    
    

//---------------CONSTRUCTOR TESTS in NoDiditTests----------------//



//-------getTimeUntilCollision() TESTS------------//
    @Test
    public void neverCollides(){
        assertTrue(Double.POSITIVE_INFINITY==circle1.getTimeUntilCollision(ball1));
    }
    
    @Test
    public void collidesWhenTouchingEdge(){
        double result = circle1.getTimeUntilCollision(ball2);
        double expected = Geometry.timeUntilCircleCollision(circle1.getBumper(), ball2.getBall(), ball2.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromLeft(){
        double result = circle1.getTimeUntilCollision(ball3);
        double expected = Geometry.timeUntilCircleCollision(circle1.getBumper(), ball3.getBall(), ball3.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromRight(){
        double result = circle1.getTimeUntilCollision(ball4);
        double expected = Geometry.timeUntilCircleCollision(circle1.getBumper(), ball4.getBall(), ball4.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromAbove(){
        double result = circle1.getTimeUntilCollision(ball5);
        double expected = Geometry.timeUntilCircleCollision(circle1.getBumper(), ball5.getBall(), ball5.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromBelow(){
        double result = circle1.getTimeUntilCollision(ball6);
        double expected = Geometry.timeUntilCircleCollision(circle1.getBumper(), ball6.getBall(), ball6.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }

//---------reflectBall() TESTS---------//
    @Test
    public void reflectBallNoReflection(){

        Ball ball1 = new Ball("ball1",6,5.75, new Vect(-1,0));
        circle1.reflectBall(ball1, SAMPLE_TIME);
        Vect result = ball1.getVelocity();
        Vect expected = Geometry.reflectCircle(circle1.getCenter(), ball1.getPosition(), new Vect(-1,0));
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);

    }


    @Test
    public void reflectBallOppositeDirection(){

        CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(0,0),new ArrayList<Gadget>());
        Ball ball2 = new Ball("ball2",2,0.5, new Vect(-1,0));
        circle1.reflectBall(ball2, SAMPLE_TIME);
        Vect result = ball2.getVelocity();
        Vect expected = Geometry.reflectCircle(circle1.getCenter(), ball2.getPosition(), new Vect(-1,0));
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);

    }
    
    @Test
    public void reflectBall90Degrees(){
        Ball ball1 = new Ball("ball1",10,10, new Vect(-1,0));
        circle1.reflectBall(ball1, SAMPLE_TIME);
        Vect result = ball1.getVelocity();
        Vect expected = Geometry.reflectCircle(circle1.getCenter(), ball1.getPosition(), new Vect(-1,0));
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
    }
    
//-------toString()---------//
    @Test
    public void circleBumpertoString(){
        assertEquals(circle1.toString(), "0");
        }
}
