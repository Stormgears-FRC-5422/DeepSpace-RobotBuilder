package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.elevator.Elevator;

public class PlaceHatch extends Command {

    private static int DELTA_ELEVATOR_HEIGHT = 50;
    private int current_elevator_height;
    private double arm_length = 7424.0;
    private double arm_angle;
    private int final_elevator_height;
    private int desired_level;
    private int level_ticks;


    public PlaceHatch(int level) {
        requires(Robot.arm);
        requires(Robot.elevator);
        desired_level = level;
    }

    public void execute() {
        level_ticks = Robot.elevator.getLevelTicks(desired_level);
        final_elevator_height = level_ticks + DELTA_ELEVATOR_HEIGHT;
        Robot.elevator.moveToPosition(final_elevator_height);
        current_elevator_height = Robot.elevator.getCurrentPositionTicks();
        int relative_elevator_height = final_elevator_height - current_elevator_height;
        arm_angle = Math.acos(relative_elevator_height/arm_length);
        Robot.arm.moveAngle(arm_angle);
    }

    @Override
    public void setName(String subsystem, String name) {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
