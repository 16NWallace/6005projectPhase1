package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import physics.Angle;
import physics.Geometry;
import physics.LineSegment;
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

public class RightFlipperTest {
    CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    
    Ball ball1 = new Ball("ball1",10,10, new Vect(-1,0));
    Ball ball2 = new Ball("ball2",5.5,5.5, new Vect(-1,0));
    
    static RightFlipper rflip0 = new RightFlipper("rflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
    static RightFlipper rflip90 = new RightFlipper("rflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
    static RightFlipper rflip180 = new RightFlipper("rflip180", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
    static RightFlipper rflip270 = new RightFlipper("rflip270", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());

//-----CONSTRUCTOR TESTS in NoDiditTests--------//

//------toString() Tests-------//
    @Test
    public void rflip0toString() {
        String result = rflip0.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip90toString() {
        String result = rflip90.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip180toString() {
        String result = rflip180.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip270toString() {
        String result = rflip270.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
 
  //-----respondToTrigger() and rotateFlipper() TESTS------//
  //Partitioning strategy: all 4 orientations of left flipper x(completely flipped + rotated < 45 + rotated>45)
    @Test
    public void rflip0Flipped() {
        RightFlipper rflip0 = new RightFlipper("rflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        rflip0.respondToTrigger( 1);
        rflip0.move(1);
        String result = rflip0.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip90Flipped() {
        RightFlipper rflip90 = new RightFlipper("rflip0", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
        rflip90.respondToTrigger(1);
        rflip90.move(1);
        String result = rflip90.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip180Flipped() {
        RightFlipper rflip180 = new RightFlipper("rflip0", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
        rflip180.respondToTrigger(1);
        rflip180.move(1);
        String result = rflip180.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip270Flipped() {
        RightFlipper rflip270 = new RightFlipper("rflip0", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());
        rflip270.respondToTrigger(1);
        rflip270.move(1);
        String result = rflip270.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }

    @Test
    public void rflip0RotateLessThan45() {
        RightFlipper rflip0 = new RightFlipper("rflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        rflip0.respondToTrigger(.002);
        rflip0.move(.002);
        String result = rflip0.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip0RotateMoreThan45() {
        RightFlipper rflip0 = new RightFlipper("rflip0", new DoublePair(0,0), 0, false, new ArrayList<Gadget>());
        rflip0.respondToTrigger(.05);
        rflip0.move(.05);
        String result = rflip0.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip90RotateLessThan45() {
        RightFlipper rflip90 = new RightFlipper("rflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
        rflip90.respondToTrigger(.002);
        rflip90.move(.002);
        String result = rflip90.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip90RotateMoreThan45() {
        RightFlipper rflip90 = new RightFlipper("rflip90", new DoublePair(0,0), 90, false, new ArrayList<Gadget>());
        rflip90.respondToTrigger(.05);
        rflip90.move(.05);
        String result = rflip90.toString();
        String expected = "FLIPPER | R";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip180RotateLessThan45() {
        RightFlipper rflip180 = new RightFlipper("rflip180", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
        rflip180.respondToTrigger(.002);
        rflip180.move(.002);
        String result = rflip180.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip180RotateMoreThan45() {
        RightFlipper rflip180 = new RightFlipper("rflip180", new DoublePair(0,0), 180, false, new ArrayList<Gadget>());
        rflip180.respondToTrigger(.05);
        rflip180.move(.05);
        String result = rflip180.toString();
        String expected = "FLIPPER - B";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip270RotateLessThan45() {
        RightFlipper rflip270 = new RightFlipper("rflip270", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());
        rflip270.respondToTrigger(.002);
        rflip270.move(.002);
        String result = rflip270.toString();
        String expected = "FLIPPER - T";
        assertEquals(result, expected);
    }
    
    @Test
    public void rflip270RotateMoreThan45() {
        RightFlipper rflip270 = new RightFlipper("rflip270", new DoublePair(0,0), 270, false, new ArrayList<Gadget>());
        rflip270.respondToTrigger(.05);
        rflip270.move(.05);
        String result = rflip270.toString();
        String expected = "FLIPPER | L";
        assertEquals(result, expected);
    }

//-----tests to figure out RotateAround() direction-----//
    @Test
    public void testRotateAroundClockwise90(){
        LineSegment initial = new LineSegment(new Vect(0, 0), new Vect(0, 1));
        LineSegment rotated = Geometry.rotateAround(initial, new Vect(0, 0), new Angle(0).DEG_90);
        LineSegment expected = new LineSegment(new Vect(0, 0), new Vect(-1, 0));
        assertEquals(rotated, expected); 
    }
    
    @Test
    public void testRotateAroundCounter90(){
        LineSegment initial = new LineSegment(new Vect(0,0), new Vect(0, 1));
        LineSegment rotated = Geometry.rotateAround(initial, new Vect(0, 0), new Angle(0).DEG_270);
        LineSegment expected = new LineSegment(new Vect(0, 0), new Vect(1, 0));
        assertEquals(rotated, expected);
    }
    
    @Test
    public void testRotateAroundCounterFrom0(){
        LineSegment initial = new LineSegment(new Vect(0,0), new Vect(0, 1));
        LineSegment rotated1 = Geometry.rotateAround(initial, new Vect(0,0), new Angle(-Math.PI/4));
        LineSegment rotated2 = Geometry.rotateAround(initial, new Vect(0,0), new Angle(Math.PI/4));
    }
    
    @Test
    public void testRotateAroundCounterFrom90(){
        LineSegment initial = new LineSegment(new Vect(1,0), new Vect(0,0));
        LineSegment rotated1 = Geometry.rotateAround(initial, new Vect(1, 0), new Angle(-Math.PI/4));
        LineSegment rotated2 = Geometry.rotateAround(initial, new Vect(1, 0), new Angle(Math.PI/4));
    }
    
    @Test
    public void testRotateAroundCounterFrom180(){
        LineSegment initial = new LineSegment(new Vect(1,1), new Vect(1,0));
        LineSegment rotated1 = Geometry.rotateAround(initial, new Vect(1,1), new Angle(-Math.PI/4));
        LineSegment rotated2 = Geometry.rotateAround(initial, new Vect(1,1), new Angle(Math.PI/4));
    }
    
    @Test
    public void testRotateAroundCounterFrom270(){
        LineSegment initial = new LineSegment(new Vect(0,1), new Vect(1,1));
        LineSegment rotated1 = Geometry.rotateAround(initial, new Vect(0,1), new Angle(-Math.PI/4));
        LineSegment rotated2 = Geometry.rotateAround(initial, new Vect(0,1), new Angle(Math.PI/4));
    }

}
