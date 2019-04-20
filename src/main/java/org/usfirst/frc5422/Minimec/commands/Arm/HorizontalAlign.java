package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class HorizontalAlign extends Command {
    private Boolean m_is_finished;
    public HorizontalAlign() {
        requires(Robot.arm);
        requires(Robot.elevator);
    }

    @Override
    // Called just before this Command runs the first time
    protected void initialize() {
        m_is_finished = false;
        Robot.valveControl.hatchStart();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        m_is_finished = Robot.arm.moveToPosition(Robot.arm.ARM_90_POSITION_TICKS);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return(m_is_finished);
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

