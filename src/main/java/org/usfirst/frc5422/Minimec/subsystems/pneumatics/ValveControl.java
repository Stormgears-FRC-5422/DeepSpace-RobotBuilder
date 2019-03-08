package org.usfirst.frc5422.Minimec.subsystems.pneumatics;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc5422.Minimec.commands.Pneumatics.DisableAllPneumatics;

public class ValveControl extends Subsystem {

    private Solenoid cargoValve;
    private Solenoid hatchValve;
    private Solenoid armValve;
    private Solenoid vacValve;

    private DigitalInput ballProxSensor;
    private DigitalInput hatchProxSensor;

    private AnalogInput vacPressureSensor;

    private boolean cargoOpen;
    private boolean hatchOpen;

    public ValveControl() {
        cargoValve = new Solenoid(11, 1);
        cargoValve.set(false);
        addChild("BALL_VALVE", cargoValve);


        hatchValve = new Solenoid(11, 0);
        hatchValve.set(false);
        addChild("HATCH_VALVE",hatchValve);


        armValve = new Solenoid(11, 3);
        armValve.set(false);
        addChild("ARM_VALVE",armValve);


        vacValve = new Solenoid(11, 4);
        vacValve.set(false);
        addChild("VAC_VALVE",vacValve);

        // TODO: Magic Numbers

        ballProxSensor = new DigitalInput(5);  // TODO
        addChild("Ball Proximity Sensor", ballProxSensor);

        hatchProxSensor = new DigitalInput(6);  // TODO
        addChild("Hatch Proximity Sensor", hatchProxSensor);

        vacPressureSensor = new AnalogInput(0);  // TODO
        addChild("Pressure Sensor", vacPressureSensor);

        // 1 - 5 V and 0 to -14

        cargoOpen = false;
        hatchOpen = false;
    }

    @Override
    public void initDefaultCommand() {

        // Set the default command for a subsystem here.
        setDefaultCommand(new DisableAllPneumatics());
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
        vacValve.set(true);
    }

    public void vacStop(){
        vacValve.set(false);
    }

    public boolean getHatchProxSensor() {
        System.out.println("Sense Hatch?: " + ! hatchProxSensor.get());
        return hatchProxSensor.get();
    }
    public boolean getBallProxSensor() {
        System.out.println("Sense Ball?: " + ! ballProxSensor.get());
        return ballProxSensor.get();
    }

    public double toVolts(double pressure) {
        return (pressure / -3.5) -3.5;
    }

    public void manageVac() {
        double upper = -5.5;
        double lower = -4;
        if (!(vacPressureSensor.getVoltage() < toVolts(upper) && vacPressureSensor.getVoltage() > toVolts(lower))) {
            vacStart();
        }
        else {
            vacStop();
        }
    }
}

