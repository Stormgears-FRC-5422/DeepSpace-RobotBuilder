package org.usfirst.frc5422.Minimec.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc5422.Minimec.Robot;

public class ArmReset extends Command {
    // An active reset will cause motion to occur. A passive reset simply zeroes the position
    boolean m_active;

    public ArmReset(boolean active){
        m_active = active;
        if (Robot.useArm) requires(Robot.arm);
    }

    @Override
    protected void initialize(){
        System.out.println("ArmReset.initialize()");
        if (!m_active) {
            Robot.arm.reset();
        }
    }

    @Override
    protected void execute(){
        if (m_active) {
            Robot.arm.returnHome(true);
        }
    }

    @Override
    protected void interrupted(){
        System.out.println("ArmReset.interrupted()");
        if (m_active) {
            Robot.arm.returnHome(false);
        }
    }

    protected void end(){
        System.out.println("ArmReset.end()");
        if (m_active) {
            Robot.arm.returnHome(false);
        }
    }

    @Override
    protected boolean isFinished() {
        if (m_active) {
            if (Robot.arm.isHome()) {
                System.out.println("Arm is home");
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
