package pingballClient.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import physics.Geometry.DoublePair;
import physics.Vect;
import pingballClient.*;
import pingballClient.boardObjects.*;

import org.junit.Test;
/**
 * BoardFactory testing strategy:
 *      valid files generate things in the right place :
 *      --> by testing equality of String representations:
 *          parsing of sampleBoard1 generates Board w things in the right place
 *          parsing of sampleBoard2-1 generates Board w things in the right place
 *          parsing of sampleBoard3 generates Board w things in the right place
 *          parsing of sampleBoard4 generates Board w things in the right place
 *          board with a lot of whitespace in between tokens generates Board w things in the right place
 *      --> proper behavior, i.e. testing that flippers flip when triggered, absorbers release when 
 *             triggerd cannot be tested here, this was tested by "playing" on various board files.
 *      invalid files:
 *          files that meet the grammar
 *              repeated names
 *              coordinates of objects outside the space of the Board
 *              reference a name that does not appear in the File
 *              Absorber extending off the board
 *              attempt to place at bottom right corner, one box away from actual bottom right corner
 *              attempt to place outside of top right corner
 *              attempt to place a Flipper at (19, 19)
 *          files that fail the grammar
 *              orientation that is not 0|90|180|270
 *              fail to provide a name
 *              name contains illegal characters
 *              fail to provide coordinates for the position of an object
 *              attempt to define a board as "Board" rather than "board"
 *              attempt to define a gadget as "Gadget" rather than "gadGet" (indicates camel case as specified in prompt)
 *  @category no_didit    
 *  @author asolei
 *  Implemented by: asolei
 */
public class BoardFactoryTest {
    // valid files
   
