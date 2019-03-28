package org.usfirst.frc5422.Minimec.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmReset;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorReset;
import org.usfirst.frc5422.Minimec.commands.Jack.BackjackReset;

public class AutoHome extends CommandGroup {
    public AutoHome(boolean active) {
        System.out.println("Starting auto-homing sequence - active = " + active);
        // Note that these aren't run here - they are created and scheduled. Running happens next
        if (Robot.useArm) addSequential(new ArmReset(active));
        if (Robot.useElevator) addSequential(new ElevatorReset(active));
        if (Robot.useBackjack) addSequential(new BackjackReset(active));
    }
}
