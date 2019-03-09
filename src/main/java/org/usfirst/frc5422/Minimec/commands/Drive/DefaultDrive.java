package org.usfirst.frc5422.Minimec.commands.Drive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.OI;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.dsio.DSIO;
import org.usfirst.frc5422.utils.StormProp;

public class DefaultDrive extends Command {
    private static DefaultDrive instance;
    public static DefaultDrive getInstance(){
        if(instance == null) instance = new DefaultDrive();
        return instance;
    }

    private OI oi;
    private boolean xboxController;

    public DefaultDrive()
    {
        requires(Robot.mecanum);
        oi = Robot.oi;
        xboxController = StormProp.getBoolean("xboxController");
    }

    @Override
    protected void execute()
    {
        if(xboxController){
            Robot.mecanum.driveCartesian(oi.getJoystick().getRawAxis(0)*-1,(oi.getJoystick().getRawAxis(3)-oi.getJoystick().getRawAxis(2))*-1,oi.getJoystick().getRawAxis(4));
        }
        else {
            Robot.mecanum.driveCartesian(oi.getJoystick().getX(),oi.getJoystick().getY(),oi.getJoystick().getZ());
        }
    }

    @Override
    protected boolean isFinished(){return false;}
}
