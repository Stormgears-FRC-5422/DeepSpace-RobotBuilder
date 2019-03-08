package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.Robot
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

        // BACKJACK
//        buttonBoard.backJackLevel2.whenPressed(SetJackLevel(2))
//        buttonBoard.backJackLevel2.whenReleased(SetJackLevel(0))
//        buttonBoard.backJackLevel3.whenPressed(SetJackLevel(3))
//        buttonBoard.backJackLevel3.whenReleased(SetJackLevel(0))
        buttonBoard.moveBackjack.whenPressed(MoveJack())

        buttonBoard.cargoIntake.whenPressed(CargoVacEnable())
        buttonBoard.cargoRelease.whenPressed(CargoVacDisable())
        buttonBoard.hatchIntake.whenPressed(HatchVacEnable())
        buttonBoard.hatchRelease.whenPressed(HatchVacDisable())

//        buttonBoard.moveBackjack.whenPressed(){
//            println("MOVE BACKJACK")
//            MoveJack()
//        }
//
//        buttonBoard.backJackLevel2.whenPressed{
//            println("BACKJACK LEVEL 0 --> 2")
//            SetJackLevel(2)
//        }
//
//        buttonBoard.backJackLevel2.whenReleased{
//            println("BACKJACK LEVEL 2 --> 0")
//            SetJackLevel(0)
//        }
//
//        buttonBoard.backJackLevel3.whenPressed{
//            println("BACKJACK LEVEL 0 --> 3")
//            SetJackLevel(3)
//        }
//        buttonBoard.backJackLevel3.whenReleased{
//            println("BACKJACK LEVEL 3--> 0")
//            SetJackLevel(0)
//        }

        buttonBoard.wristSwitch.whenFlipped {
            println("WRIST SWITCH")
        }

        buttonBoard.intakeOn.whenFlipped {
            println("INTAKE ON")
            //Robot.intake.moveIntakeWheelsIn()
            if (isFlipped) {
                Robot.intake.wheelsOff()
                isFlipped = false
            } else {
                Robot.intake.moveIntakeWheelsIn()
                isFlipped = true
            }
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


        buttonBoard.moveArm.whenPressed {
            Robot.arm.moveDown()
        }
    }

    }
