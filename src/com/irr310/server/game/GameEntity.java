package com.irr310.server.game;

public class GameEntity {
	long id;
	
	private static long nextId = 0;
	
	private  static synchronized long getNewId() {
		return nextId++;
	}
	
	public GameEntity() {
		id = getNewId();
	}
	
}
