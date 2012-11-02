package com.irr310.common.engine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.event.EngineEvent;
import com.irr310.server.Duration;

public class EngineManager<T extends EngineEvent> implements EventDispatcher<T> {

    List<Engine<T>> engineList = new CopyOnWriteArrayList<Engine<T>>();

    public void add(Engine<T> engine) {
        engineList.add(engine);
    }

    public void sendToAll(T e) {
        for (Engine<T> engine : engineList) {
            engine.pushEvent(e);
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
            Duration.HUNDRED_MILLISECONDE.sleep();
        }
    }

    public void startAndWait(Engine<T> engine) {
        add(engine);
        if (!engine.isAlive()) {
            engine.start();
        }

        // Wait engine started
        while (!engine.isRunning()) {
            Duration.TEN_MILLISECONDE.sleep();
        }

//        engine.pushEvent(new StartEngineEvent());
    }

}
