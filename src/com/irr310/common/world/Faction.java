package com.irr310.common.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.irr310.common.binder.BindVariable;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;

import fr.def.iss.vd2.lib_v3d.V3DColor;

public class Faction extends WorldEntity{

    private WorldSystem homeSystem;
    private List<WorldSystem> knownSystems = new ArrayList<WorldSystem>();
    private List<Player> players = new ArrayList<Player>();
    private Color color;
    private List<Ship> shipList = new ArrayList<Ship>();
    private BindVariable<Integer> statersAmount;
    private BindVariable<Integer> oresAmount;
    private BindVariable<Integer> koliumAmount;
    private BindVariable<Integer> neuridiumAmount;
    
    
    public Faction(World world, long id) {
        super(world, id);
        color = Color.randomDarkOpaqueColor();
        statersAmount = new BindVariable<Integer>(world.getBinderServer(), 0);
        oresAmount = new BindVariable<Integer>(world.getBinderServer(), 0);
        koliumAmount = new BindVariable<Integer>(world.getBinderServer(), 0);
        neuridiumAmount = new BindVariable<Integer>(world.getBinderServer(), 0);
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void giveShip(Ship ship) {
        shipList.add(ship);
    }
    
    public void removeShip(Ship ship) {
        shipList.remove(ship);
    }
    
    public List<Ship> getShipList() {
        return shipList;
    }

    public BindVariable<Integer> getStatersAmount() {
        return statersAmount;
    }
    
    public BindVariable<Integer> getKoliumAmount() {
        return koliumAmount;
    }
    
    public BindVariable<Integer> getNeuridiumAmount() {
        return neuridiumAmount;
    }
    
    public BindVariable<Integer> getOresAmount() {
        return oresAmount;
    }
    
}
