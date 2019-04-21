package org.usfirst.frc5422.Minimec.commands.Pneumatics;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StatusLight;

public class HatchVacDisable extends Command {
    private Boolean m_wait_for_contact = false;
    public HatchVacDisable() {
        requires(Robot.valveControl);
    }

    public HatchVacDisable(Boolean wait_for_contact) {
        this();
        m_wait_for_contact = wait_for_contact;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(Robot.useStatusLights) Robot.setStatusLight(StatusLight.Intake, 0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (m_wait_for_contact) {
            return(Robot.valveControl.getHatchProxSensorReady());
        } else {
            return true;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.valveControl.hatchStop(); //
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        // don't release the hatch if we are interrupted
    }
}

