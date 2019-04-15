package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ArmPlacement extends Command {
    private Boolean is_finished;

    public ArmPlacement(){
        if (Robot.useArm){requires(Robot.arm);}
    }

    protected void execute(){
        is_finished = Robot.arm.moveToPosition(Robot.arm.ARM_90_POSITION_TICKS);
    }
    protected void initialize(){
        is_finished =  false;
    }
    protected void interrupted(){
        Robot.arm.stop();
    }
    protected void end(){
    }

    @Override
    protected boolean isFinished() {
        return (is_finished);    
    }

}
