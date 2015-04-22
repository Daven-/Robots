/**
 * *****************************************************************************
 * RoboNewbie NaoTeam Humboldt
 *
 * @author Monika Domanska
 * @version 1.1
 * *****************************************************************************
 */
package lavidarobots;

import examples.*;
import agentIO.EffectorOutput;
import agentIO.PerceptorInput;
import agentIO.ServerCommunication;
import util.Logger;
import util.RobotConsts;

/**
 * This agent shows basic concepts of using the RoboNewbie framework and gives
 * examples for interacting with the simulation server and using the classes
 * EffectorOutput and PerceptorInput.
 */
public class Agent_BasicStructure {

    // how long should the robot be simulated for in seconds
    private static final int iSimulationRunTime = 5;
    // velocity of joint motor in radians per second
    private static final double dMotorVelocity = 1;

    private static final int initialPause = 25;

    // switch case action id
    int iActionId = 0;

    public static void main(String args[]) {

        // Change here the class to the name of your own agent file 
        // - otherwise Java will always execute the Agent_BasicStructure.
        Agent_BasicStructure agent = new Agent_BasicStructure();

        // Establish the connection to the server.
        agent.init();

        // Run the agent program synchronized with the server cycle.
        // Parameter: Time in seconds the agent program will run. 
        agent.run(iSimulationRunTime);

        // The logged informations are printed here, when the agent is not timed 
        // with the server anymore. Printing immediately when informations are 
        // gained during the server cycles could slow down the agent and impede 
        // the synchronization.
        agent.printlog();
        System.out.println("Agent stopped.");
    }

    private Logger log;
    private PerceptorInput percIn;
    private EffectorOutput effOut;

    /**
     * A player is identified in the server by its player ID and its team name.
     * There are at most two teams an the field, and every agent of a single
     * team must have a unique player ID between 1 and 11. If the identification
     * works right, it is visualized in the monitor: the robots on the field
     * have either red or blue parts. An unidentified robot has grey parts.
     */
    static final String id = "1";
    static final String team = "myT";
    /**
     * The "beam"-coordinates specify the robots initial position on the field.
     * The root of the global field coordinate system is in the middle of the
     * field, the system is right-handed. The x-axis points to the opponent
     * goal, so the initial position must have a negative x-value. The robot can
     * be placed with an initial orientation given in the variable beamRot, in
     * degrees, counterclockwise relative to the x-axis.
     */
    static final double beamX = 5;
    static final double beamY = 0;
    static final double beamRot = 0;

    /**
     * Initialize the connection to the server, the internal used classes and
     * their relations to each other, and create the robot at a specified
     * position on the field.
     */
    private void init() {

        // connection to the server
        ServerCommunication sc = new ServerCommunication();

        // internal agent classes
        log = new Logger();
        percIn = new PerceptorInput(sc);
        effOut = new EffectorOutput(sc);

        // simulated robot hardware on the soccer field
        sc.initRobot(id, team, beamX, beamY, beamRot);
    }

    /**
     * Main loop of the agent program, where it is synchronized with the
     * simulation server.
     *
     * @param timeInSec Time in seconds the agent program will run.
     */
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

            // "Think":
            // Use the perceptor data (simulated sensory data, here gained by percIn) 
            // to control the effectors (simulated motors, here activated by effOut) 
            // accordingly.
            //moveJointTo(RobotConsts.RightHipPitch, 30, true);
            //moveJointTo(RobotConsts.LeftHipPitch, 30, true);
            //moveJointTo(RobotConsts.LeftKneePitch, 20, true);
            //if(moveJointTo(RobotConsts.RightHipYawPitch, -20, false)){
            //  moveJointTo(RobotConsts.RightKneePitch, -30, false);
            //}
            /*moveJointTo(RobotConsts.RightHipYawPitch, -20, false);
             if(moveJointTo(RobotConsts.RightKneePitch, -30, false)){
             moveJointTo(RobotConsts.LeftHipPitch, -1, false);
             }*/
            moveJointTo(RobotConsts.LeftHipYawPitch, -30);
            moveJointTo(RobotConsts.RightHipYawPitch, -30);
            moveJointTo(RobotConsts.RightFootPitch, 35);
            moveJointTo(RobotConsts.LeftFootPitch, 35);

            // move right knee up and left foot down
            //moveJointTo(RobotConsts.LeftKneePitch, -20);
            //moveJointTo(RobotConsts.LeftFootPitch, 35);
            //moveJointTo(RobotConsts.RightFootPitch, 75, true);
            //moveJointTo(RobotConsts.RightKneePitch, -40, false);
            //armSwing(RobotConsts.LeftHipPitch, -2, 2);
            //armSwing(RobotConsts.RightHipPitch, -2, 2);
            // "Hardware" access to the effectors (simulated motors).
            act();
        }
    }

    /**
     * Update the world and robot hardware informations, that means process
     * perceptor values provided by the server.
     *
     * Here is listed a simple sequence of method calls, which are executed in
     * every server cycle 1) to synchronize perceptor processing classes with
     * the loop of the simulation server 2) to ensure that the agent gets the
     * actual informations about the robot and the soccer field from the
     * perceptors (simulated sensors).
     */
    private void sense() {
        // Receive the server message and parse it to get the perceptor values. 
        percIn.update();
    }

    /**
     * Move the robot hardware, that means send effector commands to the server.
     *
     * Here is listed a simple sequence of method calls, which are executed in
     * every server cycle 1) to calculate the effector commands, if needed. 2)
     * to send the effector commands to the server regularly in every server
     * cycle.
     *
     * Notice: At least the "syn" effector has to be sent in every server cycle.
     * Look up "agent sync mode" for details.
     */
    private void act() {
        // Send agent message with effector commands to the server.
        effOut.sendAgentMessage();
    }

    /**
     * Print logged informations.
     */
    private void printlog() {
        log.printLog();
    }
    /*
     righShoulderPitch -120-120
     RightshoulderYaw -95-1
        
     rightArmRoll -120-120
     RightArmYaw -1-90
        
     rightHipYawPitch -90-1
     rightHipRoll -45-25
     rightHip Pitch -25-100
        
     rightKneePitch -130-1
     rightFootPitch -45-75
     rightFootRoll -25-45
        
     leftShoulderPitch -120-120
     leftshoulderYaw -1-95
        
     leftArmRoll -120-120
     leftArmYaw -90-1
        
     leftHipYawPitch -90-1
     leftHipRoll -25-45
     leftHip Pitch -25-100
        
     leftKneePitch -130-1
     leftFootPitch -45-75
     leftFootRoll -45-25
     */

    private void armSwing(int joint, int wayPoint1, int wayPoint2) {
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

        //log.log("reached angle: " + degree);
        //log.printLog(); // print log data instantly 
    }

    public void setAction(int id, double velocity) {
        effOut.setJointCommand(id, velocity);
    }

}
