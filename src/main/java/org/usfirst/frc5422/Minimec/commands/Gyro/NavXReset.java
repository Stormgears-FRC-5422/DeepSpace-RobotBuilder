package org.usfirst.frc5422.Minimec.commands.Gyro;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class NavXReset extends Command {

    public NavXReset(){

    }

    @Override
    protected void initialize() {
        Robot.navX.calibrate();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
