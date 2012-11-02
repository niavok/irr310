package com.irr310.common.engine;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import com.irr310.common.event.EngineEvent;

public abstract class Engine<T extends EngineEvent> {

    private boolean isRunning;
    private boolean isStopped;
    protected Queue<T> eventsQueue;
    // private Time nextTime;

    Thread thread;
    private boolean isInitedStarted;
    Semaphore startSemaphore;
    Semaphore endInitSemaphore;

    public Engine() {
        eventsQueue = new LinkedBlockingQueue<T>();
        isStopped = false;
        isInitedStarted = false;
        startSemaphore = new Semaphore(0);
        endInitSemaphore = new Semaphore(0);
        
        thread = new Thread() {
            @Override
            public void run() {
                onInit();
                endInitSemaphore.release();
                try {
                    startSemaphore.acquire();
                    onStart();
                    Engine.this.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        
    }

    public boolean isAlive() {
        return thread.isAlive();
    }
    
    public void init() {

        if (!isInitedStarted) {
            isInitedStarted = true;
            
            
            thread.start();

            
        }
    }
    
    public void waitInitEnd() {
        try {
            endInitSemaphore.acquire();
            endInitSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }

    public void start() {
        init();
        startSemaphore.release();
    }

    protected abstract void run();

    protected abstract void processEvent(T e);

    protected abstract void onInit();
    protected abstract void onStart();

    protected abstract void onEnd();

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

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

}
