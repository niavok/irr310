package com.irr310.common.engine;

import com.irr310.common.event.EngineEvent;



public abstract class EventEngine<T extends EngineEvent> extends Engine<T> {


    @Override
    public void run() {
        System.out.println("Start engine " + this.getClass().getSimpleName());
        setRunning(true);
        
        while (isRunning()) {
            synchronized (eventsQueue) {
                processQueue();
                if (eventsQueue.isEmpty()) {
                    try {
                        eventsQueue.wait(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        onEnd();
        setStopped(true);

    }
   
}
