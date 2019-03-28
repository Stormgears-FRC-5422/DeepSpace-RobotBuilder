package org.usfirst.frc5422.Minimec.commands.Jack;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class JackDrive extends Command {

    private boolean m_active;
    private boolean driveOff = true;
    public JackDrive(boolean active) {
        System.out.println("JackDrive()" + active);
        m_active = active;
        requires(Robot.drive);
        requires(Robot.backjack);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        System.out.println("JackDrive.initialize()");
        Robot.navX.enable(180);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
         // derate drive joystick for precision


        double z = Robot.navX.get_pid_output();
        if (z > 1) { z = 1; }
        if (z < -1) { z= -1; }

        if (driveOff) {
            Robot.drive.driveArcade(0, .1, z);
            driveOff = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        Robot.drive.driveArcade(0,0,0);
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
