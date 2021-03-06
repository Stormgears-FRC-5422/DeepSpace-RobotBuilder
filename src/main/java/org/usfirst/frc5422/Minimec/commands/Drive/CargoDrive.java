// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5422.Minimec.commands.Drive;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.subsystems.PixyVision;
import org.usfirst.frc5422.utils.StormProp;

/**
 *
 */
public class CargoDrive extends Command {

    Joystick joy;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public CargoDrive() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if (Robot.useDrive) joy = Robot.oi.getJoystick();
        Robot.pixyVision.enable(PixyVision.VisionMode.CARGO);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Robot.useDrive) {
            double joy_vals[] = Robot.oi.getJoyXYZ(joy);
            double x = joy_vals[0];
            double y = joy_vals[1];
            double z = joy_vals[2];

            if (joy.getRawButton(5) == false) {
                // Button 5 will disable PID input to drive
                z += Robot.pixyVision.get_pid_output();
            } else {
                Robot.pixyVision.clearLastTracked();
            } 

            if (z > 1.0) z  = 1.0;
            if (z < -1.0) z = -1.0;


            Robot.drive.driveArcade(x, y, z);
        }
        SmartDashboard.putNumber("PixyVisionPidOut",Robot.pixyVision.get_pid_output());
    }
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.pixyVision.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.pixyVision.disable();
    }
}
