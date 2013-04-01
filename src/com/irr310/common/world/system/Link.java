package com.irr310.common.world.system;

import com.irr310.common.world.state.LinkState;

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

    public LinkState toState() {
        LinkState linkState = new LinkState();
        linkState.slot1Id = slot1.getId();
        linkState.slot2Id = slot2.getId();
        return linkState;
    }
	
	

}
