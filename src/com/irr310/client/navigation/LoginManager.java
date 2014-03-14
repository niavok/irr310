package com.irr310.client.navigation;

import com.irr310.common.world.Player;


public class LoginManager {
    
    public static Player localPlayer = null;
    
    public static boolean  isLogged() {
        return localPlayer != null;
    }
    
    public static Player getLocalPlayer() {
        return localPlayer;
    }
}
