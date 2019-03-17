package org.usfirst.frc5422.Minimec.subsystems.stormnet;

import java.util.concurrent.TimeUnit;

public class LineIR extends StormNetSensor {
	private float[] sensorDetails;

	public LineIR(StormNetVoice voice) {
		super(voice);

		// TODO magic number
		// [0] is the position and [1] is the number of sensors active
		sensorDetails = new float[2];
		this.m_deviceString = voice.getDeviceString();
	}

	public void pollDetails() {
		fetchFloats("V", "LineValues", sensorDetails);
	}

	// Distance match values on arduino.  presume cm
	public double getOffset() {
		pollDetails();
		return ((double) sensorDetails[0]);
	}
	

}
