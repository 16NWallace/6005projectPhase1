package pingballClient;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import physics.*;
import pingballClient.boardObjects.Ball;
import pingballClient.boardObjects.Gadget;
import pingballClient.boardObjects.OuterWall;

/**
 * TESTING STRATEGY
 *   
 * ------------------- toString -------------------
 *      empty board with all walls visible
 *      empty board with all walls invisible
 *          of character length all less than 20
 *          two walls with char length 20, 2 with char length > 20
 *      board with gadgets and balls
 * 
 * ------------------- update -------------------
 *      ball collision with all gadget types 
 *      ball collision with ball 
 *      ball collision with wall 
 *    
 * ------------------- newBallPositions -------------------
 *      empty, no balls
 *      one ball in x axis direction
 *      one ball in y axis direction
 *      one ball in -x axis direction
 *      one ball in -y axis direction
 *      
 * ------------------- updateBoard -------------------
 *      ball collision with all gadget types 
 *      ball collision with ball 
 *      ball collision with wall 
 *      
 * ------------------- addBall -------------------
 *      add no balls
 *      add one ball
 *      add > 1 ball
 *    
 * ------------------- merge -------------------
 *      merge vertically
 *          board on top
 *          board on bottom
 *      merge horizontally
 *          board on left
 *          board on right
 * 
 * @author sdrammis
 * Implemented by: sdrammis, asolei, nwallace
 *
 */
public class BoardTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }
    
    // ------------------- toString ------------------- //
    @Test
    public void testToStringEmptyVisible() {
        String expected = new String();
        
        //add the top wall
        for (int i = -1; i < 21; i++) {
            expected += ".";
        }
        expected += "\n";
        //create the inner with left and right wall
        for (int x = 0; x < 20; x++) {
            expected += ".";
            for (int y = 0; y < 20; y++) {
                expected += " ";
            }
            expected += ".";
            expected += "\n";
        }
        //add the bottom wall
        for (int i = -1; i < 21; i++) {
            expected += ".";
        }
        expected += "\n";
        Board board = new Board(new ArrayList<Gadget>(), new ArrayList<Ball>(), "board", 0.0, 0.0, 0.0);
        assertEquals(expected, board.toString());
    }
    
    @Test
    public void testToStringEmptyInvisibleLessThan20() {
        String expected = new String();
        
        //add the top wall
        for (int i = 0; i < 9; i++) {
            expected += ".";
        } expected += "Ava";
        for (int i=0; i < 10; i++){
            expected += ".";
        }
        expected += "\n";
        String expectedLeft = "......GillWoo.......";
        String expectedRight = ".......Nadia........";
        String expectedBottom = ".......Sabrina........";
        //create the inner with left and right wall
        for (int x = 0; x < 20; x++) {
            expected += Character.toString(expectedLeft.charAt(x));
            for (int y = 0; y < 20; y++) {
                expected += " ";
            }
            expected += Character.toString(expectedRight.charAt(x));
            expected += "\n";
        }
        expected += expectedBottom;
        expected += "\n";
        Board board = new Board(new ArrayList<Gadget>(), new ArrayList<Ball>(), "board", 0.0, 0.0, 0.0);
        
        for (OuterWall wall : board.getWalls()) {
            wall.changeInvisible(true);
        }
        
        HashMap<String, String> neighbors = board.getNeighbors();
        neighbors.put("T", "Ava");
        neighbors.put("B", "Sabrina");
        neighbors.put("R", "Nadia");
        neighbors.put("L", "GillWoo");

        assertEquals(expected, board.toString());
    }
    
    @Test
    public void testToStringEmptyInvisibleEqualToAndMoreThan20() {
        String expected = new String();
        
        //add the top wall
        expected += ".qwertyuiopasdfghjklz.";
        expected += "\n";
        String left = "mnbvcxzlkjhgfdsapoiuytrewq".substring(0,20);
        String right = "qwertyuiopasdfghjklz".substring(0,20);
        
        //create the inner with left and right wall
        for (int x = 0; x < 20; x++) {
            expected += Character.toString(left.charAt(x));
            for (int y = 0; y < 20; y++) {
                expected += " ";
            }
            expected += Character.toString(right.charAt(x));
            expected += "\n";
        }
        //add the bottom wall
        expected += ".mnbvcxzlkjhgfdsapoiuytrewq.".substring(0, 21);
        expected += ".";
        expected += "\n";
        Board board = new Board(new ArrayList<Gadget>(), new ArrayList<Ball>(), "board", 0.0, 0.0, 0.0);
        
        for (OuterWall wall : board.getWalls()) {
            wall.changeInvisible(true);
        }
        
        HashMap<String, String> neighbors = board.getNeighbors();
        neighbors.put("T", "qwertyuiopasdfghjklz");
        neighbors.put("B", "mnbvcxzlkjhgfdsapoiuytrewq");
        neighbors.put("R", "qwertyuiopasdfghjklz");
        neighbors.put("L", "mnbvcxzlkjhgfdsapoiuytrewq");

        assertEquals(expected, board.toString());
    }

