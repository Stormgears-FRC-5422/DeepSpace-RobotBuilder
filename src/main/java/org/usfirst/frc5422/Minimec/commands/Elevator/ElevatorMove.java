package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StormProp;

public class ElevatorMove extends Command {
    private int level;

    public ElevatorMove(int level){
        System.out.println("ElevatorMove()");
        this.level = level;
        if (Robot.useElevator) requires(Robot.elevator);
    }

    @Override
    protected void initialize(){
        System.out.println("ElevatorMove.initialize()");
    }

    @Override
    protected void execute(){
        if(level == 3){
            Robot.elevator.moveToPosition(StormProp.getInt("elevatorLevelThreeHeight"));
        }else if(level == 2){
            Robot.elevator.moveToPosition(StormProp.getInt("elevatorLevelTwoHeight"));
        }else{
            Robot.elevator.moveToPosition(0);
        }
    }

    @Override
    protected void interrupted(){
        System.out.println("ElevatorMove.interrupted()");
        Robot.elevator.hold();
    }

    protected void end() {
        System.out.println("ElevatorMove.end()");
        Robot.elevator.hold();
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.isFinished();
    }
}