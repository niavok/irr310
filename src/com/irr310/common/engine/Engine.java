package com.irr310.common.engine;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.common.event.EngineEvent;

public abstract class Engine extends Thread {

    private boolean isRunning;
    protected Queue<EngineEvent> eventsQueue;
    // private Time nextTime;
    static int numRunningEngines = 0;

    public Engine() {
        eventsQueue = new LinkedBlockingQueue<EngineEvent>();
    }

    protected abstract void processEvent(EngineEvent e);

    protected abstract void init();

    protected abstract void end();

    public static int getRunningEngineCount() {
        return numRunningEngines;
    }

    public void pushEvent(EngineEvent event) {
        synchronized (eventsQueue) {
            eventsQueue.add(event);
            eventsQueue.notify();
        }
    }

    protected void processQueue() {
        while (!eventsQueue.isEmpty()) {
            EngineEvent e = eventsQueue.poll();
            processEvent(e);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
