<h1>Robots</h1>
Version of RoboNewbie a basic framework for experiments with simulated humanid robots.  

<h2>Contents </h2>
[Simspark User Manual](#manual)<br>
[Installation on Windows, Mac, and Linux](#windows)<br>
[Getting Started with Simspark](#started)<br>
[RoboNewbie](#robot)


<h2 id="manual">Simspark User Manual</h2>
<ul>
<li>http://simspark.sourceforge.net/wiki/index.php/Users_Manual#Wiki_Version </li>
</ul>

<h2 id="windows">Installation on windows</h2>
<ol>
<li>Install the latest MS Visual C++2008 RP http://www.microsoft.com/en-us/download/details.aspx?id=29</li>
<li>Install simspark https://www.dropbox.com/s/qpfaw9zwaik1y7y/simspark-0.2.4-win32.exe?dl=0, Install rcssserver3d https://www.dropbox.com/s/zjh8ple45s2y0q3/rcssserver3d-0.6.7-win32.exe?dl=0</li>
<li>Install Ruby if you don’t have it yet on your system http://rubyinstaller.org/ </li>
<li>Ensure that your PATH variable includes C:\Program Files\ruby\bin (From Start->Control Panel->System->Advanced->Enviroment Variable)</li>
<li>For instruction on how to install on Linux or Mac and Full Wiki at: http://simspark.sourceforge.net/wiki/index.php/Main_Page</li>
</ol>

<h2 id="started">Getting Started with Simspark</h2>
<ul>
<li>Head on over to the directory were the program was installed to: C:\Program Files (x86)\rcssserver3d 0.6.7\bin if you can’t find it there it might be in C:\Program Files \rcssserver3d 0.6.7\bin</li>
<li>Inside \bin you will find the following files which you can see in the screenshot below (I’d suggest making shortcuts on your desktop) 
</li>
<li>Open <strong>rcsoccersim3d.windowscommandscript</strong> it should open up the simulation environment </li>
<li>Use the arrows to move around the field, the mouse to look around, and left click to zoom out (full list of commands at: http://simspark.sourceforge.net/wiki/index.php/Monitor)</li>
<li>In \bin open <strong>rcssagent3d.windowscommandscript</strong> to drop a NAO robot at coordinates 0,0 (upper left corner) </li>
</ul>
![](https://github.com/Daven-/Robots/blob/master/img/bin-img.png)
<h2 id="robot">RoboNewbie - Robots</h2>
<p>“RoboNewbie is a basic framework for experiments with simulated humanoid robots. It provides interfaces to the simulated sensors and effectors of the robot, and a simple control structure. The framework and the examples are implemented in JAVA with detailed documentations and explanations. That makes it useful even for beginners in Robotics.” </p>
<strong>Getting Started with Robots</strong>
<ol>
<li>Clone or download the zip file for this repository</li>
<li>Open the project in Netbeans or create a new project with existing sources using this folder. You can download NetBeans from here: https://netbeans.org/downloads/index.html. You only need the Java SE version.</li>
<li>If you would like to follow the orginial RoboNewbie "Quick Start Tutorial" you can find it here: http://www2.informatik.hu-berlin.de/~naoth/RoboNewbie/RoboNewbieQuickStartTutorial.pdf</li>
<li>Once you have the project open in NetBeans go into <strong>/lavidarobots/</strong> and open the <strong>Agent_BasicStructure.java</strong> file or create your own copy of it.</li>
<li>Open <strong>rcsoccersim3d.windowscommandscript</strong></li>
<li>run the file by right clicking inside of it and choosing "run file" or <strong>shift-F6</strong> (if you click the regular run command in NetBeans it will run the main project class which is not agent_basicstructure or your copy of it)</li>
<li>look at the 3d environment and you should see a robot pop into existence to the left of the center circle and fall forewords.</li>
</ol>
![](https://github.com/Daven-/Robots/blob/master/img/robofall.png)
<br>

The following code is the bare minimum you need to understand in order to manipulate the NAO robot:
```java
// pass in the joint you would like to manipulate, and what degree the joint should be moved to
moveJointTo(RobotConsts.LeftFootPitch, -30); 
// here the left foot should be rotated backwards to the -30 degree mark
// the foot pitches freedom is  -45 to 75
// moveJointTo needs to be called inside the run() function
```
A stripped down version of <strong>Agent_BasicStructure.java</strong> or your copy of it
```java

    // how long should the robot be simulated for in seconds
    private static final int iSimulationRunTime = 5;
    // velocity of joint motor in radians per second
    private static final double dMotorVelocity = 1;
    // pause the simulation for the first .25 seconds
    private static final int initialPause = 25;
    
    private void run(int timeInSec) {
        // The server executes about 50 cycles per second. 
        int cycles = timeInSec * 50;

        // do nothing for 2 seconds, just stay synchronized with the server
        for (int i = 0; i < initialPause; i++) {
            sense();
            act();
        }
        // Loop synchronized with server.
        for (int i = 0; i < cycles; i++) {

            // "Hardware" access to the perceptors (simulated sensors) and processing
            // of the perceptor data. 
            sense();
            
            //make the NAO robot fall forewards
            moveJointTo(RobotConsts.LeftHipYawPitch, -30);
            moveJointTo(RobotConsts.RightHipYawPitch, -30);
            moveJointTo(RobotConsts.RightFootPitch, 35);
            moveJointTo(RobotConsts.LeftFootPitch, 35);

            // "Hardware" access to the effectors (simulated motors).
            act();
        }
    }
    
   /**
     * Loop a movement going from waypoint1 to waypoint2. 
     * @param joint
     * @param wayPoint1
     * @param wayPoint2 
     */
    private void moveJointToLoop(int joint, int wayPoint1, int wayPoint2) {
        // go to waypoint1 then to waypoint2, repeat
        double degree = Math.toDegrees(percIn.getJoint(joint));
        switch (iActionId) {
            case 0:
                setAction(joint, -dMotorVelocity);
                if (degree < wayPoint1) {
                    iActionId = 1;
                }
                break;
            case 1:
                setAction(joint, dMotorVelocity);
                if (degree > wayPoint2) {
                    iActionId = 0;
                }
                break;
        }
        log.log("reached angle: " + degree);
        //log.printLog(); // print log data instantly 
    }

    /**
     * move joint to way point
     *
     * @param joint: joint to be manipulated
     * @param wayPoint: degree the joint should be moved to
     * @param foreword: are we moving the joint foreword or backwords?
     */
    public boolean moveJointTo(int joint, int wayPoint) {
        // go to waypoint1 then to waypoint2, repeat
        double degree = Math.toDegrees(percIn.getJoint(joint));

        if (wayPoint < 0) {
            if (degree > wayPoint) {
                setAction(joint, -dMotorVelocity);
                return false;
            } else {
                setAction(joint, 0);
                return true;
            }
        } else {
            if (degree < wayPoint) {
                setAction(joint, dMotorVelocity);
                return false;
            } else {
                setAction(joint, 0);
                return true;
            }
        }

    }

```

<strong> Motion Editor </strong>
RoboNewbie comes with a motion editor which you can find here: http://www2.informatik.hu-berlin.de/~naoth/RoboNewbie/MotionEditor.zip

You will need java 3d to be able to run it: http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-client-419417.html#java3d-1.5.1-oth-JPR

You can find the tutorial for this motion editor here: http://www2.informatik.hu-berlin.de/~naoth/RoboNewbie/MotionEditor.pdf

RoboNewbies Quick Start Guide: http://www2.informatik.hu-berlin.de/~naoth/RoboNewbie/RoboNewbieQuickStartTutorial.pdf

With the motion editor it is easy to move past having to create motions and get into the AI for the robots, as the RoboNewbie team has created many of the basic movements you will need to play soccer. Still the motion editor is provided if you would like to create better or more effiecient motions. 
 
The three important classes needed in order to use the motion editor files are: 
<ul>
<li> Agent.java </li>
<li> Think.java </li>
<li> KeyframeMotion.java </li>
</ul>
You can find these files inside <strong> RoboNewbie_1.1/lavidarobots </strong>

<strong>Agent.java</strong> is the skeleton that takes care of communicating to the simulated environment, you will rarely need to change anything in here, this is the main file you need to run (shift-F6 in netbeans). 

<strong>KeyframeMotion.java</strong> is where the text files from the motion editor are read in and implemented. If you create new motions it is in this class you need to read them in: 
```java
// copy txt files into /keyframes
private static KeyframeSequence WALK_FORWARD_SEQUENCE;
WALK_FORWARD_SEQUENCE = keyframeReader.getSequenceFromFile("walk_forward-flemming-nika.txt");

 public void setWalkForward() {
    if (loggingOn) log.log("motion walk forward \n");
    actualSequence = WALK_FORWARD_SEQUENCE;
    state = MotionState.BETWEEN_FRAMES;
  }
```

<strong>Think.java</strong> is where most of the AI should go. The following behavior is provided by the RoboNewbie team. The robot simply walks towards the ball if it is inside its field of vision, else it turns left until it finds the ball.

```java
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

```

