package org.usfirst.frc5422.Minimec.commands.Pneumatics;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StatusLight;

public class HatchVacDisable extends Command {


    public HatchVacDisable() {


        requires(Robot.valveControl);


    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        if(Robot.useStatusLights) Robot.setStatusLight(StatusLight.Intake, 0);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (Robot.debug) System.out.println("Hatch Unsuck");
        Robot.valveControl.hatchStop();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.valveControl.hatchStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.valveControl.hatchStop();
    }
}

