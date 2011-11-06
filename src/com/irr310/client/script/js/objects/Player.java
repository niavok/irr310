package com.irr310.client.script.js.objects;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.Game;


public class Player {

    private com.irr310.common.world.Player player;

    public Player(long id) {
        player = Game.getInstance().getWorld().getPlayerById(id);
    }

    public long getId() {
        return player.getId();
    }

    public String getLogin() {
        return player.getLogin();
    }

    public ArrayList<Ship> getShips() {
        List<com.irr310.common.world.Ship> shipList = player.getShipList();
        //NativeArray ships = new NativeArray(shipList.toArray());
        ArrayList<Ship> ships = new ArrayList<Ship>();
        
        for (com.irr310.common.world.Ship ship :shipList) {
            ships.add(new Ship(ship.getId()));
        }

        return ships;
    }
}
