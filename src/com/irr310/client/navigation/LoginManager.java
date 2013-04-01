package com.irr310.client.navigation;

import com.irr310.common.world.state.PlayerState;

public class LoginManager {
    
    public static PlayerState localPlayer = null;
    
    public static boolean  isLogged() {
        return localPlayer != null;
    }
    
    public static PlayerState getLocalPlayer() {
        return localPlayer;
    }
}
