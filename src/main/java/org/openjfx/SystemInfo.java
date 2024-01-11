package org.openjfx;

import java.util.Random;

public class SystemInfo {

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

    /**
     * Find available port for p2p communication
     */
    public static int getClientPort() {
        int s = 49152, e = 65535;
        var r = new Random();

        int port;
        boolean valid = true;

        do {
            port = r.nextInt(s, e);

            try {
                var socket = new java.net.ServerSocket(port);
                socket.close();
            } catch (java.io.IOException ex) {
                valid = false;
            }

        } while (!valid);


        return port;
    }
}