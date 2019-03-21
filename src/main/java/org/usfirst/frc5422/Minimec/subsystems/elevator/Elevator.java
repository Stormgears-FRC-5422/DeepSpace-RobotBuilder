package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.commands.elevator.ElevatorOverride;
import org.usfirst.frc5422.utils.StormProp;
import org.usfirst.frc5422.utils.logging.TalonTuner;

public class Elevator extends Subsystem {
    private static Elevator instance;
    private double currentPosition;

    private final double CLIMBER_RADIUS = 5.5;
    private final int TICKS_PER_INCH = 350;

    public final int REST_POSITION = 0;
    private final int MAX_POSITION = 25000;
    private final int IDEAL_MAX = MAX_POSITION-2000;

    private WPI_TalonSRX elevatorTalon;
    private TalonTuner elevatorMotionMagicTuner;

    public static Elevator getInstance()
    {
        if(instance == null) instance = new Elevator();
        return instance;
    }

    public Elevator()
    {
        int kTimeoutMs = 10;
        int slotIdx = 0;
        elevatorTalon = new WPI_TalonSRX(StormProp.getInt("elevatorTalonId"));
        elevatorTalon.setSelectedSensorPosition(0);

        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorTalon.configMotionAcceleration(410);
        elevatorTalon.configMotionCruiseVelocity(410);
//        elevatorTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 10);
//        elevatorTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 10);

        elevatorTalon.selectProfileSlot(slotIdx , 0);
        elevatorTalon.config_kF(slotIdx , 0, kTimeoutMs);
        elevatorTalon.config_kP(slotIdx , 0, kTimeoutMs);
        elevatorTalon.config_kI(slotIdx , 0, kTimeoutMs);
        elevatorTalon.config_kD(slotIdx , 0, kTimeoutMs);
        elevatorTalon.config_IntegralZone(slotIdx , 100, kTimeoutMs);
        elevatorTalon.configAllowableClosedloopError(0,410,kTimeoutMs);

        elevatorTalon.setInverted(true);
        elevatorTalon.setSensorPhase(true);

        elevatorMotionMagicTuner = new TalonTuner("ElevatorPosition", elevatorTalon, ControlMode.MotionMagic, slotIdx);

        currentPosition = 0; //getCurrentPositionTicks();
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

    public void hold() {
//        System.out.println("Hold current position: " + currentPosition);
//        hold(currentPosition);
    }

    public void hold(double position)
    {
        //elevatorTalon.set(ControlMode.Position, position);
        //System.out.println("TRYING TO HOLD POSITION: " + position + "  current Position: " + getCurrentPositionTicks());
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
    public void moveUpManual()
    {
        elevatorTalon.set(ControlMode.MotionMagic, IDEAL_MAX);
    }
    public void moveDownManual()
    {
        elevatorTalon.set(ControlMode.MotionMagic, REST_POSITION);
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

    public void periodic(){
        elevatorMotionMagicTuner.periodic();
    }

}
