package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.Robot
import org.usfirst.frc5422.Minimec.commands.Arm.ArmTo135
import org.usfirst.frc5422.Minimec.commands.Arm.ArmTo90
import org.usfirst.frc5422.Minimec.commands.Arm.ArmToRest
import org.usfirst.frc5422.Minimec.commands.Intake.ExtendIntake
import org.usfirst.frc5422.Minimec.commands.Jack.MoveJack
import org.usfirst.frc5422.Minimec.commands.Pneumatics.CargoVacDisable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.CargoVacEnable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacEnable
import org.usfirst.frc5422.Minimec.subsystems.pneumatics.Compression
import org.usfirst.frc5422.utils.dsio.ButtonBoardSwitchedException
import org.usfirst.frc5422.utils.dsio.JoystickDetector

object DSIO {
    private val buttonBoard:IButtonBoard
    private var checkButton: Joystick
    private var isFlipped = false

    init{
        checkButton = Joystick(ButtonIds.JOYSTICK_PORT_2)
        val detector = JoystickDetector()
        detector.detect()
        buttonBoard = detector.buttonBoard

        if(checkButton.getRawButton(1))setupControls()
        else throw ButtonBoardSwitchedException("Button board controllers are switched.")
    }

    fun getJoystick() : Joystick {
        return buttonBoard.drivingJoystick
    }

    fun getJoystick1() : Joystick {
        return buttonBoard.joy1
    }

    fun getJoystick2() : Joystick {
        return buttonBoard.joy2
    }

    fun getBackJackLevel() : Int {
        if(getJoystick1().getRawButton(ButtonIds.BACKJACK_LEVEL_2)) return 2
        if(getJoystick1().getRawButton(ButtonIds.BACKJACK_LEVEL_3)) return 3
        return 0
    }

    fun getVenturiOverride(): Boolean {
        return getJoystick().getRawButton(1)
    }

    private fun setupControls()
    {
        System.out.println("setupControls()");
        // Note that these are creating and passing new Command objects, not calling functions

        buttonBoard.moveBackjack.whenPressed(MoveJack())
// To disable compressor comment out
        buttonBoard.cargoIntake.whenPressed(CargoVacEnable())
        buttonBoard.cargoRelease.whenPressed(CargoVacDisable())
        buttonBoard.hatchIntake.whenPressed(HatchVacEnable())
        buttonBoard.hatchRelease.whenPressed(HatchVacDisable())
        buttonBoard.intakeOn.whileHeld(ExtendIntake())
// to disable

        buttonBoard.moveArm.whenPressed(ArmTo90())


        buttonBoard.wristSwitch.whenFlipped {
            println("WRIST SWITCH")
        }

        buttonBoard.elevatorRest.whenPressed{
            println("ELEVATOR REST")
        }

        buttonBoard.elevatorGround.whenPressed{
            println("ELEVATOR GROUND")
        }

        buttonBoard.elevatorLevelOne.whenPressed{
            println("ELEVATOR LEVEL ONE")
        }

        buttonBoard.elevatorLevelTwo.whenPressed{
            println("ELEVATOR LEVEL TWO")
        }

        buttonBoard.elevatorLevelThree.whenPressed{
            println("ELEVATOR LEVEL THREE")
        }

        buttonBoard.armRest.whenPressed(ArmToRest())

        buttonBoard.arm90.whenPressed(ArmTo90())

        buttonBoard.arm135.whenPressed(ArmTo135())

    }

}
