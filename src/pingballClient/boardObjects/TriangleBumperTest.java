package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import physics.Geometry;
import physics.Geometry.DoublePair;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

/*
 * TriangleBumper testing strategy:
 *      constructor
 *          fail for unaccepted orientation
 *          fail checkRep for outside board
 *      toString
 *      getTimeUntilCollision
 *          never collide
 *          collide from left - right angle edge
 *          collide from right - right angle edge
 *          collide from above - right angle edge
 *          collide from below - right angle edge
 *          collide with corner
 *          collide with hypotenuse
 *      reflectBall
 *          no reflection
 *          reflect in opposite direction
 *          reflect 90 degrees from hypotenuse
 *      
 * @author nwallace
 */

public class TriangleBumperTest {
    
    CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    
    TriangleBumper tri0 = new TriangleBumper("tri0", new DoublePair(0,0), 0, new ArrayList<Gadget>());
    TriangleBumper tri90 = new TriangleBumper("tri90", new DoublePair(0,0), 90, new ArrayList<Gadget>());
    TriangleBumper tri180 = new TriangleBumper("tri180", new DoublePair(0,0), 180, new ArrayList<Gadget>());
    TriangleBumper tri270 = new TriangleBumper("tri270", new DoublePair(0,0), 270, new ArrayList<Gadget>());
    
    Ball ball1 = new Ball("ball1", 2,2, new Vect(1,0));
    Ball ball2 = new Ball("ball2", 0.5, 1.26, new Vect(0,-1));
    
    double SAMPLE_TIME = .005;
    
    //------CONSTRUCTOR TESTS in NoDiditTests------//
    
 
    //-----toString() TESTS-------//

    @Test
    public void triangleBumperToString() {
        assertEquals(tri0.toString(), "/");
        assertEquals(tri90.toString(), "\\");
        assertEquals(tri180.toString(), "/");
        assertEquals(tri270.toString(), "\\");
        
    }

    
    //-------getTimeUntilCollision TESTS----//
    @Test
    public void neverCollides(){
        assertTrue(tri0.getTimeUntilCollision(ball1)==Double.POSITIVE_INFINITY);     
    }

    @Test
    public void collidesFromLeft(){
        Ball ball3 = new Ball("ball3", 0.5, 1.5, new Vect(1,0));
        TriangleBumper otherTri0 = new TriangleBumper("tri0other", new DoublePair(1,1), 0, new ArrayList<Gadget>());
                
        double result = otherTri0.getTimeUntilCollision(ball3);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(1, 1, 1, 2), ball3.getBall(), ball3.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromRight(){
        Ball ball4 = new Ball("ball3", 1.5, 0.5, new Vect(-1,0));
        TriangleBumper otherTri90 = new TriangleBumper("tri90other", new DoublePair(0,0), 90, new ArrayList<Gadget>());
        double result = otherTri90.getTimeUntilCollision(ball4);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(1, 0, 1, 1), ball4.getBall(), ball4.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromAbove(){
        Ball ball5 = new Ball("ball5", 1.5, 0.5, new Vect(0,1));
        TriangleBumper otherTri0 = new TriangleBumper("tri0other", new DoublePair(1,1), 0, new ArrayList<Gadget>());
        double result = otherTri0.getTimeUntilCollision(ball5);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(1, 1, 2, 1), ball5.getBall(), ball5.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesFromBelow(){
        Ball ball6 = new Ball("ball6", 0.5, 1.5, new Vect(0,-1));
        TriangleBumper otherTri180 = new TriangleBumper("tri180other", new DoublePair(0,0), 180, new ArrayList<Gadget>());
        double result = otherTri180.getTimeUntilCollision(ball6);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(0,1, 1, 1), ball6.getBall(), ball6.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);
    }
    
    @Test
    public void collidesWithCorner(){
        Ball ball7 = new Ball("ball7", 0, 1.5, new Vect(0,-1));
        TriangleBumper otherTri180 = new TriangleBumper("tri180other", new DoublePair(0,0), 180, new ArrayList<Gadget>());
        double result = otherTri180.getTimeUntilCollision(ball7);
        double expected = Geometry.timeUntilCircleCollision(new Circle(0,1,0), ball7.getBall(), ball7.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected);  
    }
    
    @Test
    public void collidesWithHypotenuse(){
        Ball ball8 = new Ball("ball8", 0.5, 0, new Vect(0,1));
        TriangleBumper otherTri270 = new TriangleBumper("tri270other", new DoublePair(0,0), 270, new ArrayList<Gadget>()); 
        double result = otherTri270.getTimeUntilCollision(ball8);
        double expected = Geometry.timeUntilWallCollision(new LineSegment(0,0,1,1), ball8.getBall(), ball8.getVelocity());
        assertFalse(Double.POSITIVE_INFINITY==result);
        assertTrue(result==expected); 
        
    }
    
 //-------ReflectBall() TESTS------------//
    @Test
    public void reflectBallNoReflection(){
            Ball ball9 = new Ball("ball9", 2,2, new Vect(-1,0));
            tri0.reflectBall(ball9, SAMPLE_TIME);
            Vect result = ball9.getVelocity();
            Vect expected = Geometry.reflectWall(new LineSegment(0,0,0,1),new Vect(-1,0));
            assertEquals(new Double(result.x()),new Double(expected.x()), .001);
            assertEquals(new Double(result.x()),new Double(expected.x()), .001);
    }

    @Test
    public void reflectBallOppositeDirection(){
        try{
            square1.reflectBall(ball2, SAMPLE_TIME);
            Vect result = ball2.getVelocity();
            Vect expected = Geometry.reflectCircle(circle1.getCenter(), ball2.getPosition(), ball2.getVelocity());
            assertTrue(result.equals(expected));
        } catch (Exception e) {
            
        }
    }
    
    @Test
    public void reflectBall90Degrees(){
        try{
            square1.reflectBall(ball1, SAMPLE_TIME);
            Vect result = ball1.getVelocity();
            Vect expected = Geometry.reflectCircle(circle1.getCenter(), ball1.getPosition(), ball1.getVelocity());
            assertTrue(result.equals(expected));
        } catch (Exception e) {
           
        }
    }
    
    
    

}
