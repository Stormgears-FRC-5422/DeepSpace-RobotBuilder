package org.usfirst.frc5422.Minimec.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ElevatorReset extends Command {
    // An active reset will cause motion to occur. A passive reset simply zeroes the position
    boolean m_active;

    public ElevatorReset (boolean active){
        m_active = active;
        requires(Robot.elevator);
    }

    @Override
    protected void initialize(){
        if (!m_active) {
            Robot.elevator.reset();
        }
    }

    @Override
    protected void execute(){
        if (m_active) {
            Robot.elevator.returnHome(true);
        }
    }

    @Override
    protected void interrupted(){
        if (m_active) {
            Robot.elevator.returnHome(false);
        }
    }

    @Override
    protected boolean isFinished() {
        if (m_active) {
            if (Robot.elevator.isHome()) {
                System.out.println("Elevator is home");
                return true;
            }
            else {
                return false;
            }
        } else {
            return true;  // the reset has already happened above. we are done
        }
    }
}
