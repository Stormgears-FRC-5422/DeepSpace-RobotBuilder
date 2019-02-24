// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5422.Minimec.subsystems.pneumatics;


import edu.wpi.first.wpilibj.Solenoid;
import org.usfirst.frc5422.Minimec.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.Compressor;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */

public class ValveControl extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private Solenoid ballValve;
    private Solenoid hatchValve;
    private Solenoid armValve;
    private Solenoid vacValve;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public ValveControl() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        ballValve = new Solenoid(11, 7);
        ballValve.set(false);
        addChild("BALL_VALVE",ballValve);


        hatchValve = new Solenoid(11, 6);
        hatchValve.set(false);
        addChild("HATCH_VALVE",hatchValve);


        armValve = new Solenoid(11, 0);
        armValve.set(false);
        addChild("ARM_VALVE",armValve);


        vacValve = new Solenoid(11, 1);
        vacValve.set(false);
        addChild("VAC_VALVE",vacValve);


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void ballStart() {
        System.out.println("Single Solenoid before set value = " + ballValve.get());
        ballValve.set(true);
        System.out.println("Single Solenoid after set value = " + ballValve.get());
    }

    public void ballStop(){
        ballValve.set(false);
    }

    public void hatchStart() {
        System.out.println("Single Solenoid before set value = " + hatchValve.get());
        hatchValve.set(true);
        System.out.println("Single Solenoid after set value = " + hatchValve.get());
    }

    public boolean getBallValve() {
        return ballValve.get();
    }

    public boolean getHatchValve() {
        return hatchValve.get();
    }
    public boolean getVacValve() {
        return vacValve.get();
    }
    public boolean getArmValve() {
        return armValve.get();
    }

    public void hatchStop(){
        hatchValve.set(false);
    }

    public void armStart() {
        System.out.println("Single Solenoid before set value = " + armValve.get());
        armValve.set(true);
        System.out.println("Single Solenoid after set value = " + armValve.get());
    }

    public void armStop(){
        armValve.set(false);
    }

    public void vacStart() {
        System.out.println("Single Solenoid before set value = " + vacValve.get());
        vacValve.set(true);
        System.out.println("Single Solenoid after set value = " + vacValve.get());
    }

    public void vacStop(){
        vacValve.set(false);
    }
}
