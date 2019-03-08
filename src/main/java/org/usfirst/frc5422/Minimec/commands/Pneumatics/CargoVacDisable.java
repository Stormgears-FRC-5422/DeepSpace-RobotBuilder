package org.usfirst.frc5422.Minimec.commands.Pneumatics;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.pneumatics.*;

/**
 *
 */
public class CargoVacDisable extends Command {


    public CargoVacDisable() {


        requires(Robot.valveControl);


    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        System.out.println("Ball Unsuck");
        Robot.valveControl.vacStop();
        Robot.valveControl.ballStop();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.valveControl.ballStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.valveControl.ballStop();
        Robot.valveControl.vacStop();
    }
}

