package org.usfirst.frc5422.Minimec.commands.Pneumatics;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.pneumatics.*;
import org.usfirst.frc5422.utils.StatusLight;

/**
 *
 */
public class HatchVacEnable extends Command {
    private Boolean m_finish_on_contact;

    public HatchVacEnable() {
        this(false);
    }

    public HatchVacEnable(Boolean finish_on_contact) {
        m_finish_on_contact = finish_on_contact;
        requires(Robot.valveControl);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(Robot.useStatusLights) Robot.setStatusLight(StatusLight.Intake, 1);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(Robot.valveControl.getHatchProxSensorReady()) {
            if (Robot.debug) System.out.println("Hatch vacuum ready");
            Robot.valveControl.hatchStart();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (m_finish_on_contact) {
            return(Robot.valveControl.getHatchProxSensorReady());
        }
        else {
           return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        if (!m_finish_on_contact) Robot.valveControl.hatchStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        if (!m_finish_on_contact) Robot.valveControl.hatchStop();
    }
}

