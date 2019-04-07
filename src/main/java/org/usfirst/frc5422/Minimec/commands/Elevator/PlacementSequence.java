package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmPlacement;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable;

public class PlacementSequence extends CommandGroup {


    public PlacementSequence(){
        addParallel(new ArmPlacement());
        addParallel(new ElevatorPlacement());
        addSequential(new HatchVacDisable());
    }



}
