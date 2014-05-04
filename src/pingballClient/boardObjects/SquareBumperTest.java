package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;

import physics.*;
import physics.Geometry.DoublePair;

import org.junit.Test;

import physics.Geometry;

/*
 * SquareBumper testing strategy:
 *      constructor
 *          fail checkRep for outside board
 *      toString
 *      getTimeUntilCollision
 *          never collide
 *          collide at an edge
 *          collide from left
 *          collide from right
 *          collide from above
 *          collide from below
 *          collide with corner
 *      reflectBall
 *          no reflection
 *          reflect in opposite direction
 *          reflect 45 degrees off of edge
 *      
 * @author asolei, nwallace
 */

public class SquareBumperTest {
    double SAMPLE_TIME = .005;
    CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    Ball ball1 = new Ball("ball1",10,10, new Vect(-1,0));
    Ball ball2 = new Ball("ball2",5.75,5, new Vect(-1,0));
    Ball ball3 = new Ball("ball3",6,5, new Vect(-1,0));
    Ball ball4 = new Ball("ball4",4,5, new Vect(1,0));
    Ball ball5 = new Ball("ball5",5,4, new Vect(0,1));
    Ball ball6 = new Ball("ball6",4.5,4, new Vect(0,1));

    
    //------CONSTRUCTOR TESTS in NoDiditTests----------//

    
    //-------toString()------------------//
    @Test
    public void squareToString(){
        String result = square1.toString();
        String expected = "#";
        assertEquals(result, expected);
    }
    
    //-----COLLISION TESTS--------------//
    @Test
    public void neverCollides(){
        Ball ball = new Ball("ball", 2,0, new Vect(0,1));
        assertTrue(square1.getTimeUntilCollision(ball)==Double.POSITIVE_INFINITY);     
    }
    
    @Test
    public void collidesWhenTouchingEdge(){
        double result = square1.getTimeUntilCollision(ball2);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(5.5, 4.5, 5.5, 5.5), ball2.getBall(), ball2.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromLeft(){
        double result = square1.getTimeUntilCollision(ball3);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(5.5, 4.5, 5.5, 5.5), ball3.getBall(), ball3.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromRight(){
        double result = square1.getTimeUntilCollision(ball4);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(4.5, 4.5, 4.5, 5.5), ball4.getBall(), ball4.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromAbove(){
        double result = square1.getTimeUntilCollision(ball5);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(4.5, 4.5, 5.5, 4.5), ball5.getBall(), ball5.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesWithCorner(){
        double result = square1.getTimeUntilCollision(ball6);
        double expected = Geometry.timeUntilCircleCollision(new Circle(4.5,4.5,0), ball6.getBall(), ball6.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
        
    }
 
    //------reflectBall() TESTS----------//
    @Test(expected = NullPointerException.class)
    public void reflectBallNoReflection(){
        //no reflection, so time until collision is infinity, 
        Ball ball1 = new Ball("ball1",10,10, new Vect(-1,0));
        square1.reflectBall(ball1, SAMPLE_TIME);
        Vect result = ball1.getVelocity();
        Vect expected = Geometry.reflectWall(new LineSegment(4.5, 4.5, 5.5, 5.5), new Vect(1,0));
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);

    }
 
    @Test
    public void reflectBallOppositeDirection(){
        Vect startVel = ball2.getVelocity();
        square1.reflectBall(ball2, SAMPLE_TIME);
        Vect result = ball2.getVelocity();
        Vect expected = Geometry.reflectWall(new LineSegment(5.5, 4.5, 5.5, 5.5), startVel);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
    }
    
    @Test
    public void reflectBall45Degrees(){
        Ball ball1 = new Ball("ball1", 5, 4.5, new Vect(1,1));
        square1.reflectBall(ball1, SAMPLE_TIME);
        Vect result = ball1.getVelocity();
        Vect expected = Geometry.reflectWall(new LineSegment(5.5, 4.5, 5.5, 5.5), new Vect(1,1));
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
        assertEquals(new Double(result.x()),new Double(expected.x()), .001);
    }
    

}
