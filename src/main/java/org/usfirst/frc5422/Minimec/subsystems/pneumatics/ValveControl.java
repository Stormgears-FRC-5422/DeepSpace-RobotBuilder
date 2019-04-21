package org.usfirst.frc5422.Minimec.subsystems.pneumatics;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.utils.StatusLight;
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

    private final double highVacuum;
    private final double lowVacuum;
    private final double warnVacuum;

    private double currentVac;
    private double rearmVac;
    private double maxVacDropPerHatch;


    private boolean in_contact;

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
        maxVenturiTime = StormProp.getNumber("maxVenturiTime",5.0);
        int mod = StormProp.getInt("CompressorModuleId",-1);

        cargoValve = new Solenoid(mod, StormProp.getInt("cargoValve",-1));
        cargoValve.set(false);
        addChild("BALL_VALVE", cargoValve);

        hatchValve = new Solenoid(mod , StormProp.getInt("hatchValve",-1));
        hatchValve.set(false);
        addChild("HATCH_VALVE",hatchValve);

        armValve = new Solenoid(mod , StormProp.getInt("armValve",-1));
        armValve.set(false);
        addChild("ARM_VALVE",armValve);

        vacValve = new Solenoid(mod , StormProp.getInt("vacValve",-1));
        vacValve.set(false);
        addChild("VAC_VALVE",vacValve);

        ballProxSensor = new DigitalInput(StormProp.getInt("ballProxSensorDIO" ,-1));
        addChild("Ball Proximity Sensor", ballProxSensor);

        hatchProxCount = StormProp.getInt("hatchProxCount",3);

        hatchProxSensor1 = new DigitalInput(StormProp.getInt("hatchProxSensorDIO1",-1));
        addChild("Hatch Proximity Sensor", hatchProxSensor1);
	    hatchProxSensor2 = new DigitalInput(StormProp.getInt("hatchProxSensorDIO2",-1));
        addChild("Hatch Proximity Sensor", hatchProxSensor2);
        hatchProxSensor3 = new DigitalInput(StormProp.getInt("hatchProxSensorDIO3",-1));
        addChild("Hatch Proximity Sensor", hatchProxSensor3);

        vacPressureSensorHigh = new DigitalInput(StormProp.getInt("vacPressureSensorHighDIO",-1));  // Most vacuum - run to this limit
        vacPressureSensorLow = new DigitalInput(StormProp.getInt("vacPressureSensorLowDIO",-1)); // low vacuum - turn on when we get here

        vacPressureSensor = new AnalogInput(StormProp.getInt("vacPressureSensor",-1));

        highVacuum = StormProp.getNumber("highVacuumKPa",0.0);
        lowVacuum = StormProp.getNumber("lowVacuumKPa",0.0);
        warnVacuum = StormProp.getNumber("warnVacuumKPa",0.0);
        maxVacDropPerHatch = StormProp.getNumber("maxVacDropPerHatchKPa",10.0);

        addChild("Pressure Sensor", vacPressureSensor);

        cargoOpen = false;
        hatchOpen = false;
        vacRunning = false;
        in_contact = false;
    }

    @Override
    public void initDefaultCommand() {
    }

    //true means low vac
    boolean lastState = false;

    @Override
    public void periodic() {
        manageVac();  // sets currentVac
        if(Robot.useStatusLights){
            Robot.setStatusLight(StatusLight.Vacuum, currentVac < warnVacuum ? 1 : 0);
        }
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
            rearmVac = currentVac - maxVacDropPerHatch;
            hatchOpen = true;
        }
    }

    public void hatchReStart() {
        // Re-enable hatch vaccum if hatchOpen is true.  Need to do this when we switch from auto to teleop
        // as the solenoid resets
        if (hatchOpen) {
            hatchOpen = false;
            hatchStart();
        }
    }

    public void hatchStop(){
        if(hatchOpen) {
            hatchValve.set(false);
            rearmVac = 0;
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

//        System.out.println("1: " + hatchProxSensor1.get() + " 2: " + hatchProxSensor2.get()+ " 3: " + hatchProxSensor3.get());
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
        currentVac = voltsToKPa(vacPressureSensor.getVoltage());

        if (hatchOpen && currentVac <= rearmVac) {
            hatchStop();  // force close because the pressure dropped too much. something must have gone wrong
        }

        if (!Robot.compressor.isActiveAndCharged() && timer.get() > maxVenturiTime) {
            // conservative stop. Let the compressor charge up first
            vacStop();
        } else if (currentVac > highVacuum ||  Robot.oi.getControlOverride() ) {
            // normal stop
            vacStop();
            if(Robot.useStatusLights) Robot.setStatusLight(StatusLight.Vacuum, 0);
        } else if ( currentVac < lowVacuum) {
            // OK to go
            vacStart();
            if(Robot.useStatusLights) Robot.setStatusLight(StatusLight.Vacuum, 1);
        }
    }
}
