package com.irr310.common.engine;



public abstract class EventEngine extends Engine {


    @Override
    public void run() {
        System.out.println("Start engine " + this.getClass().getSimpleName());
        init();
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

        end();
        setStopped(true);

    }
   
}
