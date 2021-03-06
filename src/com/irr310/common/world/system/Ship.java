package com.irr310.common.world.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.capacity.KernelCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.engine.system.SystemEngine;

public class Ship extends SystemEntity implements Container {

    private static final double MIN_LINK_DISTANCE = 0.1;
    List<Link> links = new ArrayList<Link>();
    List<Component> components = new CopyOnWriteArrayList<Component>();
    Map<String, Component> componentKeysMap = new HashMap<String, Component>();
    //KernelCapacity kernel;
    private Faction owner;
    private boolean destructible;

    public Ship(WorldSystem system, long id) {
        super(system, id);
        owner = null;
        destructible = true;
    }

    @Override
    public boolean assign(Component component) {
        if (component.getShip() != null) {
            component.getShip().remove(component);
        }
        components.add(component);
        componentKeysMap.put(component.getKey(), component);
        component.setShip(this);

        return true;
    }

    public Link link(Slot slot1, Slot slot2) {
        if (!components.contains(slot1.getComponent())) {
            java.lang.System.err.println("the first slot must be in the ship");
            return null;
        }

        if (!components.contains(slot2.getComponent())) {
            java.lang.System.err.println("the second slot must be in the ship");
            return null;
        }

        if (slot1.getAbsoluteShipPosition().distanceTo(slot2.getAbsoluteShipPosition()) > MIN_LINK_DISTANCE) {
            java.lang.System.err.println("the distance between slot is " + slot1.getAbsoluteShipPosition().distanceTo(slot2.getAbsoluteShipPosition())
                    + " but must be lesser than " + MIN_LINK_DISTANCE + "(" + slot1.getComponent().getName() + " - " + slot2.getComponent().getName()
                    + ")");
            return null;
        }

        Link link = new Link(slot1, slot2);
        links.add(link);

        return link;
    }

    @Override
    public void remove(Component component) {
        for (Iterator<Link> iterator = this.getLinks().iterator(); iterator.hasNext();) {
            Link link = iterator.next();
            if (link.getSlot1().getComponent() == component || link.getSlot2().getComponent() == component) {
                iterator.remove();
            }
        }
        components.remove(component);
    }

    public Link link(Component component1, Component component2, Vec3 position) {

        return link(component1.getSlot(component1.getLocalShipPosition(position)), component2.getSlot(component2.getLocalShipPosition(position)));
    }

    public Map<String, Component> getComponentKeysMap() {
        return componentKeysMap;
    }

    //TODO remove if found
    //    public KernelCapacity getKernel() {
    //        return kernel;
    //    }

    public List<Component> getComponents() {
        return components;
    }

    public List<Link> getLinks() {
        return links;
    }

    //TODO remove if found
    //    public void setKernel(KernelCapacity kernel) {
    //        this.kernel = kernel;
    //    }

    //TODO remove if found
    //    public KernelCapacity getController() {
    //        return kernel;
    //    }

    public void setOwner(Faction owner) {
        if (this.owner != null) {
            this.owner.removeShip(this);
        }
        this.owner = owner;
        for (Component component : components) {
            for (Part part : component.getParts()) {
                part.setOwner(owner);
            }
        }

        this.owner.giveShip(this);
    }

    public Faction getOwner() {
        return owner;
    }

    public Component getComponentByKey(String key) {
        return componentKeysMap.get(key);
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public double getMaxSpeed(boolean theorical) {
        double totalMass = 0;
        double totalMassWithDamping = 0;
        double totalForce = 0;

        for (Component component : components) {
            for (Part part : component.getParts()) {
                totalMass += part.getMass();
                //totalMassWithDamping += part.getMass() * 1 - part.getLinearDamping(), timeStep) - 1);
            }

            for (LinearEngineCapacity linearEngineCapacity : component.getCapacitiesByClass(LinearEngineCapacity.class)) {
                if(theorical) {
                    totalForce += linearEngineCapacity.getTheoricalMaxThrust();
                } else {
                    totalForce += linearEngineCapacity.getMaxThrust();
                }
            }
        }
        
        // The 10 factor it for the 
        
        double theoricalMaxSpeed = PhysicEngine.MASS_FACTOR *totalForce / totalMass;

//        Log.trace("totalMass " + totalMass);
//        Log.trace("totalMassWithDamping " + totalMassWithDamping);
//        Log.trace("totalForce " + totalForce);
//        Log.trace("theoricalMaxSpeed " + theoricalMaxSpeed);

        return theoricalMaxSpeed;
    }
}
