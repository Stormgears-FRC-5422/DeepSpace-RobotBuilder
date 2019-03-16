package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.elevator.ElevatorOverride;
import org.usfirst.frc5422.utils.StormProp;

public class Elevator extends Subsystem {
    private static Elevator instance;
    private double currentPosition;

    private final double CLIMBER_RADIUS = 5.5;
    private final int TICKS_PER_INCH = 13000;

    public final int REST_POSITION = 6000;
    private final int MAX_POSITION = -2830711;
    private final int IDEAL_MAX = MAX_POSITION-200000;

    private TalonSRX elevatorTalon;

    public static Elevator getInstance()
    {
        if(instance == null) instance = new Elevator();
        return instance;
    }

    public Elevator()
    {
        int kTimeoutMs = 10;

        //elevatorTalon = new TalonSRX((int) Double.parseDouble(StormProp.getString("elevatorTalonID")));
        elevatorTalon = new TalonSRX(18);  //TODO
        //elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0, kTimeoutMs);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        //elevatorTalon.setNeutralMode(NeutralMode.Coast);
        elevatorTalon.configMotionAcceleration(25000);
        elevatorTalon.configMotionCruiseVelocity(50000);
        elevatorTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 10);
        elevatorTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 10);


        elevatorTalon.selectProfileSlot(0, 0);
        elevatorTalon.config_kF(0, 0.02, kTimeoutMs);  // roughly 50K tics / 100 ms
        elevatorTalon.config_kP(0, 20, kTimeoutMs);
        elevatorTalon.config_kI(0, 0, kTimeoutMs);
        elevatorTalon.config_kD(0, 0, kTimeoutMs);
        elevatorTalon.config_IntegralZone(0, 100, kTimeoutMs);
        //elevatorTalon.configAllowableClosedloopError(0,4096,kTimeoutMs); // one rotation is approx 1/10 inch

        elevatorTalon.setSensorPhase(true);
        elevatorTalon.setInverted(true);

        currentPosition = -600000; //getCurrentPositionTicks();
        System.out.println("Current position: " + currentPosition);
    }

    public static void init()
    {
        instance = new Elevator();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorOverride());
    }

    public double getCurrentPositionTicks() { return elevatorTalon.getSensorCollection().getQuadraturePosition(); }

    public void holdElevator() {
        System.out.println("Hold current position: " + currentPosition);
        holdElevator(currentPosition);
    }

    public void holdElevator(double position)
    {
        elevatorTalon.set(ControlMode.Position, position);
        System.out.println("TRYING TO HOLD POSITION: " + position + "  current Position: " + getCurrentPositionTicks());
    }

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
//    public void moveUpManual()
//    {
//        elevatorTalon.set(ControlMode.MotionMagic, IDEAL_MAX);
//    }
//    public void moveDownManual()
//    {
//        elevatorTalon.set(ControlMode.MotionMagic, REST_POSITION);
//    }
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

}
