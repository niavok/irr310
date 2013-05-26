package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.server.world.product.Product;

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
    private FactionStocks stocks;
    private FactionAvailableProductList availableProductList;
    private Nexus rootNexus;
    private FactionState factionState;
    private boolean stateChanged;
    
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
        stateChanged = true;
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
        stateChanged = true;
    }
    
    public void setNeuridiumAmount(long neuridiumAmount) {
        this.neuridiumAmount = neuridiumAmount;
        stateChanged = true;
    }
    
    public void setOresAmount(long oresAmount) {
        this.oresAmount = oresAmount;
        stateChanged = true;
    }
    
    public void setStatersAmount(long statersAmount) {
        this.statersAmount = statersAmount;
        stateChanged = true;
    }
    
    public Nexus getRootNexus() {
        return rootNexus;
    }
    
    public void setRootNexus(Nexus rootNexus) {
        this.rootNexus = rootNexus;
    }
    
    public FactionState toState() {
        if(stateChanged) {
            stateChanged = false;
            factionState = new FactionState();
        
            factionState.id = getId();
            factionState.statersAmount = statersAmount;
            factionState.oresAmount = oresAmount;
            factionState.koliumAmount = koliumAmount;
            factionState.neuridiumAmount = neuridiumAmount;
            factionState.color = color;
            factionState.homeSystemId = homeSystem.getId();
            factionState.rootNexus = rootNexus.toState();
            
            factionState.knownSystemIds = new ArrayList<Long>(); 
            for(WorldSystem system: knownSystems) {
                factionState.knownSystemIds.add(system.getId());
            }
        }

        return factionState;
    }
    
    public boolean isState(FactionState factionState) {
        return getId() == toState().id;
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

    public Product getProduct(ProductState product) {
        return availableProductList.getProduct(product);
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
    
}
