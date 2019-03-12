// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc5422.Minimec;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc5422.Minimec.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc5422.Minimec.commands.Drive.CargoDrive;
import org.usfirst.frc5422.Minimec.commands.Drive.DockDrive;
import org.usfirst.frc5422.Minimec.commands.Drive.JoyDrive;

import org.usfirst.frc5422.Minimec.commands.Pneumatics.RunCompressor;
import org.usfirst.frc5422.Minimec.subsystems.dsio.DSIO;
import org.usfirst.frc5422.utils.dsio.TernarySwitch;
import org.usfirst.frc5422.utils.StormProp;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
//    public Joystick joystick1;
//    public JoystickButton button1;

    // DSIO encapsulates our button board
    private DSIO dsio;

//    private TernarySwitch dummyDeleteMe; //  = new TernarySwitch(joystick1, button1, button2) but elsewhere probably
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

//        joystick1 = new Joystick(0);
//        button1 = new JoystickButton(joystick1, 1);

//        button1.whenPressed(new RunCompressor());


        // SmartDashboard Buttons
//        SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
//        SmartDashboard.putData("JoyDrive", new JoyDrive());
//        SmartDashboard.putData("DockDrive", new DockDrive());
//        SmartDashboard.putData("CargoDrive", new CargoDrive());
//        SmartDashboard.putData("CargoShipDrive", new SelectedDockDrive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
//        joystick1.setZChannel(4); // Xbox mapping to right stick
        dsio = DSIO.INSTANCE;
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    }

    public Joystick getJoystick() {return dsio.getJoystick();}

    public Joystick getJoystick1() {return dsio.getJoystick1();}

    public Joystick getJoystick2() {return dsio.getJoystick2();}

    public int getBackJackLevel() {return dsio.getBackJackLevel();}

    public boolean getVenturiOverride() {return dsio.getVenturiOverride();}

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

    public double[] getJoyXYZ(Joystick joy) {
        double ret_vals[] = new double[3];
        double null_size = StormProp.getNumber("driveDeadZone");

        // Execute drive subsystem command from joystick input
        //double rate = 0.5 + (joy.getRawAxis(3) * .5);
        double x = joy.getRawAxis(0);
        double y = joy.getRawAxis(3) - joy.getRawAxis(2) ;
        double z = joy.getRawAxis(4);
        if (Math.abs(x) < null_size) x = 0;
        if (Math.abs(y) < null_size) y = 0;
        if (Math.abs(z) < null_size) z = 0;
        ret_vals[0] = x;
        ret_vals[1] = y;
        ret_vals[2] = z;
        return(ret_vals);
    }
}

