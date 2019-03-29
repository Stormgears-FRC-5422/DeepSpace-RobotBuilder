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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

import java.time.Year;

import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StormProp;

/**
 *
 */
public class JoyDrive extends Command {
    private Boolean m_alignment_enabled = false;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public JoyDrive() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.drive);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    private Joystick joy;
    
    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if (Robot.useDrive) {
            Robot.drive.setBrakeMode();
            joy = Robot.oi.getJoystick();
        }
        m_alignment_enabled = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        //driveCartesian(oi.getJoystick().getRawAxis(0)*-1,
        // (oi.getJoystick().getRawAxis(3)-oi.getJoystick().getRawAxis(2))*-1,
        // oi.getJoystick().getRawAxis(4));
        if (Robot.useDrive) {

            double joy_vals[] = Robot.oi.getJoyXYZ(joy);  // 
            double x = joy_vals[0];
            double y = joy_vals[1];
            double z = joy_vals[2];

            // momentary tape alignment 
            Boolean align90 = joy.getRawButton(5);
            Boolean align30 = joy.getRawButton(6);
            if (align90 || align30) { // Button 5/6 is pid override
                if (!m_alignment_enabled) {
                    Robot.tapeAlignSys.enable();
                    if (!Robot.navX.is_enabled()) { 
                        if (align90) Robot.navX.enable(Robot.navX.align_to_closest(90));
                        if (align30) Robot.navX.enable(Robot.navX.align_to_closest(30));
                        m_alignment_enabled = true;
                    }   
                }
            } else  if (m_alignment_enabled) {
                Robot.tapeAlignSys.disable();
                Robot.navX.disable();
                m_alignment_enabled = false;
                joy.setRumble(RumbleType.kLeftRumble, 0);
            }

            if (m_alignment_enabled) {
                x = x*.5; // derate drive joystick for precision
                y = y*.5; // derate drive joystick for precision
                z = z*.5; // derate drive joystick for precision
                // Get  sensor feedback for strafe
                double tape_pid_output = Robot.tapeAlignSys.get_pid_output();
                if (tape_pid_output == 0) {
                    joy.setRumble(RumbleType.kLeftRumble, .25);
                } else {
                    joy.setRumble(RumbleType.kLeftRumble, 0);
                }
                x += tape_pid_output;
                if (x > 1) { x = 1; }
                if (x < -1) { x= -1; }

                double navx_pid_output = Robot.navX.get_pid_output();

                z += navx_pid_output;
                if (z > 1) { z = 1; }
                if (z < -1) { z= -1; }
            }

            Robot.drive.driveArcade(x,y,z);
        }
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
