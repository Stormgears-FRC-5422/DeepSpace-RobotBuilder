package org.usfirst.frc5422.Minimec.commands.elevator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.elevator.Elevator;

public class ElevatorOverride extends Command {
    private Joystick joy;
    private double currentPosition;

    public ElevatorOverride()
    {
        requires(Robot.elevator);
    }

    @Override
    protected void initialize()
    {
        joy = Robot.oi.getJoystick1();
        currentPosition = Robot.elevator.REST_POSITION;
    }
    @Override
    protected void execute()
    {
        if(getElevatorJoystick() == 1) {
            currentPosition = Robot.elevator.getCurrentPositionTicks();
            Robot.elevator.moveUpManual();
        }
        else if(getElevatorJoystick() == -1) {
            currentPosition = Robot.elevator.getCurrentPositionTicks();
            Robot.elevator.moveDownManual();
        }
        else Robot.elevator.holdElevator(currentPosition);
    }

    @Override
    protected void end()
    {
        Robot.elevator.holdElevator(currentPosition);
    }

    @Override
    protected void interrupted()
    {
        Robot.elevator.holdElevator(currentPosition);
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
