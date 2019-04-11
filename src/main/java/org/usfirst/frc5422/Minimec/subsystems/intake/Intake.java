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
        if (Robot.oi.getBackJackLevel() == 0 || Robot.oi.getBackJackLevel() == -1) {
            System.out.println("WHAT ARE YOU DOING?! STOP CLICKING THE RAIL BUTTON SURYAA!");
        } else {
            Robot.valveControl.armExtend();
            Robot.valveControl.wheelExtend();
        }
    }

    public void retract() {
        Robot.valveControl.wheelRetract();
        Robot.valveControl.armRetract();
    }
    protected void initDefaultCommand() {}

}
