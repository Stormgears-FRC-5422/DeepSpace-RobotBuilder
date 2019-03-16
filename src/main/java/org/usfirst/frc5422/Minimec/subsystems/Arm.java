package org.usfirst.frc5422.Minimec.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.commands.Arm.ArmOverride;
import org.usfirst.frc5422.utils.StormProp;

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
    public final int INITIALTICKS = 100;
    private final int MAX_POSITION = 1550;
    private final int MIN_POSITION = 100;

    private Timer timer;

    private TalonSRX armTalon;
    private TalonSRX pivotTalon;

    double curArmPos;
    double curPivPos;


    public Arm() {

        timer = new Timer();
        armTalon = new TalonSRX(StormProp.getInt("armTalonId"));  // SHOULDER   TODO
        armTalon.setNeutralMode(NeutralMode.Brake);
        //armTalon.configPeakCurrentLimit((StormProp.getInt("armCurrentLimit")));
        armTalon.configMotionAcceleration(750);
        armTalon.configMotionCruiseVelocity(2500);
        pivotTalon = new TalonSRX(StormProp.getInt("wristTalonId"));  // WRIST TODO
        pivotTalon.setNeutralMode(NeutralMode.Coast);
        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
        curPivPos = pivotTalon.getSensorCollection().getQuadraturePosition();
    }

    public double getWristPosition(){return pivotTalon.getSensorCollection().getQuadraturePosition();}
    public double getWristVelocity(){return pivotTalon.getSensorCollection().getQuadratureVelocity();}
    public double getCurrentPositionTicks(){
        return armTalon.getSensorCollection().getQuadraturePosition();
    }

    public static void init() {
    }

    public void moveToBottom() {

        armTalon.selectProfileSlot(0, 0);

        moveTo135();

    }

    public void moveTo135() {
        armTalon.selectProfileSlot(0, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, 1536 * 2.5);
        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void moveTo90() {
        armTalon.selectProfileSlot(0, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, 1024 * 2.5);
        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }


    public void moveTo45() {
        armTalon.selectProfileSlot(0, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, (1024 * 2.5)/2);
        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void movePivot() {
        pivotTalon.set(ControlMode.Position, -1024 * 2.5);
    }

    public void movePivotUp() {
        pivotTalon.set(ControlMode.Position, 0);
    }


    public void moveDown() {
        moveTo135();
        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
    }

    public void stop(){
        armTalon.set(ControlMode.Velocity, 0);
    }

    public void moveUp() {
        moveToRest();
    }

    public void moveWithPivotUp() {
        armTalon.selectProfileSlot(1, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, 1024 * 2.65);
        pivotTalon.set(ControlMode.Position, 0);
    }

    public void moveToRest() {
        armTalon.selectProfileSlot(1, 0);
        armTalon.configMotionAcceleration(250);
        armTalon.configMotionCruiseVelocity(2500);
        armTalon.set(ControlMode.MotionMagic, INITIALTICKS);
    }

    public double getArmEnc() {
        return armTalon.getSensorCollection().getQuadraturePosition();
    }
    public double getPivotEnc(){return pivotTalon.getSensorCollection().getQuadraturePosition();}

    public void move() {
        SmartDashboard.putNumber("ENC VAL", armTalon.getSensorCollection().getQuadraturePosition());
    }

    public void moveTo(int ticks){

        int currentArm = armTalon.getSensorCollection().getQuadraturePosition();
        int currentPiv = pivotTalon.getSensorCollection().getQuadraturePosition();
        if(ticks > 1536*2.5){
            System.out.println("INVALID");
        }else{

            if(currentArm > 1024*2.5){
                armTalon.set(ControlMode.Position, ticks);
                pivotTalon.set(ControlMode.Position, -(1024*2.5));

            } else {
                armTalon.set(ControlMode.Position, ticks);
                pivotTalon.set(ControlMode.Position, ticks);
            }
        }

    }

    public void moveUpManual()
    {
        armTalon.selectProfileSlot(1,0);
        System.out.println("moveUpManual()");
        armTalon.set(ControlMode.MotionMagic, INITIALTICKS);
        System.out.println(armTalon.getSensorCollection().getQuadratureVelocity());
    }

    public void moveDownManual()
    {
        armTalon.selectProfileSlot(0,0);
        System.out.println("moveDownManual()");
        armTalon.set(ControlMode.MotionMagic, MAX_POSITION);
        System.out.println(armTalon.getSensorCollection().getQuadratureVelocity());
    }

    public void hold(double position)
    {
        System.out.println("TRYING TO HOLD POSITION: " + position);
        armTalon.set(ControlMode.Position, position);
    }

    public void initDefaultCommand(){
        setDefaultCommand(new ArmOverride());
    }

    public void periodic(){

    }
}

