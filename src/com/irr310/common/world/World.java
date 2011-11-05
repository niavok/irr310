package com.irr310.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.Game;
import com.irr310.common.event.PlayerAddedEvent;
import com.irr310.common.event.WorldObjectAddedEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.view.ComponentView;
import com.irr310.common.world.view.PartView;
import com.irr310.common.world.view.PlayerView;
import com.irr310.common.world.view.ShipView;

public class World {

    List<WorldObject> objects;
    List<Ship> ships;
    List<Player> players;
    Map<Long, Player> playersIdMap;
    Map<Long, Ship> shipIdMap;
    Map<Long, Component> componentIdMap;
    Map<Long, Slot> slotIdMap;
    Map<Long, Part> partIdMap;

    public World() {
        objects = new ArrayList<WorldObject>();
        ships = new ArrayList<Ship>();
        players = new ArrayList<Player>();
        playersIdMap = new HashMap<Long, Player>();
        shipIdMap = new HashMap<Long, Ship>();
        slotIdMap = new HashMap<Long, Slot>();
        componentIdMap = new HashMap<Long, Component>();
        partIdMap = new HashMap<Long, Part>();
    }

    public void addObject(WorldObject o) {
        objects.add(o);
        Game.getInstance().sendToAll(new WorldObjectAddedEvent(o));
    }
    
    public void addComponent(Component component) {
        componentIdMap.put(component.getId(), component);
    }
    
    public void addPart(Part part) {
        partIdMap.put(part.getId(), part);
    }

    private void addPlayer(Player player) {
        players.add(player);
        playersIdMap.put(player.getId(), player);
        Game.getInstance().sendToAll(new PlayerAddedEvent(player));
    }

    public void addShip(Ship ship, Vect3 position) {
        ships.add(ship);
        shipIdMap.put(ship.getId(), ship);
        Game.getInstance().sendToAll(new WorldShipAddedEvent(ship, position));
    }
    
    public void addSlot(Slot slot) {
        slotIdMap.put(slot.getId(), slot);
    }

    public Player loadPlayer(PlayerView playerView) {
        if (playersIdMap.containsKey(playerView.id)) {
            return playersIdMap.get(playerView.id);
        }

        Player player = new Player(playerView.id, playerView.login);
        player.fromView(playerView);
        addPlayer(player);
        return player;
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
        return playersIdMap.get(playerId);
    }

    public Component loadComponent(ComponentView componentView) {
        if (componentIdMap.containsKey(componentView.id)) {
            return componentIdMap.get(componentView.id);
        }

        Component component = new Component(componentView.id);
        component.fromView(componentView);
        addComponent(component);
        return component;
    }


    public Part loadPart(PartView partView) {
        if (partIdMap.containsKey(partView.id)) {
            return partIdMap.get(partView.id);
        }

        Part part = new Part(partView.id);
        part.fromView(partView);
        addPart(part);
        return part;
    }

    

}
