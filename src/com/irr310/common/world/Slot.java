package com.irr310.common.world;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.view.SlotView;

public class Slot extends GameEntity {

	private final Vect3 position;
	private final Component parentComponent;
	private final Part part;

	public Slot(long id, Component parentComponent, Part part,  Vect3 position) {
	    super(id);
		this.parentComponent = parentComponent;
		this.part = part;
		this.position = position;
	}

	public Vect3 getPosition() {
		return position;
	}

	public Component getComponent() {
		return parentComponent;
	}

	public Part getPart() {
		return part;
	}

	public Vect3 getAbsoluteShipPosition() {
		return parentComponent.getAbsoluteShipPosition(position);
	}

    public SlotView toView() {
        SlotView slotView = new SlotView();
        slotView.id = getId();
        slotView.componentId = parentComponent.getId();
        slotView.partId = part.getId();
        slotView.position = position;
        return slotView;
    }
}