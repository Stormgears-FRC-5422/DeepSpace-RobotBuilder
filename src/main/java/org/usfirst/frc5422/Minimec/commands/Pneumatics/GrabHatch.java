package org.usfirst.frc5422.Minimec.commands.Pneumatics;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmToPosition;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorReset;

public class GrabHatch extends CommandGroup {
    public GrabHatch(){
        if (Robot.useElevator) addParallel(new ElevatorReset(true));
        if (Robot.useArm) addParallel(new ArmToPosition(Robot.arm.ARM_PICKUP_POSITION_TICKS));
        if (Robot.useValveControl) addParallel(new HatchVacEnable(true));

    }
}
