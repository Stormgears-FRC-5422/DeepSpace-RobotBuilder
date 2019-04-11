package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.AutoHome;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorReset;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable;
import org.usfirst.frc5422.Minimec.commands.ReleaseReset;

public class ReleaseGroup extends CommandGroup {

    public ReleaseGroup(){
        System.out.println("Starting reset sequence command");
        if (Robot.useValveControl) addSequential(new HatchVacDisable());
        if (Robot.useArm) addSequential(new ArmReset(true));
        if (Robot.useElevator) addSequential(new ElevatorReset(true));
    }


}
