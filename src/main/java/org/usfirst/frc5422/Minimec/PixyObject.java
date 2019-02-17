package org.usfirst.frc5422.Minimec;

public class PixyObject {
        private double m_y;
        private double m_height;
        private double m_width;
        private String m_type;
        private double m_x;
        public static final double frame_center_x = 160;

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

        public PixyObject(String type,double x, double y, double height, double width) {
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
    }   