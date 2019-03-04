package org.usfirst.frc5422.Minimec.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.command.Subsystem;

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

        System.out.print("tanay chief lord");
        intake.set(ControlMode.PercentOutput, 1);
      //  System.out.println(intake.getMotorOutputPercent());
    }

    public void moveIntakeWheelsOut()
    {
        intake.set(ControlMode.Velocity, -1000);
    }

    public void wheelsOff()
    {
        intake.set(ControlMode.Velocity, 0);
    }

    protected void initDefaultCommand() {}

}
