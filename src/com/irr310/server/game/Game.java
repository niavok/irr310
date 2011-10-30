package com.irr310.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.server.GameServer;
import com.irr310.server.Vect3;
import com.irr310.server.event.AddShipEvent;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.World;

public class Game {

	World world;
	
	private List<Player> playerList = new ArrayList<Player>();
	private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();
	private Map<String, Player> playerLoginMap = new HashMap<String, Player>();

	public Game() {
		world = new World();
	}
	
	public List<Player> getPlayerList() {
		return playerList;
	}
	
	public Player createPlayer(String login, String password) {
		Player newPlayer = new Player(login);
		newPlayer.changePassword(password);
		playerLoginMap.put(login, newPlayer);
		//Ship playerShip = ShipFactory.createSimpleShip();
		
		AddShipEvent addShipEvent = new AddShipEvent();
        addShipEvent.setType(AddShipEvent.Type.SIMPLE);
        GameServer.getInstance().sendToAll(addShipEvent);
		
		/*world.addShip(playerShip, new Vect3(10.0,20.0,30.0));*/
		
		
		
		return newPlayer;
	}

	public World getWorld() {
		return world;
	}

    public Player getPlayerByLogin(String login) {
        return playerLoginMap.get(login);
    }

    public boolean isPlayerExist(String login) {
        return playerLoginMap.containsKey(login);
    }
}
