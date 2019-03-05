package org.usfirst.frc5422.Minimec.subsystems.stormnet;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

// This same pattern could someday be used to implement an I2C listener or something else entirely
// that's why I didn't subclass EthernetVoice. It is really solving a slightly different problem.
public class EthernetListener extends StormNetVoice implements Runnable{
    private EthernetVoice m_ethernetVoice;
    private byte m_deviceAddress;
    private Map<String, Integer> m_commandMap;
    private byte[] m_receiveBuffer = new byte[0];
    private boolean m_stopNow = false;

    public EthernetListener(EthernetVoice eVoice, int deviceAddress) {
        m_ethernetVoice = eVoice;
        m_deviceAddress = (byte) deviceAddress; // Constrains us to 7 bit addresses for now
    }

    @Override
    public String getDeviceString() {
        return m_ethernetVoice.getDeviceString() + "listener " + Integer.toString(m_deviceAddress);
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
        System.out.println("Starting listener thread...");
        byte[] localBuffer = new byte[m_receiveBuffer.length];
        DataInputStream inputStream = m_ethernetVoice.getDataInputStream();
        m_stopNow = false;  // in case we ended up here a second time - should only happen if we reconnect
        while (!m_stopNow) {
            try {
                inputStream.readFully(localBuffer, 0, localBuffer.length);
                System.out.print("local buffer: ");
                System.out.println(Arrays.toString(localBuffer));
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (m_receiveBuffer) {
                System.arraycopy(localBuffer, 0, m_receiveBuffer, 0, localBuffer.length);
            }
        }
    }

    public void stop() {
        m_stopNow = true;
    }
}

