package org.usfirst.frc5422.Minimec.subsystems.navigator;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.usfirst.frc5422.utils.StormProp;

public class DriveTalons {
    private final TalonSRX[] talons = new TalonSRX[4];
    private static DriveTalons instance;

    public static DriveTalons getInstance() {
        if (instance == null) {
            instance = new DriveTalons();
        }
        return instance;
    }

    private DriveTalons() {
        talons[0] = new TalonSRX(StormProp.getInt("rearRightTalonId"));
        talons[0].selectProfileSlot(0, 0);

        talons[1] = new TalonSRX(StormProp.getInt("rearLeftTalonId"));
        talons[1].selectProfileSlot(0, 0);

        talons[2] = new TalonSRX(StormProp.getInt("frontRightTalonId"));
        talons[2].selectProfileSlot(0, 0);

        talons[3] = new TalonSRX(StormProp.getInt("frontLeftTalonId"));
        talons[3].selectProfileSlot(0, 0);

        reconfig();

        //Maybe SensorPhase and inverted should be in the config
        talons[0].setSensorPhase(true);
        talons[2].setSensorPhase(true);
        talons[1].setSensorPhase(true);
        talons[3].setSensorPhase(true);

        talons[0].setInverted(true);
        talons[2].setInverted(true);
    }
    public void configure(){
        for(int i=0;i<talons.length;i++){
            talons[i].config_kP(0,StormProp.getNumber("positionP"));
            talons[i].config_kI(0,StormProp.getNumber("positionI"));
            talons[i].config_kD(0,StormProp.getNumber("positionD"));
        }
    }

    public TalonSRX[] getTalons() {
        return talons;
    }

    public void reconfig()
    {
        for(int i = 0; i<talons.length;i++)
        {
            talons[i].config_kP(0, StormProp.getNumber("positionP"));
            talons[i].config_kI(0, StormProp.getNumber("positionI"));
            talons[i].config_kD(0, StormProp.getNumber("positionD"));
            //talons[i].config_kF(0, StormProp.getNumber("positionP"));
        }
    }
}
