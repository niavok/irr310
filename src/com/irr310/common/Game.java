package com.irr310.common;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.engine.PhysicEngine;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Player;
import com.irr310.common.world.Ship;
import com.irr310.common.world.World;
import com.irr310.common.world.item.ItemSlot;
import com.irr310.common.world.item.ShipSchema;
import com.irr310.server.GameServer;
import com.irr310.server.game.ShipFactory;
import com.irr310.server.upgrade.UpgradeFactory;

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
        
        UpgradeFactory.refresh(newPlayer);
        // Ship playerShip = ShipFactory.createSimpleShip();

        
        
        
        Ship ship = ShipFactory.createSimpleFighter();
        ship.setOwner(newPlayer);
        ShipSchema shipShema = new ShipSchema();
        // Center slot
        //public ItemSlot(Ship ship, Component slotComponent,  Vec3 position, Vec3 connectionPosition)
        ItemSlot centerSlot = new ItemSlot(ship, ship.getComponentByName("trusterBlock1"), new Vec3(0, 0.5, 0), new Vec3(0, -0.5, 0));
        shipShema.addItemSlot(centerSlot);
        // Engine slot
        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("rightReator"),new Vec3(5.5, -3.5, 0),new Vec3(6, -3.5, 0)));
        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("leftReator"),new Vec3(-5.5, -3.5, 0),new Vec3(-6, -3.5, 0)));
        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("topReactor"),new Vec3(0, -3.5, 5.5),new Vec3(0, -3.5, 6)));
        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("bottomReactor"),new Vec3(0, -3.5, -5.5),new Vec3(0, -3.5, -6)));
        // Wings slot
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(2., -3.5, 1)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(2., -3.5, -1)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(3., -3.5, 1)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(3., -3.5, -1)));
//
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-2., -3.5, 1)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-2., -3.5, -1)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-3., -3.5, 1)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-3., -3.5, -1)));
//
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(1, -3.5, 2.)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-1, -3.5, 2.)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(1, -3.5, 3.)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-1, -3.5, 3.)));
//
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(1, -3.5, -2.)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-1, -3.5, -2.)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(1, -3.5, -3.)));
//        shipShema.addItemSlot(new ItemSlot(ship, ship.getComponentByName("trusterBlock1"),new Vec3(-1, -3.5, -3.)));

        newPlayer.setShipShema(shipShema);
        Game.getInstance().getWorld().addShip(ship, new Vec3(20, -50, 0));
        
        centerSlot.setContent(newPlayer.getInventory().get(0));
        
        

        return newPlayer;
    }
    
    public Player getPlayerByLogin(String login) {
        return playerLoginMap.get(login);
    }

    public boolean isPlayerExist(String login) {
        return playerLoginMap.containsKey(login);
    }

    

    public abstract PhysicEngine getPhysicEngine();

    public abstract void gameOver();
    
}
