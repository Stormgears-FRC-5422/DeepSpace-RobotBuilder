package org.usfirst.frc5422.Minimec.subsystems.elevator;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorOverride;
import org.usfirst.frc5422.utils.StormProp;

public class Elevator extends Subsystem {
    private int currentPosition;
    private int targetPosition;
    private int allowableError;

    private final int REST_POSITION;
    private final int MAX_POSITION;
    private final int LEVEL2_POSITION;
    private final int NEG_INFINITY = Integer.MIN_VALUE;
    private final int INFINITY = Integer.MAX_VALUE;

    private final int kTimeoutMs;
    private WPI_TalonSRX elevatorTalon;
    //private TalonTuner elevatorMotionMagicTuner;
    public Elevator()
    {
        int slotIdx = 0;
        kTimeoutMs = StormProp.getInt("canTimeout",0);
        REST_POSITION = StormProp.getInt("elevatorRestPosition",0);
        MAX_POSITION = StormProp.getInt("elevatorMaxPosition",0);
        LEVEL2_POSITION = StormProp.getInt("elevatorLevelTwoHeight",0);
        elevatorTalon = new WPI_TalonSRX(StormProp.getInt("elevatorTalonId",-1));

        elevatorTalon.setNeutralMode(NeutralMode.Brake);

        elevatorTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, kTimeoutMs);

        elevatorTalon.configReverseSoftLimitThreshold(StormProp.getInt("elevatorLevelTwoHeight",0), kTimeoutMs);
        elevatorTalon.configReverseSoftLimitEnable(false);

        elevatorTalon.configForwardSoftLimitThreshold(MAX_POSITION, kTimeoutMs);
        elevatorTalon.configForwardSoftLimitEnable(true);

        // safety
        reset();

        elevatorTalon.configMotionAcceleration(StormProp.getInt("elevatorMMAcceleration",0));
        elevatorTalon.configMotionCruiseVelocity(StormProp.getInt("elevatorMMVelocity",0));

        elevatorTalon.selectProfileSlot(slotIdx , 0);
        elevatorTalon.config_kF(slotIdx , StormProp.getNumber("elevatorConfig_kF",0.0), kTimeoutMs);
        elevatorTalon.config_kP(slotIdx , StormProp.getNumber("elevatorConfig_kP",0.0), kTimeoutMs);
        elevatorTalon.config_kI(slotIdx , StormProp.getNumber("elevatorConfig_kI",0.0), kTimeoutMs);
        elevatorTalon.config_kD(slotIdx , StormProp.getNumber("elevatorConfig_kD",0.0), kTimeoutMs);
        elevatorTalon.config_IntegralZone(slotIdx , StormProp.getInt("elevatorConfig_iZone",0), kTimeoutMs);
        allowableError = StormProp.getInt("elevatorAllowableError",0);
        elevatorTalon.configAllowableClosedloopError(0,allowableError,kTimeoutMs);
        //elevatorMotionMagicTuner = new TalonTuner("ElevatorMotionMagic", elevatorTalon, ControlMode.MotionMagic, slotIdx);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new ElevatorOverride());
    }

    public int getCurrentPositionTicks() { return currentPosition; }

    public void reset() {
        elevatorTalon.setSelectedSensorPosition(0);
        currentPosition = 0;
        targetPosition = 0;
    }

    // This elevator just doesn't move from gravity. No reason to actively hold position
    public void stop() {
        targetPosition = currentPosition;
        elevatorTalon.set(ControlMode.PercentOutput, 0.0);
    }
//    public void moveToTicks(int ticks){
//
//        elevatorTalon.configForwardSoftLimitThreshold(ticks, kTimeoutMs);
//        elevatorTalon.set(ControlMode.PercentOutput, StormProp.getInt("elevatorClimbPercent", 0));
//    }
    
