package org.usfirst.frc5422.Minimec.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmOverride;
import org.usfirst.frc5422.utils.StormProp;
import org.usfirst.frc5422.utils.logging.TalonTuner;
import org.usfirst.frc5422.Minimec.Robot;

import static java.lang.Math.abs;

public class Arm extends Subsystem {
    /*
    PID VALUES FOR ARM:
    IZONE:1000
    kD: 60
    kI: 5*10^-5
    kP: 0.2
     */

    /*
    PID VALUES FOR Pivot:
    IZONE:1000
    kD:
    kI: 5*10^-5
    kP: 0.25

     *///1850
    public final int INITIALTICKS = 150;
    private final int MAX_POSITION = 2180;

    // These can be passed in from the outside
    public final int ARM_PICKUP_POSITION_TICKS = 2000;
    public final int ARM_HOME_POSITION_TICKS = 0;
    public final int ARM_90_POSITION_TICKS = 2500;
    public final int ARM_135_POSITION_TICKS = 3000;

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

//    private TalonTuner armPositionTuner;
//    private TalonTuner armMotionMagicTuner;

    boolean isHolding = false;
    private boolean isReturningHome;
    private double avgPosition;
    private int count;

    public Arm() {

        Shuffleboard.selectTab("Arm");
        armTalon = new WPI_TalonSRX(StormProp.getInt("armTalonId"));  // SHOULDER   TODO
        pivotTalon = new WPI_TalonSRX(StormProp.getInt("wristTalonId"));  // WRIST TODO

        // safety
        reset();
        armTalon.configClosedloopRamp(0.0);

//        armTalon.config_kD(armHatchOnIdx , 60);
//        armTalon.config_kI(armHatchOnIdx , 0.001);
//        armTalon.config_kP(armHatchOnIdx , 10.0);
//        armTalon.config_kF(armHatchOnIdx , 2.5);
//        armTalon.config_IntegralZone(armHatchOnIdx , 100);
//        armTalon.configClosedLoopPeakOutput(armHatchOnIdx, .5);


//        armTalon.config_kD(armVelocitySlotIdx, 0);
//        armTalon.config_kI(armVelocitySlotIdx, 0);
//        armTalon.config_kP(armVelocitySlotIdx, 1.0);
//        armTalon.config_kF(armVelocitySlotIdx, 2.5);
//
//        armTalon.configAllowableClosedloopError(armMotionMagicSlotIdx, 25);
//        armTalon.config_IntegralZone(armMotionMagicSlotIdx, 100);
//        armTalon.config_kD(armMotionMagicSlotIdx, 60);
//        armTalon.config_kF(armMotionMagicSlotIdx, 2.5);
//        armTalon.config_kI(armMotionMagicSlotIdx, 0.001);
//        armTalon.config_kP(armMotionMagicSlotIdx, 5.0);
//        armTalon.configClosedLoopPeakOutput(armMotionMagicSlotIdx, .5);
//        armMotionMagicTuner = new TalonTuner("Arm MotionMagic", armTalon, ControlMode.MotionMagic, armMotionMagicSlotIdx);

        armTalon.setNeutralMode(NeutralMode.Brake);

        armTalon.configAllowableClosedloopError(armPositionUnloadedSlotIdx, 50);
        armTalon.config_IntegralZone(armPositionUnloadedSlotIdx, 100);
        armTalon.config_kD(armPositionUnloadedSlotIdx, 150);
        armTalon.config_kF(armPositionUnloadedSlotIdx, 0);
        armTalon.config_kI(armPositionUnloadedSlotIdx, 0.015);
        armTalon.config_kP(armPositionUnloadedSlotIdx, 1.5);
        armTalon.configClosedLoopPeakOutput(armPositionUnloadedSlotIdx, .25);
        //armMotionMagicTuner = new TalonTuner("Arm Position Unloaded", armTalon, ControlMode.Position, armPositionloadedSlotIdx);

        //armTalon.configPeakCurrentLimit((StormProp.getInt("armCurrentLimit")));
//        armTalon.configMotionAcceleration(750);
//        armTalon.configMotionCruiseVelocity(2500);

//        pivotTalon.setNeutralMode(NeutralMode.Brake);
//        pivotTalon.config_kD(0, 0);
//        pivotTalon.config_IntegralZone(0, 1000);
//        pivotTalon.config_kP(0, .25);
//        pivotTalon.config_kI(0, .00005);
//        pivotTalon.configAllowableClosedloopError(0, 23);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ArmOverride());
    }

    public void periodic() {
        if (isHome()) reset();
    }

    public void reset() {
        armTalon.setSelectedSensorPosition(0);
        pivotTalon.setSelectedSensorPosition(0);
        curArmPos = 0;
        curPivPos = 0;
    }

    public int getArmPositionTicks() {
        return armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void movePivot() {
//        pivotTalon.set(ControlMode.Position, -1024 * 2.5);
    }

    public void movePivotUp() {
//        pivotTalon.set(ControlMode.Position, 0);
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
    public void moveToPosition_internal(int position, boolean loaded) {
        if (loaded) {
//            armTalon.selectProfileSlot(armMotionMagicSlotIdx, 0);
//            armTalon.configMotionAcceleration(250);
//            armTalon.configMotionCruiseVelocity(2500);
//            armTalon.set(ControlMode.MotionMagic, ARM_HOME_POSITION_TICKS);

        } else {
            // Presume unloaded for now
            armTalon.selectProfileSlot(armPositionUnloadedSlotIdx, 0);
            armTalon.set(ControlMode.Position, position);
        }
        curArmPos = getArmPositionTicks();
    }

    public void moveUpManual() {
        moveToPosition(curArmPos - 500);
    }

    public void moveDownManual() {
        moveToPosition(curArmPos + 500);
    }

    public void returnHome(boolean go) {
        isHolding = false;
        if (go) {
            armTalon.set(ControlMode.PercentOutput, -0.25);  // unloaded more like -0.15 - this may be fast
        } else {  // Stop returning
            System.out.println("Stop returning");
        }
    }

    public boolean isHome() {
        return armTalon.getSensorCollection().isRevLimitSwitchClosed();
    }
}


//    public void moveTo(int ticks) {
//
//        int currentArm = getArmPositionTicks();
//        int currentPiv = getWristPositionTicks();
//        if(ticks > 1536*2.5){
//            System.out.println("INVALID");
//        }else{
//
//            if(currentArm > 1024*2.5){
//                armTalon.set(ControlMode.Position, ticks);
//                pivotTalon.set(ControlMode.Position, -(1024*2.5));
//
//            } else {
//                armTalon.set(ControlMode.Position, ticks);
//                pivotTalon.set(ControlMode.Position, ticks);
//            }
//        }
//
//    }


//    public void moveWithPivotUp() {
//        armTalon.selectProfileSlot(1, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, 1024 * 2.65);
//        pivotTalon.set(ControlMode.Position, 0);
//    }

