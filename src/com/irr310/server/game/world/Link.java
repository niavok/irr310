package com.irr310.server.game.world;

public class Link {

	private final Slot slot1;
	private final Slot slot2;

	public Link(Slot slot1, Slot slot2) {
		this.slot1 = slot1;
		this.slot2 = slot2;
	}

	public Slot getSlot1() {
		return slot1;
	}

	public Slot getSlot2() {
		return slot2;
	}
	
	

}
