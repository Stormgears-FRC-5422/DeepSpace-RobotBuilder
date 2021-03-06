/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc5422.Minimec.subsystems;

import org.usfirst.frc5422.Minimec.commands.*;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;  // Import the Collections class
import org.usfirst.frc5422.utils.PixyObject;
import org.usfirst.frc5422.utils.PixyObjectCollection;
import org.usfirst.frc5422.utils.PixyObject.PixyType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import org.usfirst.frc5422.utils.StormProp;

/**
 *
 */
public class PixyVision extends PIDSubsystem {

    private NetworkTable m_vision_table;
    private VisionMode m_mode;
    private PixyObject m_last_item_tracked;
    private double m_pid_out;
    private NetworkTableEntry m_entry_brightness; // Control camera brightness
    private NetworkTableEntry m_entry_invert; // Tell pixy if it is inverted so it can fix the data
    private NetworkTableEntry m_entry_x;
    private NetworkTableEntry m_entry_y;
    private NetworkTableEntry m_entry_height;
    private NetworkTableEntry m_entry_width;
    private NetworkTableEntry m_entry_type;
    private NetworkTableEntry m_entry_raw;
    private String m_vision_table_name;
    private NetworkTableInstance m_nt_inst; 
    private DockSelection m_dock_mode = DockSelection.MIDDLE;
    private static double m_min_dock_height = 5;  // Ignore dock objects less than this height
    private double m_min_tracking_delta_x = 10; // Limit how far off the next closest object to previous is for considering the object to be the same
    private double m_last_dock_pair_separation = 0; // Track the distance between dock pairs to filter out bad data
    private int m_lost_track_count = 0;
    private boolean m_inverted = true;
    private static final double m_strafe_pid = .02;
    private static final double m_turn_pid = .005;

    private NetworkTableEntry m_dockmode_entry;
    private NetworkTableEntry m_mode_entry;
    private NetworkTableEntry m_num_objects_entry;
    private NetworkTableEntry m_dbg1_entry;
    private NetworkTableEntry m_dbg2_entry;
    private int m_dock_brightness;

    private ShuffleboardTab match_tab;
    private NetworkTableEntry match_dockmode;
    private NetworkTableEntry match_dock_distance;

    public enum ObjectType {
        CARGO,DOCK
    }
    public enum VisionMode {
        CARGO(1),
        DOCK(2),
        DISABLED(0);

        private int numVal;

        VisionMode(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }

    }

    public enum DockSelection {
        LEFT(1),
        MIDDLE(2),
        RIGHT(3);

        private int numVal;

        DockSelection(int numVal) { this.numVal = numVal; }

        public int getNumVal() { return numVal; }
    }

    // Leave setpoint at 0. We will calculate relative position to target
    // so that we can always set input to 0 when we don't have an object
    // to keep the PID from reacting.

