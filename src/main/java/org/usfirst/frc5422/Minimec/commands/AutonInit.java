package org.usfirst.frc5422.Minimec.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.commands.Arm.HatchMatchStart;

public class AutonInit extends CommandGroup {
    public AutonInit() {
        addSequential(new AutoHome(true,true));
        addSequential(new HatchMatchStart());
    }

}
