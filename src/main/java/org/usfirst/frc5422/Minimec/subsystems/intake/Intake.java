package org.usfirst.frc5422.Minimec.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Intake.ExtendIntake;

import java.util.Random;

public class Intake extends Subsystem {
    public static Intake instance;
    public static Intake getInstance()
    {
        if(instance == null) instance = getInstance();
        return instance;
    }

    private VictorSPX intake;

    public Intake()
    {
        intake = new VictorSPX(15);
    }

    public void moveIntakeWheelsIn()
    {
        intake.set(ControlMode.PercentOutput, 1);
    }

    public void moveIntakeWheelsOut()
    {
        intake.set(ControlMode.Velocity, -1000);
    }

    public void wheelsOff()
    {
        intake.set(ControlMode.Velocity, 0);
    }

    public void extend() {
        if(Robot.oi.getBackJackLevel() == 3){
            Robot.valveControl.armExtend();
        }
        else if(Robot.oi.getBackJackLevel() == 2){
            Robot.valveControl.wheelExtend();
        } else {
            System.out.println("FLUSH THIS ROBOT DOWN THE TOILET");
        }
    }

    public void retract() {
        if(Robot.valveControl.wheelState()){
            Robot.valveControl.wheelRetract();
        }
        if(Robot.valveControl.armState()){
            Robot.valveControl.armRetract();
        }
    }
    protected void initDefaultCommand() {}

}
