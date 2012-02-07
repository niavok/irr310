package com.irr310.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.KernelCapacity;
import com.irr310.common.world.view.ComponentView;
import com.irr310.common.world.view.LinkView;
import com.irr310.common.world.view.ShipView;

public class Ship extends GameEntity implements Container {

    private static final double MIN_LINK_DISTANCE = 0.1;
    List<Link> links = new ArrayList<Link>();
    List<Component> components = new ArrayList<Component>();
    Map<String, Component> componentNamesMap = new HashMap<String, Component>();
    KernelCapacity kernel;
    private Player owner;

    public Ship(long id) {
        super(id);
        owner = null;
    }

    @Override
    public boolean assign(Component component) {
        if (component.getContainer() != null) {
            component.getContainer().remove(component);
        }
        components.add(component);
        componentNamesMap.put(component.getName(), component);
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

        if (slot1.getAbsoluteShipPosition().distanceTo(slot2.getAbsoluteShipPosition()) > MIN_LINK_DISTANCE) {
            System.err.println("the distance between slot is " + slot1.getAbsoluteShipPosition().distanceTo(slot2.getAbsoluteShipPosition())
                    + " but must be lesser than " + MIN_LINK_DISTANCE);
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
        // return
        // link(component1.getSlot(component1.getShipPosition().diff(position)),
        // component2.getSlot(component2.getShipPosition().diff(position)));
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setKernel(KernelCapacity kernel) {
        this.kernel = kernel;
    }

    public KernelCapacity getController() {
        return kernel;
    }

    public void setOwner(Player owner) {
        if (this.owner != null) {
            this.owner.removeShip(this);
        }
        this.owner = owner;
        this.owner.giveShip(this);
    }

    public ShipView toView() {
        ShipView shipView = new ShipView();
        shipView.id = getId();
        shipView.owner = owner.toView();

        for (Link link : links) {
            shipView.links.add(link.toView());
        }

        for (Component component : components) {
            shipView.components.add(component.toView());
        }

        return shipView;
    }

    public void fromView(ShipView shipView) {
        World world = Game.getInstance().getWorld();
        setOwner(world.loadPlayer(shipView.owner));

        for (ComponentView component : shipView.components) {
            assign(world.loadComponent(component));
        }

        for (LinkView link : shipView.links) {
            link(world.getSlotById(link.slot1Id), world.getSlotById(link.slot2Id));
        }

    }

    public Component getComponentByName(String name) {
        return componentNamesMap.get(name);
    }

}
