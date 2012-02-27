package com.irr310.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Wave {

    private final int id;
    private Duration duration;
    private Duration activeDuration;
    private Queue<WaveEvent> waveEventQueue = new LinkedBlockingQueue<WaveEvent>();

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
    
    public void update(Duration currentWaveDuration) {
        if(waveEventQueue.isEmpty()) {
            return;
        }
        
        
        WaveEvent newWaveEvent = waveEventQueue.peek();
        if(currentWaveDuration.longer(newWaveEvent.getDurationBeforeActivation())) {
            waveEventQueue.poll();
            newWaveEvent.action();
        }
    }
    
    public void setActiveDuration(Duration activeDuration) {
        this.activeDuration = activeDuration;
    }
    
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void addWaveEvent(WaveEvent waveEvent) {
        waveEventQueue.add(waveEvent);
    }

}
