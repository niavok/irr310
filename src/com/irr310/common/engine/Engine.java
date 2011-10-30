package com.irr310.common.engine;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.server.event.ServerEngineEvent;

public abstract class Engine<T extends EngineEvent> extends Thread {

    private boolean isRunning;
    protected Queue<T> eventsQueue;
    // private Time nextTime;
    static int numRunningEngines = 0;

    public Engine() {
        eventsQueue = new LinkedBlockingQueue<T>();
    }

    protected abstract void processEvent(T e);

    protected abstract void init();

    protected abstract void end();

    public static int getRunningEngineCount() {
        return numRunningEngines;
    }

    public void pushEvent(T event) {
        synchronized (eventsQueue) {
            eventsQueue.add(event);
            eventsQueue.notify();
        }
    }

    protected void processQueue() {
        while (!eventsQueue.isEmpty()) {
            T e = eventsQueue.poll();
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
