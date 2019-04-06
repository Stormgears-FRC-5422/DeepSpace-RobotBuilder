// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc5422.Minimec.subsystems;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import org.usfirst.frc5422.Minimec.subsystems.stormnet.*;
import org.usfirst.frc5422.utils.StormProp;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class StormNetSubsystem extends Subsystem {
    StormNet m_stormNet;
    ShuffleboardTab tab = Shuffleboard.getTab("StormNet");

    public StormNetSubsystem() {
        Shuffleboard.selectTab("StormNet");
    }

    public void connect() {
        System.out.println("in subsystem connect");

        System.out.println("initializing StormNet");
        StormNet.init();

        System.out.println("get instance:");
        m_stormNet = StormNet.getInstance();

        if (Robot.testStormNet) {
            System.out.println("testing");
            m_stormNet.test();
            System.out.println("done testing");
        } else System.out.println("not testing");
    }

    public void stop() {
        m_stormNet.stop();
    }

    @Override
    public void initDefaultCommand() {
        System.out.println("In StormNetSubsystem default command");
        setDefaultCommand(new StormConnect());
    }

    public double getLidarDistance() {
		return m_stormNet.getLidarDistance();
	}

	public double getLidarOffset() {
		return m_stormNet.getLidarOffset();
	}

	public double getLineIROffset()  { 
        return m_stormNet.getLineIROffset();
    } 


    @Override
    public void periodic() {
    }
}

