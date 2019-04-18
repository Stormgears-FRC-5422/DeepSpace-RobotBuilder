package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorReset;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable;

public class HatchRelease extends CommandGroup {
    public HatchRelease(){
        addSequential(new HatchVacDisable());
        addSequential(new ArmToPosition(Robot.arm.ARM_REST_POSITION_TICKS));
        addSequential(new ElevatorReset(true));
    }
}