//    public void moveToTicks(int ticks, int level){
//        switch (level) {
//            case 3: // must be moving up
//                // Moving up - change the limit to level 3
//                targetPosition = MAX_POSITION ;
//                elevatorTalon.configForwardSoftLimitThreshold(targetPosition, kTimeoutMs);
//                elevatorTalon.set(ControlMode.PercentOutput, StormProp.getInt("elevatorClimbPercent",0));
//                break;
//            case 2:
//                // what if these are changing when this gets called - ugh
//                targetPosition = LEVEL2_POSITION + ticks;
//                if ( currentPosition < targetPosition) {
//                    // Moving up - change the limit to level 2
//                    elevatorTalon.configForwardSoftLimitThreshold(targetPosition, kTimeoutMs);
//                    elevatorTalon.set(ControlMode.PercentOutput, StormProp.getInt("elevatorClimbPercent",0));
//                } else {
//                    // Moving down
//                    elevatorTalon.configReverseSoftLimitEnable(true);
//                    elevatorTalon.set(ControlMode.PercentOutput, -StormProp.getInt("elevatorReturnPercent",0));
//                }
//                break;
//            case 0:  // must be moving down
//            default:
//                targetPosition = REST_POSITION + ticks;
//                elevatorTalon
//                        .configReverseSoftLimitEnable(false); // still have the hard limit - don't want to stop on level 2
//                elevatorTalon.set(ControlMode.PercentOutput, -StormProp.getInt("elevatorReturnPercent",0));
//        } }

    public void moveToLevel(int level){
        switch (level) {
            case 3: // must be moving up
                // Moving up - change the limit to level 3
                targetPosition = MAX_POSITION;
                elevatorTalon.configForwardSoftLimitThreshold(targetPosition, kTimeoutMs);
                elevatorTalon.set(ControlMode.PercentOutput, StormProp.getInt("elevatorClimbPercent",0));
                break;
            case 2:
                // what if these are changing when this gets called - ugh
                targetPosition = LEVEL2_POSITION;
                if ( currentPosition < targetPosition) {
                    // Moving up - change the limit to level 2
                    elevatorTalon.configForwardSoftLimitThreshold(targetPosition, kTimeoutMs);
                    elevatorTalon.set(ControlMode.PercentOutput, StormProp.getInt("elevatorClimbPercent",0));
                } else {
                    // Moving down
                    elevatorTalon.configReverseSoftLimitEnable(true);
                    elevatorTalon.set(ControlMode.PercentOutput, -StormProp.getInt("elevatorReturnPercent",0));
                }
                break;
            case 0:  // must be moving down
            default:
                targetPosition = REST_POSITION;
                elevatorTalon.configReverseSoftLimitEnable(false); // still have the hard limit - don't want to stop on level 2
                elevatorTalon.set(ControlMode.PercentOutput, -StormProp.getInt("elevatorReturnPercent",0));
        }

    }

    public void moveUpManual()
    {
        // Moving up - change the limit to level 3
        targetPosition = INFINITY;
        elevatorTalon.configForwardSoftLimitThreshold(MAX_POSITION, kTimeoutMs);
        elevatorTalon.set(ControlMode.PercentOutput, StormProp.getInt("elevatorReturnPercent",0));
    }



    public void moveDownManual()
    {
        targetPosition = NEG_INFINITY;
        elevatorTalon.configReverseSoftLimitEnable(false); // still have the hard limit - don't want to stop on level 2
        elevatorTalon.set(ControlMode.PercentOutput, -StormProp.getInt("elevatorReturnPercent",0));
    }

    public void periodic(){
        currentPosition = elevatorTalon.getSensorCollection().getQuadraturePosition();
        if (isHome()) reset();
    }

    public void returnHome(boolean go) {
        if (go) {
            targetPosition = NEG_INFINITY;
            elevatorTalon.configReverseSoftLimitEnable(false); // still have the hard limit - don't want to stop on level 2
            elevatorTalon.set(ControlMode.PercentOutput, -StormProp.getInt("elevatorReturnPercent",0));
        } else {  // Stop returning
            stop();
        }
    }

    public boolean isHome() {
        // Switches are normally closed - triggered means they would be open
        return !elevatorTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    // For elevatorMove commands
    public boolean isFinished() {
        return (Math.abs(currentPosition - targetPosition) < allowableError);
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
