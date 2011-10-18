package com.irr310.server;

public class Time {
	private final long nanotime;
	
	public Time() {
		nanotime = System.nanoTime(); 
	}
	
	public Time(int nanotime) {
		this.nanotime = nanotime;
	}

	public long getTime() {
		return nanotime;
	}
	
	public boolean after(Time otherTime) {
		return nanotime > otherTime.getTime();
	}
}
