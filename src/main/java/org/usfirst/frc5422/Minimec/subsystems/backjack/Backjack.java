package org.usfirst.frc5422.Minimec.subsystems.backjack;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Jack.MoveJack;
import org.usfirst.frc5422.utils.StormProp;


/**
 *
 */
public class Backjack extends Subsystem {
    //private TalonSRX jackTalon;
    private WPI_TalonSRX jackTalon;

    private int nextLevel;
    private boolean moving = false;
    private NetworkTableEntry currentEntry;

    public Backjack() {
        int kTimeoutMs = StormProp.getInt("canTimeout");
        //NetworkTableEntry currentEntry = Shuffleboard.getTab("LiveWindow").add("Current", 0).getEntry();


        // backjack gear radius is 1 inches
        // two seconds per rotation
        // so, pi inches per second.
        // 4096 tics per rev
        // 2048 tics per second
        // 200 tics per 100 ms is a reasonable target velocity

        jackTalon = new WPI_TalonSRX(StormProp.getInt("jackTalonId"));
        jackTalon.setNeutralMode(NeutralMode.Brake);
        //jackTalon.configPeakCurrentLimit((StormProp.getInt("armCurrentLimit")));

//        jackTalon.selectProfileSlot(0, 0);
//        jackTalon.config_kF(0, 5.0, kTimeoutMs);  // 1023 / 200
//        jackTalon.config_kP(0, 0.2, kTimeoutMs);
//        jackTalon.config_kI(0, 0, kTimeoutMs);
//        jackTalon.config_kD(0, 0, kTimeoutMs);
//        jackTalon.config_IntegralZone(0, 1000, kTimeoutMs);
//
//        jackTalon.configMotionAcceleration(500);
//        jackTalon.configMotionCruiseVelocity(500);

        jackTalon.setInverted(true);
//        jackTalon.setSensorPhase(false);

        addChild("JackTalon", jackTalon);

        nextLevel = 0;
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new MoveJack());
    }

    @Override
    public void periodic() {
        //currentEntry.setDouble(jackTalon.getOutputCurrent());
    }

    public void move() {
        nextLevel = Robot.oi.getBackJackLevel();

        switch(nextLevel) {
            case 1:  // Extend
                moving = true;
                jackTalon.set(ControlMode.PercentOutput, 1.0);
                break;
            case -1:  // retract
                moving = true;
                jackTalon.set(ControlMode.PercentOutput, -1.0);
                break;
            case 0:
            default:
                stop();
                break;
        }
    }

    public void stop() {
        jackTalon.set(ControlMode.PercentOutput, 0);
        moving = false;
    }
}
