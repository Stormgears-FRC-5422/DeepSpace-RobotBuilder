package org.usfirst.frc5422.Minimec.subsystems.backjack;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

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

    private static final int MAX_POSITION = 23500; //21000
    private static final int MIN_POSITION = 0;
    private static final int SECONDLEVEL = 21000;
    private DigitalInput fLightR;
    private DigitalInput fLightL;
    private int nextLevel;
    private boolean moving = false;
    private NetworkTableEntry encoderEntry;
    private NetworkTableEntry rightEntry;
    private NetworkTableEntry leftEntry;
    private ShuffleboardTab tab;

    private int currentPosition;

    public Backjack() {
        int kTimeoutMs = StormProp.getInt("canTimeout",0);
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

        jackTalon = new WPI_TalonSRX(StormProp.getInt("jackTalonId",-1));
        jackTalon.setNeutralMode(NeutralMode.Brake);


        reset();

        jackTalon.setInverted(true);
        jackTalon.configForwardSoftLimitThreshold(MAX_POSITION, kTimeoutMs);
        jackTalon.configForwardSoftLimitEnable(false);

        nextLevel = -1;

        tab = Shuffleboard.getTab("Backjack");
        Shuffleboard.selectTab("Backjack");
        currentPosition = getCurrentPositionTicks();
        encoderEntry = tab.add("encoder ticks", currentPosition).getEntry();
    }

    public void reset() {
        jackTalon.setSelectedSensorPosition(0);
    }

    public int getCurrentPositionTicks(){
        return jackTalon.getSensorCollection().getQuadraturePosition();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new MoveJack(false));
    }

    @Override
    public void periodic() {
        //currentEntry.setDouble(jackTalon.getOutputCurrent());
        currentPosition = getCurrentPositionTicks();
        encoderEntry.setDouble(currentPosition);
        rightEntry.setBoolean((fLightR).get());
        leftEntry.setBoolean((fLightL).get());
        if (!fLightL.get() && !fLightR.get()) {
            Robot.drive.driveArcade(0, .15, 0);
        }
        else {
            if (currentPosition > MAX_POSITION){
                returnHome(true);
            }
        }
        if (isHome()) reset();
    }

    public boolean atMax() {
        if (nextLevel == 2) return (currentPosition > SECONDLEVEL);
        else if (nextLevel == 3) return (currentPosition > MAX_POSITION);
        else return false;
    }

    public boolean pastMin() {
        return (currentPosition > MIN_POSITION);
    }

    public void move(boolean active) {
        if (active) {
            nextLevel = Robot.oi.getBackJackLevel();
        } else {
            nextLevel = -1;
        }

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

    public boolean getSensors() {
        return (!fLightL.get() || !fLightR.get());
    }

    public void returnHome(boolean go) {
        if (go) {
            jackTalon.set(ControlMode.PercentOutput, -1.0);
        } else {  // Stop returning
            stop();
        }

    }

    public boolean isHome() {
        // Switches are normally open - triggered means they would be open
        return jackTalon.getSensorCollection().isRevLimitSwitchClosed();
    }
}
