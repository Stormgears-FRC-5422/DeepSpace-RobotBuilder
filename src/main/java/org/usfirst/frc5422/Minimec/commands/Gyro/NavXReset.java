package org.usfirst.frc5422.Minimec.commands.Gyro;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class NavXReset extends Command {

    private boolean active;

    public NavXReset(boolean active){
        this.active = active;
    }

    @Override
    protected void initialize() {
        if(active) Robot.navX.calibrate();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
