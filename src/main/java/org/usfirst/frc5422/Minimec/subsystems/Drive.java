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


import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.NeutralMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private ShuffleboardTab tab = Shuffleboard.getTab("Drive");
    private ShuffleboardTab match_tab = Shuffleboard.getTab("Match Tab");
    private NetworkTableEntry match_precision = match_tab.add("Precision Mode", false).getEntry();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Drive() {
        if (Robot.useDrive) {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
            Shuffleboard.selectTab("Drive");
            leftFront = new WPI_TalonSRX(StormProp.getInt("frontLeftTalonId",-1));



            rightFront = new WPI_TalonSRX(StormProp.getInt("frontRightTalonId",-1));



            leftBack = new WPI_TalonSRX(StormProp.getInt("rearLeftTalonId",-1));



            rightBack = new WPI_TalonSRX(StormProp.getInt("rearRightTalonId",-1));
            
            leftFront.setNeutralMode(NeutralMode.Brake);
            leftBack.setNeutralMode(NeutralMode.Brake);
            rightBack.setNeutralMode(NeutralMode.Brake);
            rightFront.setNeutralMode(NeutralMode.Brake);


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

    public void setCoastMode()  {
        leftFront.setNeutralMode(NeutralMode.Coast);
        rightFront.setNeutralMode(NeutralMode.Coast);
        leftBack.setNeutralMode(NeutralMode.Coast);
        rightBack.setNeutralMode(NeutralMode.Coast);
    }
 
    public void setBrakeMode() {
        leftFront.setNeutralMode(NeutralMode.Brake);
        rightFront.setNeutralMode(NeutralMode.Brake);
        leftBack.setNeutralMode(NeutralMode.Brake);
        rightBack.setNeutralMode(NeutralMode.Brake);

    
    }
    public void driveArcade(double x, double y, double z) {
        if (Robot.useDrive) mecanumDrive1.driveCartesian(x,  y, z);
        SmartDashboard.putNumber("Front Left Talon Output: ", leftFront.getSensorCollection().getQuadratureVelocity());
        SmartDashboard.putNumber("Front Right Talon Output: ", rightFront.getSensorCollection().getQuadratureVelocity());
        SmartDashboard.putNumber("Back Left Talon Output: ", leftBack.getSensorCollection().getQuadratureVelocity());
        SmartDashboard.putNumber("Back Right Talon Output: ", rightBack.getSensorCollection().getQuadratureVelocity());
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

        match_precision.setBoolean(Robot.oi.getPrecisionDrive());
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

