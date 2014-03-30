package com.irr310.server;

import java.text.DecimalFormat;

public class Time {
	private final long nanotime;
	private static long initialTime = System.nanoTime();
	private static boolean sIsPaused = true;
	private static long pausedTime = initialTime;
	
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
    
    public static void startGame(Time time) {
        initialTime = time.nanotime;
        pausedTime = time.nanotime;
        sIsPaused = true;
    }
    
    public static void pauseGame(Time time) {
        sIsPaused = true;
        pausedTime = time.nanotime;
    }
    
    public static void resumeGame(Time time) {
        initialTime += time.nanotime - pausedTime;
        sIsPaused = false;
    }
    
    private static long computeGameNanotime(long now) {
        if(sIsPaused) {
            return pausedTime - initialTime;
        } else {
            return now - initialTime;
        }
    }
    
    public static boolean isPaused() {
        return sIsPaused;
    }
    
    public static Time getGameTime() {
        return new Time(computeGameNanotime(System.nanoTime()));
    }

	public static Timestamp getTimestamp() {
		long now = System.nanoTime();
		
		return new Timestamp(new Time(now), new Time(computeGameNanotime(now)));
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

    public String format() {
        long millitime = nanotime / 1000000;
        long hours = millitime / (60 * 60 * 1000 );
        long minutes = millitime / (60 * 1000 ) - hours * 60;
        long seconds = millitime / (1000 ) - hours * 60 - minutes * 60;
        
        String formatted = "";
        
        if(hours > 0) {
            formatted += hours+ " h" + formatted;
        }
        
        if(minutes > 0) {
            if(formatted.length() > 0) {
                DecimalFormat df = new DecimalFormat("00");
                formatted += df.format(minutes)+ " min ";
            } else {
                formatted += minutes+ " min ";
            }
        }
        
        if(formatted.length() > 0) {
            DecimalFormat df = new DecimalFormat("00");
            formatted += df.format(seconds)+ " s ";
        } else {
            formatted += seconds+ " s ";
        }
        
        return formatted;
    }
}
