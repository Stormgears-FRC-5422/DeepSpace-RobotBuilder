package org.usfirst.frc5422.Minimec.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.usfirst.frc5422.utils.StormProp;
import org.usfirst.frc5422.utils.logging.TalonTuner;

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

    private WPI_TalonSRX armTalon;
    private WPI_TalonSRX pivotTalon;

    private final int armMotionMagicSlotIdx = 0;
    private final int armPositionSlotIdx = 1;
    private final int armVelocitySlotIdx = 2;

    //    private final int armHatchOnIdx = 3;
    double curArmPos;
    double curPivPos;
//    private TalonTuner armPositionTuner;
//    private TalonTuner armMotionMagicTuner;

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
//        armTalon.config_kD(armMotionMagicSlotIdx, 60);
//        armTalon.config_kI(armMotionMagicSlotIdx, 0.001);
//        armTalon.config_kP(armMotionMagicSlotIdx, 5.0);
//        armTalon.config_kF(armMotionMagicSlotIdx, 2.5);
//        armTalon.config_IntegralZone(armMotionMagicSlotIdx, 100);
//        armTalon.configClosedLoopPeakOutput(armMotionMagicSlotIdx, .5);
//        armTalon.configAllowableClosedloopError(armMotionMagicSlotIdx, 25);
//        armTalon.configClosedLoopPeakOutput(armMotionMagicSlotIdx, 0.25);
//        armMotionMagicTuner = new TalonTuner("Arm MotionMagic", armTalon, ControlMode.MotionMagic, armMotionMagicSlotIdx);

//        armTalon.config_kD(armPositionSlotIdx, 5);
//        armTalon.config_kI(armPositionSlotIdx, 0.1);
//        armTalon.config_kP(armPositionSlotIdx, 2.5);
//        armTalon.config_kF(armPositionSlotIdx, 0.0);
//        armTalon.config_IntegralZone(armPositionSlotIdx, 100);
//        armTalon.configAllowableClosedloopError(armPositionSlotIdx, 100);
//        armTalon.configClosedLoopPeakOutput(armPositionSlotIdx, 0.25);
//        armPositionTuner = new TalonTuner("Arm Position", armTalon, ControlMode.Position, armPositionSlotIdx);

        armTalon.setNeutralMode(NeutralMode.Brake);
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
        //setDefaultCommand(new ArmOverride());
    }

    public void periodic() {
        if (isHome()) reset();

//        armMotionMagicTuner.periodic();
//        armPositionTuner.periodic();
    }

    public void reset() {
        armTalon.setSelectedSensorPosition(0);
        pivotTalon.setSelectedSensorPosition(0);
        curArmPos = 0;
        curPivPos = 0;
    }

    public double getWristPositionTicks() {
        return pivotTalon.getSensorCollection().getQuadraturePosition();
    }

    public double getArmPositionTicks() {
        return armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void moveTo135() {
//        armTalon.selectProfileSlot(0, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, 1536 * 2.5);
//        curArmPos = getArmPositionTicks();
    }

    public void moveTo90() {
        armTalon.selectProfileSlot(0, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, 1024 * 2.5);
        curArmPos = getArmPositionTicks();
    }

    public void moveTo45() {
//        armTalon.selectProfileSlot(0, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, (1024 * 2.5)/2);
//        curArmPos = getArmPositionTicks();
    }

    public void movePivot() {
//        pivotTalon.set(ControlMode.Position, -1024 * 2.5);
    }

    public void movePivotUp() {
//        pivotTalon.set(ControlMode.Position, 0);
    }


    public void moveDown() {
//        moveTo135();
//        curArmPos = getArmPositionTicks();
    }

    public void moveToRest() {
        armTalon.selectProfileSlot(1, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, INITIALTICKS);
    }

    public void moveUpManual() {
        armTalon.selectProfileSlot(armMotionMagicSlotIdx, 0);
        armTalon.set(ControlMode.MotionMagic, INITIALTICKS);
        curArmPos = getArmPositionTicks();
    }

    public void moveDownManual() {
        armTalon.selectProfileSlot(armMotionMagicSlotIdx, 0);
        armTalon.set(ControlMode.MotionMagic, MAX_POSITION);
        curArmPos = getArmPositionTicks();
    }

    public void stop() {
        armTalon.set(ControlMode.PercentOutput, 0);
    }

    public void hold() {
        armTalon.selectProfileSlot(armPositionSlotIdx,0);
        armTalon.set(ControlMode.Position, curArmPos);
    }

    public void returnHome(boolean go) {
        if (go) {
            armTalon.set(ControlMode.PercentOutput, -0.25);  // unloaded more like -0.15 - this may be fast

        } else {  // Stop returning
            armTalon.set(ControlMode.PercentOutput, 0.0);
        }
    }

    public boolean isHome() {
        return armTalon.getSensorCollection().isRevLimitSwitchClosed();
    }
}


//    public void holdWrist() {
//        pivotTalon.set(ControlMode.Position, 0);
//    }


//    public void moveToBottom() {
//
//        armTalon.selectProfileSlot(0, 0);
//
//        moveTo135();
//
//    }

//    public void move() {
//        SmartDashboard.putNumber("ENC VAL", armTalon.getSensorCollection().getQuadraturePosition());
//    }

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

//    public void moveUp() {
//        moveToRest();
//    }

//    public void moveWithPivotUp() {
//        armTalon.selectProfileSlot(1, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, 1024 * 2.65);
//        pivotTalon.set(ControlMode.Position, 0);
//    }

