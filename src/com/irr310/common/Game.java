package com.irr310.common;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.AddShipEvent;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Monolith;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.server.GameServer;

public abstract class Game {

    private static Game instance = null;

    public static Game getInstance() {
        return instance;
    }

    public abstract World getWorld();

    public static void setInstance(Game instance) {
        Game.instance = instance;
    }

    public abstract void sendToAll(EngineEvent event);

    
    //TODO: move all that
    private Map<String, Player> playerLoginMap = new HashMap<String, Player>();

    public Player createPlayer(String login, String password) {
        Player newPlayer = new Player(GameServer.pickNewId(), login);
        newPlayer.changePassword(password);
        playerLoginMap.put(login, newPlayer);
        getWorld().addPlayer(newPlayer);
        // Ship playerShip = ShipFactory.createSimpleShip();

        AddShipEvent addShipEvent = new AddShipEvent(newPlayer);
        // addShipEvent.setType(AddShipEvent.Type.SIMPLE);
        addShipEvent.setType(AddShipEvent.Type.SIMPLE_FIGHTER);
        addShipEvent.setPosition(new Vec3(20, -50, 0));
        Game.getInstance().sendToAll(addShipEvent);

        /* world.addShip(playerShip, new Vect3(10.0,20.0,30.0)); */

        return newPlayer;
    }
    
    public Player getPlayerByLogin(String login) {
        return playerLoginMap.get(login);
    }

    public boolean isPlayerExist(String login) {
        return playerLoginMap.containsKey(login);
    }

    

    public abstract PhysicEngine getPhysicEngine();
    
}
