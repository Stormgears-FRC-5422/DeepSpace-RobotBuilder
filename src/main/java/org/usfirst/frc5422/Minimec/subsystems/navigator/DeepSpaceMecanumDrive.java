package org.usfirst.frc5422.Minimec.subsystems.navigator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc5422.Minimec.subsystems.navigator.motionprofile.MotionMagic;
import org.usfirst.frc5422.utils.StormProp;

public class DeepSpaceMecanumDrive extends Subsystem {
    private static DeepSpaceMecanumDrive DriveInstance = new DeepSpaceMecanumDrive();
    private double deadZone;
    private TalonSRX[] talons;
    private static int maxVel;
    private static int maxAccel;
    private static MotionMagic[] motions;

    private DeepSpaceMecanumDrive() {
        //talons = new TalonSRX[4];
        // for (int i = 0; i < talons.length; i++) {
        //talons[i] = new TalonSRX(i);
        // }

        deadZone = Double.parseDouble(StormProp.getString("driveDeadZone"));
        talons = DriveTalons.getInstance().getTalons();
        motions = new MotionMagic[DriveTalons.getInstance().getTalons().length];
        maxVel = 5000;
        maxAccel = 2500;

    }

    public static DeepSpaceMecanumDrive getInstance() {
        return DriveInstance;
    }

    public void driveCartesian(double xSpeed, double ySpeed, double zRotation) {
        driveCartesian(xSpeed, ySpeed, zRotation, 0);
    }

    private boolean useTheta = false; // change to true if you want absolute position control
    public boolean slow = false;

    public void driveCartesian(double xSpeed, double ySpeed, double zRotation, double navx_theta) {
        double[] speeds = new double[4];
        double multiplier = slow ? 0.3 : 1;

        ySpeed = applyDeadZone(ySpeed) * -1 * multiplier;
        multiplier = slow ? 0.6 : 1;
        xSpeed = applyDeadZone(xSpeed) * -1 * multiplier;
        zRotation = applyDeadZone(zRotation) * -1 * multiplier;
        if (useTheta) {
            Vector2d input = new Vector2d(xSpeed, ySpeed);
            input.rotate(-navx_theta);
            xSpeed = input.x;
            ySpeed = input.y;
        }

        speeds[0] = xSpeed + ySpeed + zRotation;
        speeds[1] = -xSpeed + ySpeed - zRotation;
        speeds[2] = -xSpeed + ySpeed + zRotation;
        speeds[3] = xSpeed + ySpeed - zRotation;

        double maxMagnitude = Math.abs(speeds[0]);
        for (int i = 1; i < speeds.length; i++) {
            double spd = Math.abs(speeds[i]);
            if (maxMagnitude < spd) {
                maxMagnitude = spd;
            }
        }
        if (maxMagnitude > 1.0) {
            for (int i = 0; i < speeds.length; i++) {
                speeds[i] = speeds[i] / maxMagnitude;
            }
        }

        for (int i = 0; i < speeds.length; i++) {
            SmartDashboard.putNumber("Speed: " + i, speeds[i]);
            talons[i].set(ControlMode.PercentOutput, speeds[i] * 0.75);
        }
    }
    public void strafeTo(double distX, double distY){
        distX *= Math.sqrt(2);
        distY *= -1;
        double distance = Math.sqrt(distX*distX + distY*distY);
        double theta;
        if(distX > 0){
            theta = Math.atan2(distY, distX);
        }else if(distX < 0){
            theta = Math.atan2(distY, distX)+Math.PI;
        }else{
            if(distY > 0){
                theta = Math.PI/2;
            }else if(distY < 0){
                theta = 3*Math.PI/2;
            }else{
                theta = 0;
            }
        }
        theta -= Math.PI/4;
        double v0 = 10000*Math.sin(theta);
        double v1 = 10000*Math.cos(theta);

        talons[0].set(ControlMode.Velocity, v0);
        talons[1].set(ControlMode.Velocity, v1);
        talons[2].set(ControlMode.Velocity, v1);
        talons[3].set(ControlMode.Velocity, v0);
    }

    public void disable() {
        for (int i = 0; i < talons.length; i++) {
            talons[i].set(ControlMode.PercentOutput, 0);
        }
    }


    private double applyDeadZone(double joy) {
        if (Math.abs(joy) < deadZone) {
            return 0;
        } else if (joy > 0) {
            return (joy - deadZone) / (1 - deadZone);
        } else {
            return (joy + deadZone) / (1 - deadZone);
        }
    }

    public void resetTicks() {
        for (int i = 0; i < talons.length; i++) {
            talons[i].setSelectedSensorPosition(0);
        }
    }

    public void toSmartDashboard() {
        double sum = 0;
        double sumPercent = 0;
        double sumVel = 0;
        for (int i = 0; i < talons.length; i++) {
            sum += talons[i].getSelectedSensorPosition();
            sumPercent += talons[i].getMotorOutputPercent();
            sumVel += talons[i].getSelectedSensorVelocity();
        }
        SmartDashboard.putNumber("pos", sum / 4);
        SmartDashboard.putNumber("Motor Percent", sumPercent / 4);
        SmartDashboard.putNumber("Motor Velocity", sumVel / 4);
    }

    public void drive(int pos) {
        for (int i = 0; i < talons.length; i++) {
            talons[i].set(ControlMode.Position, pos);
        }
    }

    public void turnTo(double theta) {
//		double navX_theta = Robot.sensors.getNavX().getTheta();
//		theta = navX_theta + theta;

        theta *= Math.PI / 180;

        /*
        double robotLength  = 34.5;
        double robotWidth = 27;
        */

        double robotLength = StormProp.getNumber("robotLength");
        double robotWidth = StormProp.getNumber("robotWidth");
        double wheelRadius = StormProp.getNumber("wheelRadius");


        double r = Math.sqrt(Math.pow(robotLength, 2) + Math.pow((robotWidth), 2)) / 2.0;

        double s = r * theta;

        double encoderTicks = (s / (wheelRadius * Math.PI) * 8192);
        //encoderTicks *= Math.sqrt(2);

        for (int i = 0; i < motions.length; i++) {
            motions[i] = new MotionMagic(DriveTalons.getInstance().getTalons()[i], maxVel / 2, maxAccel / 2);
        }

        System.out.println(encoderTicks + "");
        motions[0].runMotionMagic((int) encoderTicks * -1);
        motions[1].runMotionMagic((int) encoderTicks);
        motions[2].runMotionMagic((int) encoderTicks * -1);
        motions[3].runMotionMagic((int) encoderTicks);

        //for (int i = 0; i < talons.length; i++) {
        //System.out.println("Talon " + i + " Commanded: " + (encoderTicks));
        // motions[i].runMotionMagic((int) encoderTicks);
        //}
    }

    @Override
    protected void initDefaultCommand() {

    }
}
