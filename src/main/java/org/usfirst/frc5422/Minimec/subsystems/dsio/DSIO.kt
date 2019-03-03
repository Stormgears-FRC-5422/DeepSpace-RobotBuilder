package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.utils.dsio.ButtonBoardSwitchedException
import org.usfirst.frc5422.utils.dsio.IRawJoystick
import org.usfirst.frc5422.utils.dsio.JoystickDetector

object DSIO {
    val buttonBoard:IButtonBoard
    var checkButton: Joystick

    init{
        checkButton = Joystick(2)
        val detector = JoystickDetector()
        detector.detect()
        buttonBoard = detector.buttonBoard

        if(checkButton.getRawButton(1))setupControls()
        else throw ButtonBoardSwitchedException("Button board controllers are switched.")
    }

    private fun setupControls()
    {
        buttonBoard.backJackLevel2.whenPressed{
            println("BACKJACK LEVEL 2")
        }

        buttonBoard.backJackLevel3.whenPressed{
            println("BACKJACK LEVEL 3")
        }

        buttonBoard.wristSwitch.whenFlipped {
            println("WRIST SWITCH")
        }

        buttonBoard.intakeOn.whenFlipped {
            println("INTAKE ON")
        }

        buttonBoard.hatchIntake.whenPressed{
            println("HATCH INTAKE")
        }

        buttonBoard.hatchRelease.whenPressed{
            println("HATCH RELEASE")
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

        buttonBoard.cargoIntake.whenPressed{
            println("CARGO INTAKE")
        }

        buttonBoard.cargoRelease.whenPressed{
            println("CARGO RELEASE")
        }
    }

    }
