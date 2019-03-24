package org.usfirst.frc5422.utils.logging;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class TalonTuner {
    private WPI_TalonSRX talon;
    private ControlMode mode;
    private int slotIdx;
    private int kTimeoutMS = 10;
    private NetworkTableEntry updatePIDEntry;
    private NetworkTableEntry activeModeEntry;
    private NetworkTableEntry closedLoopErrorEntry;
    private NetworkTableEntry allowableClosedLoopErrorEntry;
    private NetworkTableEntry getClosedLoopTargetEntry;
    private NetworkTableEntry closedLoopPeakOutputEntry;


    private NetworkTableEntry slotIdxEntry;
    private NetworkTableEntry kPEntry;
    private NetworkTableEntry kIEntry;
    private NetworkTableEntry kDEntry;
    private NetworkTableEntry kFEntry;
    private NetworkTableEntry iZoneEntry;


    public TalonTuner(String tabName, WPI_TalonSRX talon, ControlMode mode, int slotIdx) {
        SlotConfiguration slotConfig = new SlotConfiguration();
        this.talon = talon;
        this.slotIdx = slotIdx;
        this.mode = mode;
        ShuffleboardTab tab = Shuffleboard.getTab(tabName);

        switch (mode) {
            case Velocity:
                activeModeEntry = tab.add("Velocity", talon.getSelectedSensorVelocity()).getEntry();
                break;
            case MotionMagic:
            case Position:
            default:
                activeModeEntry = tab.add("Position", talon.getSensorCollection().getQuadraturePosition()).getEntry();
                break;
        }

        closedLoopErrorEntry = tab.add("Error", (talon.getControlMode() == mode) ? talon.getClosedLoopError() : 0).getEntry();
        getClosedLoopTargetEntry = tab.add("Target", (talon.getControlMode() == mode) ? talon.getClosedLoopTarget() : 0).getEntry();

        talon.getSlotConfigs(slotConfig, slotIdx , 10);
        kPEntry = tab.add("kP", slotConfig.kP).getEntry();
        kIEntry = tab.add("kI", slotConfig.kI).getEntry();
        kDEntry = tab.add("kD", slotConfig.kD).getEntry();
        kFEntry = tab.add("kF", slotConfig.kF).getEntry();
        iZoneEntry = tab.add("iZone", slotConfig.integralZone).getEntry();
        allowableClosedLoopErrorEntry = tab.add("ACLE", slotConfig.allowableClosedloopError).getEntry();
        closedLoopPeakOutputEntry = tab.add("CLPO", slotConfig.closedLoopPeakOutput).getEntry();

        updatePIDEntry = tab.add("Apply", 0).getEntry();

    }

    public void periodic() {
        switch(mode) {
            case Velocity:
                activeModeEntry.setNumber(talon.getSelectedSensorVelocity());
                break;
            case Position:
            case MotionMagic:
            default:
                activeModeEntry.setNumber(talon.getSensorCollection().getQuadraturePosition());
                break;

        }

        closedLoopErrorEntry.setNumber( (talon.getControlMode() == mode) ? talon.getClosedLoopError() : 0);
        getClosedLoopTargetEntry.setNumber( (talon.getControlMode() == mode) ? talon.getClosedLoopTarget() : 0);

        // We want to be able to set the pid values all at once and then choose when to apply them.
        // So, switch the apply toggle to true when ready, and we'll reset the toggle below.
        if (updatePIDEntry.getNumber(0).intValue() == 1) {
            talon.config_kP(slotIdx, kPEntry.getDouble(0), kTimeoutMS);
            talon.config_kI(slotIdx, kIEntry.getDouble(0), kTimeoutMS);
            talon.config_kD(slotIdx, kDEntry.getDouble(0), kTimeoutMS);
            talon.config_kF(slotIdx, kFEntry.getDouble(0), kTimeoutMS);
            talon.config_IntegralZone(slotIdx, iZoneEntry.getNumber(0).intValue(), kTimeoutMS);
            talon.configAllowableClosedloopError(slotIdx, allowableClosedLoopErrorEntry.getNumber(0).intValue());
            talon.configClosedLoopPeakOutput(slotIdx, (double)closedLoopPeakOutputEntry.getNumber(0));
            updatePIDEntry.setNumber(0);
        }
    }
}

