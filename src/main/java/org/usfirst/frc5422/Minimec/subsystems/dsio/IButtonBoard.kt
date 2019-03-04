package org.usfirst.frc5422.Minimec.subsystems.dsio

import org.usfirst.frc5422.utils.dsio.IButton
import org.usfirst.frc5422.utils.dsio.ISwitch

interface IButtonBoard {
    val elevatorSwitch: ISwitch

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


}