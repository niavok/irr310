package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.common.world.view.FactionView;
import com.irr310.i3d.Color;

public class Faction extends WorldEntity{

    private WorldSystem homeSystem;
    private List<WorldSystem> knownSystems = new ArrayList<WorldSystem>();
    private List<Player> players = new ArrayList<Player>();
    private Color color;
    private List<Ship> shipList = new ArrayList<Ship>();
    private long statersAmount;
    private long oresAmount;
    private long koliumAmount;
    private long neuridiumAmount;
    private FactionProduction production;
    
    
    public Faction(World world, long id) {
        super(world, id);
        color = Color.randomDarkOpaqueColor();
        production = new FactionProduction(this);
        statersAmount = 0;
        oresAmount = 0;
        koliumAmount = 0;
        neuridiumAmount = 0;
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

    public long getStatersAmount() {
        return statersAmount;
    }
    
    public long getKoliumAmount() {
        return koliumAmount;
    }
    
    public long getNeuridiumAmount() {
        return neuridiumAmount;
    }
    
    public long getOresAmount() {
        return oresAmount;
    }
    
    public void setKoliumAmount(long koliumAmount) {
        this.koliumAmount = koliumAmount;
    }
    
    public void setNeuridiumAmount(long neuridiumAmount) {
        this.neuridiumAmount = neuridiumAmount;
    }
    
    public void setOresAmount(long oresAmount) {
        this.oresAmount = oresAmount;
    }
    
    public void setStatersAmount(long statersAmount) {
        this.statersAmount = statersAmount;
    }
    
    public FactionView toView() {
        FactionView factionView = new FactionView();
        factionView.id = getId();
        factionView.statersAmount = statersAmount;
        factionView.oresAmount = oresAmount;
        factionView.koliumAmount = koliumAmount;
        factionView.neuridiumAmount = neuridiumAmount;
        factionView.color = color;
        factionView.homeSystemId = homeSystem.getId();
        
        factionView.knownSystemIds = new ArrayList<Long>(); 
        for(WorldSystem system: knownSystems) {
            factionView.knownSystemIds.add(system.getId());
        }

        return factionView;
    }
    
    public boolean isView(FactionView factionView) {
        return getId() == factionView.id;
    }

    public boolean takeStaters(long price) {
        if(getStatersAmount() > price) {
            setStatersAmount(getStatersAmount() - price);
            return true;
        }
        return false;
    }

    public void giveStaters(long amount) {
        setStatersAmount(getStatersAmount() + amount);
    }
    
    public FactionProduction getProduction() {
        return production;
    }
    
    
    
}
