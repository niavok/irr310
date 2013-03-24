package com.irr310.client.navigation;

import com.irr310.common.world.view.PlayerView;

public class LoginManager {
    
    public static PlayerView localPlayer = null;
    
    public static boolean  isLogged() {
        return localPlayer != null;
    }
    
    public static PlayerView getLocalPlayer() {
        return localPlayer;
    }
}
