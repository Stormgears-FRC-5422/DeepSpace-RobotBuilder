// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5422.Minimec.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Drive.JoyDrive;
import org.usfirst.frc5422.utils.StormProp;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Drive extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX leftFront;
    private WPI_TalonSRX rightFront;
    private WPI_TalonSRX leftBack;
    private WPI_TalonSRX rightBack;
    private MecanumDrive mecanumDrive1;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Drive() {
        if (Robot.useDrive) {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
            leftFront = new WPI_TalonSRX(StormProp.getInt("frontLeftTalonId"));



            rightFront = new WPI_TalonSRX(StormProp.getInt("frontRightTalonId"));



            leftBack = new WPI_TalonSRX(StormProp.getInt("rearLeftTalonId"));



            rightBack = new WPI_TalonSRX(StormProp.getInt("rearRightTalonId"));
            
            
            
            mecanumDrive1 = new MecanumDrive(leftFront, leftBack,
                    rightFront, rightBack);
            addChild("Mecanum Drive 1",mecanumDrive1);
            mecanumDrive1.setSafetyEnabled(true);
            mecanumDrive1.setExpiration(0.1);
            mecanumDrive1.setMaxOutput(1.0);


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        }
    
      //  rightFront.setInverted(true);
      //  rightBack.setInverted(true);
    }

 
    public void driveArcade(double x, double y, double z) {
        if (Robot.useDrive) mecanumDrive1.driveCartesian(x,  y, z);
    }   

    public void driveArcadeDeRate(double x, double y, double z,double rate) {
        if (rate > 1) {
            driveArcade(x, y, z);
        } 
        else {
            if (Robot.useDrive) mecanumDrive1.driveCartesian(rate * x,  rate * y, rate *z);
        }
    }   

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new JoyDrive());

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

}

