package com.irr310.common.engine;

import com.irr310.server.Time.Timestamp;

public interface Engine {
    void init();
    void start();
    void tick(Timestamp time);
    void stop();
    void destroy();
}
