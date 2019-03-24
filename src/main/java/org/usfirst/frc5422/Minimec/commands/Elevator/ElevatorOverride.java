package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ElevatorOverride extends Command {
//    private Joystick joy;

    public ElevatorOverride()
    {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize()
    {
//        joy = Robot.oi.getJoystick1();
    }

    @Override
    protected void execute()
    {
        if(getElevatorJoystick() == 1) {
            Robot.elevator.moveUpManual();
        }
        else if(getElevatorJoystick() == -1) {
            Robot.elevator.moveDownManual();
        }
        else {
            Robot.elevator.hold();
        }
    }

    @Override
    protected void end()
    {
    }

    @Override
    protected void interrupted()
    {
        Robot.elevator.hold();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private double getElevatorJoystick()
    {
        return -1 * Robot.oi.getJoystick1().getRawAxis(1);
    }

}
