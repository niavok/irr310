package com.irr310.common.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.irr310.common.world.system.GameEntity;
import com.irr310.common.world.system.WorldSystem;

public class Faction extends GameEntity{

    private WorldSystem homeSystem;
    private List<WorldSystem> knownSystems = new ArrayList<WorldSystem>();
    private List<Player> players = new ArrayList<Player>();
    
    public Faction(long id) {
        super(id);
    }

    public void setHomeSystem(WorldSystem system) {
        this.homeSystem = system;
        discoverSystem(system);
    }

    private void discoverSystem(WorldSystem system) {
        knownSystems.add(system);
    }
    
    public WorldSystem getHomeSystem() {
        return homeSystem;
    }

    public List<WorldSystem> getKnownSystems() {
        return knownSystems;
    }

    public void assignPlayer(Player player) {
        players.add(player);
        player.setFaction(this);
    }

}
