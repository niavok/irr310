package com.irr310.client.script.js.objects;

import com.irr310.common.Game;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vect3;

public class Component {

	private com.irr310.common.world.Component component;
	
	public Component(long id) {
	    component = Game.getInstance().getWorld().getComponentBy(id);
	}

	public long getId() {
        return component.getId();
    }

	public Capacity getCapacityByName(String name) {
	    com.irr310.common.world.capacity.Capacity capacity = component.getCapacitiesByName(name);
	    
	    if(capacity.getName().equals("linearEngine")) {
	            return new LinearEngineCapacity((com.irr310.common.world.capacity.LinearEngineCapacity) capacity);
	    }
	    return null;
    }
	
	public Vect3 getLinearSpeed() {
	    return component.getFirstPart().getLinearSpeed();
	}
	
	public Vect3 getPosition() {
	    return component.getFirstPart().getTransform().getTranslation();
	}
	
	public TransformMatrix getTransform() {
        return component.getFirstPart().getTransform();
    }
	
	public Vect3 getRotationSpeed() {
        return component.getFirstPart().getRotationSpeed();
    }
}
