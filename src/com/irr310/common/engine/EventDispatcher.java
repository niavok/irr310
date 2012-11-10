package com.irr310.common.engine;

import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.EngineEventVisitor;

public interface EventDispatcher<U extends EngineEventVisitor, T extends EngineEvent<U>> {

    void sendToAll(T event);
    
    void registerEventVisitor(U visitor);

    void unregisterEventVisitor(U visitor);
}
