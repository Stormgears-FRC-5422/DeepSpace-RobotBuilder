package org.usfirst.frc5422.Minimec.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorReset;
import org.usfirst.frc5422.Minimec.commands.Jack.BackjackReset;

public class AutoHome extends CommandGroup {

    public AutoHome(boolean active) {
        addParallel(new BackjackReset(active));
        addParallel(new ElevatorReset(active));
    }

}
