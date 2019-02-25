package org.usfirst.frc5422.Minimec.subsystems.stormnet;

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class EthernetLidar(voice: StormNetVoice) : StormNetSensor(voice) {
	companion object {
		const val INCHES = 0
		const val MILLIMETERS = 1
	}

	private val sensorValues: ShortArray
	private val sensorPairValues: ShortArray
	private val addressValues: ByteArray

	private var threshold: Int = 0

	val isAligned: Boolean
		get() = Math.abs(sensorPairValues[0] - sensorPairValues[1]) <= threshold

	init {
		// TODO magic number
		sensorCount = 2
		sensorValues = ShortArray(m_numSensors)
		sensorPairValues = ShortArray(2)
		// TODO: count of addresses is hardcoded on the Mega
		addressValues = ByteArray(2)

		this.m_deviceString = voice.deviceString

		threshold = 5
	}

	override fun test(sleep: Int): Boolean {
		//		boolean superResult = super.test(sleep);
		testAddresses(sleep)

		try {
			for (i in 0 until sleep) {
				pollDistance()
				log("Lidar test returned [ " +
					sensorValues[0] + " ] [ " +
					sensorValues[1] + " ] ")
				TimeUnit.SECONDS.sleep(1)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return true
	}

	private fun testAddresses(sleep: Int) {
		try {
			for (i in 0 until sleep) {
				pollAddress()
				log("Lidar Address test returned [ " +
					addressValues[0] + " ] [ " +
					addressValues[1] + " ]")
				TimeUnit.SECONDS.sleep(1)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}

	}

	private fun pollDistance() {
		fetchShorts("L", "Lidar", sensorValues)
	}

	private fun pollAddress() {
		fetchBytes("A", "Address", addressValues)
	}

	// Distance in millimeters
	fun getDistance(sensorNumber: Int): Int {
		pollDistance()
		return sensorValues[sensorNumber].toInt() // Java wants shorts to be signed.  We want unsigned value
	}

	fun getAverageDistance(unit: Int = MILLIMETERS): Double {
		pollDistance()

		val distance = (sensorValues[0] + sensorValues[1]) / 2.0

		return when (unit) {
			INCHES -> distance / 25.4
			else -> distance
		}
	}

	fun getOffset(unit: Int = MILLIMETERS): Double {
		pollDistance()

		val offset = (sensorValues[0] - sensorValues[1]).toDouble()

		return when (unit) {
			INCHES -> offset / 25.4
			else -> offset
		}
	}

}
