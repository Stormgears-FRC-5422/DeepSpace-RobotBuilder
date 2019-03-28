package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.Robot
import org.usfirst.frc5422.Minimec.commands.Arm.ArmTo135
import org.usfirst.frc5422.Minimec.commands.Arm.ArmTo90
import org.usfirst.frc5422.Minimec.commands.Arm.ArmToRest
import org.usfirst.frc5422.Minimec.commands.AutoHome
import org.usfirst.frc5422.Minimec.commands.Drive.JoyDrive
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorMove
import org.usfirst.frc5422.Minimec.commands.Intake.ExtendIntake
import org.usfirst.frc5422.Minimec.commands.Jack.MoveJack
import org.usfirst.frc5422.Minimec.commands.Pneumatics.CargoVacDisable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.CargoVacEnable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacEnable
import org.usfirst.frc5422.utils.dsio.JoystickDetector

object DSIO {

    private var buttonBoard:IButtonBoard
    var precision = false

    init{
        val detector = JoystickDetector()
        detector.detect()
        buttonBoard = detector.buttonBoard
        setupControls()
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

    fun getPrecisionDrive(): Boolean{
        return precision
    }

    fun controlOverride(): Boolean {
        return getJoystick().getRawButton(1)
    }

    private fun setupControls()
    {
        System.out.println("setupControls()")
        buttonBoard.resetCode.whenPressed(AutoHome(true))

        // Note that these are creating and passing new Command objects, not calling functions
        if (Robot.useBackjack) {
            buttonBoard.moveBackjack.whileHeld(MoveJack(true))
        }

        if (Robot.useDrive) {
            buttonBoard.xboxA.whenPressed(JoyDrive())
//            buttonBoard.xboxY.whenPressed(DockDrive())
        }

        if (Robot.useCompressor) {
            buttonBoard.cargoIntake.whenPressed(CargoVacEnable())
            buttonBoard.cargoRelease.whenPressed(CargoVacDisable())
            buttonBoard.hatchIntake.whenPressed(HatchVacEnable())
            buttonBoard.hatchRelease.whenPressed(HatchVacDisable())
        }

        if (Robot.useIntake) {
            buttonBoard.intakeOn.whileHeld(ExtendIntake())
        }

        if (Robot.useArm) {
            buttonBoard.moveArm.whenPressed(ArmTo90())
            buttonBoard.armRest.whenPressed(ArmToRest())
            buttonBoard.arm90.whenPressed(ArmTo90())
            buttonBoard.arm135.whenPressed(ArmTo135())
        }

        if (Robot.useElevator) {
            //ignore this button. It really means "let go of the override" which is handled
            //buttonBoard.elevatorRest.whenPressed(???);

            buttonBoard.elevatorGround.whenPressed(ElevatorMove(0))
            buttonBoard.elevatorLevelOne.whenPressed(ElevatorMove(0))
            buttonBoard.elevatorLevelTwo.whenPressed(ElevatorMove(2))
            buttonBoard.elevatorLevelThree.whenPressed(ElevatorMove(3))
        }

        buttonBoard.wristSwitch.whenFlipped {
            println("WRIST SWITCH")
        }

        buttonBoard.precisionButton.whenPressed {
            precision = !precision
            println(precision)
        }

    }

}
