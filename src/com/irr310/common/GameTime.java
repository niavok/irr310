package com.irr310.common;

import com.irr310.server.Duration;

public class GameTime {

    private static long initialTime = System.nanoTime();
    
    public static Duration getGameTime() {
        return new Duration(System.nanoTime() - initialTime);
    }
    
}
