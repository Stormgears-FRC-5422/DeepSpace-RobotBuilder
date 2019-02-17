// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5422.Minimec.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;


import org.usfirst.frc5422.Minimec.Robot;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 *
 */
public class PixyCargoDrive extends PIDCommand {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    private NetworkTable m_vision_table;
    private Joystick joy;
    double m_centerX[];
    double m_centerY[];
    double m_height[];
    double m_width[];
    double m_pid_out;
    String m_type[];

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public PixyCargoDrive() {
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("PixyCargoDrive", 0.01, 0.0, 0.0, 0.01);
        getPIDController().setContinuous(false);
        getPIDController().setAbsoluteTolerance(0.03);
        getPIDController().setOutputRange(-1.0, 1.0);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.drive);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        m_vision_table = inst.getTable("vision");

        m_pid_out = 0.0;
    }

    @Override
    protected double returnPIDInput() {
        double[] default_data = new double[0];
        m_centerX = m_vision_table.getEntry("centerX").getDoubleArray(default_data);
        m_centerY = m_vision_table.getEntry("centerY").getDoubleArray(default_data);
        m_height = m_vision_table.getEntry("height").getDoubleArray(default_data);
        m_width = m_vision_table.getEntry("width").getDoubleArray(default_data);
        m_type = m_vision_table.getEntry("Type").getStringArray(new String[0]);

        
        if (m_centerX != null && m_centerX.length > 0 &&
            m_type[0].equals("Cargo")) {
            SmartDashboard.putString("PixyType",m_type[0]);
            SmartDashboard.putNumber("PixyCenterX",m_centerX[0]);
            return(160 - m_centerX[0]);
        } else {
            SmartDashboard.putString("PixyType","Not Tracking");
            return(0.0);
        }


        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
    }

    @Override
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        m_pid_out = output;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
         joy = Robot.oi.getJoystick1();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double null_size = .1;
        double rate = 0.5 + (joy.getRawAxis(3) * .5);
        double z_out = joy.getZ();
        if (Math.abs(z_out) < null_size) z_out = 0;

        z_out = rate * joy.getZ();
        if (joy.getRawButton(5) == false) {
            // Button 5 will disable PID input to drive
            z_out += m_pid_out;
        }  
        if (z_out > 1.0) z_out  = 1.0;
        if (z_out < -1.0) z_out = -1.0;

        double x_out = rate * joy.getX();
        double y_out = rate * -1 * joy.getY();

        // Create a joystick null zone to reduce drift
        if (Math.abs(x_out) < null_size) x_out = 0;
        if (Math.abs(y_out) < null_size) y_out = 0;

        SmartDashboard.putNumber("PixyCargoPidOut",m_pid_out);
        Robot.drive.driveArcade(x_out,y_out, z_out);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}