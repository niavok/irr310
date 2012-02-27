package com.irr310.server;

public abstract class WaveEvent {

    private final Duration durationBeforeActivation;

    public WaveEvent(Duration durationBeforeActivation) {
        this.durationBeforeActivation = durationBeforeActivation;
    }
    
    public Duration getDurationBeforeActivation() {
        return durationBeforeActivation;
    }

    public abstract void action();

}
