package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.AutoHome;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable;
import org.usfirst.frc5422.Minimec.commands.ReleaseReset;

public class ReleaseGroup extends CommandGroup {

    public ReleaseGroup(){
       if(Robot.useValveControl) addParallel(new HatchVacDisable());
       addParallel(new AutoHome(true, false));
    }
}
