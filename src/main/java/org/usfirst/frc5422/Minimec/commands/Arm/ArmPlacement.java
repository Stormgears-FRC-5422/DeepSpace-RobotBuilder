package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ArmPlacement extends Command {

    public ArmPlacement(){
        if (Robot.useArm){requires(Robot.arm);}
    }

    protected void execute(){
        Robot.arm.moveToPosition(Robot.arm.ARM_90_POSITION_TICKS);
    }
    protected void initialize(){
        //Nothing
    }
    protected void interrupted(){
        System.out.println("ArmToPosition.interrupted()");
        Robot.arm.stop();
    }
    protected void end(){
        System.out.println("ArmToPosition.end()");
        Robot.arm.stop();
    }

    @Override
    protected boolean isFinished() {
        // Don't end unless someone grabs the manual override
        return (getArmJoystick() != 0);    }

    private int getArmJoystick()
    {
        return (int) Math.round(-1 * Robot.oi.getJoystick2().getRawAxis(1));
    }

}
