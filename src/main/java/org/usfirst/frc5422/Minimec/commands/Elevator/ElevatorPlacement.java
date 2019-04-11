package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StormProp;

public class ElevatorPlacement extends Command {

    public ElevatorPlacement(){
        if (Robot.useElevator){requires(Robot.elevator);}
    }

    protected void execute(){
//       Robot.elevator.moveToTicks(StormProp.getInt("elevatorPlacementPosition", 0),0);
    }
    protected void initialize(){
        System.out.println("ElevatorMove.initialize() : " + StormProp.getInt("elevatorPlacementPosition", 0));
    }
    protected void interrupted(){
        System.out.println("ElevatorMove.interrupted()");
        Robot.elevator.stop();}

    protected void end(){
        System.out.println("ElevatorMove.end()");
        Robot.elevator.stop();
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.isFinished();
    }
}
