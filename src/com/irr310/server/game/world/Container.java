package com.irr310.server.game.world;

public interface Container {

	boolean assign(Component object);

	void remove(Component component);
}
