// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5422.Minimec.commands.Arm;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.subsystems.PixyVision;

/**
 *
 */
public class ArmToPosition extends Command {
    int m_position;
    public ArmToPosition(int position) {
        System.out.println("ArmToPosition() : " + m_position);
        m_position = position;
        if (Robot.useArm) requires(Robot.arm);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("ArmToPosition.initialize() : " + m_position);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        Robot.arm.moveToPosition(m_position);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        // Don't end unless someone grabs the manual override
        return (getArmJoystick() != 0);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        System.out.println("ArmToPosition.end() : " + m_position);
        Robot.arm.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        System.out.println("ArmToPosition.interrupted() : " + m_position);
        Robot.arm.stop();
    }

    private int getArmJoystick()
    {
        return (int) Math.round(-1 * Robot.oi.getJoystick2().getRawAxis(1));
    }

}