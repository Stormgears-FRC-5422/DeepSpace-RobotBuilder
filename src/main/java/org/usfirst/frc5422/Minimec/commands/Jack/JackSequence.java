package org.usfirst.frc5422.Minimec.commands.Jack;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class JackSequence extends CommandGroup {

    public JackSequence(boolean active) {
        addParallel (new MoveJack(active));
        addParallel (new JackDrive(active));
    }

}
