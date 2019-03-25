package org.usfirst.frc5422.Minimec.commands.Jack;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class BackjackReset extends Command {
    // An active reset will cause motion to occur. A passive reset simply zeroes the position
    boolean m_active;

    public BackjackReset(boolean active){
        m_active = active;
        requires(Robot.backjack);
    }

    @Override
    protected void initialize(){
        if (!m_active) {
            Robot.backjack.reset();
        }
    }

    @Override
    protected void execute(){
        if (m_active) {
            Robot.backjack.returnHome(true);
        }
    }

    @Override
    protected void interrupted(){
        if (m_active) {
            Robot.backjack.returnHome(false);
        }
    }

    @Override
    protected boolean isFinished() {
        if (m_active) {
            if (Robot.backjack.isHome()) {
                System.out.println("Backjack is home");
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
