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
        joy = Robot.oi.getJoystick();
    }

    protected void execute() {

        double joy_vals[] = Robot.oi.getJoyXYZ(joy);  // 
        double x = joy_vals[0];
        double y = joy_vals[1];
        double z = joy_vals[2];

        if (joy.getRawButton(5) == false) {
            // Button 5 will disable PID input to drive
            z += Robot.pixyVision.get_pid_output();
        } else {
            Robot.pixyVision.clearLastTracked();
        }

        if (z > 1.0) z  = 1.0;
        if (z < -1.0) z = -1.0;


        SmartDashboard.putNumber("PixyVisionPidOut",Robot.pixyVision.get_pid_output());
        Robot.drive.driveArcade(x,y, z);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

}
