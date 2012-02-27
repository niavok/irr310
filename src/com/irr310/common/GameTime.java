package com.irr310.common;

import com.irr310.server.Time;

public class GameTime {

    private static long initialTime = System.nanoTime();
    
    public static Time getGameTime() {
        return new Time(System.nanoTime() - initialTime);
    }
    
}
