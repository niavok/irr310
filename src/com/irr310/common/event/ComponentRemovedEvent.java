package com.irr310.common.event;

import com.irr310.common.world.Component;


public class ComponentRemovedEvent extends EngineEvent {

    public enum Reason {
        INVENTORY, DESTROYED,
    }
    
	final private Component component;
    private final Reason reason;

	public ComponentRemovedEvent(Component component, Reason reason) {
		this.component = component;
        this.reason = reason;
	}
	
	@Override
	public void accept(EngineEventVisitor visitor) {
				visitor.visit(this);
	}

	public Component getComponent() {
		return component;
	}
	
	public Reason getReason() {
        return reason;
    }
	
	
}
