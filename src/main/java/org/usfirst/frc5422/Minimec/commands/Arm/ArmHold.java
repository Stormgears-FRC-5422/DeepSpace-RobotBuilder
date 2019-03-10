package org.usfirst.frc5422.Minimec.commands.Arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ArmHold extends Command {

    private double position;

    public ArmHold()
    {
        requires(Robot.arm);
    }

    @Override
    protected void initialize()
    {
        position = Robot.arm.armTalon.getSensorCollection().getQuadraturePosition();
    }

    @Override
    protected void execute()
    {
       Robot.arm.armTalon.set(ControlMode.Position, position);
    }

    @Override
    protected void end()
    {
        Robot.arm.stop();
    }

    @Override
    protected void interrupted()
    {
        Robot.arm.stop();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
