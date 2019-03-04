package org.usfirst.frc5422.Minimec.subsystems.dsio

import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.Robot
import org.usfirst.frc5422.Minimec.commands.Jack.MoveJack
import org.usfirst.frc5422.Minimec.commands.Jack.SetJackLevel
import org.usfirst.frc5422.Minimec.commands.Pneumatics.RunCompressor
import org.usfirst.frc5422.Minimec.commands.Pneumatics.StopCompressor
import org.usfirst.frc5422.Minimec.subsystems.intake.Intake
import org.usfirst.frc5422.Minimec.subsystems.pneumatics.Compression
import org.usfirst.frc5422.utils.dsio.ButtonBoardSwitchedException
import org.usfirst.frc5422.utils.dsio.IRawJoystick
import org.usfirst.frc5422.utils.dsio.ISwitch
import org.usfirst.frc5422.utils.dsio.JoystickDetector

object DSIO {
    val buttonBoard:IButtonBoard
    var checkButton: Joystick
    var isFlipped = false


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
        buttonBoard.moveBackjack.whenPressed{
            println("MOVE BACKJACK")
            MoveJack()
        }

        buttonBoard.backJackLevel2.whenPressed{
            println("BACKJACK LEVEL 2")
            SetJackLevel(2)
        }

        buttonBoard.backJackLevel3.whenPressed{
            println("BACKJACK LEVEL 3")
            SetJackLevel(3)
        }

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

        buttonBoard.hatchIntake.whenPressed{
            println("HATCH PRESSED")
            //RunCompressor()
            Compression.getInstance().startCompressor()
        }

        buttonBoard.hatchRelease.whenPressed{
            println("HATCH RELEASE")
            //StopCompressor()
            Compression.getInstance().stopCompressor()
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
