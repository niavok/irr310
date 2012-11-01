package com.irr310.common.event;

import com.irr310.common.world.system.Component;


public class ComponentAddedEvent extends EngineEvent {

    
	final private Component component;

	public ComponentAddedEvent(Component component) {
		this.component = component;
	}
	
	@Override
	public void accept(EngineEventVisitor visitor) {
				visitor.visit(this);
	}

	public Component getComponent() {
		return component;
	}
	
}
