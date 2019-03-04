package org.usfirst.frc5422.Minimec.commands.Drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.Robot;

public class LidarDrive extends Command {
    private Joystick joy;

    public LidarDrive() {
    }

    protected void initialize() {
        joy = Robot.oi.getJoystick1();
    }

    protected void execute() {
        double null_size = .1;
        double rate = 0.5 + (joy.getRawAxis(3) * .5);
        double z_out = joy.getZ();
        if (Math.abs(z_out) < null_size) z_out = 0;

        z_out = rate * joy.getZ();
        if (joy.getRawButton(5) == false) {
            // Button 5 will disable PID input to drive
            z_out += Robot.pixyVision.get_pid_output();
        } else {
            Robot.pixyVision.clearLastTracked();
        }

        if (z_out > 1.0) z_out  = 1.0;
        if (z_out < -1.0) z_out = -1.0;

        double x_out = rate * joy.getX();
        double y_out = rate * -1 * joy.getY();

        // Create a joystick null zone to reduce drift
        if (Math.abs(x_out) < null_size) x_out = 0;
        if (Math.abs(y_out) < null_size) y_out = 0;

        SmartDashboard.putNumber("PixyVisionPidOut",Robot.pixyVision.get_pid_output());
        Robot.drive.driveArcade(x_out,y_out, z_out);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
