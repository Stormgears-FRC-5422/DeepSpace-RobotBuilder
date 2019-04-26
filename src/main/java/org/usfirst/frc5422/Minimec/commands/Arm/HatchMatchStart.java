package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import kotlinx.coroutines.Delay;
import org.usfirst.frc5422.Minimec.commands.DelayCommand;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.*;
import org.usfirst.frc5422.utils.StormProp;

public class HatchMatchStart extends CommandGroup {

    public HatchMatchStart(){
        System.out.println("Starting hatch match start pickup sequence command");
//        if (Robot.useElevator) addParallel(new ElevatorReset(true));
//        if (Robot.useArm) addParallel(new ArmToPosition(Robot.arm.ARM_PICKUP_POSITION_TICKS));
//        if (Robot.useValveControl) addParallel(new HatchVacEnable(true));
        addSequential(new GrabHatch());
//        addSequential(new DelayCommand(StormProp.getNumber("autoHatchReleaseDelay", 1.0)));
//        addSequential(new HatchRelease(1));
    }
    public void end() {
        System.out.println("Hatch match start pickup sequence command complete");
    }
}
