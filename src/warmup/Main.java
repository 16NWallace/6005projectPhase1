package warmup;
import java.util.Timer;
import java.util.TimerTask;

import physics.*;

/**
 * TODO: put documentation for your class here
 */
public class Main {
    
    /**
     * TODO: describe your main function's command line arguments here
     */
    
    private static BallWarmup ball;
    private static BoardWarmup board;
    public static void main(String[] args) {

        /**
         * PsuedoCode:
         * 
         * create a ball with position and velocity
         * create a board
         * create a timer
         * 
         * time step (java.util.Timer, scheduleAtFixedRate):
         *      check if colliding with a wall or corner
         *      if colliding:
         *          call reflect on appropriate wall/corner
         *      update ball
         *      update board
         */
        
        ball = new BallWarmup(5,5, new Vect(20, 20));
        board = new BoardWarmup(ball);
        
        Timer timer = new Timer();
        PlayTask play = new PlayTask();
        timer.scheduleAtFixedRate(play, 0, 100);
    }
    
    
    static class PlayTask extends TimerTask {

        @Override
        public void run() {
            //check if the ball is colliding with a wall
            for (LineSegment wall : board.walls){
                double collisionTime = Geometry.timeUntilWallCollision(wall, ball.getBall(), ball.getVelocity());
                //if it is colliding

                if (-0.05 < collisionTime && collisionTime < 0.05) {

                    //get the new velocity from the reflection
                    Vect newVel = Geometry.reflectWall(wall, ball.getVelocity());
                    //create a new ball with new velocity
                    ball = new BallWarmup(ball.getX(), ball.getY(), newVel);
                    break;
                }
            }
            
            //check if the ball is colliding with a corner
            for (Circle corner : board.corners) {
                double collisionTime = Geometry.timeUntilCircleCollision(corner, ball.getBall(), ball.getVelocity());
                //if it is colliding
                if (-0.05 < collisionTime && collisionTime < 0.05) {
                    //get the new velocity from the reflection
                    Vect newVel = Geometry.reflectCircle(corner.getCenter(), ball.getPosition(), ball.getVelocity());
                    //create a new ball with new velocity
                    ball = new BallWarmup(ball.getX(), ball.getY(), newVel);
                    break;
                }
            }
            
            //update the ball
            ball = ball.updateBallPosition();
            //update the board
            board.update(ball);
            
            System.out.println(board);
        }
    }
    
}