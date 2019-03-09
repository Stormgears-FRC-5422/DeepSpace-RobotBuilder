package org.usfirst.frc5422.Minimec.subsystems.backjack;

// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.Spark;
import org.usfirst.frc5422.Minimec.Robot;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Backjack extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private Spark jackSpark;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private DigitalOutput jackDIO;
    private int nextLevel;
    private boolean moving = false;

    public Backjack() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        jackSpark = new Spark(0);  // TODO magic number
        addChild("JackSpark",jackSpark);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        jackSpark.setInverted(false);
        jackDIO = new DigitalOutput(0);  // TODO magic number
        nextLevel = 0;  // default to retract. Probably need to move() at startup TODO
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        // setDefaultCommand(new SetJackLevel(0));

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    }
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void setSpeed(double speed) {
        jackSpark.set(speed);
    }

    public void setLevel(int habLevel) {
        System.out.println("In backjack setLevel, nextLevel = " + habLevel);
        nextLevel = habLevel;

        // The DIO controls whether or not the level 2 limit switch is activated
        jackDIO.set( habLevel == 2);  // shorthand for if (hablevel ==2 ) true else false
    }

    public void move() {
        System.out.println("In backjack move, nextLevel = " + nextLevel);
        moving = true;
        setLevel(Robot.oi.getBackJackLevel());

        if(nextLevel == 0) {
            // Retract
            jackSpark.set(-0.25);
        }
        else {
            // Extend
            jackSpark.set(1.00);
        }
    }

    public void stop() {
        jackSpark.set(0);
        moving = false;
    }
}
