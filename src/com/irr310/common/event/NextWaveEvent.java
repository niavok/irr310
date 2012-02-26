package com.irr310.common.event;

import com.irr310.server.Duration;

public class NextWaveEvent extends EngineEvent {

    private final int waveId;
    private final Duration duration;
    private final Duration activeDuration;

    public NextWaveEvent(int waveId, Duration duration, Duration activeDuration) {
        this.waveId = waveId;
        this.duration = duration;
        this.activeDuration = activeDuration;
        
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    
    public Duration getActiveDuration() {
        return activeDuration;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public int getWaveId() {
        return waveId;
    }
}
