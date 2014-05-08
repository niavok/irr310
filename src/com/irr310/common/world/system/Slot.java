package com.irr310.common.world.system;

import com.irr310.common.tools.Vec3;

public class Slot extends SystemEntity {

	private final Vec3 position;
	private Component parentComponent;
	private Part part;

	public Slot(WorldSystem system, long id, Vec3 position) {
	    super(system, id);
		this.position = position;
	}

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Vec3 getPosition() {
		return position;
	}

	public Component getComponent() {
		return parentComponent;
	}

	public Part getPart() {
		return part;
	}

	public Vec3 getAbsoluteShipPosition() {
		return parentComponent.getAbsoluteShipLocation(position);
	}
}
