package org.usfirst.frc5422.utils.dsio

interface IRawJoystick {
	val joystickX: Double

	val joystickY: Double

	val joystickZ: Double

	val throttleV: Double

	fun getRawButton(button: Int): Boolean
}
