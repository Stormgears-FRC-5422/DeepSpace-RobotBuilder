// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc5422.Minimec.commands.Jack;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

/**
 *
 */
public class MoveJack extends Command {
    private static MoveJack instance;
    public static MoveJack getInstance() {
        if(instance == null) instance = new MoveJack();
        return instance;
    }

    public MoveJack() {
        System.out.println("MoveJack()");
        requires(Robot.backjack);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("MoveJack.initialize()");
        Robot.backjack.move();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        System.out.println("MoveJack.interrupted()");
        Robot.backjack.stop();
    }
}