package com.irr310.common.world.system;

import java.security.cert.CertPathValidatorException.Reason;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.state.CelestialObjectState;
import com.irr310.common.world.state.ComponentState;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.NexusState;
import com.irr310.common.world.state.PartState;
import com.irr310.common.world.state.ShipState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.Color;
import com.irr310.server.SystemEngine;

public class WorldSystem extends WorldEntity {

    private  Vec2 location;
    private double radius;
    
    ReentrantLock mutex;
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

        mutex = new ReentrantLock();
        
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
        if (part.getOwner().isState(LoginManager.localPlayer.faction)) {
            myParts.remove(part);
        }
        parts.remove(part);
    }
    
    public CelestialObject loadCelestialObject(CelestialObjectState celestialObjectView) {
        if (celestialObjectIdMap.containsKey(celestialObjectView.id)) {
            return celestialObjectIdMap.get(celestialObjectView.id);
        }

        CelestialObject celestialObject = new CelestialObject(this, celestialObjectView.id, celestialObjectView.name);
        celestialObject.fromState(celestialObjectView);
        addCelestialObject(celestialObject);
        return celestialObject;
    }

    public Ship loadShip(ShipState shipView) {
        if (shipIdMap.containsKey(shipView.id)) {
            return shipIdMap.get(shipView.id);
        }

        Ship ship = new Ship(this, shipView.id);
        ship.fromState(shipView);
        addShip(ship, null);
        return ship;
    }

    public void addShip(Ship ship, TransformMatrix transform) {
        ships.add(ship);
        for (Component component : ship.getComponents()) {
            addComponent(component);
        }
        shipIdMap.put(ship.getId(), ship);
//        systemEngine.sendToAll(new WorldShipAddedEvent(ship, transform));
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

    public Component loadComponent(ComponentState componentView) {
        if (componentIdMap.containsKey(componentView.id)) {
            return componentIdMap.get(componentView.id);
        }

        Component component = new Component(this, componentView.id, componentView.name);
        component.fromState(componentView);
        addComponent(component);
        return component;
    }

    public List<CelestialObject> getCelestialsObjects() {
        return celestialObjects;
    }

    public Part loadPart(PartState partView, SystemObject parentObject) {
        if (partIdMap.containsKey(partView.id)) {
            return partIdMap.get(partView.id);
        }

        Part part = new Part(partView.id, parentObject);
        part.fromState(partView);
        addPart(part);
        return part;
    }

    public List<Part> getParts() {
        return parts;
    }

    public List<Part> getMyParts() {
        return myParts;
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

    public WorldSystemState toState() {
        WorldSystemState systemState = new WorldSystemState();
        systemState.id = getId();
        
        systemState.homeSystem = homeSystem;
        systemState.location = location;
        systemState.name = name;
        if(owner != null) {
            systemState.ownerColor = owner.getColor();
            systemState.ownerId = owner.getId();
        } else {
            systemState.ownerColor = Color.black;
            systemState.ownerId = -1l;
        }

        return systemState;
    }

    public void addNexus(Nexus rootNexus) {
        nexuses.add(rootNexus);        
    }

    public Nexus getNexus(NexusState nexusState) {
        for(Nexus nexus: nexuses) {
            if(nexus.isState(nexusState)) {
                return nexus;
            }
        }
        return null;
    }

    public void lock() {
        mutex.lock();
    }

    public void unlock() {
        mutex.unlock();
    }

    public boolean isState(WorldSystemState systemState) {
            return getId() == systemState.id;
    }
    
//    public void removeShip(Ship ship) {
//        ships.remove(ship);
//        shipIdMap.remove(ship.getId());
//        for (Component component : ship.getComponents()) {
//            removeComponent(component, com.irr310.common.event.ComponentRemovedEvent.Reason.SHIP);
//        }
////        systemEngine.sendToAll(new WorldShipRemovedEvent(ship));
//    }
    
}
