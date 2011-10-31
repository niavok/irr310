package com.irr310.common.engine;



public abstract class EventEngine extends Engine {


    @Override
    public void run() {
        System.out.println("Start engine " + this.getClass().getSimpleName());
        setRunning(true);
        numRunningEngines++;

        init();

        while (isRunning()) {
            synchronized (eventsQueue) {
                processQueue();
                if (eventsQueue.isEmpty()) {
                    try {
                        eventsQueue.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        end();

        numRunningEngines--;

    }
}