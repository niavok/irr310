package com.irr310.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Engine extends Thread{

	protected ServerGame game;
	protected boolean isRunning;
	private boolean isPaused;
	private Duration framerate; // nsec
	protected Queue<EngineEvent> eventsQueue;
	// private Time nextTime;
	static int numRunningEngines = 0;

	public Engine(ServerGame game) {
		this.game = game;
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
		
		init();
		
		while (isRunning) {
			processQueue();
			if (!isPaused) {

				frame();

				// nextTime.add(framerate);
				framerate.sleep();
				// boost::thread::sleep(nextTime);

			} else {
				Duration.ONE_SECOND.sleep();
				/*
				 * boost::xtime xt; boost::xtime_get(&xt,boost::TIME_UTC);
				 * xt.sec += 1; boost::thread::sleep(xt);
				 */
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
