package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Vect3;
import com.irr310.common.world.capacity.KernelCapacity;
import com.irr310.common.world.view.ShipView;
import com.irr310.server.game.driver.Controller;

public class Ship extends GameEntity implements Container {

	
	private static final double MIN_LINK_DISTANCE = 0.1;
	List<Link> links = new ArrayList<Link>();
	List<Component> components = new ArrayList<Component>();
	KernelCapacity kernel;
    private Player owner;

    public Ship(long id) {
        super(id);
        owner = null;
    }
    
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
		
		
		if(slot1.getAbsoluteShipPosition().distanceTo(slot2.getAbsoluteShipPosition()) > MIN_LINK_DISTANCE) {
			System.err.println("the distance between slot is "+slot1.getAbsoluteShipPosition().distanceTo(slot2.getAbsoluteShipPosition())+" but must be lesser than "+MIN_LINK_DISTANCE);
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

	public Link link(Component component1, Component component2, Vect3 position) {
				
		
		return link(component1.getSlot(component1.getLocalShipPosition(position)), component2.getSlot(component2.getLocalShipPosition(position)));
		//return link(component1.getSlot(component1.getShipPosition().diff(position)), component2.getSlot(component2.getShipPosition().diff(position)));
	}

	public List<Component> getComponents() {
		return  components;
	}

	public List<Link> getLinks() {
		return  links;
	}

	public void setKernel(KernelCapacity kernel) {
		this.kernel = kernel;
	}
	
	public KernelCapacity getController() {
		return kernel;
	}

    public void setOwner(Player owner) {
        if(this.owner != null) {
            this.owner.removeShip(this);
        }
        this.owner = owner;
        this.owner.giveShip(this);
    }

    public ShipView toView() {
        ShipView shipView = new ShipView();
        shipView.id = getId();
        return shipView;
    }
}
