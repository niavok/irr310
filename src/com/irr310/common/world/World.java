package com.irr310.common.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.event.CelestialObjectAddedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent.Reason;
import com.irr310.common.event.PlayerAddedEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.view.CelestialObjectView;
import com.irr310.common.world.view.ComponentView;
import com.irr310.common.world.view.PartView;
import com.irr310.common.world.view.PlayerView;
import com.irr310.common.world.view.ShipView;

public class World {

    private final List<CelestialObject> celestialObjects;
    private final List<Ship> ships;
    private final List<Player> players;
    private final List<Part> parts;
    private final List<Part> myParts;
    private final Map<String, Upgrade> availableUpgrades;
    private final Map<Long, Player> playerIdMap;
    private final Map<Long, Ship> shipIdMap;
    private final Map<Long, CelestialObject> celestialObjectIdMap;
    private final Map<Long, Capacity> capacityIdMap;
    private final Map<Long, Component> componentIdMap;
    private final Map<Long, Slot> slotIdMap;
    private final Map<Long, Part> partIdMap;
    
    

    ReentrantLock mutex;

    public World() {
        celestialObjects = new CopyOnWriteArrayList<CelestialObject>();
        ships = new CopyOnWriteArrayList<Ship>();
        players = new CopyOnWriteArrayList<Player>();
        parts = new CopyOnWriteArrayList<Part>();
        myParts = new CopyOnWriteArrayList<Part>();
        playerIdMap = new HashMap<Long, Player>();
        shipIdMap = new HashMap<Long, Ship>();
        celestialObjectIdMap = new HashMap<Long, CelestialObject>();
        capacityIdMap = new HashMap<Long, Capacity>();
        slotIdMap = new HashMap<Long, Slot>();
        componentIdMap = new HashMap<Long, Component>();
        partIdMap = new HashMap<Long, Part>();
        availableUpgrades = new HashMap<String, Upgrade>();
        mutex = new ReentrantLock();
    }

    public void addCelestialObject(CelestialObject o) {
        addParts(o.getParts());
        celestialObjects.add(o);
        celestialObjectIdMap.put(o.getId(), o);
        Game.getInstance().sendToAll(new CelestialObjectAddedEvent(o));
    }

    public void removeCelestialObject(CelestialObject o, Reason reason) {
        if (celestialObjects.contains(o)) {
            removeParts(o.getParts());
            celestialObjects.remove(o);
            celestialObjectIdMap.remove(o.getId());
            Game.getInstance().sendToAll(new CelestialObjectRemovedEvent(o, reason));
        }
    }

    public void addComponent(Component component) {
        addParts(component.getParts());
        componentIdMap.put(component.getId(), component);
        List<Capacity> capacities = component.getCapacities();
        for (Capacity capacity : capacities) {
            capacityIdMap.put(capacity.getId(), capacity);
        }
    }

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
        if (part.getOwner() == LoginManager.localPlayer) {
            myParts.add(part);
        }
    }

    private void removePart(Part part) {
        if (part.getOwner() == LoginManager.localPlayer) {
            myParts.remove(part);
        }
        parts.remove(part);
    }

    public void addPlayer(Player player) {
        players.add(player);
        playerIdMap.put(player.getId(), player);
        Game.getInstance().sendToAll(new PlayerAddedEvent(player));
    }

    public void addShip(Ship ship, Vec3 position) {
        ships.add(ship);
        shipIdMap.put(ship.getId(), ship);
        Game.getInstance().sendToAll(new WorldShipAddedEvent(ship, position));
    }

    public void addSlot(Slot slot) {
        slotIdMap.put(slot.getId(), slot);
    }

    public Player loadPlayer(PlayerView playerView) {
        if (playerIdMap.containsKey(playerView.id)) {
            return playerIdMap.get(playerView.id);
        }

        Player player = new Player(playerView.id, playerView.login);
        player.fromView(playerView);
        addPlayer(player);
        return player;
    }

    public CelestialObject loadCelestialObject(CelestialObjectView celestialObjectView) {
        if (celestialObjectIdMap.containsKey(celestialObjectView.id)) {
            return celestialObjectIdMap.get(celestialObjectView.id);
        }

        CelestialObject celestialObject = new CelestialObject(celestialObjectView.id, celestialObjectView.name);
        celestialObject.fromView(celestialObjectView);
        addCelestialObject(celestialObject);
        return celestialObject;
    }

    public Ship loadShip(ShipView shipView) {
        if (shipIdMap.containsKey(shipView.id)) {
            return shipIdMap.get(shipView.id);
        }

        Ship ship = new Ship(shipView.id);
        ship.fromView(shipView);
        addShip(ship, null);
        return ship;
    }

    public Slot getSlotById(long slotId) {
        return slotIdMap.get(slotId);
    }

    public Part getPartById(long partId) {
        return partIdMap.get(partId);
    }

    public Player getPlayerById(long playerId) {
        return playerIdMap.get(playerId);
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

    public Component loadComponent(ComponentView componentView) {
        if (componentIdMap.containsKey(componentView.id)) {
            return componentIdMap.get(componentView.id);
        }

        Component component = new Component(componentView.id, componentView.name);
        component.fromView(componentView);
        addComponent(component);
        return component;
    }

    public List<CelestialObject> getCelestialsObjects() {
        return celestialObjects;
    }

    public Part loadPart(PartView partView, WorldObject parentObject) {
        if (partIdMap.containsKey(partView.id)) {
            return partIdMap.get(partView.id);
        }

        Part part = new Part(partView.id, parentObject);
        part.fromView(partView);
        addPart(part);
        return part;
    }

    public List<Part> getParts() {
        return parts;
    }

    public List<Part> getMyParts() {
        return myParts;
    }

    public void lock() {
        mutex.lock();
    }

    public void unlock() {
        mutex.unlock();
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addUpgrade(Upgrade upgrade) {
        availableUpgrades.put(upgrade.getTag(), upgrade);
    }
    
    public Map<String, Upgrade> getAvailableUpgrades() {
        return availableUpgrades;
    }

}
