package com.irr310.server.game.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.server.Vect3;
import com.irr310.server.game.GameEntity;

public class Ship extends GameEntity implements Container {

	List<Link> links = new ArrayList<Link>();
	List<Component> components = new ArrayList<Component>();
	Component rootComponent;

	public boolean setRootComposent(Component component) {
		if (rootComponent != null) {
			return false;
		}
		assign(component);
		rootComponent = component;
		component.setShipPosition(new Vect3(0, 0, 0));
		component.setShipRotation(new Vect3(0, 0, 0));

		return true;
	}

	@Override
	public boolean assign(Component component) {
		component.getContainer().remove(component);
		components.add(component);
		component.setContainer(this);
		return true;
	}

	public void link(Slot slot1, Slot slot2, double angle) {
		if (!components.contains(slot1.getComponent())) {
			System.err.println("the first slot must be in the ship");
		}

		if (components.contains(slot2.getComponent())) {
			System.err.println("the second slot must not be in the ship");
		}
		//assign(slot2.getComponent());

		Shape shape1 = slot1.getComponent().getShape();
		
		
		Vect3 slot1Position = slot1.getLocalPosition();
		Vect3 slot1Normal = slot1.getLocalNormal();
		
		
		// Find slot 1 position
		switch (slot1.getFace()) {
		case BACK:
			double slot1X = slot1.getPositionX() - 0.5;
			double slot1Y = shape1.getSize().y;
			double slot1Z = slot1.getPositionY() - 0.5;

			switch (slot2.getFace()) {
			case BACK:

				break;
			case BOTTOM:
				break;
			case FRONT:
				double slot2X = slot1.getPositionX() - 0.5;
				double slot2Y = shape1.getSize().y;
				double slot2Z = slot1.getPositionY() - 0.5;
				
				
				
				break;
			case LEFT:
				break;
			case RIGHT:
				break;
			case TOP:
				break;
			}

			break;
		case BOTTOM:
			break;
		case FRONT:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		case TOP:
			break;

		}

		links.add(new Link(slot1, slot2));
	}

	@Override
	public void remove(Component component) {
		components.remove(component);
		component.setContainer(null);
	}

}
