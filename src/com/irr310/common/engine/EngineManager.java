package com.irr310.common.engine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

public class EngineManager {

	private boolean mRunning;
	
    List<Engine> engineList = new CopyOnWriteArrayList<Engine>();

//    private EngineThread mEngineThread;

    public synchronized void add(Engine engine) {
        if(!engineList.contains(engine)) {
            engineList.add(engine);
            engine.init();
            if(mRunning) {
                engine.start();
            }
        }
    }

//    public void start() {
//        
//        mEngineThread = new EngineThread();
//        mRunning = true;
//        mEngineThread.start();
//    }
    
//    public void waitStop() {
//        try {
//            this.wait();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    
    public synchronized  void stop() {
        
//        synchronized (this) {
            mRunning = false;
//            try {
//                this.wait();
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

//    private class EngineThread extends Thread {
//
//		@Override
//    	public void run() {
//    		
//    	}
//    }
    
    
    public void run() {
        synchronized (this) {
            mRunning = true;
        }
        
        for (Engine engine : engineList) {
            engine.start();
        }
        
        while(mRunning) {
            Timestamp time = Time.getTimestamp();
            for (Engine engine : engineList) {
                engine.tick(time);
            }   
        }
        
        for (Engine engine : engineList) {
            engine.stop();
        }
        
        for (Engine engine : engineList) {
            engine.destroy();
        }
        
//        synchronized (EngineManager.this) {
//            EngineManager.this.notifyAll();
//            
//        }
    }
    
}
