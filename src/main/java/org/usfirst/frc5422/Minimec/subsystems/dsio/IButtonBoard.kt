package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.utils.dsio.IButton
import org.usfirst.frc5422.utils.dsio.ISwitch
import org.usfirst.frc5422.utils.dsio.IRawJoystick
import org.usfirst.frc5422.utils.dsio.SwitchControl

interface IButtonBoard {
    val drivingJoystick: Joystick
    val joy1: Joystick
    val joy2: Joystick

    val resetCode: ISwitch

    val backJackLevel2: IButton

    val backJackLevel3: IButton

    val wristSwitch: ISwitch

    val intakeOn: ISwitch

    val hatchIntake: IButton

    val hatchRelease: IButton

    val elevatorRest: IButton

    val elevatorGround: IButton

    val elevatorLevelOne: IButton

    val elevatorLevelTwo: IButton

    val elevatorLevelThree: IButton

    val cargoIntake: IButton

    val cargoRelease: IButton

    val moveBackjack: IButton

    val moveArm: IButton

    val armRest: IButton

    val arm90: IButton

    val arm135: IButton

    val precisionButton: IButton

}