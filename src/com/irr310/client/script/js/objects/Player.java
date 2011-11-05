package com.irr310.client.script.js.objects;

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

}
