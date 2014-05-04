package warmup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import physics.*;

/**
 * Thread safety argument: All of the components of the edge of the board are final and immutable;
 * The monitor pattern will be implemented for collisions that occur within the board. Board itself is
 * a mutable datatype, and its mutability is protected by synchronization on collision events and the finality
 * of its components.
 * @author nwallace
 *
 */

/*THINGS TO ADD AFTER WARM-UP SUBMISSION: ArrayList of Balls in board, HashMap of {LineSegment wall: Board adjacentBoard} 
pairs, change walls to OuterWall objects*/

public class BoardWarmup {
    //create the walls
    private final LineSegment topWall = new LineSegment(new Vect(-0.05,-0.05), new Vect(20.05, -0.05));
    private final LineSegment bottomWall = new LineSegment(new Vect(-0.05,20.05), new Vect(20.05, 20.05));
    private final LineSegment leftWall = new LineSegment(new Vect(-0.05,-0.05), new Vect(-0.05, 20.05));
    private final LineSegment rightWall = new LineSegment(new Vect(20.05,-0.05), new Vect(20.05, 20.05));
    final List<LineSegment> walls = new ArrayList<LineSegment>(Arrays.asList(topWall, bottomWall, leftWall, rightWall));
    
    //add in the corners
    private final Circle topLeftCorner = new Circle(-0.05, -0.05, 0);
    private final Circle topRightCorner = new Circle(20.05, -0.05, 0);
    private final Circle bottomLeftCorner = new Circle(-0.05, 20.05, 0);
    private final Circle bottomRightCorner = new Circle(20.05, 20.05, 0);
    final List<Circle> corners = new ArrayList<Circle>(Arrays.asList(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner));

    private String[][] board = new String[20][20]; //row by column
    int ballXPos;
    int ballYPos;
    
    /**
     * Construct a new 20 by 20 inner board area without walls
     * Empty spots are represented by " "
     * Ball is represented by "*"
     * 
     * @param ball where 20 > x and y position >= 0
     */
    public BoardWarmup(BallWarmup ball) {
        //initialize the inner board
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                board[y][x] = " ";
            }
        }
        
        //set the ball on the board
        ballXPos = (int) ball.getX();
        ballYPos = (int) ball.getY();
        board[ballYPos][ballXPos] = ball.toString();
    }
    
    /**
     * Create string representation of a board
     */
    @Override
    public String toString() {
        String boardToString = new String();
        
        //add the top wall
        for (int i = -1; i < 21; i++) {
            boardToString += ".";
        }
        boardToString += "\n";


        //create the inner with left and right wall
        for (int x = 0; x < 20; x++) {
            boardToString += ".";
            for (int y = 0; y < 20; y++) {
                boardToString += board[y][x];
            }
            boardToString += ".";
            boardToString += "\n";
        }
        
        //add the bottom wall
        for (int i = -1; i < 21; i++) {
            boardToString += ".";
        }
        return boardToString;
    }
    
    
    /**Method to be called every time the board is printed; changes position of the ball
     * 
     * @param ball Ball object that represents the new position of the ball
     */
    public void update(BallWarmup ball){
        //remove the old ball from the array and change it to a space
        board[ballYPos][ballXPos] = " ";
        
        //update the position of the ball
        ball.updateBallPosition();
        ballXPos = (int) ball.getX();
        ballYPos = (int) ball.getY();
        
        board[ballYPos][ballXPos]="*";
    }

    }

