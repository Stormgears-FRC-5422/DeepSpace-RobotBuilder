package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ArmHold extends Command {
    // An active hold will cause motion to occur. A passive hold simply zeroes the position
    boolean m_active;

    public ArmHold(){
        if (Robot.useArm) requires(Robot.arm);
    }

    @Override
    protected void initialize(){
        System.out.println("ArmHold.initialize()");
        Robot.arm.hold();
    }

    @Override
    protected void execute(){
    }

    @Override
    protected void interrupted(){
        if (Robot.useArm) Robot.arm.hold();
    }

    protected void end(){
    }

    @Override
    protected boolean isFinished() {
        return true;  // the hold has already happened above. we are done
    }
}
