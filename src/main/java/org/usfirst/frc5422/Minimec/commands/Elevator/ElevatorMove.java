package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ElevatorMove extends Command {

    private final double LEVELTWOHEIGHT = 15023.0;
    private final double LEVELTHREEHEIGHT = 27251.0;


    private int level;
    public ElevatorMove(int level){
        this.level = level;
        requires(Robot.elevator);
    }

    @Override
    protected void initialize(){

    }

    @Override
    protected void execute(){
        if(level == 3){
            Robot.elevator.moveToPosition(LEVELTHREEHEIGHT);

        }else if(level == 2){
            Robot.elevator.moveToPosition(LEVELTWOHEIGHT);

        }else{
            Robot.elevator.moveToPosition(0);

        }
        System.out.println("Elevator Pos: " + Robot.elevator.getCurrentPositionTicks());
        System.out.println("Elevator Vel: " + Robot.elevator.getCurrentVelocity());
    }

    @Override
    protected void interrupted(){

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
