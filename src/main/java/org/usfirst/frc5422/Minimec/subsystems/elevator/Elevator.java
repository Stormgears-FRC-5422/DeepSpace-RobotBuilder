package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.utils.StormProp;

public class Elevator extends Subsystem {
    public static Elevator instance;
    public Elevator getInstance()
    {
        return instance;
    }

    private final double CLIMBER_RADIUS = 5.5;
    private final int TICKS_PER_INCH = 13000;

    private TalonSRX elevatorTalon;
    private double currentPositionTicks = elevatorTalon.getSensorCollection().getQuadraturePosition();

    public Elevator()
    {
        elevatorTalon = new TalonSRX((int) Double.parseDouble(StormProp.getString("elevatorTalonID")));
    }

    public static void init()
    {
        instance = new Elevator();
    }

    public void moveElevatorToPosition(double position)
    {
        boolean lowering = false;
        int multiplier = 1;
        int destination = toTicks(position);
        if(destination > currentPositionTicks)
        {
            //TODO: add PID
            lowering = true;
            multiplier = -1;
        }else if(destination < currentPositionTicks) {
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
            double cpt = currentPositionTicks;
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
        elevatorTalon.set(ControlMode.Position, currentPositionTicks);
    }

    public void moveUpManual()
    {
        if (currentPositionTicks < -1190000) {
            elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        } else {
            elevatorTalon.set(ControlMode.PercentOutput, 0.9);
        }
    }
    public void moveDownManual()
    {
        if(currentPositionTicks > -1190000){
            elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        }else{
            elevatorTalon.set(ControlMode.PercentOutput, -0.9);
        }
    }

    private int toTicks(double inches)
    {
        return (int)Math.round(inches * TICKS_PER_INCH);
    }

    @Override
    protected void initDefaultCommand() {

    }

}
