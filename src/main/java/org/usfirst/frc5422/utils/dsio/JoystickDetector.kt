package org.usfirst.frc5422.utils.dsio

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Joystick
import org.usfirst.frc5422.Minimec.subsystems.dsio.ButtonBoard
import org.usfirst.frc5422.Minimec.subsystems.dsio.IButtonBoard

class JoystickDetector {

    private val ds = DriverStation.getInstance()
    private val names: Array<String?> = arrayOfNulls(6)
    private val joysticks: Array<Joystick?> = arrayOfNulls(6)

    private var drivingJoystickChannel = -1
    private var mspChannel = -1
    private var jumperGamepadChannel = -1
    private var normalGamepadChannel = -1
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
//            return ButtonBoard.getInstance(Joystick(1), Joystick(2), Joystick(0))
            return if (jumperGamepadChannel != -1 && normalGamepadChannel != -1) {
                val drivingJoystick = drivingJoystick
                ButtonBoard.getInstance(Joystick(normalGamepadChannel), Joystick(jumperGamepadChannel), drivingJoystick as XboxJoystick)
            } else {
                DummyButtonBoard()
            }
        }

    fun detect() {
        for (channel in names.indices) {
            names[channel] = ds.getJoystickName(channel)

            if (names[channel]!!.length > 1) {
                joysticks[channel] = Joystick(channel)
            }
        }

        for (i in joysticks.indices) {
            val joystick = joysticks[i]
            if (joystick != null) {
                if (joystick.name.toUpperCase().contains("XBOX")) {
                    xboxChannel = i
                } else if (joystick.name.contains("Generic")) {    // Match Generic USB Joystick
                    if (joystick.getRawButton(1)) {
                        jumperGamepadChannel = i
                    } else {
                        normalGamepadChannel = i
                    }
                }
            }
        }
    }
}