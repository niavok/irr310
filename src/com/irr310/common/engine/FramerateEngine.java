package com.irr310.common.engine;

import com.irr310.server.Duration;
import com.irr310.server.Time;

public abstract class FramerateEngine<T extends EngineEvent> extends Engine<T> {

    protected Duration framerate; // nsec
    private boolean isPaused;

    public FramerateEngine() {
        framerate = new Duration(20000000); // 20ms or 50fps
    }

    protected abstract void frame();

    @Override
    public void run() {
        System.out.println("Start engine " + this.getClass().getSimpleName());
        setRunning(true);
        isPaused = true;
        numRunningEngines++;
        Time lastTime = new Time();

        init();

        while (isRunning()) {
            processQueue();
            if (!isPaused) {

                lastTime = new Time();
                frame();

                Time currentTime = new Time();
                Time nextTime = lastTime.add(framerate);
                if (nextTime.after(currentTime)) {
                    // Pause
                    currentTime.durationTo(nextTime).sleep();
                } else {
                    System.err.println(this.getClass().getSimpleName() + " engine is late by " + nextTime.durationTo(currentTime).getDuration()
                            + " ns ! No pause !");
                }

            } else {
                Duration.ONE_SECOND.sleep();
            }
        }

        end();

        numRunningEngines--;

    }

    protected void pause(boolean pause) {
        if (pause) {
            isPaused = true;
        } else {
            isPaused = false;
        }
    }
}
