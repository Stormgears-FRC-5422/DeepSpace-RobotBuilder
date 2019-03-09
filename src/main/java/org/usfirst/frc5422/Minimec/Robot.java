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

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Commands
import org.usfirst.frc5422.Minimec.commands.*;

// subsystems
import org.usfirst.frc5422.Minimec.commands.Drive.DefaultDrive;
import org.usfirst.frc5422.Minimec.subsystems.*;
import org.usfirst.frc5422.Minimec.subsystems.dsio.DSIO;
import org.usfirst.frc5422.Minimec.subsystems.intake.Intake;
import org.usfirst.frc5422.Minimec.subsystems.navigator.DeepSpaceMecanumDrive;
import org.usfirst.frc5422.Minimec.subsystems.pneumatics.*;
import org.usfirst.frc5422.Minimec.subsystems.backjack.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in
 * the project.
 */
public class Robot extends TimedRobot {

    public static Arm arm;
    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;
    //    public static DSIO dsio;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Drive drive;
    public static StormNetSubsystem stormNetSubsystem;
    public static Backjack backjack;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static PixyVision pixyVision;
    public static Compression compressor;
    public static ValveControl valveControl;
    private TalonSRX elevator;
    public static Intake intake;
    public static DeepSpaceMecanumDrive mecanum;
    public static DSIO dsio;

    //commands
    private DefaultDrive defaultDrive;


    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        System.out.println("robotInit()");
//        pixyVision = new PixyVision("vision"); // FIXME - read table name from config file?
        drive = new Drive();
        mecanum = DeepSpaceMecanumDrive.getInstance();
        stormNetSubsystem = new StormNetSubsystem();
        arm = new Arm();
        backjack = new Backjack();
        intake = new Intake();
        valveControl = new ValveControl();
        compressor = new Compression();
        dsio = DSIO.INSTANCE;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        onInitCheck();

        //compressor.startCompressor();

        // Add commands to Autonomous Sendable Chooser
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

        chooser.setDefaultOption("Autonomous Command", new AutonomousCommand());

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        SmartDashboard.putData("Auto mode", chooser);
        defaultDrive = DefaultDrive.getInstance();
    }


    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit(){
        System.out.println("disabledInit()");
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        System.out.println("autonomousInit()");

        onInitCheck();
        autonomousCommand = chooser.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        System.out.println("teleopInit()");
        onInitCheck();

        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //defaultDrive.start();
    }

    @Override
    public void testPeriodic()
    {
        SmartDashboard.putNumber("Current", elevator.getOutputCurrent());
    }

    // We have a few buttons that are really switches. They might be in either position when the match starts
    // Try to set the subsystem state accordingly
    public void onInitCheck() {
        System.out.println("onInitCheck()");

        // No longer neessary for jack.  May be needed for Wrist and Intake
        // This is no longer necessary for backjack
        //        System.out.println("jack level " + oi.getBackJackLevel());
        //
        //      backjack.setLevel(oi.getBackJackLevel());
    }
}