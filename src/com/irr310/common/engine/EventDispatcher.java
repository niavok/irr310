package com.irr310.common.engine;

import com.irr310.common.event.EngineEvent;

public interface EventDispatcher<T extends EngineEvent> {

    void sendToAll(T event);
}