/**
 * Testing strategy for board behavior:
 * Because of difficulty in debugging other bugs, we did not have time to implement full JUnit testings for
 * board behaviors on single update time steps. The following files were used to test the corresponding behavior:
 * 
 * BOARDS IN SRC
 * 
 * absorberExtendsOff.pb.txt -- checks construction of an absorber fails when given a top left coordinate and 
 *      dimensions that would cause part of the absorber to extend off of the board
 * 
 * boardCoordsOutside.pb.txt -- this file tries to create gadgets with origins outside of the bounds of the board
 * 
 * emptyBoard.pb.txt -- used to check that balls properly bounce off of outer walls that are solid, and are correctly
 *      passed through invisible walls to the boards of joined players
 * 
 * extendBottomRight.pb.txt --used to check that a gadget cannot be placed at a point where it's origin is in the
 *      board at the bottom right but the rest of the object is not in the bounds of the board.
 *      
 * extendTopRight.pb.txt -- same as extendBottomRight.pb.txt, but for the top right corner of the board
 * 
 * flipperBottomRight.pb.txt -- flipper is instantiated such that its original position is in the board, but when
 *      it rotates, the flipper moves out of the bounds of the board.
 * 
 * flipperSelf.pb.txt -- verifies self-triggering of flippers
 * 
 * illegalName.pb.txt -- checks that the parser cannot accept a name that has special characters or space [^A-Za-z0-9+]
 * 
 * noCoords.pb.txt -- checks that a board cannot be created if all objects in the file are not given coordinates
 * 
 * noName.pb.txt -- checks that a board cannot be created if the first line does not contain a name
 * 
 * referenceNameNotInBoard.pb.txt -- tries to create a trigger link to an object using a name for which no object
 *      has been created.
 *      
 * sameNameBoard.pb.txt -- cannot create a board with a name that is already in the set of names for boards created
 *      in BoardFactory
 * 
 * sampleBoards -- used to verify proper behavior in sample boards 
 * 
 * testAbsorberSelfTrigger.pb.txt -- verifies proper behavior of self-triggering absorbers
 * 
 * testAbsorberTriggeredBySquares.pb.txt -- verifies proper behavior when a square bumpers is the trigger for
 *      an absorber
 *      
 * testLeftFlipperRotateNotBySelf.pb.txt -- verifies proper rotation behavior when a left flipper is triggered by another gadget 
 *      in the board
 *
 * testLeftFlipperSelfRotate.pb.txt -- verifies proper behavior of self-triggering left flippers
 * 
 * testLeftFlipperNoSelfRotate.pb.txt -- verifies that left flippers that are not self-triggering do not rotate when hit
 * 
 * testRightFlipperNoSelfRotate.pb.txt -- verifies that right flippers that are not self-triggering do not rotate when hit
 * 
 * testRightFlipperRotateNotBySelf.pb.txt -- verifies proper rotation behavior when a left flipper is triggered by another gadget
 *      in the board
 * 
 * testRightFlipperSelfRotate.pb.txt -- verifies proper behavior of self-triggering right flippers
 *
 * whitespaceBoard3.pb.txt -- still parses the board file properly when extra whitespace is added
 *
 * wrongBoardDef.pb.txt -- a board cannot be defined with a capital letter "Board" tag
 *
 * wrongGadgetDef.pb.txt -- gadget types cannot be constructed when they start with a capital, makes sure definition
 *      of gadget is properly named
 * 
 * wrongOri.pb.txt -- passes in invalid orientation values [^0|90|180|270] for flippers and TriangleBumper
 * 
 * BOARDS IN PINGBALL-PHASE1
 * 
 * p1Board.pb.txt, p2Board.pb.txt, p3Board.pb.txt, p4Board.pb.txt -- used to create clients for BlockingQueueThreadTest tests
 *  
 *  @author nwallace, asolei
 */
}
