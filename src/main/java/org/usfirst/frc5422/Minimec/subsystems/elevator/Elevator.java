package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorOverride;
import org.usfirst.frc5422.utils.StormProp;

public class Elevator extends Subsystem {
    private int currentPosition;
    private int targetPosition;
    private int allowableError;

    public final int REST_POSITION;
    private final int MAX_POSITION;

    private WPI_TalonSRX elevatorTalon;
    //private TalonTuner elevatorMotionMagicTuner;
    public Elevator()
    {
        int slotIdx = 0;
        int kTimeoutMs = StormProp.getInt("canTimeout");
        REST_POSITION = StormProp.getInt("elevatorRestPosition");
        MAX_POSITION = StormProp.getInt("elevatorMaxPosition");
        elevatorTalon = new WPI_TalonSRX(StormProp.getInt("elevatorTalonId"));

        elevatorTalon.setNeutralMode(NeutralMode.Brake);
        elevatorTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, kTimeoutMs);
        elevatorTalon.configForwardSoftLimitThreshold(MAX_POSITION, kTimeoutMs);
        elevatorTalon.configForwardSoftLimitEnable(true);

        reset();

        elevatorTalon.configMotionAcceleration(StormProp.getInt("elevatorMMAcceleration"));
        elevatorTalon.configMotionCruiseVelocity(StormProp.getInt("elevatorMMVelocity"));

        elevatorTalon.selectProfileSlot(slotIdx , 0);
        elevatorTalon.config_kF(slotIdx , StormProp.getNumber("elevatorConfig_kF"), kTimeoutMs);
        elevatorTalon.config_kP(slotIdx , StormProp.getNumber("elevatorConfig_kP"), kTimeoutMs);
        elevatorTalon.config_kI(slotIdx , StormProp.getNumber("elevatorConfig_kI"), kTimeoutMs);
        elevatorTalon.config_kD(slotIdx , StormProp.getNumber("elevatorConfig_kD"), kTimeoutMs);
        elevatorTalon.config_IntegralZone(slotIdx , StormProp.getInt("elevatorConfig_iZone"), kTimeoutMs);
        allowableError = StormProp.getInt("elevatorAllowableError");
        elevatorTalon.configAllowableClosedloopError(0,allowableError,kTimeoutMs);
        //elevatorMotionMagicTuner = new TalonTuner("ElevatorMotionMagic", elevatorTalon, ControlMode.MotionMagic, slotIdx);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorOverride());
    }

    public void reset() {
        elevatorTalon.setSelectedSensorPosition(0);
        currentPosition = 0;
        targetPosition = 0;
    }

    public int getCurrentPositionTicks() { return elevatorTalon.getSensorCollection().getQuadraturePosition(); }
    public double getCurrentVelocity(){return elevatorTalon.getSensorCollection().getQuadratureVelocity();}

    // This elevator just doesn't move from gravity. No reason to hold position
    public void hold() {
        targetPosition = currentPosition;
        elevatorTalon.set(ControlMode.Velocity, 0);
    }

    public void moveToPosition(int position){
        targetPosition = position;
        elevatorTalon.set(ControlMode.MotionMagic, targetPosition);
        currentPosition = getCurrentPositionTicks();
    }

    public void moveUpManual()
    {
        targetPosition = MAX_POSITION;
        elevatorTalon.set(ControlMode.MotionMagic, targetPosition);
        currentPosition = getCurrentPositionTicks();
    }

    public void moveDownManual()
    {
        // Allow override by pressing the right button - in case position gets messed up.
        // probably not necessary after returnHome was implemented, but safe to leave here.
        targetPosition = REST_POSITION;
        if (! Robot.oi.getControlOverride())
            elevatorTalon.set(ControlMode.MotionMagic, targetPosition );
        else
            elevatorTalon.set(ControlMode.Velocity, -StormProp.getInt("elevatorMMVelocity"));

        currentPosition = getCurrentPositionTicks();
    }

    public void periodic(){
        currentPosition = getCurrentPositionTicks();

        //elevatorMotionMagicTuner.periodic();
        if (isHome()) {
            reset();
        }
    }

    public void returnHome(boolean go) {
        if (go) {
            elevatorTalon.set(ControlMode.Velocity, -StormProp.getInt("elevatorMMVelocity"));
        } else {  // Stop returning
            elevatorTalon.set(ControlMode.Velocity, 0);
        }
    }

    public boolean isHome() {
        // Switches are normally closed - triggered means they would be open
        return !elevatorTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    public boolean isFinished() {
        return (Math.abs(getCurrentPositionTicks() - targetPosition) < allowableError);
    }
}

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
