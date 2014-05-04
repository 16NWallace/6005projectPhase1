package pingballClient.boardObjects;

import static org.junit.Assert.*;

import physics.*;
import physics.Geometry.DoublePair;
import physics.Geometry.VectPair;

import org.junit.Test;

/**
 * Ball testing strategy:
 *      reflect
 *          reflect two balls
 *      add
 *      equals
 *          equal balls
 *          --> test hashCode
 *          unequal balls
 *              different name
 *              different x/y
 *              different velocity
 * @author asolei
 */
public class BallTest {
    
    // test reflect
    
    @Test
    public void testBallReflect(){
        Ball ball = new Ball("ball1", 0, 0, new Vect(10, 10));
        Ball ball2 = new Ball("ball2", 0, 0, new Vect(-10, 10));
        VectPair reflectVects = Geometry.reflectBalls(new Vect(0,0), 1, new Vect(10, 10), new Vect(0,0), 1, new Vect(-10, 10));
        ball.reflect(ball2);
        assertEquals(ball.getVelocity(), reflectVects.v1);
    }
    
    // test add
    
    @Test
    public void testBallAdd(){
        Ball ball = new Ball("ball1", 0, 0, new Vect(10,10));
        Ball ball2 = new Ball("ball2", 0, 0, new Vect(-10,10));
        Vect vect1= new Vect(10,10);
        Vect vect2 = new Vect(-10, 10);
        Vect ballAdd = ball.add(ball2);
        assertEquals(ballAdd, vect1.plus(vect2));
    }
    
    // test equals and hashCode
    
    @Test
    public void testBallEqualsAndHashCode(){
        Ball ball1 = new Ball("ball1", 0, 0, new Vect(10,10));
        Ball ball2 = new Ball("ball1", 0, 0, new Vect(10,10));
        assertTrue(ball1.equals(ball2));
        assertTrue(ball2.equals(ball1));
        assertTrue(ball1.hashCode() == ball2.hashCode());
    }
    
    @Test
    public void testBallEqualsDifferentName(){
        Ball ball1 = new Ball("ball2", 0, 0, new Vect(10,10));
        Ball ball2 = new Ball("ball1", 0, 0, new Vect(10,10));
        assertFalse(ball1.equals(ball2));
    }
    
    @Test
    public void testBallEqualsDifferentXY(){
        Ball ball1 = new Ball("ball1", 1, 0, new Vect(10,10));
        Ball ball2 = new Ball("ball1", 0, 0, new Vect(10,10));
        assertFalse(ball1.equals(ball2));
    }
    
    @Test
    public void testBallEqualsDifferentVel(){
        Ball ball1 = new Ball("ball1", 0, 0, new Vect(-10,10));
        Ball ball2 = new Ball("ball1", 0, 0, new Vect(10,10));
        assertFalse(ball1.equals(ball2));
    }

}
