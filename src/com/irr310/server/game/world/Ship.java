package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.game.GameEntity;

public class Ship extends GameEntity implements Container {

	List<Link> links = new ArrayList<Link>();	
	List<Component> components = new ArrayList<Component>();
	@Override
	public boolean assign(Component component) {
		component.getContainer().remove(component);
		components.add(component);
		component.setContainer(this);
		return true;
	}
	


	public void link(Slot slot1, Slot slot2) {
		links.add(new Link(slot1, slot2));
	}



	@Override
	public void remove(Component component) {
		components.remove(component);
		component.setContainer(null);
	}

}
