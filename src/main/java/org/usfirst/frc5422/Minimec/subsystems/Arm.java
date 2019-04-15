package org.usfirst.frc5422.Minimec.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmOverride;
import org.usfirst.frc5422.utils.StormProp;

public class Arm extends Subsystem {
    public final int ARM_PICKUP_POSITION_TICKS;
    public final int ARM_HOME_POSITION_TICKS;
    public final int ARM_90_POSITION_TICKS;
    public final int ARM_135_POSITION_TICKS;
    public final int ARM_REST_POSITION_TICKS;

    private WPI_TalonSRX armTalon;
    private WPI_TalonSRX pivotTalon;

    private final int armMotionMagicSlotIdx = 0;
    private final int armPositionUnloadedSlotIdx = 1;
    private final int armPositionLoadedSlotIdx = 2;
    private final int armVelocitySlotIdx = 3;

    //    private final int armHatchOnIdx = 3;
    int curArmPos;
    int curPivPos;
    int holdPosition;

    boolean isHolding = false;

    public Arm() {
        ARM_PICKUP_POSITION_TICKS = StormProp.getInt("arm_pickup_position_ticks",0);
        ARM_HOME_POSITION_TICKS = StormProp.getInt("arm_home_position_ticks",0);
        ARM_90_POSITION_TICKS = StormProp.getInt("arm_90_position_ticks",0);
        ARM_135_POSITION_TICKS = StormProp.getInt("arm_135_position_ticks",0);
        ARM_REST_POSITION_TICKS = StormProp.getInt("arm_rest_position_ticks",0);

        //Shuffleboard.selectTab("Arm");
        armTalon = new WPI_TalonSRX(StormProp.getInt("armTalonId",-1));  // SHOULDER   TODO
        pivotTalon = new WPI_TalonSRX(StormProp.getInt("wristTalonId",-1));  // WRIST TODO

        // safety
        reset();

        armTalon.setNeutralMode(NeutralMode.Brake);

        // unloaded position mode
        armTalon.configAllowableClosedloopError(armPositionUnloadedSlotIdx, StormProp.getInt("arm_pos_unloaded_AllowableClosedloopError",0));
        armTalon.config_IntegralZone(armPositionUnloadedSlotIdx, StormProp.getInt("arm_pos_unloaded_IntegralZone",0));
        armTalon.config_kD(armPositionUnloadedSlotIdx, StormProp.getNumber("arm_pos_unloaded_kD",0.0));
        armTalon.config_kF(armPositionUnloadedSlotIdx, StormProp.getNumber("arm_pos_unloaded_kF",0.0));
        armTalon.config_kI(armPositionUnloadedSlotIdx, StormProp.getNumber("arm_pos_unloaded_kI",0.0));
        armTalon.config_kP(armPositionUnloadedSlotIdx, StormProp.getNumber("arm_pos_unloaded_kP",0.0));
        armTalon.configClosedLoopPeakOutput(armPositionUnloadedSlotIdx, StormProp.getNumber("arm_pos_unloaded_ClosedLoopPeakOutput",0.0));

        // loaded position mode
        armTalon.configAllowableClosedloopError(armPositionLoadedSlotIdx, StormProp.getInt("arm_pos_loaded_AllowableClosedloopError",0));
        armTalon.config_IntegralZone(armPositionLoadedSlotIdx, StormProp.getInt("arm_pos_loaded_IntegralZone",0));
        armTalon.config_kD(armPositionLoadedSlotIdx, StormProp.getNumber("arm_pos_loaded_kD",0.0));
        armTalon.config_kF(armPositionLoadedSlotIdx, StormProp.getNumber("arm_pos_loaded_kF",0.0));
        armTalon.config_kI(armPositionLoadedSlotIdx, StormProp.getNumber("arm_pos_loaded_kI",0.0));
        armTalon.config_kP(armPositionLoadedSlotIdx, StormProp.getNumber("arm_pos_loaded_kP",0.0));
        armTalon.configClosedLoopPeakOutput(armPositionLoadedSlotIdx, StormProp.getNumber("arm_pos_loaded_ClosedLoopPeakOutput",0.0));

//        armTalon.configPeakCurrentLimit((StormPROP.getInt("armCurrentLimit")));
//        armTalon.configMotionAcceleration(750);
//        armTalon.configMotionCruiseVelocity(2500);
    }

    public void initDefaultCommand() {
        //setDefaultCommand(new ArmOverride());
    }

    public void periodic() {
        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
        if (isHome()) reset();
    }

    public void reset() {
        armTalon.setSelectedSensorPosition(0);
        pivotTalon.setSelectedSensorPosition(0);
        curArmPos = 0;
        curPivPos = 0;
    }

    private int getArmPositionTicks() {
        return curArmPos;
    }

    public void stop() {
        armTalon.set(ControlMode.PercentOutput, 0);
    }

    public void hold(boolean loaded) {
        if (!isHolding) {
            holdPosition = curArmPos;
            isHolding = true;
        }
        moveToPosition_internal(holdPosition, loaded);
    }

    // Call this function. It will eventually figure out whether you are loaded or not
    public void moveToPosition(int position) {
        isHolding = false;
        moveToPosition_internal(position, false);
    }

    // Don't call this function casually! It makes assusmptions
    private void moveToPosition_internal(int position, boolean loaded) {
        if (isLoaded()) {
            armTalon.selectProfileSlot(armPositionLoadedSlotIdx, 0);
            armTalon.set(ControlMode.Position, position);
        } else {
            armTalon.selectProfileSlot(armPositionUnloadedSlotIdx, 0);
            armTalon.set(ControlMode.Position, position);
        }

    }

    public void moveUpManual() {
        isHolding = false;

        if (isLoaded()) {
            armTalon.set(ControlMode.PercentOutput, -StormProp.getNumber("arm_percent_loaded_up",0.0));
        } else {
            armTalon.set(ControlMode.PercentOutput, -StormProp.getNumber("arm_percent_unloaded_up",0.0));
        }

    }

    public void moveDownManual() {
        isHolding = false;

        if (isLoaded()) {
            armTalon.set(ControlMode.PercentOutput, StormProp.getNumber("arm_percent_loaded_down",0.0));
        } else {
            armTalon.set(ControlMode.PercentOutput, StormProp.getNumber("arm_percent_unloaded_down",0.0));
        }
    }

    public void returnHome(boolean go) {
        isHolding = false;
        if (go) {
            if (isLoaded()) {
                armTalon.set(ControlMode.PercentOutput, -StormProp.getNumber("armReturnPercentOutputLoaded",0.0));
            } else {
                armTalon.set(ControlMode.PercentOutput, -StormProp.getNumber("armReturnPercentOutputUnloaded",0.0));
            }
        } else {
            System.out.println("Stop returning");
        }
    }

    public boolean isHome() {
        return armTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    private boolean isLoaded() {
        // could/should be based on the proximity sensors
        return true;
    }
}
