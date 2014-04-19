package com.irr310.client;

public class ClientConfig {
    
    static boolean sound_isEnabled() {
        return true;
    }

    public static String getCacheDirectoryPath(String file) {
        return System.getProperty("user.home")+"/.cache/irr310/"+file;
    }

    public static String getSaveDirectoryPath(String file) {
        return System.getProperty("user.home")+"/.local/share/irr310/saves/"+file;
    }
}
