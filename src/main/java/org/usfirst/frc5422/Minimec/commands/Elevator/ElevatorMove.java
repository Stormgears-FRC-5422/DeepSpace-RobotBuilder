package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StormProp;

public class ElevatorMove extends Command {
    private int level;

    public ElevatorMove(int level){
        System.out.println("ElevatorMove() : " + level);
        this.level = level;
        if (Robot.useElevator) requires(Robot.elevator);
    }

    @Override
    protected void initialize(){
        System.out.println("ElevatorMove.initialize() : " + level);
    }

    @Override
    protected void execute(){
        Robot.elevator.moveToLevel(level);
    }

    @Override
    protected void interrupted(){
        System.out.println("ElevatorMove.interrupted()");
        Robot.elevator.stop();
    }

    protected void end() {
        System.out.println("ElevatorMove.end()");
        Robot.elevator.stop();
    }

    @Override
    protected boolean isFinished() {
        return Robot.elevator.isFinished();
    }
}