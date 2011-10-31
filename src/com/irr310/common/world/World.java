package com.irr310.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.event.PlayerAddedEvent;
import com.irr310.common.event.WorldObjectAddedEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Vect3;
import com.irr310.common.world.view.PlayerView;
import com.irr310.common.world.view.ShipView;
import com.irr310.server.GameServer;

public class World {

    List<WorldObject> objects;
    List<Ship> ships;
    List<Player> players;
    Map<Long, Player> playersIdMap;
    Map<Long, Ship> shipIdMap;

    public World() {
        objects = new ArrayList<WorldObject>();
        ships = new ArrayList<Ship>();
        playersIdMap = new HashMap<Long, Player>();
    }

    public void addObject(WorldObject o) {
        objects.add(o);
        GameServer.getInstance().sendToAll(new WorldObjectAddedEvent(o));
    }

    private void addPlayer(Player player) {
        players.add(player);
        playersIdMap.put(player.getId(), player);
        GameServer.getInstance().sendToAll(new PlayerAddedEvent(player));
    }

    public void addShip(Ship ship, Vect3 position) {
        ships.add(ship);
        shipIdMap.put(ship.getId(), ship);
        GameServer.getInstance().sendToAll(new WorldShipAddedEvent(ship, position));
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

    public Ship getOrCreateShip(ShipView shipView) {
        if (shipIdMap.containsKey(shipView.id)) {
            return shipIdMap.get(shipView.id);
        }

        Ship ship = new Ship(shipView.id);
        ship.fromView(shipView);
        addShip(ship, null);
        return ship;
    }

}
