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

import java.time.Year;

import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StormProp;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DockCloseDrive extends Command {
    private static DockCloseDrive instance;
    private static double m_distance_scale_factor = 100;  // Used determine at what distance from target to start de-rating joystick input
    private static double m_target_distance = 40;
    private static double m_forward_speed_limit = .35;
//    public static DockCloseDrive getInstance() {
//        if(instance == null) instance = new DockCloseDrive();
//        return instance;
//    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public DockCloseDrive() {
        
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
            joy = Robot.oi.getJoystick();
        }
        Robot.drive.setBrakeMode();

    }
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.tapeAlignSys.enable();
        Robot.lidarAlignSys.enable();
        if (Robot.useDrive) {
            //driveCartesian(oi.getJoystick().getRawAxis(0)*-1,
            // (oi.getJoystick().getRawAxis(3)-oi.getJoystick().getRawAxis(2))*-1,
            // oi.getJoystick().getRawAxis(4));
            double joy_vals[] = Robot.oi.getJoyXYZ(joy);
            double x = joy_vals[0];
            double y = joy_vals[1] * m_forward_speed_limit;
            double z = joy_vals[2] * .5;

            if (!joy.getRawButton(5)) { // Button 5 is pid override
                // Get  sensor feedback for strafe
                x = .5 * x + Robot.tapeAlignSys.get_pid_output();
                if (x > 1) { x = 1; }
                if (x < -1) { x= -1; }

                // Get Lidar alignment for Z axis
                z = z + Robot.lidarAlignSys.get_pid_output();
                if (z > 1) { z = 1; }
                if (z < -1) { z= -1; }

                double distance = Robot.stormNetSubsystem.getLidarDistance();
                SmartDashboard.putNumber("Lidar distance (cm)",distance);

                if (distance < m_distance_scale_factor) {
                    // Modulate driver forward input
                    if (y > 0 && distance < (m_target_distance + m_distance_scale_factor)) {
                        y = y * ((distance - m_target_distance)/(m_distance_scale_factor)) ;
                        if (y<0) y = 0;
                    }   
                }	
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
        Robot.drive.setCoastMode();
        Robot.tapeAlignSys.disable();
        Robot.lidarAlignSys.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.drive.setCoastMode();
        Robot.tapeAlignSys.disable();
        Robot.lidarAlignSys.disable();
    }
}
