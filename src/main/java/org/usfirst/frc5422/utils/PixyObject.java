package org.usfirst.frc5422.utils;

public class PixyObject {
    private double m_y;
    private double m_height;
    private double m_width;
    private String m_type;
    private double m_x;
    
    public static final double FRAME_SIZE_X = 320;
    public static final double FRAME_SIZE_Y = 200;
    public static final double frame_center_x = FRAME_SIZE_X/2;
    private static final double min_dock_height = 5;
    private static final double min_cargo_height = 20;
    public static final int num_attributes = 5;

    public enum PixyType {
        CARGO(1),
        DOCK(2);

        private int numVal;

        PixyType(int numVal) {
            this.numVal = numVal;
        }
    
        public int getNumVal() {
            return numVal;
        }
    }

    public PixyObject(String type,double x, double y, double width, double height) {
        // invert needs to be set if camera is upside down
        m_type = type;
        m_x = x;
        m_y = y;
        m_height = height;
        m_width = width;
    }

    public double getX() {
        return(m_x);
    }
    public double getY() {
        return(m_y);
    }
    public double getHeight() {
        return(m_height);
    }
    public double getWidth() {
        return(m_width);
    }
    public String getType() {
        return(m_type);
    }
    public double getArea() {
        return(m_width * m_height);
    }

    public double getMinHeight() {
        if (m_type.equals("Dock")) {
            return(min_dock_height);
        }
        if (m_type.equals("Cargo")) {
            return(min_cargo_height);
        }
        return(0);
    }
}   
