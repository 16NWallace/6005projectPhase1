package pingballClient.boardObjects;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.Geometry;
import physics.Vect;

/**
 * OuterWall testingStrategy:
 *      test constructor
 *          assign correct names
 *          all initialized as solid
 *      toString
 *      getTimeUntilCollision
 *          never collides
 *          ball collides when nearly touching wall
 *          ball collides after starting somewhere within the board
 *          ball collides from outside the board
 *          ball collides with an invisible top wall
 *          ball collides with an invisible bottom wall
 *          ball collides with an invisible left wall
 *          ball collides with an invisible right wall
 * @author asolei, nwallace
 */
public class OuterWallTest {

    OuterWall top = new OuterWall(new Vect(-0.01, -0.01), new Vect(20.01, -0.01));
    OuterWall bottom =new OuterWall(new Vect(-0.01, 20.01), new Vect(20.01, 20.01));
    OuterWall left = new OuterWall(new Vect(-0.01, -0.01), new Vect(-0.01, 20.01));
    OuterWall right = new OuterWall(new Vect(20.01, -0.01), new Vect(20.01, 20.01));

//-------CONSTRUCTOR TESTS--------//
    
    @Test 
    public void assignsCorrectNames() {
        assertEquals(top.getName(), "T");
        assertEquals(bottom.getName(), "B");
        assertEquals(left.getName(), "L");
        assertEquals(right.getName(), "R");

    }
    
    @Test
    public void allInitializedAsSolid() {
        assertFalse(top.isInvisible());
        assertFalse(bottom.isInvisible());
        assertFalse(left.isInvisible());
        assertFalse(right.isInvisible());
    }

//-------toString() TESTS-----------//
    
    @Test
    public void validToString() {
        String horizontal = ". . . . . . . . . . . . . . . . . . . . ";
        String vertical = ".\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n";
        assertTrue(top.toString().equals(horizontal));
        assertTrue(bottom.toString().equals(horizontal));
        assertTrue(left.toString().equals(vertical));
        assertTrue(right.toString().equals(vertical));

    }
    
//------COLLISION TESTS--------//
//getTimeUntilCollision returns a value directly from a physics method, so it is assumed that this value is correct
    
    @Test
    public void neverCollides(){
        Ball horizontalV = new Ball("horiz",2,2, new Vect(1,0));
        Ball verticalV = new Ball("vert", 2,2, new Vect(0,-1));
        
        assertTrue(top.getTimeUntilCollision(horizontalV)==Double.POSITIVE_INFINITY);
        assertTrue(bottom.getTimeUntilCollision(horizontalV)==Double.POSITIVE_INFINITY);
        assertTrue(left.getTimeUntilCollision(verticalV)==Double.POSITIVE_INFINITY);
        assertTrue(right.getTimeUntilCollision(verticalV)==Double.POSITIVE_INFINITY);
        
    }
    
    @Test
    public void collidesWhenNearlyTouchingWall(){
        Ball ballAtEdge= new Ball("edge",10,0.55, new Vect(0,-1));
        assertTrue(top.getTimeUntilCollision(ballAtEdge)!=Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void collidesFromWithinBoard(){
        Ball ballAtEdge= new Ball("edge",10,5.55, new Vect(0,-1));
        assertTrue(top.getTimeUntilCollision(ballAtEdge)!=Double.POSITIVE_INFINITY);
        assertTrue(top.getTimeUntilCollision(ballAtEdge)==
                     Geometry.timeUntilWallCollision(top.getWall(), ballAtEdge.getBall(), ballAtEdge.getVelocity()));
        
    }
    
    @Test
    public void collidesFromOutsideBoard(){
        Ball ballAtEdge= new Ball("edge",10,-5.55, new Vect(0,1));
        assertTrue(top.getTimeUntilCollision(ballAtEdge)!=Double.POSITIVE_INFINITY);
        assertTrue(top.getTimeUntilCollision(ballAtEdge)==
                     Geometry.timeUntilWallCollision(top.getWall(), ballAtEdge.getBall(), ballAtEdge.getVelocity()));
        
    }
    
    @Test
    public void collidesWithInvisibleTop(){
        OuterWall top = new OuterWall(new Vect(-0.01, -0.01), new Vect(20.01, -0.01));
        top.changeInvisible(true);
        assertTrue(top.isInvisible());
        
        Ball ball = new Ball("test", 5,19, new Vect(0,-1));
        top.reflectBall(ball, .05);
        assertTrue(ball.getX()==ball.getX()&&ball.getY()==19);
        
    }
    
    @Test
    public void collidesWithInvisibleBottom(){
        OuterWall bottom =new OuterWall(new Vect(-0.01, 20.01), new Vect(20.01, 20.01));
        bottom.changeInvisible(true);
        assertTrue(bottom.isInvisible());
        
        Ball ball = new Ball("test", 5,19, new Vect(0,1));
        bottom.reflectBall(ball, .05);
        assertTrue(ball.getX()==ball.getX()&&ball.getY()==1);
        
    }
    
    @Test
    public void collidesWithInvisibleLeft(){
        OuterWall left = new OuterWall(new Vect(-0.01, -0.01), new Vect(-0.01, 20.01));
        left.changeInvisible(true);
        assertTrue(left.isInvisible());
        
        Ball ball = new Ball("test", 5,1, new Vect(-1,0));
        Double originalY = ball.getY();
        left.reflectBall(ball, .05);
        System.out.println(ball.getX());
        System.out.println(left.getName());
        System.out.println(left.isInvisible());
        assertTrue(originalY==ball.getY()&&ball.getX()==19);
    }
    
    @Test
    public void collidesWithInvisibleRight(){
        OuterWall right = new OuterWall(new Vect(20.01, -0.01), new Vect(20.01, 20.01));
        right.changeInvisible(true);
        assertTrue(right.isInvisible());
        
        Ball ball = new Ball("test", 5,19, new Vect(1,0));
        right.reflectBall(ball, .05);
        assertTrue(ball.getY()==ball.getY());
        assertTrue(ball.getX()==1);
    }

}
