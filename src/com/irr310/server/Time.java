package com.irr310.server;

public class Time {
	private final long nanotime;
	
	public Time() {
		nanotime = System.nanoTime(); 
	}
	
	public Time(long nanotime) {
		this.nanotime = nanotime;
	}

	public long getTime() {
		return nanotime;
	}
	
	public boolean after(Time otherTime) {
		return nanotime > otherTime.getTime();
	}

	public Time add(Duration duration) {
		return new Time(nanotime + duration.getDuration());
	}

	public Duration durationTo(Time otherTime) {
		return new Duration(otherTime.nanotime - nanotime);
	}
}
