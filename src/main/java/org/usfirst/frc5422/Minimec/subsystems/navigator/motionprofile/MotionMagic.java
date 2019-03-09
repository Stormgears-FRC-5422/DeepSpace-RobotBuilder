package org.usfirst.frc5422.Minimec.subsystems.navigator.motionprofile;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.util.concurrent.TimeUnit;

public class MotionMagic {

    private TalonSRX talon;
   // public static RobotConfiguration config = RobotConfiguration.getInstance();
    private static final int TALON_FPID_TIMEOUT = 10;

    public MotionMagic(TalonSRX talon, double maxVel, double maxAccel) {
        this.talon = talon;
        /* first choose the sensor */
        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, TALON_FPID_TIMEOUT);
        talon.setSensorPhase(true);
        //talon.setInverted(false);

        /* Set relevant frame periods to be at least as fast as periodic rate */
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, TALON_FPID_TIMEOUT, TALON_FPID_TIMEOUT);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, TALON_FPID_TIMEOUT, TALON_FPID_TIMEOUT);
        /* set the peak and nominal outputs */
        talon.configNominalOutputForward(0, TALON_FPID_TIMEOUT);
        talon.configNominalOutputReverse(0, TALON_FPID_TIMEOUT);
        talon.configPeakOutputForward(1, TALON_FPID_TIMEOUT);
        talon.configPeakOutputReverse(-1, TALON_FPID_TIMEOUT);

        /* set closed loop gains in slot0 - see documentation */
        /*
        talon.selectProfileSlot(0, 0);
        talon.config_kF(0, StormProp.getNumber("velocityF"), TALON_FPID_TIMEOUT);
        talon.config_kP(0, StormProp.getNumber("velocityP"), TALON_FPID_TIMEOUT);
        talon.config_kI(0, StormProp.getNumber("velocityI"), TALON_FPID_TIMEOUT);
        talon.config_kD(0, StormProp.getNumber("velocityD"), TALON_FPID_TIMEOUT);
        /* set acceleration and vcruise velocity - see documentation */


        talon.configMotionCruiseVelocity((int) Math.round(maxVel), TALON_FPID_TIMEOUT);
        talon.configMotionAcceleration((int) Math.round(maxAccel), TALON_FPID_TIMEOUT);
        /* zero the sensor */
        talon.setSelectedSensorPosition(0, 0, TALON_FPID_TIMEOUT);
    }

    /**
     * The runMotionMagic method receives an encoder position
     * (8192 ticks / 1 revolution) and uses the MotionMagic
     * ControlMode along with PID to get to the commanded position.
     * This class and method applies to only one talon.
     *
     * @param targetPos - encoder position
     */
    public void runMotionMagic(int targetPos) {
        //redundant code
        /*
        talon.config_kP(0, Double.parseDouble(StormProp.getString("positionP")), TALON_FPID_TIMEOUT);
        talon.config_kI(0, Double.parseDouble(StormProp.getString("positionI")), TALON_FPID_TIMEOUT);
        talon.config_kD(0, Double.parseDouble(StormProp.getString("positionD")), TALON_FPID_TIMEOUT);

        talon.config_IntegralZone(0,(int)Double.parseDouble(StormProp.getString("positionIzone")), TALON_FPID_TIMEOUT);
        */
        talon.set(ControlMode.MotionMagic, targetPos);

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (Exception e) {
            System.out.println("Error with Motion Magic.");
            System.out.println(e.getMessage());
        }
    }
}