package com.irr310.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.server.Vect3;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.World;

public class Game {

	World world;
	
	private List<Player> playerList = new ArrayList<Player>();
	private Map<Integer, Player> playerMap = new HashMap<Integer, Player>();

	public List<Player> getPlayerList() {
		return playerList;
	}
	
	public Player createPlayer() {
		Player newPlayer = new Player();
		Ship playerShip = ShipFactory.createSimpleShip();
		
		world.placeShipSafely(playerShip, new Vect3(10.0,20.0,30.0));
		
		return newPlayer;
	}

	public Object getWorld() {
		return world;
	}
}
