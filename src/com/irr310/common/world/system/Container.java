package com.irr310.common.world.system;

public interface Container {

	boolean assign(Component object);

	void remove(Component component);
}
