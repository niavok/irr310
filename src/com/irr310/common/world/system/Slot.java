package com.irr310.common.world.system;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.state.SlotState;

public class Slot extends WorldEntity {

	private final Vec3 position;
	private final Component parentComponent;
	private final Part part;

	public Slot(long id, Component parentComponent, Part part,  Vec3 position) {
	    super(parentComponent.getWorld(), id);
		this.parentComponent = parentComponent;
		this.part = part;
		this.position = position;
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

    public SlotState toState() {
        SlotState slotState = new SlotState();
        slotState.id = getId();
        slotState.componentId = parentComponent.getId();
        slotState.partId = part.getId();
        slotState.position = position;
        return slotState;
    }
}
