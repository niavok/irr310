package com.irr310.common.engine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.server.Duration;
import com.irr310.server.SystemEngine;

public class EngineManager<V extends EngineEventVisitor , T extends EngineEvent<V>> implements EventDispatcher<V,T> {

    List<Engine<T>> engineList = new CopyOnWriteArrayList<Engine<T>>();
    List<V> visitorList = new CopyOnWriteArrayList<V>();

    public void add(Engine<T> engine) {
        if(!engineList.contains(engine)) {
            engineList.add(engine);
        }
    }

    public void sendToAll(T e) {
        for (Engine<T> engine : engineList) {
            engine.pushEvent(e);
        }
        
        for (V visitor : visitorList) {
            e.accept(visitor);
        }
    }

    public void startAndWaitAllEngines() {
        for (Engine<T> engine : engineList) {
            engine.init();
        }
        
        for (Engine<T> engine : engineList) {
            engine.waitInitEnd();
        }
        
        for (Engine<T> engine : engineList) {
            engine.start();
        }

        // Wait engines started
        boolean waitStart = true;
        while (waitStart) {
            waitStart = false;
            for (Engine<T> engine : engineList) {
                if (!engine.isRunning()) {
                    waitStart = true;
                    break;
                }
            }
            Duration.TEN_MILLISECONDE.sleep();
        }

        //sendToAll(new StartEngineEvent());
    }

    public void waitStop() {
        boolean waitStop = true;

        while (waitStop) {
            waitStop = false;
            for (Engine<T> engine : engineList) {
                if (!engine.isStopped()) {
                    waitStop = true;
                    break;
                }
            }
            if(waitStop) {
                Duration.HUNDRED_MILLISECONDE.sleep();
            }
        }
    }

    public void startAndWait(Engine<T> engine) {
        start(engine);
        wait(engine);
//        engine.pushEvent(new StartEngineEvent());
    }

    public void start(Engine<T> engine) {
        add(engine);
        if (!engine.isAlive()) {
            engine.start();
        }
    }
    public void wait(Engine<T> engine) {
        // Wait engine started
        while (!engine.isRunning()) {
            Duration.TEN_MILLISECONDE.sleep();
        }
    }

    public void registerEventVisitor(V visitor) {
        visitorList.add(visitor);
    }

    public void unregisterEventVisitor(V visitor) {
        visitorList.remove(visitor);
    }

   
}
