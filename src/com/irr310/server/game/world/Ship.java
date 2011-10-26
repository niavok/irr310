package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;

public class Ship extends GameEntity implements Container {

	
	private static final double MIN_LINK_DISTANCE = 0.1;
	List<Link> links = new ArrayList<Link>();
	List<Component> components = new ArrayList<Component>();

	@Override
	public boolean assign(Component component) {
		if(component.getContainer() != null) {
			component.getContainer().remove(component);
		}
		components.add(component);
		component.setContainer(this);
		return true;
	}

	public Link link(Slot slot1, Slot slot2) {
		if (!components.contains(slot1.getComponent())) {
			System.err.println("the first slot must be in the ship");
			return null;
		}

		if (!components.contains(slot2.getComponent())) {
			System.err.println("the second slot must be in the ship");
			return null;
		}
		
		
		if(slot1.getPosition().plus(slot1.getComponent().getShipPosition()).distanceTo(slot2.getPosition().plus(slot2.getComponent().getShipPosition())) > MIN_LINK_DISTANCE) {
			System.err.println("the distance between slot is "+slot1.getPosition().distanceTo(slot2.getPosition())+" but must be lesser than "+MIN_LINK_DISTANCE);
			return null;
		}
		
		Link link = new Link(slot1, slot2);
		links.add(link);
		
		return link;
	}

	@Override
	public void remove(Component component) {
		components.remove(component);
		component.setContainer(null);
	}

	public Link link(Kernel kernel, Camera camera, Vect3 position) {
		return link(kernel.getSlot(kernel.getShipPosition().diff(position)), camera.getSlot(camera.getShipPosition().diff(position)));
	}

	public List<Component> getComponents() {
		return  components;
	}

}
