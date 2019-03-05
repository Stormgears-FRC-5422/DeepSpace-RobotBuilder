package org.usfirst.frc5422.Minimec.commands.Drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.PixyObject;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.PixyVision;

public class SelectedDockDrive extends Command {
    private Joystick joy;

    public SelectedDockDrive(){

    }

    protected void initialize() {
        joy = Robot.oi.getJoystick1();
        Robot.pixyVision.enable(PixyVision.VisionMode.SIDE_CARGO_DOCK);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double null_size = .1;
        double rate = 0.5 + (joy.getRawAxis(3) * .5);
        double x_out = joy.getX();
        if (Math.abs(x_out) < null_size) x_out = 0;

        x_out = rate * joy.getX();
        if (joy.getRawButton(5) == false) {
            // Button 5 will disable PID input to drive
            //x_out += Robot.pixyVision.get_pid_output();
        } else {
            Robot.pixyVision.clearLastTracked();
        }

        if (x_out > 1.0) x_out  = 1.0;
        if (x_out < -1.0) x_out = -1.0;

        double z_out = rate * joy.getZ();
        double y_out = rate * -1 * joy.getY();

        // Create a joystick null zone to reduce drift
        if (Math.abs(y_out) < null_size) y_out = 0;

        SmartDashboard.putNumber("PixyVisionPidOut",Robot.pixyVision.get_pid_output());
        Robot.drive.driveArcade(x_out, y_out, z_out);

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.pixyVision.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        Robot.pixyVision.disable();
    }
}