    @Test
    public void testSampleBoard1() throws IOException {
        File sample1 = new File("./src/sampleBoard1.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);
        String name = "sampleBoard1";
        double gravity = 20.0;
        double friction1 = 0.020;
        double friction2 = 0.020;
        List<Ball> ball = new ArrayList<Ball>(Arrays.asList(new Ball("Ball", 0.5, 0.5, new Vect(2.5, 2.5))));
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        boardGadgets.add(new SquareBumper("Square0", new DoublePair(0, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square1", new DoublePair(1, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square2", new DoublePair(2, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square3", new DoublePair(3, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square4", new DoublePair(4, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square5", new DoublePair(5, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square6", new DoublePair(6, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square7", new DoublePair(7, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square12", new DoublePair(12, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square13", new DoublePair(13, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square14", new DoublePair(14, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square15", new DoublePair(15, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square16", new DoublePair(16, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square17", new DoublePair(17, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square18", new DoublePair(18, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square19", new DoublePair(19, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle4", new DoublePair(4, 3), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle5", new DoublePair(5, 4), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle6", new DoublePair(6, 5), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle7", new DoublePair(7, 6), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle12", new DoublePair(12, 6), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle13", new DoublePair(13, 5), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle14", new DoublePair(14, 4), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle15", new DoublePair(15, 3), new ArrayList<Gadget>()));
        boardGadgets.add(new TriangleBumper("Tri1", new DoublePair(8, 9), 270, new ArrayList<Gadget>()));
        boardGadgets.add(new TriangleBumper("Tri2", new DoublePair(11, 9), 180, new ArrayList<Gadget>()));
        boardGadgets.add(new LeftFlipper("FlipL1", new DoublePair(0, 2), 0, false, new ArrayList<Gadget>()));
        boardGadgets.add(new RightFlipper("FlipR1", new DoublePair(0, 2), 0, false, new ArrayList<Gadget>()));
        boardGadgets.add(new LeftFlipper("FlipL2", new DoublePair(0, 2), 0, false, new ArrayList<Gadget>()));
        boardGadgets.add(new RightFlipper("FlipR2", new DoublePair(0, 2), 0, false, new ArrayList<Gadget>()));
        boardGadgets.add(new Absorber(20, 1, new DoublePair(0, 19), "Abs", true, new ArrayList<Gadget>()));
        Board expectedBoard = new Board(boardGadgets, ball, name, gravity, friction1, friction2);
        assert(expectedBoard.toString().equals(createdBoard.toString()));

    }
    
    @Test
    public void testSampleBoard2a() throws IOException {
        File sample1 = new File("./src/sampleBoard2-1.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);
        String name = "sampleBoard2_1";
        double gravity = 20.0;
        double friction1 = 0.020;
        double friction2 = 0.020;
        List<Ball> ball = new ArrayList<Ball>(Arrays.asList(new Ball("Ball", 0.5, 0.5, new Vect(2.5, 2.5))));
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        boardGadgets.add(new SquareBumper("Square0", new DoublePair(0, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square1", new DoublePair(1, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square2", new DoublePair(2, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square3", new DoublePair(3, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square4", new DoublePair(4, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square5", new DoublePair(5, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square6", new DoublePair(6, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square7", new DoublePair(7, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square8", new DoublePair(8, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square9", new DoublePair(9, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square10", new DoublePair(10, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square11", new DoublePair(11, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square12", new DoublePair(12, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square13", new DoublePair(13, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square14", new DoublePair(14, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square15", new DoublePair(15, 2), new ArrayList<Gadget>()));

        boardGadgets.add(new CircleBumper("Circle10", new DoublePair(10, 3), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle11", new DoublePair(11, 4), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle12", new DoublePair(12, 5), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle13", new DoublePair(13, 6), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle14", new DoublePair(14, 7), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle15", new DoublePair(15, 8), new ArrayList<Gadget>()));

        boardGadgets.add(new TriangleBumper("Tri1", new DoublePair(17, 11), 270, new ArrayList<Gadget>()));
        boardGadgets.add(new TriangleBumper("Tri2", new DoublePair(18, 12), 270, new ArrayList<Gadget>()));
        boardGadgets.add(new LeftFlipper("FlipL1", new DoublePair(16, 2), 0, false, new ArrayList<Gadget>()));
        boardGadgets.add(new LeftFlipper("FlipL2", new DoublePair(16, 9), 0, false, new ArrayList<Gadget>()));

        boardGadgets.add(new Absorber(20, 1, new DoublePair(0, 19), "Abs", true, new ArrayList<Gadget>()));
        Board expectedBoard = new Board(boardGadgets, ball, name, gravity, friction1, friction2);
        assert(expectedBoard.toString().equals(createdBoard.toString()));

    }
    
    @Test
    public void testSampleBoard2b() throws IOException {
        File sample1 = new File("./src/sampleBoard2-2.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);
        String name = "sampleBoard2_2";
        double gravity = 20.0;
        double friction1 = 0.020;
        double friction2 = 0.020;
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        boardGadgets.add(new SquareBumper("Square4", new DoublePair(4, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square5", new DoublePair(5, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square6", new DoublePair(6, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square7", new DoublePair(7, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square8", new DoublePair(8, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square9", new DoublePair(9, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square10", new DoublePair(10, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square11", new DoublePair(11, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square12", new DoublePair(12, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square13", new DoublePair(13, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square14", new DoublePair(14, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square15", new DoublePair(15, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square16", new DoublePair(16, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square17", new DoublePair(17, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square18", new DoublePair(18, 2), new ArrayList<Gadget>()));
        boardGadgets.add(new SquareBumper("Square19", new DoublePair(19, 2), new ArrayList<Gadget>()));
        

        boardGadgets.add(new CircleBumper("Circle4", new DoublePair(4, 8), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle5", new DoublePair(5, 7), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle6", new DoublePair(6, 6), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle7", new DoublePair(7, 5), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle8", new DoublePair(8, 4), new ArrayList<Gadget>()));
        boardGadgets.add(new CircleBumper("Circle9", new DoublePair(9, 3), new ArrayList<Gadget>()));

        boardGadgets.add(new TriangleBumper("Tri1", new DoublePair(1, 12), 180, new ArrayList<Gadget>()));
        boardGadgets.add(new TriangleBumper("Tri2", new DoublePair(2, 11), 180, new ArrayList<Gadget>()));
        boardGadgets.add(new RightFlipper("FlipR1", new DoublePair(2, 2), 0, false, new ArrayList<Gadget>()));
        boardGadgets.add(new RightFlipper("FlipR2", new DoublePair(2, 9), 0, false, new ArrayList<Gadget>()));

        boardGadgets.add(new Absorber(20, 1, new DoublePair(0, 19), "Abs", true, new ArrayList<Gadget>()));
        Board expectedBoard = new Board(boardGadgets, new ArrayList<Ball>(), name, gravity, friction1, friction2);
        assert(expectedBoard.toString().equals(createdBoard.toString()));

    }
    
    @Test
    public void testSampleBoard3() throws IOException{
        File sample3 = new File("./src/sampleBoard3.pb.txt");
        Board createdBoard = BoardFactory.parse(sample3);
        String name = "ExampleB";
        double gravity = 10.0;
        double friction1 = 0.025;
        double friction2 = 0.025;
        List<Ball> balls = new ArrayList<Ball>(Arrays.asList(new Ball("BallA", 1.8, 4.5, new Vect(10.4, 10.3)), 
                new Ball("BallB", 10.0, 13.0, new Vect(-3.4, -2.3))));
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        Gadget flipL = new LeftFlipper("FlipL", new DoublePair(10, 7), 0, false, new ArrayList<Gadget>());
        Gadget flipR = new RightFlipper("FlipR", new DoublePair(12, 7), 0, false, new ArrayList<Gadget>());
        Gadget abs = new Absorber(10, 2, new DoublePair(10, 17), "Abs", true, new ArrayList<Gadget>());
        Gadget square = new SquareBumper("Square", new DoublePair(0, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareB = new SquareBumper("SquareB", new DoublePair(1, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareC = new SquareBumper("SquareC", new DoublePair(2, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareD = new SquareBumper("SquareD", new DoublePair(3, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareE = new SquareBumper("SquareE", new DoublePair(4, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareF = new SquareBumper("SquareF", new DoublePair(5, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareG = new SquareBumper("SquareG", new DoublePair(6, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareH = new SquareBumper("SquareH", new DoublePair(7, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget circle = new CircleBumper("Circle", new DoublePair(4, 3), new ArrayList<Gadget>());
        boardGadgets.addAll(Arrays.asList(square, squareB, squareC, squareD, squareE, squareF, squareG, squareH, circle, flipL, flipR, abs));

        Board expectedBoard = new Board(boardGadgets, balls, name, gravity, friction1, friction2);

        assert(expectedBoard.toString().equals(createdBoard.toString()));
    }
    
    @Test
    public void testSampleBoard4() throws IOException{
        File sample4 = new File("./src/sampleBoard4.pb.txt");
        Board createdBoard = BoardFactory.parse(sample4);
        String name = "ExampleA";
        double gravity = 20.0;
        double friction1 = 0.025;
        double friction2 = 0.025;
        List<Ball> balls = new ArrayList<Ball>(Arrays.asList(new Ball("BallC", 1.8, 4.5, new Vect(10.4, 10.3))));
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        Gadget flipL = new LeftFlipper("FlipL", new DoublePair(10, 7), 0, false, new ArrayList<Gadget>());
        Gadget flipR = new RightFlipper("FlipR", new DoublePair(12, 7), 0, false, new ArrayList<Gadget>());
        Gadget abs = new Absorber(10, 2, new DoublePair(10, 17), "Abs", true, new ArrayList<Gadget>());
        Gadget square = new SquareBumper("Square", new DoublePair(0, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareB = new SquareBumper("SquareB", new DoublePair(1, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareC = new SquareBumper("SquareC", new DoublePair(2, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareD = new SquareBumper("SquareD", new DoublePair(3, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget circle = new CircleBumper("Circle", new DoublePair(4, 3), new ArrayList<Gadget>());
        Gadget tri = new TriangleBumper("Tri", new DoublePair(19, 3), 90, new ArrayList<Gadget>());
        boardGadgets.addAll(Arrays.asList(square, squareB, squareC, squareD, circle, tri, flipL, flipR));
        Board expectedBoard = new Board(boardGadgets, balls, name, gravity, friction1, friction2);
        assert(expectedBoard.toString().equals(createdBoard.toString()));
    }
    
    
    @Test
    public void testWhitespaceBoard3() throws IOException{
        File sample3 = new File("./src/whitespaceBoard3.pb.txt");
        Board createdBoard = BoardFactory.parse(sample3);
        String name = "ExampleB";
        double gravity = 10.0;
        double friction1 = 0.025;
        double friction2 = 0.025;
        List<Ball> balls = new ArrayList<Ball>(Arrays.asList(new Ball("BallA", 1.8, 4.5, new Vect(10.4, 10.3)), 
                new Ball("BallB", 10.0, 13.0, new Vect(-3.4, -2.3))));
        List<Gadget> boardGadgets = new ArrayList<Gadget>();
        Gadget flipL = new LeftFlipper("FlipL", new DoublePair(10, 7), 0, false, new ArrayList<Gadget>());
        Gadget flipR = new RightFlipper("FlipR", new DoublePair(12, 7), 0, false, new ArrayList<Gadget>());
        Gadget abs = new Absorber(10, 2, new DoublePair(10, 17), "Abs", true, new ArrayList<Gadget>());
        Gadget square = new SquareBumper("Square", new DoublePair(0, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareB = new SquareBumper("SquareB", new DoublePair(1, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareC = new SquareBumper("SquareC", new DoublePair(2, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareD = new SquareBumper("SquareD", new DoublePair(3, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareE = new SquareBumper("SquareE", new DoublePair(4, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareF = new SquareBumper("SquareF", new DoublePair(5, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareG = new SquareBumper("SquareG", new DoublePair(6, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget squareH = new SquareBumper("SquareH", new DoublePair(7, 10), new ArrayList<Gadget>(Arrays.asList(flipL)));
        Gadget circle = new CircleBumper("Circle", new DoublePair(4, 3), new ArrayList<Gadget>());
        boardGadgets.addAll(Arrays.asList(square, squareB, squareC, squareD, squareE, squareF, squareG, squareH, circle, flipL, flipR, abs));

        Board expectedBoard = new Board(boardGadgets, balls, name, gravity, friction1, friction2);

        assert(expectedBoard.toString().equals(createdBoard.toString()));
    }
    
    /*
     * The test below was used to debug the improper linking of objects named by "fire" 
     */
//    
//    @Test
//    public void testTriggeredAbsorber() throws IOException{
//        File selfFlip = new File("./src/testAbsorberTriggeredBySquares.pb.txt");
//        Board createdBoard = BoardFactory.parse(selfFlip);
//        createdBoard.checkRep();
//        System.out.println(createdBoard.toString());
//    }
//    
    
    // invalid files: meet the grammar
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardRepeatedNames() throws IOException{
        File sample1 = new File("./src/sameNameBoard.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardCoordsOutside() throws IOException{
        File sample1 = new File("./src/boardCoordsOutside.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1); 
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardReferenceIllegalName() throws IOException{
        File sample1 = new File("./src/referenceNameNotInBoard.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1); 
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardAbsorberExtendsOff() throws IOException{
        File sample1 = new File("./src/absorberExtendsOff.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1); 
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardPlaceOffRightCorner() throws IOException{
        File sample1 = new File("./src/extendTopRight.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardPlaceOffBottomRight() throws IOException{
        File sample1 = new File("./src/extendBottomRight.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testBoardPlaceFlipperOff() throws IOException{
        File sample1 = new File("./src/flipperBottomRight.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    // invalid files: fail the grammar
    
    @Test (expected=RuntimeException.class)
    public void testBoardWrongOri() throws IOException{
        File sample1 = new File("./src/wrongOri.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=RuntimeException.class)
    public void testBoardNoName() throws IOException{
        File sample1 = new File("./src/noName.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=RuntimeException.class)
    public void testBoardIllegalName() throws IOException{
        File sample1 = new File("./src/illegalName.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=RuntimeException.class)
    public void testBoardNoCoords() throws IOException{
        File sample1 = new File("./src/noCoords.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=RuntimeException.class)
    public void testBoardWrongBoardDef() throws IOException{
        File sample1 = new File("./src/wrongBoardDef.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
    
    @Test (expected=RuntimeException.class)
    public void testBoardWrongGadgetDef() throws IOException{
        File sample1 = new File("./src/wrongGadgetDef.pb.txt");
        Board createdBoard = BoardFactory.parse(sample1);  
    }
}
