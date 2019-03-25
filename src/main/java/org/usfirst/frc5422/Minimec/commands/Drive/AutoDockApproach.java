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
import org.usfirst.frc5422.Minimec.PixyObject;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.subsystems.PixyVision;
import org.usfirst.frc5422.Minimec.subsystems.NavX;
import org.usfirst.frc5422.utils.StormProp;

/**
 *
 */
public class AutoDockApproach extends Command {

    private Joystick joy;
    private double m_target_distance = 6;
    private double m_max_dist = 30;
    private Boolean m_rumble_left = false;
    private Boolean m_pause = false;
    private Boolean m_ship_mode = true;  // alignment for cargo ship only (90 degress)

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public AutoDockApproach() {

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
        if (Robot.useDrive) {
            joy = Robot.oi.getJoystick();
        }
        set_navx_target();
        Robot.drive.setBrakeMode();
        Robot.pixyVision.set_strafe_mode();
        Robot.pixyVision.enable(PixyVision.VisionMode.DOCK);
    }

    private void set_navx_target() {
        if (Robot.navX.is_enabled()) Robot.navX.disable();
        if (m_ship_mode) {
            Robot.navX.enable(Robot.navX.align_to_closest(90));
        } else {
            Robot.navX.enable(Robot.navX.align_to_closest(30));
        }
    }
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Robot.useDrive) {
            double distance = Robot.pixyVision.getDockDistance();


            double derate = 1;
	        if (Robot.oi.getPrecisionDrive()) {
                derate = .3;
            }
            double joy_vals[] = Robot.oi.getJoyXYZ(joy,derate);

            double x = joy_vals[0];
            double y = joy_vals[1];
            double z = joy_vals[2];

            // Detect and make mode changes
            if (m_ship_mode) {
                if (joy.getRawButton(8)) {
                    m_ship_mode = false;
                    set_navx_target();
                }
            }
            else {
                if (joy.getRawButton(7)) {
                    m_ship_mode = true;
                    set_navx_target();
                }
            }

            // Temporary disable
            if (joy.getRawButton(5) == false) {
                if (m_pause) set_navx_target();
                m_pause = false;

                // Button 5 will disable PID input to drive
                x += .65 * Robot.pixyVision.get_pid_output(); // vision controls strafing
                z += Robot.navX.get_pid_output();  // NavX controls turning
            } else {
                Robot.pixyVision.clearLastTracked();
                m_pause = true;
            } 
            if (z > 1.0) z  = 1.0;
            if (z < -1.0) z = -1.0;

            if (x > 1.0) x  = 1.0;
            if (x < -1.0) x = -1.0;


            // Check for dock mode changes
            if (joy.getRawButton(3)) {
                Robot.pixyVision.changeDockMode(PixyVision.DockSelection.LEFT);
            }
            if (joy.getRawButton(4)) {
                Robot.pixyVision.changeDockMode(PixyVision.DockSelection.MIDDLE);
            }
            if (joy.getRawButton(2)) {
                Robot.pixyVision.changeDockMode(PixyVision.DockSelection.RIGHT);
            }

            if (distance > 0 && distance < (m_target_distance + m_max_dist)) {
                SmartDashboard.putNumber("PixyVisionDistance",distance);
                // Modulate driver forward input
                if (y > 0) {
                    if (!m_rumble_left) {
                        set_rumble(true);
                        m_rumble_left = true;
                    }
                    y = y * (distance - m_target_distance)/m_max_dist ;
                    if (y<0) y = 0;
                } else {
                    set_rumble(true);
                }
            } else {
                set_rumble(false);
            }


            Robot.drive.driveArcade(x,y, z);
        }
        SmartDashboard.putNumber("PixyVisionPidOut",Robot.pixyVision.get_pid_output());
    }

    private void set_rumble(Boolean enable) {
        if (enable && !m_rumble_left) {
            joy.setRumble(RumbleType.kLeftRumble, .5);
            m_rumble_left = true;
        }
        if (!enable && m_rumble_left) {
            joy.setRumble(RumbleType.kLeftRumble, 0);
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
        Robot.navX.disable();
        Robot.pixyVision.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.drive.setCoastMode();
        Robot.navX.disable();
        Robot.pixyVision.disable();
    }
}
