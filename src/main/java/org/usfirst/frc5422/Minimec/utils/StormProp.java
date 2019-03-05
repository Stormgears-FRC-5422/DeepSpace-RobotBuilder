package org.usfirst.frc5422.Minimec.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class StormProp {
    //TODO put config file in /home/lvuser
    // Maybe rename this file to something better
    private static final String path = "/home/lvuser";
    private static final String name = "config.properties";
    private static final String backUP = "config_backup.properties";
    private static File configFile = new File(path, name);
    private static Properties properties;
    private static boolean initialized = false;

    public static void init() {
        properties = new Properties();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Using backup config file");
            try {
                inputStream = new FileInputStream(new File("/home/lvuser/deploy", backUP));
                properties.load(inputStream);
            } catch (IOException w) {
                System.out.println("Failed to find back up file");
            }

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error coming from config. This should not run");
                }
            }
        }
        initialized = true;
    }

    public static String getString(String key) {
        if (!initialized) {
            init();
        }
        return properties.getProperty(key);
    }

    //This was failing and I have no idea why
    public static double getNumber(String key) {
        if (!initialized) {
            init();
        }
        return Double.parseDouble(getString(key));
    }

    public static int getInt(String key) {
        if (!initialized) {
            init();
        }
        return Integer.parseInt(getString(key));
    }

    //untested but should work
    public static boolean getBoolean(String key) {
        if (!initialized) {
            init();
        }
        String str = properties.getProperty(key);
        return str.toLowerCase().equals("true");
    }

    //puts all values to Smartdashboard except for values that should not change like talon ids.
    public static void toSmartDashBoard() {
        if (!initialized) {
            init();
        }
        String[] Blacklist = {"robotName", "hasNavX", "rearRightTalonId", "rearLeftTalonId", "frontRightTalonId", "frontLeftTalonId", "wheelRadius"};
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            if (!Arrays.asList(Blacklist).contains(key))
                SmartDashboard.putString(key, properties.getProperty(key));
        }
    }

    //updates properties object using values from SmartDashboard
    public static void updateProperties() {
        if (!initialized) {
            init();
        }
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            properties.setProperty(key, SmartDashboard.getString(key, properties.getProperty(key)));
        }
    }
}
