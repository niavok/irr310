package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;

public class Faction extends WorldEntity{

    private WorldSystem homeSystem;
    private List<WorldSystem> knownSystems = new ArrayList<WorldSystem>();
    private List<Player> mPlayers = new ArrayList<Player>();
    private Color color;
    private List<Ship> shipList = new ArrayList<Ship>();
    private long statersAmount;
    private long oresAmount;
    private long koliumAmount;
    private long neuridiumAmount;
    private FactionProduction production;
    private FactionStocks stocks;
    private FactionAvailableProductList availableProductList;
    private Nexus rootNexus;
    
    public Faction(World world, long id) {
        super(world, id);
        color = Color.randomDarkOpaqueColor();
        production = new FactionProduction(this);
        stocks = new FactionStocks(this);
        availableProductList = new FactionAvailableProductList(this, world.getProductManager());
        statersAmount = 0;
        oresAmount = 0;
        koliumAmount = 0;
        neuridiumAmount = 0;
    }

    public void setHomeSystem(WorldSystem system) {
        this.homeSystem = system;
        discoverSystem(system);
    }

    public void discoverSystem(WorldSystem system) {
        if(!knownSystems.contains(system)) {
            knownSystems.add(system);
        }
    }
    
    public WorldSystem getHomeSystem() {
        return homeSystem;
    }

    public List<WorldSystem> getKnownSystems() {
        return knownSystems;
    }

    public void assignPlayer(Player player) {
        mPlayers.add(player);
        player.setFaction(this);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void giveShip(Ship ship) {
        if(!shipList.contains(ship)) {
            shipList.add(ship);
        }
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
    
    public Nexus getRootNexus() {
        return rootNexus;
    }
    
    public void setRootNexus(Nexus rootNexus) {
        this.rootNexus = rootNexus;
    }
    
    public boolean takeStaters(long price) {
        if(getStatersAmount() >= price) {
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
    
    public FactionAvailableProductList getAvailableProductList() {
        return availableProductList;
    }

    public boolean takeOres(long price) {
        if(getOresAmount() >= price) {
            setOresAmount(getOresAmount() - price);
            return true;
        }
        return false;
    }

    public void giveOres(long amount) {
        setOresAmount(getOresAmount() + amount);
    }

    public FactionStocks getStocks() {
        return stocks;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }
}
