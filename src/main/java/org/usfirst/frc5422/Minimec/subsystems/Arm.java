package org.usfirst.frc5422.Minimec.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmOverride;
import org.usfirst.frc5422.utils.StormProp;
import org.usfirst.frc5422.utils.logging.TalonTuner;

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
    private final int MAX_POSITION = 2250;
    private final int MIN_POSITION = 100;

    private WPI_TalonSRX armTalon;
    private WPI_TalonSRX pivotTalon;

    private int armPositionSlotIdx = 1;
    private int armMotionMagicSlotIdx = 0;

    double curArmPos;
    double curPivPos;
    private double currentPosition;
    private TalonTuner armPositionTuner;
    private TalonTuner armMotionMagicTuner;

    public Arm() {
        Shuffleboard.selectTab("Arm");
        armTalon = new WPI_TalonSRX(StormProp.getInt("armTalonId"));  // SHOULDER   TODO
        pivotTalon = new WPI_TalonSRX(StormProp.getInt("wristTalonId"));  // WRIST TODO

        // TODO - something more sensible here
        armTalon.setSelectedSensorPosition(0);
        pivotTalon.setSelectedSensorPosition(0);

//        armTalon.config_kD(armMotionMagicSlotIdx , 60);
//        armTalon.config_kI(armMotionMagicSlotIdx , 0.01);
//        armTalon.config_kP(armMotionMagicSlotIdx , 5.0);
//        armTalon.config_kF(armMotionMagicSlotIdx , 2.5);
//        armTalon.config_IntegralZone(armMotionMagicSlotIdx , 100);
        armTalon.config_kD(armMotionMagicSlotIdx , 0);
        armTalon.config_kI(armMotionMagicSlotIdx , 0);
        armTalon.config_kP(armMotionMagicSlotIdx , 0);
        armTalon.config_kF(armMotionMagicSlotIdx , 0);
        armTalon.config_IntegralZone(armMotionMagicSlotIdx , 0);
        armTalon.configAllowableClosedloopError(armMotionMagicSlotIdx , 25);
        armTalon.configClosedLoopPeakOutput(armMotionMagicSlotIdx ,0.10);
        armMotionMagicTuner = new TalonTuner("Arm MotionMagic", armTalon, ControlMode.MotionMagic, armMotionMagicSlotIdx );

//        armTalon.config_kD(armPositionSlotIdx , 1);
//        armTalon.config_kI(armPositionSlotIdx , 0.01);
//        armTalon.config_kP(armPositionSlotIdx , 0.2);
//        armTalon.config_kF(armPositionSlotIdx , 0.0);
//        armTalon.config_IntegralZone(armPositionSlotIdx , 100);
        armTalon.configAllowableClosedloopError(armPositionSlotIdx , 25);
        armTalon.configClosedLoopPeakOutput(armPositionSlotIdx ,0.10);
        armPositionTuner = new TalonTuner("Arm Position", armTalon, ControlMode.Position, armPositionSlotIdx );

        armTalon.setNeutralMode(NeutralMode.Brake);
        //armTalon.configPeakCurrentLimit((StormProp.getInt("armCurrentLimit")));
        armTalon.configMotionAcceleration(750);
        armTalon.configMotionCruiseVelocity(2500);

        pivotTalon.setNeutralMode(NeutralMode.Brake);

        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
        curPivPos = pivotTalon.getSensorCollection().getQuadraturePosition();
        Shuffleboard.getTab("Arm").add("Min Position:", INITIALTICKS);
        Shuffleboard.getTab("Arm").add("Max Position:", MAX_POSITION);
        addChild("armTalon", armTalon);
        addChild("pivotTalon", pivotTalon);
    }

    public double getWristPosition(){return pivotTalon.getSensorCollection().getQuadraturePosition();}
    public double getWristVelocity(){return pivotTalon.getSensorCollection().getQuadratureVelocity();}
    public double getCurrentPositionTicks(){
        return armTalon.getSensorCollection().getQuadraturePosition();
    }


    public static void init() {
    }

    public void moveToBottom() {
//
//        armTalon.selectProfileSlot(0, 0);
//
//        moveTo135();
//
    }

    public void moveTo135() {
//        armTalon.selectProfileSlot(0, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, 1536 * 2.5);
//        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void moveTo90() {
//        armTalon.selectProfileSlot(0, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, 1024 * 2.5);
//        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }


    public void moveTo45() {
//        armTalon.selectProfileSlot(0, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, (1024 * 2.5)/2);
//        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void movePivot() {
//        pivotTalon.set(ControlMode.Position, -1024 * 2.5);
    }

    public void movePivotUp() {
//        pivotTalon.set(ControlMode.Position, 0);
    }


    public void moveDown() {
//        moveTo135();
//        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void stop(){
//        armTalon.set(ControlMode.Velocity, 0);
    }

    public void moveUp() {
//        moveToRest();
    }

    public void moveWithPivotUp() {
//        armTalon.selectProfileSlot(1, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, 1024 * 2.65);
//        pivotTalon.set(ControlMode.Position, 0);
    }

    public void moveToRest() {
//        armTalon.selectProfileSlot(1, 0);
//        armTalon.configMotionAcceleration(250);
//        armTalon.configMotionCruiseVelocity(2500);
//        armTalon.set(ControlMode.MotionMagic, INITIALTICKS);
    }

    public double getArmEnc() {
        return armTalon.getSensorCollection().getQuadraturePosition();
    }

    public double getPivotEnc() {
        return pivotTalon.getSensorCollection().getQuadraturePosition();
    }

    public void move() {
//        SmartDashboard.putNumber("ENC VAL", armTalon.getSensorCollection().getQuadraturePosition());
    }

    public void moveTo(int ticks){
//
//        int currentArm = armTalon.getSensorCollection().getQuadraturePosition();
//        int currentPiv = pivotTalon.getSensorCollection().getQuadraturePosition();
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
    }

    public void moveUpManual()
    {
        currentPosition = getCurrentPositionTicks();
        armTalon.selectProfileSlot(armMotionMagicSlotIdx,0);
        armTalon.set(ControlMode.MotionMagic, INITIALTICKS);
    }

    public void moveDownManual()
    {
        currentPosition = getCurrentPositionTicks();
        armTalon.selectProfileSlot(armMotionMagicSlotIdx,0);
        armTalon.set(ControlMode.MotionMagic, MAX_POSITION);
    }

    public void hold()
    {
//        System.out.println("TRYING TO HOLD POSITION: " + currentPosition);
//        armTalon.selectProfileSlot(armPositionSlotIdx,0);
//        armTalon.set(ControlMode.Position, currentPosition);
    }

    public void initDefaultCommand(){
        setDefaultCommand(new ArmOverride());
    }

    public void periodic(){
        armMotionMagicTuner.periodic();
    }
}