    // Initialize your subsystem here
    public PixyVision(String vision_table,boolean camera_inverted) {
        super("PixyVision", m_turn_pid, 0.0, .04, 0.0, .02);
        getPIDController().setContinuous(false);
        getPIDController().setName("PixyVision", "PIDSubsystem Controller");
        LiveWindow.add(getPIDController());
        getPIDController().setAbsoluteTolerance(.03);
        getPIDController().setOutputRange(-1, 1);

        m_dock_brightness = StormProp.getInt("dock_brightness",15);
        m_vision_table_name = vision_table;
        m_nt_inst = NetworkTableInstance.getDefault();
        m_entry_brightness = m_nt_inst.getTable(m_vision_table_name).getEntry("brightness");
        m_pid_out = 0;
        m_inverted = camera_inverted;
        m_mode = VisionMode.DISABLED;

        // Debug data
        ShuffleboardTab debug_tab = Shuffleboard.getTab("PixyDebug");
        m_mode_entry = debug_tab.add("Mode",m_mode.toString() ).getEntry();
        m_dockmode_entry = debug_tab.add("Dock Selection", m_dock_mode.toString()).getEntry();
        m_num_objects_entry = debug_tab.add("Number of objects seen", 0).getEntry();
        m_dbg1_entry = debug_tab.add("TargetByMode Objects", 0).getEntry();
        m_dbg2_entry = debug_tab.add("New lock Center", 0.0).getEntry();
        SmartDashboard.getNumber("Pixy PID Value", get_pid_output());

        match_tab = Shuffleboard.getTab("Match Tab");
        match_dockmode = match_tab.add("Vision State", m_mode.toString()).getEntry();
        match_dock_distance = match_tab.add("Distance to Dock", 0).getEntry();
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void set_turn_mode() {
        getPIDController().setP(m_turn_pid);
        getPIDController().setD(.04);
        getPIDController().setOutputRange(-1, 1);
    }

    public void set_strafe_mode() {
        getPIDController().setP(m_strafe_pid);
        getPIDController().setD(.1);
        getPIDController().setOutputRange(-.6, .6);
    }

    public void clearLastTracked() {
        m_last_item_tracked = null;
        m_last_dock_pair_separation = 0;
    }

    public void changeDockMode(DockSelection mode) {
        m_dock_mode = mode;
        clearLastTracked();
        SmartDashboard.putString("DOCK MODE", mode.toString());
        m_dockmode_entry.setString(m_dock_mode.toString());
    }

    // How many items are in this byte stream
    private int getRawNumItems(byte[] data) {
        if (data.length < 2) {
            return(0);
        }
        else {
            return((data[0] & 0xFF << 8) | (data[1] & 0xFF));  // Big Endian
        }
    }

    // Get item "n" from the byte stream. Returns a pixy object
    private PixyObject extractRawItem(byte data[],int pos) {
        int object_data[] = new int[PixyObject.num_attributes];
        int data_offset = 2 + (pos * (2 * PixyObject.num_attributes)); // 2 bytes per attribute
        String type = "UNKNOWN";
        
        for (int i=0; i < PixyObject.num_attributes; i++) {
            object_data[i] = (0xFF & data[data_offset+i*2]) << 8 | (0xFF & data[data_offset+i*2+1]); // Big Endian
        }
        if (object_data[0] == 2) { type = "DOCK"; }
        else if (object_data[0] == 1) { type = "CARGO"; }
        
        return(new PixyObject(type, object_data[1]/1.0, object_data[2]/1.0, object_data[3]/1.0, object_data[4]/1.0));
    }

    public void enable(VisionMode mode) {
        m_mode = mode;
	// assumes only two pixy object types CARGO and DOCK.
        if (m_mode == VisionMode.CARGO) {
            m_vision_table = m_nt_inst.getTable(String.format("%s/cargo",m_vision_table_name));
            m_entry_brightness.setNumber(65);
            m_dockmode_entry.setString("DISABLED");
        }
	    else {
            m_vision_table = m_nt_inst.getTable(String.format("%s/dock",m_vision_table_name));
            m_entry_brightness.setNumber(m_dock_brightness);
            m_dockmode_entry.setString(m_dock_mode.toString());
        }

    
        m_entry_invert = m_nt_inst.getTable(m_vision_table_name).getEntry("invert");
        m_entry_raw = m_vision_table.getEntry("raw");
        m_entry_x = m_vision_table.getEntry("centerX");
        m_entry_y = m_vision_table.getEntry("centerY");
        m_entry_type = m_vision_table.getEntry("Type");
        m_entry_height = m_vision_table.getEntry("height");
        m_entry_width = m_vision_table.getEntry("width");

        
        m_mode_entry.setString(m_mode.toString());
        m_entry_invert.setBoolean(m_inverted);
        getPIDController().enable();
        SmartDashboard.putString("Pixy mode", mode.toString());
    }

    public void disable() {
        getPIDController().disable();
        clearLastTracked();
        m_mode = VisionMode.DISABLED;
        m_mode_entry.setString(m_mode.toString());
        SmartDashboard.putString("Pixy mode", "DISABLED");
    }

    @Override
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        // These double/string arrays are for updating network tables with human readable values
        double centerX[];
        double centerY[];
        double height[];
        double width[];
        String type[];
        double typeid[];

        // We get raw binary data from the pi
        byte raw_data[];
        PixyObjectCollection objects;

        raw_data = m_entry_raw.getRaw(new byte[0]);

        int num_objects = getRawNumItems(raw_data);
        // The following are for updating network tables with human readable values
        centerX = new double[num_objects];
        centerY = new double[num_objects];
        height = new double[num_objects];
        width = new double[num_objects];
        type = new String[num_objects];
        ArrayList<PixyObject> object_list = new ArrayList<PixyObject>();
        for (int i = 0; i < num_objects; i++) {
            PixyObject obj = extractRawItem(raw_data,i);
            object_list.add(obj);
            centerX[i] = obj.getX();
            centerY[i] = obj.getY();
            height[i] = obj.getHeight();
            width[i] = obj.getWidth();
            type[i] = obj.getType();            
        }
        // Push data to network tables
        m_entry_x.setDoubleArray(centerX);
        m_entry_y.setDoubleArray(centerY);
        m_entry_width.setDoubleArray(width);
        m_entry_height.setDoubleArray(height);
        m_entry_type.setStringArray(type);

        objects = new PixyObjectCollection(object_list);

        List<PixyObject> targets;
        if (m_mode == VisionMode.CARGO) {
            // Get closest (largest) cargo, or last known
            if (m_last_item_tracked != null) {
                targets = objects.getClosest(m_last_item_tracked.getX(), m_last_item_tracked.getY());
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
        else if (m_mode == VisionMode.DOCK) {
            objects.filterDocksByProximity(); // don't consider docks way off to the side
            objects.filterDocksForRocket(m_dock_mode == DockSelection.MIDDLE);  // This is safe for cargoship
            // Get markers closest to center
            if (m_last_item_tracked != null) {
                targets = objects.getAdjacentPair(m_last_item_tracked.getX());
            } else {
                targets = getTargetByMode(objects,m_dock_mode);
                m_dbg1_entry.setNumber(targets.size());

            }

            if (targets.size() > 1) {
                PixyObject dock0 = targets.get(0);
                PixyObject dock1 = targets.get(1);
                double center = (dock0.getX() + dock1.getX())/2;
                m_last_dock_pair_separation = Math.abs(dock0.getX() - dock1.getX());
                if (m_last_item_tracked == null) { 
                    m_dbg2_entry.setDouble(center);
                }
                m_last_item_tracked = new PixyObject("Dock", center, dock0.getY(), dock0.getWidth(), dock0.getHeight());
                return(PixyObject.frame_center_x - center);
            }
            else {
                if (m_last_item_tracked != null && m_lost_track_count >= 1) {
                    m_lost_track_count = 0;
                    m_last_item_tracked = null;
                    // Give up on waiting to find it again
                    m_dbg2_entry.setDouble(-1);
                }
                m_lost_track_count++;
                return(0);
            }
        }
        return(0); // Return no error if nothing seen
    }

    public double getDockDistance() {
        return(800/m_last_dock_pair_separation);
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

    private List<PixyObject> getTargetByMode(PixyObjectCollection objects, DockSelection mode) {
        List<PixyObject> targets;
        SmartDashboard.putNumber("Number of docks seen: ", objects.size());
        m_num_objects_entry.setNumber(objects.size());
	    // Get distance between first two items.  This will be used to determine if we are at the
	    // left or right ends by making sure there is nothing within that [range] to left or right sides
        if (objects.size() > 1) {
    	    targets = objects.leftToRight();
            if (mode == DockSelection.LEFT) {
                // No targets within certain distance from left edge
                return(targets.subList(0,2));
            } 
            else if (mode == DockSelection.RIGHT) {
                // No targets within certain distance from right edge
                return(targets.subList(targets.size()-2,targets.size()));
            }
            else if (mode == DockSelection.MIDDLE) {
                if (targets.size() >= 4) {
                    return(targets.subList(2,4));
                }       
                else {
                    return(targets.subList(0,2));
                }
            }
            return(objects.getClosest(PixyObject.frame_center_x));
        }
        else {
            return(objects.getClosest(PixyObject.frame_center_x));
        }
    }

    public void periodic(){
        match_dockmode.setString(m_mode.toString());
        match_dock_distance.setDouble(getDockDistance());
    }
}

