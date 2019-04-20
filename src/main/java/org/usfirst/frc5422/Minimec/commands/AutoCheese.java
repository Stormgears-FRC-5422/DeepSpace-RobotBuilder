package org.usfirst.frc5422.Minimec.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmPlacement;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacEnable;

public class AutoCheese extends CommandGroup {
    public AutoCheese(){
        addSequential(new HatchVacEnable());
        addSequential(new ArmPlacement());
    }
}
