/*******************************************************************************
*  RoboNewbie
* NaoTeam Humboldt
* @author Monika Domanska
* @version 1.1
*******************************************************************************/
package lavidarobots;

import keyframeMotion.KeyframeMotion;
import localFieldView.BallModel;
import localFieldView.LocalFieldView;

/**
 *
 * @author Daven
 */
public class Think {
     private static final double TOLERANCE_ANGLE = Math.toRadians(30);
  
  private KeyframeMotion motion;
  private BallModel ball;
  
  private boolean robotIsWalking = false;

  /**
   * Constructor. 
   * 
   * @param localView Has to be already initialized. 
   * @param motion Has to be already initialized. 
   */
  public Think(LocalFieldView localView, KeyframeMotion motion) {
    this.ball = localView.getBall();
    this.motion = motion;
  }
    
  /**
   * Decide, whether the robot should walk or turn to reach the ball and set the
   * chosen movement. 
   * If the ball is in front of the robot, it can just walk forward, else it 
   * should turn left, and check its position relative to the ball again. 
   */
  public void decide() {
    
    // Take care not to interrupt an actually executed movement.
    // This has to be checked always when using class KeyframeMotion. 
    if (motion.ready()) {

      // If the ball lies in front of the robot, walk towards it. 
      if (ball.isInFOVnow()
          && (Math.abs(ball.getCoords().getAlpha())) < TOLERANCE_ANGLE) {
        motion.setWalkForward();
        robotIsWalking = true;
      }else if (robotIsWalking) {
        motion.setStopWalking();
        robotIsWalking = false;
      } else {
        motion.setTurnLeft();
      }
    }
    
  }
}
