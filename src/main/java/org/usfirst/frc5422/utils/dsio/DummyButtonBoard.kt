package org.usfirst.frc5422.utils.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.subsystems.dsio.ButtonIds
import org.usfirst.frc5422.Minimec.subsystems.dsio.IButtonBoard


class DummyButtonBoard: IButtonBoard {
    override val drivingJoystick = Joystick(0)
    override val joy1 = Joystick(1)
    override val joy2 = Joystick(2)

    override val resetCode = DummySwitch()

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

    override val armPickupPosition = DummyButton()

    override val armRest = DummyButton()

    override val arm90 = DummyButton()

    override  val arm135 = DummyButton()

    override val precisionButton = EnhancedButton(drivingJoystick, ButtonIds.XBOX_BUTTON_RIGHT_STICK)

    override val xboxA = EnhancedButton(drivingJoystick, ButtonIds.XBOX_BUTTON_A)
    override val xboxB = EnhancedButton(drivingJoystick, ButtonIds.XBOX_BUTTON_B)
    override val xboxX = EnhancedButton(drivingJoystick, ButtonIds.XBOX_BUTTON_X)
    override val xboxY = EnhancedButton(drivingJoystick, ButtonIds.XBOX_BUTTON_Y)


}