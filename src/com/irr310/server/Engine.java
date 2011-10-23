package com.irr310.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.server.event.EngineEvent;

public abstract class Engine extends Thread{

	protected boolean isRunning;
	private boolean isPaused;
	protected Duration framerate; // nsec
	protected Queue<EngineEvent> eventsQueue;
	// private Time nextTime;
	static int numRunningEngines = 0;

	public Engine() {
		framerate = new Duration(20000000); // 20ms or 50fps
		eventsQueue = new LinkedBlockingQueue<EngineEvent>();
	}

	protected abstract void frame();

	protected abstract void processEvent(EngineEvent e);

	@Override
	public void run() {
		System.out.println("Start engine "+ this.getClass().getSimpleName());
		isRunning = true;
		isPaused = true;
		numRunningEngines++;
		Time lastTime = new Time();
		
		init();
		
		while (isRunning) {
			processQueue();
			if (!isPaused) {

				lastTime = new Time();
				frame();

				Time currentTime = new Time();
				Time nextTime = lastTime.add(framerate);
				if(nextTime.after(currentTime)) {
					// Pause
					currentTime.durationTo(nextTime).sleep();
				} else {
					System.err.println(this.getClass().getSimpleName()+" engine is late by "+nextTime.durationTo(currentTime).getDuration()+" ns ! No pause !");
				}

			} else {
				Duration.ONE_SECOND.sleep();
			}
		}
		
		end();
		
		numRunningEngines--;

	}

	protected abstract void init() ;
	
	protected abstract void end();

	public static int getRunningEngineCount() {
		return numRunningEngines;
	}

	public void pushEvent(EngineEvent event) {
		eventsQueue.add(event);
	}

	public void attachEngines() {

	}

	

	protected void pause(boolean pause) {
		if (pause) {
			isPaused = true;
		} else {
			isPaused = false;
		}
	}

	protected void processQueue() {
		while (!eventsQueue.isEmpty()) {
			EngineEvent e = eventsQueue.poll();
			processEvent(e);
		}
	}

}
