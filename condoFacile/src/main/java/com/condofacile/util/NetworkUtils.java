package com.condofacile.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;

public class NetworkUtils {

    public static String getLocalIP(int port) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return "http://" + ip.getHostAddress() + ":" + port;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel recupero dell'IP locale", e);
        }
    }

    public static String getPublicIP(int port) {
        try (InputStream is = new URL("https://api.ipify.org").openStream()) {
            String ip = new String(is.readAllBytes()).trim();
            return "http://" + ip + ":" + port;
        } catch (IOException e) {
            throw new RuntimeException("Errore nel recupero dell'IP pubblico", e);
        }
    }
}