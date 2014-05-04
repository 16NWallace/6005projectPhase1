package pingballClient.boardObjects;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import physics.Geometry.DoublePair;

/** Contains all constructor tests that are dependent on assertions being enabled, since assertions are not enabled
 * on Didit
 * 
 * @category no_didit
 * @author nwallace
 *
 */


public class NoDiditTests {

    //-----CONSTRUCTOR TESTS --- fail checkRep()------//
    @Test(expected = AssertionError.class)
    
    //Circle
    public void failForTopLeftOutsideBoardC() {
        CircleBumper invalidCircle = new CircleBumper("invalid", new DoublePair(25, 25), new ArrayList<Gadget>()); 
    }
    
    @Test(expected = AssertionError.class)
    public void failForTopLeftOutsideBoardLF() {
        LeftFlipper invalid1 = new LeftFlipper("invalid1", new DoublePair(25, 25), 90, false, new ArrayList<Gadget>()); 
    }
    
    //Left flipper
    @Test(expected = AssertionError.class)
    public void failForInvalidOrientationLF(){
    CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
    SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
        Gadget[] invalidGadgets = new Gadget[]{circle1, square1};
        LeftFlipper invalid3 = new LeftFlipper("invalid3", new DoublePair(10, 10), 30, false, new ArrayList<Gadget>());  
    }
    
    //Right flipper
    @Test(expected = AssertionError.class)
    public void failForTopLeftOutsideBoardRF() {
        RightFlipper invalid1 = new RightFlipper("invalid1", new DoublePair(25, 25), 90, false, new ArrayList<Gadget>()); 
    }
    
    @Test(expected = AssertionError.class)
    public void failForInvalidOrientationRF(){
        CircleBumper circle1 = new CircleBumper("circle1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
        SquareBumper square1 = new SquareBumper("square1", new DoublePair(4.5,4.5), new ArrayList<Gadget>());
        Gadget[] invalidGadgets = new Gadget[]{circle1, square1};
        RightFlipper invalid3 = new RightFlipper("invalid3", new DoublePair(10, 10), 30, false, new ArrayList<Gadget>());  
    }
    
    //Square
    @Test(expected = AssertionError.class)
    public void failForTopLeftOutsideBoardSQ() {
        SquareBumper invalidSquare = new SquareBumper("invalid", new DoublePair(25, 25), new ArrayList<Gadget>()); 
    }
    
    //Triangle
    
    //expects IndexOutOfBoundsException because sides will never be initialized, so they can't be iterated through
    //in constructor for corners, or in checkRep()
    @Test(expected = AssertionError.class)
    public void failForInvalidOrientationTRI(){
        TriangleBumper invalidTri3 = new TriangleBumper("invalid", new DoublePair(10, 10), 30, new ArrayList<Gadget>()); 
    }
    
    @Test(expected = AssertionError.class)
    public void failForTopLeftOutsideBoardTRI() {
        TriangleBumper invalidSquare = new TriangleBumper("invalid", new DoublePair(25, 25),0, new ArrayList<Gadget>()); 
    }

}
