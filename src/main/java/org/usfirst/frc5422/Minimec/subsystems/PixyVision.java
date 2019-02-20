/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc5422.Minimec.subsystems;

import org.usfirst.frc5422.Minimec.commands.*;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;  // Import the Collections class
import org.usfirst.frc5422.Minimec.PixyObject;
import org.usfirst.frc5422.Minimec.PixyObjectCollection;
import org.usfirst.frc5422.Minimec.PixyObject.PixyType;


/**
 *
 */
public class PixyVision extends PIDSubsystem {

    private NetworkTable m_vision_table;
    private PixyObject.PixyType m_mode;
    private PixyObject m_last_item_tracked;
    private double m_pid_out;
    private NetworkTableEntry m_entry_brightness; // Control camera brightness
    private NetworkTableEntry m_entry_x;
    private NetworkTableEntry m_entry_y;
    private NetworkTableEntry m_entry_height;
    private NetworkTableEntry m_entry_width;
    private NetworkTableEntry m_entry_type;
    private String m_vision_table_name;
    private NetworkTableInstance m_nt_inst;

    // Leave setpoint at 0. We will calculate relative position to target
    // so that we can always set input to 0 when we don't have an object
    // to keep the PID from reacting.

    // Initialize your subsystem here
    public PixyVision(String vision_table) {
        super("PixyVision", .01, 0.0, 0.02, 0.0, .04);
        getPIDController().setContinuous(false);
        getPIDController().setName("PixyVision", "PIDSubsystem Controller");
        LiveWindow.add(getPIDController());
        getPIDController().setAbsoluteTolerance(.02);
        getPIDController().setOutputRange(-1.0, 1.0);
        m_vision_table_name = vision_table;
        m_nt_inst = NetworkTableInstance.getDefault();
        m_entry_brightness = m_nt_inst.getTable(m_vision_table_name).getEntry("brightness");
        m_pid_out = 0;
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void clearLastTracked() {
        m_last_item_tracked = null;
    }

    public void enable(PixyObject.PixyType mode) {
        m_mode = mode;
        if (mode == PixyType.DOCK) {   
            m_vision_table = m_nt_inst.getTable(String.format("%s/dock",m_vision_table_name));
            m_entry_brightness.setNumber(15);
        }
        if (mode == PixyType.CARGO) {
            m_vision_table = m_nt_inst.getTable(String.format("%s/cargo",m_vision_table_name));
            m_entry_brightness.setNumber(65);
        }
        m_entry_x = m_vision_table.getEntry("centerX");
        m_entry_y = m_vision_table.getEntry("centerY");
        m_entry_type = m_vision_table.getEntry("Type");
        m_entry_height = m_vision_table.getEntry("height");
        m_entry_width = m_vision_table.getEntry("width");
        
        getPIDController().enable();
        SmartDashboard.putString("Pixy mode", mode.toString());
    }

    public void disable() {
        getPIDController().disable();
        clearLastTracked();
        SmartDashboard.putString("Pixy mode", "DISABLED");
    }

    @Override
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        double[] default_data = new double[0];
        double centerX[];
        double centerY[];
        double height[];
        double width[];
        String type[];
        PixyObjectCollection objects;

        centerX = m_entry_x.getDoubleArray(default_data);
        centerY = m_entry_y.getDoubleArray(default_data);
        height = m_entry_height.getDoubleArray(default_data);
        width = m_entry_width.getDoubleArray(default_data);
        type = m_entry_type.getStringArray(new String[0]);

        // To make sure the data didn't change while we were reading it
        if (! (type.length != centerX.length  ||
            type.length != centerY.length  ||  
            type.length != height.length  ||
            type.length != width.length )) {
            objects = new PixyObjectCollection(type,centerX,centerY,height,width);
        }
        else {
            objects = new PixyObjectCollection(new ArrayList<PixyObject>(0));
        }

        List<PixyObject> targets;
        if (m_mode == PixyType.CARGO) {
            // Get closest (largest) cargo, or last known
            if (m_last_item_tracked != null) {
                targets = objects.getClosest(m_last_item_tracked.getX());
            }
            else {
                targets = objects.getLargest();
            }
            if (targets.size() > 0) {
                PixyObject cargo = targets.get(0);
                m_last_item_tracked = cargo;
                return(PixyObject.frame_center_x - cargo.getX());                
            }
        }
        else if (m_mode == PixyType.DOCK) {
            // Get markers closest to center
            targets = objects.getClosest(PixyObject.frame_center_x);
            if (targets.size() > 1) {
                PixyObject dock0 = targets.get(0);
                PixyObject dock1 = targets.get(1);
                double center = (dock0.getX() + dock1.getX())/2;
                return(PixyObject.frame_center_x - center);                
            } 
            else if (targets.size() == 1) {
                return(PixyObject.frame_center_x -targets.get(0).getX());
            }
        }
        return(0); // Return no error if nothing seen
    }

    public double get_pid_output() {
        return(m_pid_out);
    }

    @Override
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
        m_pid_out = output;
    }
}

