package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StormProp;

public class Elevator extends Subsystem {
    private static Elevator instance;
    public static Elevator getInstance()
    {
        if(instance == null) instance = new Elevator();
        return instance;
    }

    private final double CLIMBER_RADIUS = 5.5;
    private final int TICKS_PER_INCH = 13000;

    private final int REST_POSITION = 6000;
    private final int MAX_POSITION = -2830711;
    private final int IDEAL_MAX = MAX_POSITION-200000;

    private TalonSRX elevatorTalon;
    private double getCurrentPositionTicks() { return elevatorTalon.getSensorCollection().getQuadraturePosition(); }

    public Elevator()
    {
        //elevatorTalon = new TalonSRX((int) Double.parseDouble(StormProp.getString("elevatorTalonID")));
        elevatorTalon = new TalonSRX(18);  //TODO
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 10);
        elevatorTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 10);
    }

    public static void init()
    {
        instance = new Elevator();
    }

    public double getOutputCurrent() {
        return elevatorTalon.getOutputCurrent();
    }

    public void moveElevatorToPosition(double position)
    {
        boolean lowering = false;
        int multiplier = 1;
        int destination = toTicks(position);
        if(destination > getCurrentPositionTicks())
        {
            //TODO: add PID
            lowering = true;
            multiplier = -1;
        }else if(destination < getCurrentPositionTicks()) {
            //TODO: add PID
            lowering = false;
            multiplier = 1;
        }else {
            lowering = false;
            multiplier = 0;
        }
        double basePower;
        if(lowering) basePower = 0.7;
        else basePower =  1.0;

        elevatorTalon.set(ControlMode.PercentOutput, basePower * multiplier);

        boolean shouldStop;
        do{
            double cpt = getCurrentPositionTicks();
            elevatorTalon.set(ControlMode.PercentOutput, basePower * multiplier);

            if(lowering)shouldStop = cpt > destination - 13000;
            else shouldStop = cpt < destination + 13000;
        }while (!shouldStop);
        holdElevator();
    }

    //returns (theta, elevatorheight)
    public double[] getPositions(int x, int y)
    {
        double theta = Math.atan(y/x);
        double height  = Math.sqrt(Math.pow(CLIMBER_RADIUS, 2) - Math.pow(x,2));
        double elevatorHeight = y - height;
        double values[] = {theta, elevatorHeight};
        return values;
    }

    public void holdElevator()
    {
        elevatorTalon.set(ControlMode.Position, getCurrentPositionTicks());
    }

    public void moveUpManual()
    {
        if (getCurrentPositionTicks() < -1190000) {
            elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        } else {
            elevatorTalon.set(ControlMode.PercentOutput, 0.9);
        }
    }
    public void moveDownManual()
    {
        if(getCurrentPositionTicks() > -1190000){
            elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        }else{
            elevatorTalon.set(ControlMode.PercentOutput, -0.9);
        }
    }

    public void moveElevatorJoystick()
    {
        if(getElevatorJoystick() == 1) moveUpManual();
        else if(getElevatorJoystick() == -1) moveDownManual();
    }

    private int toTicks(double inches)
    {
        elevatorTalon.set(ControlMode.Position, -1000000);
        return 0;
    }

    private double getElevatorJoystick()
    {
        return -1 * Robot.oi.getJoystick1().getRawAxis(1);
    }

    @Override
    protected void initDefaultCommand() {

    }

}
