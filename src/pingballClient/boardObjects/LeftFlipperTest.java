package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import physics.Vect;
import physics.Geometry.DoublePair;

/**
 * LeftFlipper testing strategy:
 *      toString:
 *          ori 0
 *          ori 90
 *          ori 180 
 *          ori 270
 *      respondToTrigger and rotateFlipper
 *          ori 0
 *              completely flipped
 *              rotated < 45
 *              rotated > 45
 *          ori 90
 *              completely flipped
 *              rotated < 45
 *              rotated > 45
 *          ori 180
 *              completely flipped
 *              rotated < 45
 *              rotated > 45
 *          ori 270 
 *              completely flipped
 *              rotated < 45
 *              rotated > 45
 *      reflectBall
 *      constructor tests
 * @author asolei, nwallace
 */
public class LeftFlipperTest {
    CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    
    Ball ball1 = new Ball("ball1",10,10, new Vect(-1,0));
    Ball ball2 = new Ball("ball2",5.5,5.5, new Vect(-1,0));
    Ball ball3 = new Ball("ball3", 1.2, 1.2, new Vect(-1,0));
    
    LeftFlipper lflip0 = new LeftFlipper("lflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
    LeftFlipper lflip90 = new LeftFlipper("lflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
    LeftFlipper lflip180 = new LeftFlipper("lflip180", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
    LeftFlipper lflip270 = new LeftFlipper("lflip270", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());

//---------CONSTRUCTOR TESTS in NoDiditTest file----------//

    
//-------toString() TESTS----------//
/*Testing partition: all 4 orientations of left flipper
 */
    @Test
    public void lflip0toString() {
        String result = lflip0.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip90toString() {
        String result = lflip90.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
    
    @Test
    public void lFlip180toString() {
        String result = lflip180.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip270toString() {
        String result = lflip270.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    

//-----respondToTrigger() and rotateFlipper() TESTS------//
//Partitioning strategy: all 4 orientations of left flipper x(completely flipped + rotated < 45 + rotated>45)
    
    @Test
    public void lflip0Flipped() {
        LeftFlipper lflip0 = new LeftFlipper("lflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        String before = lflip0.toString();
        lflip0.respondToTrigger(1.0);
        lflip0.move(1.0);
        String result = lflip0.toString();
        String expected = "FLIPPER - T";
        //check that flipper is mutated
        assertFalse(before.equals(result));
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip90Flipped() {
        LeftFlipper lflip90 = new LeftFlipper("lflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
        lflip90.respondToTrigger(1);
        lflip90.move(1);
        String result = lflip90.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void lFlip180Flipped() {
        LeftFlipper lflip180 = new LeftFlipper("lflip280", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
        lflip180.respondToTrigger( 1);
        lflip180.move(1);
        String result = lflip180.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip270Flipped() {
        LeftFlipper lflip270 = new LeftFlipper("lflip0", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());
        lflip270.respondToTrigger(1);
        lflip270.move(1);
        String result = lflip270.toString();
        String expected = "FLIPPER | L";
       
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip0RotateLessThan45() {
        LeftFlipper lflip0 = new LeftFlipper("lflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        lflip0.respondToTrigger(.002);
        lflip0.move(.002);
        String result = lflip0.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip0RotateMoreThan45() {
        LeftFlipper lflip0 = new LeftFlipper("lflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        lflip0.respondToTrigger(.05);
        lflip0.move(.05);
        String result = lflip0.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip90RotateLessThan45() {
        LeftFlipper lflip90 = new LeftFlipper("lflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
        lflip90.respondToTrigger(.002);
        lflip90.move(.002);
        String result = lflip90.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip90RotateMoreThan45() {
        LeftFlipper lflip90 = new LeftFlipper("lflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
        lflip90.respondToTrigger(.05);
        lflip90.move(.05);
        String result = lflip90.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip180RotateLessThan45() {
        LeftFlipper lflip180 = new LeftFlipper("lflip180", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
        lflip180.respondToTrigger(.002);
        lflip180.move(.002);
        String result = lflip180.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip180RotateMoreThan45() {
        LeftFlipper lflip180 = new LeftFlipper("lflip180", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
        lflip180.respondToTrigger(.05);
        lflip180.move(.05);
        String result = lflip180.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip270RotateLessThan45() {
        LeftFlipper lflip270 = new LeftFlipper("lflip270", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());
        lflip270.respondToTrigger(.002);
        lflip270.move(.002);
        String result = lflip270.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void lflip270RotateMoreThan45() {
        LeftFlipper lflip270 = new LeftFlipper("lflip270", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());
        lflip270.respondToTrigger(.05);
        lflip270.move(.05);
        String result = lflip270.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }
    

//-------Collision Handling, reflectBall() TESTS -----------///
    @Test
    public void lflip0reflect(){
        try{
        Ball ball3 = new Ball("ball3", 1.2, 1.2, new Vect(-1,0));
        Vect startVel = ball3.getVelocity();
        LeftFlipper lflip0 = new LeftFlipper("lflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        lflip0.reflectBall(ball3, 1);
        assertFalse(startVel.equals(ball3.getVelocity()));
        
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//--------test multiplying by positive infinity----------------///
    @Test
    public void testMultiplyInfinity(){
        double theoreticalAngle = -1 * .001 * Double.POSITIVE_INFINITY;
        assertTrue(theoreticalAngle <= -Math.PI/2);
    }

}
