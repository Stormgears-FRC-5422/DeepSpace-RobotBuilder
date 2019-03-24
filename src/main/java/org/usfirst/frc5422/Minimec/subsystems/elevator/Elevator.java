package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorOverride;
import org.usfirst.frc5422.utils.StormProp;
import org.usfirst.frc5422.utils.logging.TalonTuner;

public class Elevator extends Subsystem {
    private static Elevator instance;
    private double currentPosition;

    private final int TICKS_PER_INCH = 512;

    public final int REST_POSITION = 0;
    private final int MAX_POSITION = 27500;

    private WPI_TalonSRX elevatorTalon;
    private TalonTuner elevatorMotionMagicTuner;

    public Elevator()
    {
        int kTimeoutMs = 10;
        int slotIdx = 0;
        elevatorTalon = new WPI_TalonSRX(StormProp.getInt("elevatorTalonId"));
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, kTimeoutMs);
        elevatorTalon.configForwardSoftLimitThreshold(MAX_POSITION, kTimeoutMs);
        elevatorTalon.configForwardSoftLimitEnable(true);

        reset();

        elevatorTalon.configMotionAcceleration(500);
        elevatorTalon.configMotionCruiseVelocity(1000);

        elevatorTalon.selectProfileSlot(slotIdx , 0);
        elevatorTalon.config_kF(slotIdx , 2.0, kTimeoutMs);
        elevatorTalon.config_kP(slotIdx , 2.0, kTimeoutMs);
        elevatorTalon.config_kI(slotIdx , 0.02, kTimeoutMs);
        elevatorTalon.config_kD(slotIdx , 10.0, kTimeoutMs);
        elevatorTalon.config_IntegralZone(slotIdx , 100, kTimeoutMs);
        elevatorTalon.configAllowableClosedloopError(0,410,kTimeoutMs);

        //elevatorMotionMagicTuner = new TalonTuner("ElevatorMotionMagic", elevatorTalon, ControlMode.MotionMagic, slotIdx);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorOverride());
    }

    public void reset() {
        elevatorTalon.setSelectedSensorPosition(0);
        currentPosition = 0;
    }

    public void moveToPosition(double position){
        elevatorTalon.set(ControlMode.MotionMagic, position);
    }

    public double getCurrentPositionTicks() { return elevatorTalon.getSensorCollection().getQuadraturePosition(); }
    public double getCurrentVelocity(){return elevatorTalon.getSensorCollection().getQuadratureVelocity();}
    public void hold() {
        hold(currentPosition);
    }

    public void hold(double position)
    {

        //elevatorTalon.set(ControlMode.Position, position);
        elevatorTalon.set(ControlMode.Velocity, 0);

        //System.out.println("TRYING TO HOLD POSITION: " + position + "  current Position: " + getCurrentPositionTicks());
    }

    public void moveUpManual()
    {
        elevatorTalon.set(ControlMode.MotionMagic, MAX_POSITION);
        //elevatorTalon.set(ControlMode.Velocity, 1000);
        currentPosition = getCurrentPositionTicks();
    }

    public void moveTo(double ticks)
    {
        elevatorTalon.set(ControlMode.MotionMagic, ticks);
        currentPosition = getCurrentPositionTicks();
    }


    public void moveDownManual()
    {
        if (! Robot.oi.getControlOverride())
            elevatorTalon.set(ControlMode.MotionMagic, REST_POSITION);
        else
            elevatorTalon.set(ControlMode.Velocity, -1000);

        currentPosition = getCurrentPositionTicks();
    }

    public void periodic(){
        //elevatorMotionMagicTuner.periodic();
    }

}


//
//    public void stopElevator()
//    {
//        elevatorTalon.set(ControlMode.Velocity, 0);
//    }
//
//    private int toTicks(double inches)
//    {
//        elevatorTalon.set(ControlMode.Position, -1000000);
//        return 0;
//    }
//
//

//    public double getOutputCurrent() {
//        return elevatorTalon.getOutputCurrent();
//    }
//
//    public void moveElevatorToPosition(double position)
//    {
//        boolean lowering = false;
//        int multiplier = 1;
//        int destination = toTicks(position);
//        if(destination > getCurrentPositionTicks())
//        {
//            //TODO: add PID
//            lowering = true;
//            multiplier = -1;
//        }else if(destination < getCurrentPositionTicks()) {
//            //TODO: add PID
//            lowering = false;
//            multiplier = 1;
//        }else {
//            lowering = false;
//            multiplier = 0;
//        }
//        double basePower;
//        if(lowering) basePower = 0.7;
//        else basePower =  1.0;
//
//        elevatorTalon.set(ControlMode.PercentOutput, basePower * multiplier);
//
//        boolean shouldStop;
//        do{
//            double cpt = getCurrentPositionTicks();
//            elevatorTalon.set(ControlMode.PercentOutput, basePower * multiplier);
//
//            if(lowering)shouldStop = cpt > destination - 13000;
//            else shouldStop = cpt < destination + 13000;
//        }while (!shouldStop);
//        //holdElevator();
//    }
//
//    //returns (theta, elevatorheight)
//    public double[] getPositions(int x, int y)
//    {
//        double theta = Math.atan(y/x);
//        double height  = Math.sqrt(Math.pow(CLIMBER_RADIUS, 2) - Math.pow(x,2));
//        double elevatorHeight = y - height;
//        double values[] = {theta, elevatorHeight};
//        return values;
//    }
//
//
