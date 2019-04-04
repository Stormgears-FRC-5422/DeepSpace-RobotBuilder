package org.usfirst.frc5422.Minimec.subsystems.pneumatics;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.EnableVacuum;
import org.usfirst.frc5422.utils.StormProp;
import org.usfirst.frc5422.Minimec.Robot;


public class ValveControl extends Subsystem {
    boolean vacRunning;
    private Solenoid cargoValve;
    private Solenoid hatchValve;
    private Solenoid armValve;
    private Solenoid vacValve;

    private DigitalInput ballProxSensor;
    private DigitalInput hatchProxSensor1;
    private DigitalInput hatchProxSensor2;
    private DigitalInput hatchProxSensor3;

    // How many prox sensors need to vote to release
    // 0 means trigger vacuum immediately
    int hatchProxCount;

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

        hatchProxCount = StormProp.getInt("hatchProxCount");

        hatchProxSensor1 = new DigitalInput(StormProp.getInt("hatchProxSensorDIO1"));
        addChild("Hatch Proximity Sensor", hatchProxSensor1);
            hatchProxSensor2 = new DigitalInput(StormProp.getInt("hatchProxSensorDIO2"));
        addChild("Hatch Proximity Sensor", hatchProxSensor2);
        hatchProxSensor3 = new DigitalInput(StormProp.getInt("hatchProxSensorDIO3"));
        addChild("Hatch Proximity Sensor", hatchProxSensor3);

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
        System.out.println("IntakeArmExtend");
        armValve.set(true);
    }

    public void armRetract(){
        System.out.println("IntakeArmRetract");
        armValve.set(false);
    }

    public void vacStart() {
        // Don't bother doing anything if we are already running
        if (!vacRunning) {
            timer.reset();
            timer.start();
            vacValve.set(true);
            vacRunning = true;
            if (Robot.debug) System.out.println("Vacuum running");
        }
    }

    public void vacStop(){
        if (vacRunning) {
            vacValve.set(false);
            timer.stop();
            vacRunning = false;
            if (Robot.debug) System.out.println("Vacuum stopped");
        }
    }

    public boolean getHatchOpen(){return hatchOpen;}

    public boolean getHatchProxSensorReady() {
        // These sensors return true when the are off!
        // This 0 / 1 flip makes everything else a lot easier to think about.
        int voteReady = hatchProxSensor1.get() ? 0 : 1;
        voteReady += hatchProxSensor2.get() ? 0 : 1;
        voteReady += hatchProxSensor3.get() ? 0 : 1;

        return ( voteReady >= hatchProxCount);
    }

    public boolean getBallProxSensor() {
        if (Robot.debug) System.out.println("Sense Ball?: " + ! ballProxSensor.get());
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

