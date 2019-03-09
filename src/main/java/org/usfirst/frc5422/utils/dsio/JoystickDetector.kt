package org.usfirst.frc5422.utils.dsio

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.subsystems.dsio.ButtonBoard
import org.usfirst.frc5422.Minimec.subsystems.dsio.IButtonBoard

class JoystickDetector {

    private val ds = DriverStation.getInstance()
    private val names: Array<String?> = arrayOfNulls(5)
    private val joysticks: Array<Joystick?> = arrayOfNulls(5)

    private var drivingJoystickChannel = -1
    private var mspChannel = -1
    private var jumperGamepadChannel = 1
    private var normalGamepadChannel = 2
    private var destroyedLogitechChannel = -1
    private var xboxChannel = -1

    val drivingJoystick: IRawJoystick
        get() {
            return when {
                xboxChannel != -1 -> {
                   XboxJoystick(xboxChannel)
                }
                drivingJoystickChannel != -1 -> {
                    LogitechJoystick(drivingJoystickChannel)
                }
                else -> {
                    DummyJoystick()
                }
            }
        }

    val buttonBoard: IButtonBoard
        get() {
            return ButtonBoard.getInstance(Joystick(1), Joystick(2), Joystick(0))
//            return if (destroyedLogitechChannel != -1 && mspChannel != -1) {
//                val drivingJoystick = drivingJoystick
//                ButtonBoard.getInstance(Joystick(mspChannel), Joystick(destroyedLogitechChannel), drivingJoystick as LogitechJoystick)
//            } else if (jumperGamepadChannel != -1 && normalGamepadChannel != -1) {
//                val drivingJoystick = drivingJoystick
//                ButtonBoard.getInstance(Joystick(jumperGamepadChannel), Joystick(normalGamepadChannel), drivingJoystick as LogitechJoystick)
//            } else {
//                DummyButtonBoard()
//            }
        }

    fun detect() {
             // pass
//        for (channel in names.indices) {
//            names[channel] = ds.getJoystickName(channel)
//
//            if (names[channel]!!.length > 1) {
//                joysticks[channel] = Joystick(channel)
//            }
//        }
//
//        for (i in joysticks.indices) {
//            val joystick = joysticks[i]
//            if (joystick != null) {
//                if (joystick.name.contains("MSP")) {    // Match MSP-430 board
//                    mspChannel = i
//                } else if (joystick.name.toUpperCase().contains("XBOX")) {
//                    xboxChannel = i
//                } else if (joystick.name.contains("Logitech")) {    // Match Logitech Extreme 3D joystick
//                    if (joystick.getRawAxis(0) < -0.9 && joystick.getRawAxis(2) > 0.9) {
//                        destroyedLogitechChannel = i
//                    } else {
//                        drivingJoystickChannel = i
//                    }
//                } else if (joystick.name.contains("Generic")) {    // Match
//                    if (joystick.getRawButton(4)) {
//                        jumperGamepadChannel = i
//                    } else {
//                        normalGamepadChannel = i
//                    }
//                }
//            }
//        }
    }
}