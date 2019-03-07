package org.usfirst.frc5422.utils.dsio

import org.usfirst.frc5422.Minimec.subsystems.dsio.ButtonIds
import org.usfirst.frc5422.Minimec.subsystems.dsio.IButtonBoard

class DummyButtonBoard: IButtonBoard {
    override val elevatorSwitch = DummySwitch()

    override val backJackLevel2 = DummyButton()

    override val backJackLevel3 = DummyButton()

    override val wristSwitch = DummySwitch()

    override val intakeOn = DummySwitch()

    override val hatchIntake = DummyButton()

    override val hatchRelease = DummyButton()

    override val elevatorRest = DummyButton()

    override val elevatorGround = DummyButton()

    override val elevatorLevelOne = DummyButton()

    override val elevatorLevelTwo = DummyButton()

    override val elevatorLevelThree = DummyButton()


    override val cargoIntake = DummyButton()

    override val cargoRelease = DummyButton()

    override val moveBackjack = DummyButton()

    override val moveArm = DummyButton()

}