package org.usfirst.frc5422.Minimec.commands.elevator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.elevator.Elevator;

public class ElevatorOverride extends Command {
    private Joystick joy;

    public ElevatorOverride()
    {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize()
    {
        joy = Robot.oi.getJoystick1();
    }
    @Override
    protected void execute()
    {
        if(getElevatorJoystick() == 1) Robot.elevator.moveUpManual();
        else if(getElevatorJoystick() == -1) Robot.elevator.moveDownManual();
        else Robot.elevator.stopElevator();
    }

    @Override
    protected void end()
    {
        Robot.elevator.holdElevator();
    }

    @Override
    protected void interrupted()
    {
        Robot.elevator.holdElevator();
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
