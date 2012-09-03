package com.irr310.common.event;

import com.irr310.common.world.zone.Component;
import com.irr310.common.world.zone.Ship;


public class ComponentRemovedEvent extends EngineEvent {

    public enum Reason {
        INVENTORY, DESTROYED, SHIP,
    }
    
	final private Component component;
    private final Reason reason;
    private final Ship ship;

	public ComponentRemovedEvent(Component component, Ship ship, Reason reason) {
		this.component = component;
        this.ship = ship;
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
	
	public Ship getShip() {
        return ship;
    }
	
	
}
