package com.irr310.server;

public class Duration {
	private final long nanotime;
	
	public final static Duration ONE_SECOND = new Duration(1000000000);

	
	public Duration(int nanotime) {
		this.nanotime = nanotime;
	}

	public long getDuration() {
		return nanotime;
	}
	
	public boolean longer(Duration otherTime) {
		return nanotime > otherTime.getDuration();
	}

	public void sleep() {
		long millistime = nanotime/1000000;
		try {
			Thread.sleep(millistime, (int) (nanotime - millistime * 1000000));
		} catch (InterruptedException e) {
			// TODO Handle error
		}
		
	}
}
