package org.usfirst.frc5422.Minimec.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.naming.ldap.Control;

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
     */

    private final int INITIALTICKS = 58;



    private Timer timer;

    private TalonSRX armTalon;
    private TalonSRX pivotTalon;

    double curArmPos;
    double curPivPos;

    public Arm() {
        timer = new Timer();
        armTalon = new TalonSRX(12);
        pivotTalon = new TalonSRX(13);

        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();
        curPivPos = pivotTalon.getSensorCollection().getQuadraturePosition();
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
        pivotTalon.set(ControlMode.Position, 5);
    }


    public void moveDown() {

        moveTo135();

        curArmPos = armTalon.getSensorCollection().getQuadraturePosition();


    }

    public void stop(){
        armTalon.set(ControlMode.Position, 0);
        pivotTalon.set(ControlMode.Position, 0);
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

            }else {
                armTalon.set(ControlMode.Position, ticks);
                pivotTalon.set(ControlMode.Position, ticks);
            }



        }

    }

    public void initDefaultCommand(){

    }

    public void periodic(){

    }
}

