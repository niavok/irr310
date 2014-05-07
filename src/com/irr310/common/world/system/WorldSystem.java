package com.irr310.common.world.system;

import java.security.cert.CertPathValidatorException.Reason;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.server.engine.system.SystemEngine;

public class WorldSystem extends WorldEntity {

    private  Vec2 location;
    private double radius;
    
    private final List<CelestialObject> celestialObjects;
    private final List<Nexus> nexuses;
    private final List<Ship> ships;
    private final List<Part> parts;
    private final List<Part> myParts;
    private final Map<Long, CelestialObject> celestialObjectIdMap;
    private final Map<Long, Capacity> capacityIdMap;
    private final Map<Long, Component> componentIdMap;
    private final Map<Long, Slot> slotIdMap;
    private final Map<Long, Part> partIdMap;
    private final Map<Long, Ship> shipIdMap;
    private SystemEngine systemEngine;
    private Faction owner;
    private boolean homeSystem;
    private String name;
    
    public WorldSystem(World world, long id, Vec2 location) {
        super(world, id);
        this.location = location;
        this.radius = 1000;

        celestialObjects = new CopyOnWriteArrayList<CelestialObject>();
        nexuses =  new CopyOnWriteArrayList<Nexus>();
        ships = new CopyOnWriteArrayList<Ship>();
        parts = new CopyOnWriteArrayList<Part>();
        myParts = new CopyOnWriteArrayList<Part>();
        shipIdMap = new HashMap<Long, Ship>();
        celestialObjectIdMap = new HashMap<Long, CelestialObject>();
        capacityIdMap = new HashMap<Long, Capacity>();
        slotIdMap = new HashMap<Long, Slot>();
        componentIdMap = new HashMap<Long, Component>();
        partIdMap = new HashMap<Long, Part>();
    }
    
    public Vec2 getLocation() {
        return location;
    }

    public Vec3 getRandomEmptySpace(double deployedRadius) {
        
        Random random = new Random();
        
        Vec3 position = new Vec3(random.nextDouble() * (radius - deployedRadius), 0, 0);

        TransformMatrix rotation = TransformMatrix.identity();
        rotation.rotateX(random.nextDouble() * 360);
        rotation.rotateY(random.nextDouble() * 360);
        rotation.rotateZ(random.nextDouble() * 360);

        position = position.rotate(rotation);
        
        
        return position;
    }
    
    public double getSystemSize() {
        return radius;
    }
    
    
    public void addCelestialObject(CelestialObject o) {
        addParts(o.getParts());
        celestialObjects.add(o);
        celestialObjectIdMap.put(o.getId(), o);
//        systemEngine.sendToAll(new CelestialObjectAddedEvent(o));
    }

    public void removeCelestialObject(CelestialObject o, Reason reason) {
        if (celestialObjects.contains(o)) {
            removeParts(o.getParts());
            celestialObjects.remove(o);
            celestialObjectIdMap.remove(o.getId());
//            systemEngine.sendToAll(new CelestialObjectRemovedEvent(o, reason));
        }
    }

    public void addComponent(Component component) {
        addParts(component.getParts());
        componentIdMap.put(component.getId(), component);
        List<Capacity> capacities = component.getCapacities();
        for (Capacity capacity : capacities) {
            capacityIdMap.put(capacity.getId(), capacity);
        }
//        systemEngine.sendToAll(new ComponentAddedEvent(component));
    }

//    public void removeComponent(Component component, com.irr310.common.event.ComponentRemovedEvent.Reason reason) {
//        if (componentIdMap.containsKey(component.getId())) {
//            removeParts(component.getParts());
//            componentIdMap.remove(component.getId());
//            Ship ship = component.getShip();
//            ship.remove(component);
////            systemEngine.sendToAll(new ComponentRemovedEvent(component, ship, reason));
//        }
//    }

    private void addParts(List<Part> parts) {
        for (Part part : parts) {
            addPart(part);
        }
    }

    private void removeParts(List<Part> parts) {
        for (Part part : parts) {
            removePart(part);
        }
    }

    private void addPart(Part part) {
        partIdMap.put(part.getId(), part);
        parts.add(part);
//        if (part.getOwner() == LoginManager.localPlayer.getFaction()) {
//            myParts.add(part);
//        }
    }

    private void removePart(Part part) {
        if (part.getOwner().equals(LoginManager.localPlayer.getFaction())) {
            myParts.remove(part);
        }
        parts.remove(part);
    }

    public void addShip(Ship ship) {
        ships.add(ship);
        for (Component component : ship.getComponents()) {
            addComponent(component);
        }
        shipIdMap.put(ship.getId(), ship);
    }

    public Map<Long, Slot> getSlotIdMap() {
        return slotIdMap;
    }

    public Map<Long, Component> getComponentIdMap() {
        return componentIdMap;
    }

    public Map<Long, Capacity> getCapacityIdMap() {
        return capacityIdMap;
    }

    public List<Ship> getShips() {
        return ships;
    }
    
    public void addSlot(Slot slot) {
        slotIdMap.put(slot.getId(), slot);
    }
    
    public Slot getSlotById(long slotId) {
        return slotIdMap.get(slotId);
    }

    public Part getPartById(long partId) {
        return partIdMap.get(partId);
    }

    public Ship getShipById(long shipId) {
        return shipIdMap.get(shipId);
    }

    public CelestialObject getCelestialObjectById(long celestialObjectId) {
        return celestialObjectIdMap.get(celestialObjectId);
    }

    public Component getComponentBy(long componentId) {
        return componentIdMap.get(componentId);
    }

    public Capacity getCapacityById(long capacityId) {
        return capacityIdMap.get(capacityId);
    }

    public List<CelestialObject> getCelestialsObjects() {
        return celestialObjects;
    }

    public List<Part> getParts() {
        return parts;
    }

    public List<Part> getMyParts() {
        return myParts;
    }
    
    public List<Nexus> getNexuses() {
        return nexuses;
    }

    public void setOwner(Faction faction) {
        this.owner = faction;
    }
    
    public Faction getOwner() {
        return owner;
    }

    public void setHomeSystem(boolean homeSystem) {
        this.homeSystem = homeSystem;
    }

    public boolean isHomeSystem() {
        return homeSystem;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public double getRadius() {
		return radius;
	}

    public void addNexus(Nexus rootNexus) {
        nexuses.add(rootNexus);        
    }
  
    public void setRadius(double radius) {
        this.radius = radius;
    }
}
