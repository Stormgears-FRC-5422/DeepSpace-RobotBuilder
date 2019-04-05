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
import org.usfirst.frc5422.Minimec.subsystems.TapeAlign;
import org.usfirst.frc5422.Minimec.subsystems.PixyVision.DockSelection;
import org.usfirst.frc5422.Minimec.subsystems.PixyVision.VisionMode;
import org.usfirst.frc5422.utils.StormProp;

import javax.swing.plaf.synth.SynthToolTipUI;
/**
 *
 */
public class AutoDockApproach extends Command {

    private Joystick joy;
    private double m_target_distance = 7.9;
    private double m_max_dist = 40;
    private Boolean m_rumble_left = false;
    private Boolean m_pause = false;
    private Boolean m_ship_mode = true;  // alignment for cargo ship only (90 degress)
    private CargoPosition m_dock_select = CargoPosition.middle;
    public enum RocketSide {
        left, right
    }
    public enum CargoPosition {
        left, middle, right
    }

    private RocketSide m_rocketside = RocketSide.left;

    private final double m_approach_speed;
    private final double m_approach_cutover_speed;
    private final double m_approach_kp;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public AutoDockApproach() {
        m_approach_speed = StormProp.getNumber("dockApproachSpeed");
        m_approach_cutover_speed = StormProp.getNumber("dockApproachCutoverSpeed");
        m_approach_kp = StormProp.getNumber("dockApproachKP");


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.drive);
        m_ship_mode = true;
    }
    public AutoDockApproach(RocketSide side){
        this();
        m_rocketside = side;
        m_dock_select = CargoPosition.middle;
        m_ship_mode = false;
    }

    public AutoDockApproach(CargoPosition dock){
        this();
        m_dock_select = dock;
        m_ship_mode = true;
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
        if      (m_dock_select == CargoPosition.left) Robot.pixyVision.changeDockMode(DockSelection.LEFT);
        else if (m_dock_select == CargoPosition.middle) Robot.pixyVision.changeDockMode(DockSelection.MIDDLE);
        else if (m_dock_select == CargoPosition.right) Robot.pixyVision.changeDockMode(DockSelection.RIGHT);
        Robot.pixyVision.enable(VisionMode.DOCK);
    }

    private void set_navx_target() {
        if (Robot.navX.is_enabled()) Robot.navX.disable();
        if (m_ship_mode) {
            Robot.navX.enable(Robot.navX.align_to_closest(90));
        } else {
            double cur_heading = Robot.navX.getHeading();
            double rocket_heading = 30;
            if (cur_heading >= 90 && cur_heading <=270){
                if (m_rocketside == RocketSide.left){
                    m_dock_select = CargoPosition.right;
                    rocket_heading = 210;
                } else {
                    m_dock_select = CargoPosition.left;
                    rocket_heading = 150;
                }
            } else {
                if (m_rocketside == RocketSide.left){
                    m_dock_select = CargoPosition.left;
                    rocket_heading = 330;
                } else {
                    m_dock_select = CargoPosition.right;
                    rocket_heading = 30;
                }
            }
            if      (m_dock_select == CargoPosition.left) Robot.pixyVision.changeDockMode(DockSelection.LEFT);
            else if (m_dock_select == CargoPosition.middle) Robot.pixyVision.changeDockMode(DockSelection.MIDDLE);
            else if (m_dock_select == CargoPosition.right) Robot.pixyVision.changeDockMode(DockSelection.RIGHT);
            Robot.navX.enable(rocket_heading);
        }
    }
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Robot.useDrive) {
            double distance = Robot.pixyVision.getDockDistance();


            double joy_vals[] = Robot.oi.getJoyXYZ(joy);

            double x = joy_vals[0];
            double y = joy_vals[1];
            double z = joy_vals[2];

            // Detect and make mode changes
            if (m_ship_mode) {
                if (joy.getRawButton(8)) {
                    m_ship_mode = false;
                    set_navx_target();
                }
            } else {
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
                if(Robot.tapeAlignSys.tapeDetected()){
                    Robot.tapeAlignSys.enable();
                    x += Robot.tapeAlignSys.get_pid_output();
                }
                else{
                    x += .65 * Robot.pixyVision.get_pid_output(); // vision controls strafing
                }
                z += Robot.navX.get_pid_output();  // NavX controls turning
            } else {
                Robot.pixyVision.clearLastTracked();
                m_pause = true;
            }
            if (z > 1.0) z = 1.0;
            if (z < -1.0) z = -1.0;

            if (x > 1.0) x = 1.0;
            if (x < -1.0) x = -1.0;


            // // Check for dock mode changes - these buttons now just change the command that is running
            // if (joy.getRawButton(3)) {
            //     Robot.pixyVision.changeDockMode(PixyVision.DockSelection.LEFT);
            // }
            // if (joy.getRawButton(4)) {
            //     Robot.pixyVision.changeDockMode(PixyVision.DockSelection.MIDDLE);
            // }
            // if (joy.getRawButton(2)) {
            //     Robot.pixyVision.changeDockMode(PixyVision.DockSelection.RIGHT);
	    //            }

            if (distance > 0 && distance < (m_target_distance + m_max_dist)) {
                SmartDashboard.putNumber("PixyVisionDistance", distance);
                // Modulate driver forward input
                if (y > 0) {
                    if (!m_rumble_left) {
                        set_rumble(true);
                        m_rumble_left = true;
                    }
                    if (m_pause) {
                        // if paused, limit driver move forward to .2 when close
                        if (m_target_distance < m_target_distance/2) {
                            if (y > .1) y = .1;
                        }
                    }
                    else {
                        y = m_approach_kp * y * (distance - m_target_distance) / m_max_dist;
                        if (y > .5) y = .5; // Max approach speed

                        if (y > 0 && y < m_approach_cutover_speed) y = m_approach_speed; // max approach speed
                    }
                } else {
                    set_rumble(true);
                }
            } else {
                set_rumble(false);
            }


            Robot.drive.driveArcade(x, y, z);

            SmartDashboard.putNumber("PixyVisionPidOut", Robot.pixyVision.get_pid_output());
        }
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
