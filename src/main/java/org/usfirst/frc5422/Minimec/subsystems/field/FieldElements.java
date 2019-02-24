package org.usfirst.frc5422.Minimec.subsystems.field;

public class FieldElements {

    //Measurements are in inches

    public static class Field {
        public static final double LENGTH = 648;
        public static final double WIDTH = 324;
        public static final double HEIGHT = 0;
    }

    public static class Hab_Platform {
        public static final double LEVEL_ONE_HEIGHT = 3;
        public static final double LEVEL_ONE_LENGTH = 47.25;
        public static final double LEVEL_ONE_WIDTH = 150.5;
        public static final double LEVEL_TWO_HEIGHT = 9;
        public static final double LEVEL_TWO_LENGTH = 16;
        public static final double LEVEL_TWO_WIDTH = 40;
        public static final double LEVEL_THREE_HEIGHT = 22;
        public static final double LEVEL_THREE_LENGTH = 16;
        public static final double LEVEL_THREE_WIDTH = 16;
    }

    public static class Depot {
        public static final double LENGTH = 45;
        public static final double WIDTH = 22.625;
        public static final double RAIL_HEIGHT = 1.125;
        public static final double RAIL_WIDTH = 4;
    }

    public static class Cargo_Ship {
        public static final double LENGTH = 95.75;
        public static final double WIDTH = 55.75;
        public static final double HEIGHT = 48;
        public static final double DISTANCE_FROM_MIDDLE = 9;
        public static final double BACKSTOPWIDTH = 7.75;
        public static final double BACKSTOPDEPTH = 3.25;
        public static final double TOP_BACKSTOP_HEIGHT = 3;
        public static final double BOTTOM_BACKSTOP_HEIGHT = 3.25;
        public static final double BALL_HOLE_WIDTH = 20.125;
        public static final double HATCH_OPENING_HEIGHT = 31.5;
        public static final double CENTER_TO_FLOOR = 19;
    }

    public static class Loading_Station {
        public static final double LENGTH = 0;
        public static final double WIDTH = 45;
        public static final double HEIGHT = 37;
    }

    public static class Rocket {
        public static final double HEIGHT = 124;
        public static final double SIDE_ANGLE = 61.25;
        public static final double DISTANCE_FROM_GUARDRAIL = 27.5;

        public static final double PORT_DIAMETER = 16;
        public static final double FIRST_PORT_CENTER_HEIGHT = 27.5;
        public static final double SECOND_PORT_CENTER_HEIGHT = 55.5;
        public static final double THIRD_PORT_CENTER_HEIGHT = 83.5;
        public static final double DISTANCE_BETWEEN_PORT_CENTERS = 28;

        public static final double HATCHDIAMETER = 16.5;
        public static final double FIRST_HATCH_CENTER_HEIGHT = 19;
        public static final double SECOND_HATCH_CENTER_HEIGHT = 47;
        public static final double THIRD_HATCH_CENTER_HEIGHT = 75;
        public static final double DISTANCE_BETWEEN_HATCH_CENTERS = 28;
    }
}