package com.irr310.server;

public class Time {
	private final long nanotime;
	private static long initialTime = System.nanoTime();
	
	public Time(long nanotime) {
		this.nanotime = nanotime;
	}
	
	public Time(float f) {
        this((long)(f*1000000000f));
    }

	public long getTime() {
		return nanotime;
	}
	
	public double getSeconds() {
        return (double) ((double) nanotime / (double) 1000000000);
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

    public Duration getDurationToNow(boolean gameTime) {
        return durationTo(now(gameTime));
    }
    
    public static Time now(boolean gameTime) {
        if(gameTime) {
            return getGameTime();
        } else {
            return new Time(System.nanoTime()); 
        }
    }
    
    public static Time getGameTime() {
        return new Time(System.nanoTime() - initialTime);
    }

	public static Timestamp getTimestamp() {
		long now = System.nanoTime();
		
		return new Timestamp(new Time(now), new Time(now - initialTime));
	}
	
	public static class Timestamp {

		private Time gameTime;
		private Time time;

		public Timestamp(Time time, Time gameTime) {
			this.time = time;
			this.gameTime = gameTime;
		}
		
		public Time getGameTime() {
			return gameTime;
		}
		
		public Time getTime() {
			return time;
		}
	}
}
