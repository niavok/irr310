package com.irr310.common.engine;

import com.irr310.common.event.EngineEvent;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public abstract class FramerateEngine<T extends EngineEvent> extends Engine<T> {

    protected Duration framerate; // nsec
    private boolean isPaused;

    public FramerateEngine() {
        framerate = new Duration(20000000); // 20ms or 50fps
        isPaused = true;
    }

    public void setFramerate(Duration framerate) {
        this.framerate = framerate;
    }
    
    public Duration getFramerate() {
        return framerate;
    }
    
    protected abstract void frame();

    @Override
    public void run() {
        System.out.println("Start engine " + this.getClass().getSimpleName());

        
        Time lastTime = Time.now(false);

        setRunning(true);

        while (isRunning()) {
            processQueue();
            if(!isRunning()) {
                break;
            }
            
            if (!isPaused) {

                lastTime = Time.now(false);
                frame();

                Time currentTime = Time.now(false);
                Time nextTime = lastTime.add(framerate);
                if (nextTime.after(currentTime)) {
                    // Pause
                    currentTime.durationTo(nextTime).sleep();
                } else {
                    // TODO : check fps system
                    // System.err.println(this.getClass().getSimpleName() +
                    // " engine is late by " +
                    // nextTime.durationTo(currentTime).getDuration()
                    // + " ns ! No pause !");
                }

            } else {
                Duration.HUNDRED_MILLISECONDE.sleep();
            }
        }

        onEnd();
        setStopped(true);
    }

    protected void pause(boolean pause) {
        if (pause) {
            isPaused = true;
        } else {
            isPaused = false;
        }
    }
}
