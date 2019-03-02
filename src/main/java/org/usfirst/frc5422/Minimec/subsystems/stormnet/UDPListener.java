package org.usfirst.frc5422.Minimec.subsystems.stormnet;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

// This same pattern could someday be used to implement an I2C listener or something else entirely
// that's why I didn't subclass EthernetVoice. It is really solving a slightly different problem.
public class UDPListener extends StormNetVoice implements Runnable{
    private byte m_deviceAddress;
    private String m_address;
    private int m_port;
    private Map<String, Integer> m_commandMap;
    private byte[] m_receiveBuffer = new byte[0];
    private boolean m_stopNow = false;

    public UDPListener(String address, int port) {
        m_address = address;
        m_port = port;
        m_deviceAddress = 0; // not really meaningful here
    }

    @Override
    public String getDeviceString() {
        return "UDP listener: " + m_port;
    }

    public void setCommandLocations(Map<String, Integer> commandMap, int size) {
        m_commandMap = commandMap;
        synchronized (m_receiveBuffer) {
            m_receiveBuffer = new byte[size];
        }
    }

    @Override
    protected boolean transaction_internal(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize) {
        // This listener is just managing bytes that are coming in on the wire.
        // The downstream side already knows what it is sending, and this side must
        // agree. I can think of more complex patterns where this is negotiated at startup
        // but not right now.
        //
        // All commands start with a unique character, so map that character to its position
        // in the receiveBuffer. So <"P", 0>;<"F", 4> would make sense since PING return 4 bytes
        //
        // No bytes are actually sent, but the command name is trapped to find the offset
        // from which to pull the relevant bytes

//        System.out.println(new String(new byte[]{dataToSend[0]}));// wow really?
//        System.out.println(m_commandMap.toString());
        int offset = m_commandMap.get( new String(new byte[]{dataToSend[0]}) );  //todo error handling
        synchronized (m_receiveBuffer) {
            System.arraycopy(m_receiveBuffer, offset, dataReceived,0, receiveSize);
        }
//        System.out.println(Arrays.toString(dataReceived));
        return StormNetSensor.STORMNET_SUCCESS;
    }

    // This only happens once, so we know a lot about its synchrony with other
    // parts of the class
    public void run() {
        DatagramSocket socket;
        InetAddress address;
        m_stopNow = false;

        System.out.println("Starting listener thread...");
        byte[] localBuffer = new byte[m_receiveBuffer.length];

        try {
            // Listening only - lets use a different port
            socket = new DatagramSocket(5423);
            address = InetAddress.getByName("10.54.22.2");
            DatagramPacket packet = new DatagramPacket(localBuffer, localBuffer.length);

            while (!m_stopNow) {
                socket.receive(packet);
                //for debugging
                //System.out.print("local buffer: ");
                //System.out.println(Arrays.toString(localBuffer));
                synchronized (m_receiveBuffer) {
                    System.arraycopy(packet.getData(), 0, m_receiveBuffer, 0, packet.getLength());
                }
            }
            socket.close(); // so we don't run into ourselves later
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        m_stopNow = true;
    }
}

