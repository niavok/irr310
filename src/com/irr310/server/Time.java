package com.irr310.server;

public class Time {
	private final long nanotime;
	
	public Time() {
		nanotime = System.nanoTime(); 
	}
	
	public Time(long nanotime) {
		this.nanotime = nanotime;
	}
	
	public Time(float f) {
        this((long)(f*1000000000f));
    }

	public long getTime() {
		return nanotime;
	}
	
	public float getSeconds() {
        return (float) ((double) nanotime / (double) 1000000000);
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
