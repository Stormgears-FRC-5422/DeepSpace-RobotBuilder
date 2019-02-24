package org.usfirst.frc5422.Minimec.subsystems.stormnet;

import java.util.HashMap;
import java.util.Map;

public class StormNet {
	private static StormNet instance;
	private Thread m_thread;
	private EthernetListener m_listener;
	private EthernetLidar m_lidar;
	private StormNetSensor m_testSensor;
	private Map<String,Integer> m_commandMap;
	private int m_commandSize = 36;

	private String m_address;
	private int m_port;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int FRONT = 2;
	public static final int BACK = 3;

	private StormNet(String address, int port) {
		m_address = address;
		m_port = port;

		// This MUST match what is coming from the StormNet board itself
		m_commandMap = new HashMap<String,Integer>();
		m_commandMap.put("P", 0); // ping
		m_commandMap.put("F", 1); // fast
		m_commandMap.put("S", 5); // slow
		m_commandMap.put("B", 9); // blink
		m_commandMap.put("L", 13); // lidar
		m_commandMap.put("V", 17); // line Value
		m_commandMap.put(":", 25); // timer
		m_commandSize = 37; // count of bytes from above
	}

	public static void init(String address, int port) {
		instance = new StormNet(address, port);
		instance.connect();
	}

	public static StormNet getInstance() {return instance;}

	private void connect() {
		EthernetVoice ethernetVoice = new EthernetVoice(m_address, m_port);
		m_listener = new EthernetListener(ethernetVoice,0);
		m_listener.setCommandLocations(m_commandMap, m_commandSize);

		// TODO This might leak a thread if we end up here twice
		m_thread = new Thread(m_listener);
		m_thread.start();
		m_lidar = new EthernetLidar(m_listener);
		m_testSensor = new StormNetSensor(m_listener);
	}

	public void stop() {
		m_listener.stop();
	}

	public boolean test() {
		System.out.println("In StormNet Test");
		System.out.println("about to test core");
		m_testSensor.setDebug(true);
		System.out.println("core test " + new String(m_testSensor.test() ? "passed" : "failed"));
//		System.out.println("about to test lidar");
//		m_lidar.test();
		m_testSensor.setDebug(false);
		return true;
	}

	public EthernetLidar getM_lidar() {
		return m_lidar;
	}
	public StormNetSensor getM_testSensor() { return m_testSensor; }

	public int getLidarDistance(int sensorNumber) {
		return m_lidar.getDistance(sensorNumber);
	}

	public String printLidarPair(int num) {
		return m_lidar.getPair(num)[0] + ", " + m_lidar.getPair(num)[1];
	}
}
