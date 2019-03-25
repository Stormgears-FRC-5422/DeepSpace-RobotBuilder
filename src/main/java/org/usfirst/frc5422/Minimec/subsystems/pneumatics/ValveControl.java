package org.usfirst.frc5422.Minimec.subsystems.pneumatics;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.EnableVacuum;
import org.usfirst.frc5422.utils.StormProp;

public class ValveControl extends Subsystem {
    boolean vacRunning;
    private Solenoid cargoValve;
    private Solenoid hatchValve;
    private Solenoid armValve;
    private Solenoid vacValve;

    private DigitalInput ballProxSensor;
    private DigitalInput hatchProxSensor;

    private DigitalInput vacPressureSensorHigh;  // Most vacuum - run to this limit
    private DigitalInput vacPressureSensorLow;   // low vacuum - turn on when we get here

    private AnalogInput vacPressureSensor;

    Timer timer;
    double maxVenturiTime;

    private boolean cargoOpen;
    private boolean hatchOpen;

    public ValveControl() {
        timer = new Timer();
        maxVenturiTime = StormProp.getNumber("maxVenturiTime");

        int mod = StormProp.getInt("CompressorModuleId");

        cargoValve = new Solenoid(mod, StormProp.getInt("cargoValve"));
        cargoValve.set(false);
        addChild("BALL_VALVE", cargoValve);

        hatchValve = new Solenoid(mod , StormProp.getInt("hatchValve"));
        hatchValve.set(false);
        addChild("HATCH_VALVE",hatchValve);

        armValve = new Solenoid(mod , StormProp.getInt("armValve"));
        armValve.set(false);
        addChild("ARM_VALVE",armValve);

        vacValve = new Solenoid(mod , StormProp.getInt("vacValve"));
        vacValve.set(false);
        addChild("VAC_VALVE",vacValve);

        ballProxSensor = new DigitalInput(StormProp.getInt("ballProxSensorDIO" ));
        addChild("Ball Proximity Sensor", ballProxSensor);

        hatchProxSensor = new DigitalInput(StormProp.getInt("hatchProxSensorDIO"));
        addChild("Hatch Proximity Sensor", hatchProxSensor);

        vacPressureSensorHigh = new DigitalInput(StormProp.getInt("vacPressureSensorHighDIO"));  // Most vacuum - run to this limit
        vacPressureSensorLow = new DigitalInput(StormProp.getInt("vacPressureSensorLowDIO")); // low vacuum - turn on when we get here

        vacPressureSensor = new AnalogInput(StormProp.getInt("vacPressureSensor"));
        addChild("Pressure Sensor", vacPressureSensor);

        cargoOpen = false;
        hatchOpen = false;
        vacRunning = false;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new EnableVacuum());
    }

    @Override
    public void periodic() {
        manageVac();
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void cargoStart() {
        if(!cargoOpen) {
            cargoValve.set(true);
            cargoOpen = true;
        }
    }

    public void cargoStop(){
        if(cargoOpen) {
            cargoValve.set(false);
            cargoOpen = false;
        }
    }

    public void hatchStart() {
        if(!hatchOpen) {
            hatchValve.set(true);
            hatchOpen = true;
        }
    }

    public void hatchStop(){
        if(hatchOpen) {
            hatchValve.set(false);
            hatchOpen = false;
        }
    }

    public void armExtend() {
        armValve.set(true);
    }

    public void armRetract(){
        armValve.set(false);
    }

    public void vacStart() {
        // Don't bother doing anything if we are already running
        if (!vacRunning) {
            timer.reset();
            timer.start();
            vacValve.set(true);
            vacRunning = true;
            System.out.println("Vacuum running");
        }
    }

    public void vacStop(){
        if (vacRunning) {
            vacValve.set(false);
            timer.stop();
            vacRunning = false;
            System.out.println("Vacuum stopped");
        }
    }

    public boolean getHatchOpen(){return hatchOpen;}

    public boolean getHatchProxSensor() {
        System.out.println("Sense Hatch?: " + ! hatchProxSensor.get());
        return hatchProxSensor.get();
    }

    public boolean getBallProxSensor() {
        System.out.println("Sense Ball?: " + ! ballProxSensor.get());
        return ballProxSensor.get();
    }

    // 1 V --> 5 V  means 0 kPa to 100 kPa (really -100, let's keep the signs out of it)
    private double voltsToKPa(double volts) {
        // Upper limit on sensor is -101.3 kPa
        // 101.3 / 4.0 = 25.3

        // These aren't magic numbers - let's leave them as constants.
        // this is debatable, but...
        return ( 25.3 *  (volts - 1.0));
    }

    public void manageVac() {
        double highVacuum = StormProp.getNumber("highVacuumKPa");
        double lowVacuum = StormProp.getNumber("lowVacuumKPa");
        double currentVac = voltsToKPa(vacPressureSensor.getVoltage());

        if (!Robot.compressor.isActiveAndCharged() && timer.get() > maxVenturiTime) {
            // conservative stop. Let the compressor charge up first
            vacStop();
        } else if (currentVac > highVacuum ||  Robot.oi.getControlOverride() ) {
            // normal stop
            vacStop();
        } else if ( currentVac < lowVacuum) {
            // OK to go
            vacStart();
        }
    }
}

