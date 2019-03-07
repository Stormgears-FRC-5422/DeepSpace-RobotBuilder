package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.utils.dsio.*
import java.awt.Button

class ButtonBoard private constructor(joy1:Joystick, joy2:Joystick, actualJoystick: LogitechJoystick):IButtonBoard {
    override val elevatorSwitch = SwitchControl(joy1, ButtonIds.ELEVATOR_FAILSAFE_BUTTON_ID)

    override val backJackLevel2 = EnhancedButton(joy1, ButtonIds.BACKJACK_LEVEL_2)

    override val backJackLevel3 = EnhancedButton(joy1, ButtonIds.BACKJACK_LEVEL_3)

    override val wristSwitch = SwitchControl(joy2, ButtonIds.WRIST_SWITCH_ID)

    override val intakeOn = SwitchControl(joy2, ButtonIds.INTAKE_SWITCH_ID)

    override val hatchIntake = EnhancedButton(joy2, ButtonIds.HATCH_STATUS_INTAKE_BUTTON_ID)

    override val hatchRelease = EnhancedButton(joy2, ButtonIds.HATCH_STATUS_RELEASE_BUTTON_ID)

    override val elevatorRest = EnhancedButton(joy2, ButtonIds.ELEVATOR_REST_BUTTON_ID)

    override val elevatorGround = EnhancedButton(joy2, ButtonIds.ELEVATOR_LEVEL_GROUND_BUTTON_ID)

    override val elevatorLevelOne = EnhancedButton(joy2, ButtonIds.ELEVATOR_LEVEL_1_BUTTON_ID)

    override val elevatorLevelTwo = EnhancedButton(joy2, ButtonIds.ELEVATOR_LEVEL_2_BUTTON_ID)

    override val elevatorLevelThree = EnhancedButton(joy2, ButtonIds.ELEVATOR_LEVEL_3_BUTTON_ID)

    override val cargoIntake = EnhancedButton(joy1, ButtonIds.CARGO_STATUS_INTAKE_BUTTON_ID)

    override val cargoRelease = EnhancedButton(joy1, ButtonIds.CARGO_STATUS_RELEASE_BUTTON_ID)

    override val moveBackjack = EnhancedButton(joy1, ButtonIds.MOVE_BACKJACK)

    override val moveArm = EnhancedButton(joy1, ButtonIds.MOVE_ARM)

    companion object {
        private var instance: ButtonBoard? = null
        @JvmStatic
        fun getInstance(joy1: Joystick, joy2: Joystick, actualJoystick: LogitechJoystick): IButtonBoard {
            if (instance == null) {

                instance = ButtonBoard(joy1, joy2, actualJoystick)
                return instance as IButtonBoard
            }
            return instance as IButtonBoard
        }
    }
}

