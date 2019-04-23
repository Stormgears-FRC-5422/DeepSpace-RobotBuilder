package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Arm.*;
import org.usfirst.frc5422.Minimec.commands.Elevator.*;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.*;

public class HatchPickup extends CommandGroup {

    public HatchPickup(){
        System.out.println("Starting hatch pickup sequence command");
        if (Robot.useElevator) addParallel(new ElevatorReset(true));
        if (Robot.useArm) addParallel(new ArmToPosition(Robot.arm.ARM_PICKUP_POSITION_TICKS));
        if (Robot.useValveControl) addSequential(new HatchVacEnable(true));
        if (Robot.useArm) addSequential(new ArmToPosition(Robot.arm.ARM_REST_POSITION_TICKS));
    }


}
