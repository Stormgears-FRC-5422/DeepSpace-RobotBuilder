package org.usfirst.frc5422.Minimec.subsystems.backjack;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
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

    private static final int MAX_POSITION = 32600;
    private DigitalInput fLightR;
    private DigitalInput fLightL;
    private int nextLevel;
    private boolean moving = false;
    private NetworkTableEntry encoderEntry;
    private NetworkTableEntry rightEntry;
    private NetworkTableEntry leftEntry;
    private ShuffleboardTab tab;

    public Backjack() {
        int kTimeoutMs = StormProp.getInt("canTimeout");
        Shuffleboard.selectTab("Backjack");
        tab = Shuffleboard.getTab("Backjack");
        fLightR = new DigitalInput(9);
        fLightL = new DigitalInput(8);
        rightEntry = tab.add("Right Light", (fLightR).get()).getEntry();
        leftEntry = tab.add("Left Light", (fLightL).get()).getEntry();

        // backjack gear radius is 1 inches
        // two seconds per rotation
        // so, pi inches per second.
        // 4096 tics per rev
        // 2048 tics per second
        // 200 tics per 100 ms is a reasonable target velocity

        jackTalon = new WPI_TalonSRX(StormProp.getInt("jackTalonId"));
        jackTalon.setNeutralMode(NeutralMode.Brake);


        reset();

        jackTalon.setInverted(true);
        jackTalon.configForwardSoftLimitThreshold(MAX_POSITION, kTimeoutMs);
        jackTalon.configForwardSoftLimitEnable(true);

        //        jackTalon.getSensorCollection().setQuadraturePosition(0, 20);
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

        nextLevel = -1;

        tab = Shuffleboard.getTab("Backjack");
        Shuffleboard.selectTab("Backjack");
        encoderEntry = tab.add("encoder ticks", getCurrentPositionTicks()).getEntry();
    }



    public void reset() {
        jackTalon.setSelectedSensorPosition(0);
    }

    public double getCurrentPositionTicks(){
        return jackTalon.getSensorCollection().getQuadraturePosition();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new MoveJack(false));
    }

    @Override
    public void periodic() {
        //currentEntry.setDouble(jackTalon.getOutputCurrent());
        encoderEntry.setDouble(getCurrentPositionTicks());
        rightEntry.setBoolean((fLightR).get());
        leftEntry.setBoolean((fLightL).get());
    }

    public void move(boolean active) {
        if (active) {
            nextLevel = Robot.oi.getBackJackLevel();
        } else {
            nextLevel = -1;
        }

        //System.out.println("Move() :" + nextLevel);

        switch(nextLevel) {
            case 2:  // Extend
            case 3:  // Extend
                moving = true;
                jackTalon.set(ControlMode.PercentOutput, 1.0);
                break;
            case 0:  // retract
                moving = true;
                jackTalon.set(ControlMode.PercentOutput, -1.0);
                break;
            case -1:
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
