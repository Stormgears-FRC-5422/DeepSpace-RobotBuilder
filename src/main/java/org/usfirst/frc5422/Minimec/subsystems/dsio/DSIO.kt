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

    public fun getJoystick() : Joystick {
        return buttonBoard.drivingJoystick
    }

    public fun getJoystick1() : Joystick {
        return buttonBoard.joy1
    }

    public fun getJoystick2() : Joystick {
        return buttonBoard.joy2
    }

    public fun getBackJackLevel() : Int {
        if(getJoystick1().getRawButton(ButtonIds.BACKJACK_LEVEL_2)) return 2
        if(getJoystick1().getRawButton(ButtonIds.BACKJACK_LEVEL_3)) return 3
        return 0
    }

    private fun setupControls()
    {
        System.out.println("setupControls()");
        // Note that these are creating and passing new Command objects, not calling functions

        buttonBoard.moveBackjack.whenPressed(MoveJack())

        buttonBoard.cargoIntake.whenPressed(CargoVacEnable())
        buttonBoard.cargoRelease.whenPressed(CargoVacDisable())
        buttonBoard.hatchIntake.whenPressed(HatchVacEnable())
        buttonBoard.hatchRelease.whenPressed(HatchVacDisable())

        buttonBoard.intakeOn.whileHeld(ExtendIntake())

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

        buttonBoard.armRest.whenPressed{
            println("ARM REST")
            Robot.arm.moveToRest()
            Robot.arm.movePivotUp()
        }

        buttonBoard.arm90.whenPressed{
            println("ARM 90")
            Robot.arm.moveTo90()
            Robot.arm.movePivotUp()
        }

        buttonBoard.arm135.whenPressed{
            println("ARM 135")
            Robot.arm.moveTo135()
            Robot.arm.movePivotUp()
        }

    }

}
