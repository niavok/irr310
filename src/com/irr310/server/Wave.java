package com.irr310.server;

public class Wave {

    private final int id;
    private Duration duration;
    private Duration activeDuration;

    public Wave(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public Duration getActiveDuration() {
        return activeDuration;
    }
    
    public void update() {
        
    }
    
    public void setActiveDuration(Duration activeDuration) {
        this.activeDuration = activeDuration;
    }
    
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
