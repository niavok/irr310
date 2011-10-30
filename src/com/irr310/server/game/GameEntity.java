package com.irr310.server.game;

public class GameEntity {
	private long id;
	
	private static long nextId = 0;
	
	private  static synchronized long getNewId() {
		return nextId++;
	}
	
	public GameEntity() {
		id = getNewId();
	}
	
	public long getId() {
        return id;
    }
}
