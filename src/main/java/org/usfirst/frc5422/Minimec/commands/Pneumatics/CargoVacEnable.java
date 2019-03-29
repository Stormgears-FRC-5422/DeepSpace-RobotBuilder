package org.usfirst.frc5422.Minimec.commands.Pneumatics;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

/**
 *
 */
public class CargoVacEnable extends Command {


    public CargoVacEnable() {
        requires(Robot.valveControl);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(!Robot.valveControl.getBallProxSensor() ){
            if (Robot.debug) System.out.println("Cargo Suck");
            Robot.valveControl.cargoStart();
        }
        else {
            Robot.valveControl.cargoStop();
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
        Robot.valveControl.cargoStop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.valveControl.cargoStop();
    }
}

