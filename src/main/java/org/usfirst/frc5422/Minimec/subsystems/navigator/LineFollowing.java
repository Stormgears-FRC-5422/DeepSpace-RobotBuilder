package org.usfirst.frc5422.Minimec.subsystems.navigator;

import org.usfirst.frc5422.Minimec.Robot;
import org.usfirst.frc5422.Minimec.subsystems.StormNetSubsystem;
import org.usfirst.frc5422.Minimec.subsystems.stormnet.LineIR;
import org.usfirst.frc5422.Minimec.subsystems.stormnet.StormNet;
import org.usfirst.frc5422.Minimec.subsystems.stormnet.StormNetVoice;

public class LineFollowing {



    public LineFollowing() {

    }

    public void printValues() {
        System.out.println(Robot.stormNet.getLineIROffset());
    }
}
