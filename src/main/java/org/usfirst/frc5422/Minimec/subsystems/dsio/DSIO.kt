package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.Button
import org.usfirst.frc5422.Minimec.Robot
import org.usfirst.frc5422.Minimec.commands.Arm.ArmToPosition
import org.usfirst.frc5422.Minimec.commands.Arm.HatchPickup
import org.usfirst.frc5422.Minimec.commands.Arm.HatchRelease
import org.usfirst.frc5422.Minimec.commands.Arm.ReleaseGroup
import org.usfirst.frc5422.Minimec.commands.AutoHome
import org.usfirst.frc5422.Minimec.commands.Drive.JoyDrive
import org.usfirst.frc5422.Minimec.commands.Drive.AutoDockApproach
import org.usfirst.frc5422.Minimec.commands.Elevator.ElevatorMove
import org.usfirst.frc5422.Minimec.commands.Intake.ExtendIntake
import org.usfirst.frc5422.Minimec.commands.Jack.JackSequence
import org.usfirst.frc5422.Minimec.commands.Jack.MoveJack
import org.usfirst.frc5422.Minimec.commands.Pneumatics.CargoVacDisable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.CargoVacEnable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacDisable
import org.usfirst.frc5422.Minimec.commands.Pneumatics.HatchVacEnable
import org.usfirst.frc5422.utils.StatusLight
import org.usfirst.frc5422.utils.StormProp
import org.usfirst.frc5422.utils.dsio.JoystickDetector
import org.usfirst.frc5422.utils.DeepSpaceTypes

object DSIO {

    private var buttonBoard:IButtonBoard
    var precision:Boolean = false

    init{
       // val detector = JoystickDetector()
        //detector.detect()
        buttonBoard = ButtonBoard.getInstance(getJoystick1(), getJoystick2(),getJoystick())
        setupControls()
    }

    fun getJoystick() : Joystick {
        return Joystick(0)
    }

    fun getJoystick1() : Joystick {
        return Joystick(1)
    }

    fun getJoystick2() : Joystick {
        return Joystick(2)
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
        buttonBoard.resetCode.whenPressed(AutoHome(true, true))

        // Note that these are creating and passing new Command objects, not calling functions
        if (Robot.useBackjack) {
            if (Robot.testBackjack) {
                buttonBoard.moveBackjack.whileHeld(MoveJack(true))
            } else {
                buttonBoard.moveBackjack.whileHeld(JackSequence(true))
            }
        }

        if (Robot.useDrive) {
            buttonBoard.xboxA.whenPressed(JoyDrive())
            buttonBoard.xboxX.whenPressed(AutoDockApproach(DeepSpaceTypes.DockTarget.SHIP_LEFT))
            buttonBoard.xboxY.whenPressed(AutoDockApproach(DeepSpaceTypes.DockTarget.SHIP_MIDDLE))
            buttonBoard.xboxB.whenPressed(AutoDockApproach(DeepSpaceTypes.DockTarget.SHIP_RIGHT))
        }

        if (Robot.useCompressor) {
            buttonBoard.cargoIntake.whenPressed(CargoVacEnable())
            buttonBoard.cargoRelease.whenPressed(CargoVacDisable())
            when(Robot.hatchCommandMode){
                0 -> {
                    buttonBoard.hatchIntake.whenPressed(HatchVacEnable())
                    buttonBoard.hatchRelease.whenPressed(HatchVacDisable())
                }
                1 -> {
                    buttonBoard.hatchIntake.whenPressed(HatchPickup())
                    buttonBoard.hatchRelease.whenPressed(HatchRelease())
                }

            }
//            buttonBoard.hatchRelease.whenPressed(ReleaseGroup())
        }

        if (Robot.useIntake) {
            buttonBoard.intakeOn.whileHeld(ExtendIntake())
        }

        if (Robot.useArm) {
            buttonBoard.armRest.whenPressed(ArmToPosition(Robot.arm.ARM_REST_POSITION_TICKS))
            buttonBoard.armPickupPosition.whenPressed(ArmToPosition(Robot.arm.ARM_PICKUP_POSITION_TICKS))
            buttonBoard.arm90.whenPressed(ArmToPosition(Robot.arm.ARM_90_POSITION_TICKS))
            buttonBoard.arm135.whenPressed(ArmToPosition(Robot.arm.ARM_135_POSITION_TICKS))
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
            if(Robot.useStatusLights) Robot.setStatusLight(StatusLight.Precision, if( precision) 1 else 0);
            if (Robot.debug) println(precision)
        }

    }


}
