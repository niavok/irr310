package com.irr310.common.world;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.common.world.system.WorldSystem;
import com.irr310.common.world.view.FactionProductionView;
import com.irr310.common.world.view.FactionView;

public class FactionProduction {

    public static final long FACTORY_CAPACITY_INCREASE_PRICE = 300;
    public static final long FACTORY_CAPACITY_SELL_PRICE = 200;
    public static final long FACTORY_CAPACITY_INCREASE_DELAY = 10; //TickCount
    public static final long FACTORY_MAINTENANCE_PRICE = 10;
    
    private long factoryCapacity;
    private long nextFactoryCapacityIncreaseTicks;
    private FactoryCapacityOrder nextFactoryCapacityOrder;
    
    private Queue<FactoryCapacityOrder> factoryCapacityActiveList = new LinkedBlockingQueue<FactoryCapacityOrder>();
    
    private Queue<FactoryCapacityOrder> factoryCapacityOrderList = new LinkedBlockingQueue<FactoryCapacityOrder>();
    private final Faction faction;
    
    public static class FactoryCapacityOrder {
        
        public final long sellPricePerCount;
        public final long count;
        
        public FactoryCapacityOrder(long pricePerCount, long count) {
            this.sellPricePerCount = pricePerCount;
            this.count = count;
        }
    }
    
    public FactionProduction(Faction faction) {
        this.faction = faction;
    }
    
    public long getFactoryCapacity() {
        return factoryCapacity;
    }
    
    public Faction getFaction() {
        return faction;
    }

    public long getNextFactoryCapacityIncreaseTicks() {
        return nextFactoryCapacityIncreaseTicks;
    }

    public void setNextFactoryCapacityIncreaseTicks(long ticks) {
        this.nextFactoryCapacityIncreaseTicks = ticks;
    }

    public FactoryCapacityOrder getNextFactoryCapacityOrder() {
        return nextFactoryCapacityOrder;
    }

    public void setNextFactoryCapacityOrder(FactoryCapacityOrder nextFactoryCapacityOrder) {
        this.nextFactoryCapacityOrder = nextFactoryCapacityOrder;
    }

    public void setFactoryCapacity(long factoryCapacity) {
        this.factoryCapacity = factoryCapacity;
    }

    public Queue<FactoryCapacityOrder> getFactoryCapacityOrderList() {
        return factoryCapacityOrderList;
    }

    public void setFactoryCapacityOrderList(Queue<FactoryCapacityOrder> factoryCapacityOrderList) {
        this.factoryCapacityOrderList = factoryCapacityOrderList;
    }

    public Queue<FactoryCapacityOrder> getFactoryCapacityActiveList() {
        return factoryCapacityActiveList;
    }

    public void setFactoryCapacityActiveList(Queue<FactoryCapacityOrder> factoryCapacityActiveList) {
        this.factoryCapacityActiveList = factoryCapacityActiveList;
    }

    public FactionProductionView toView() {
        FactionProductionView factionProductionView = new FactionProductionView();
        factionProductionView.factionId = faction.getId();
        factionProductionView.factoryCapacity = factoryCapacity;
        factionProductionView.incomingCapacity = computeIncomingCapacity();
        factionProductionView.nextFactoryCapacityIncreaseTicks = nextFactoryCapacityIncreaseTicks;
        factionProductionView.maintenanceAmount = factoryCapacity * FACTORY_MAINTENANCE_PRICE;
        factionProductionView.factoryRentCapacity = 0; 
        factionProductionView.factoryTotalCapacity = factoryCapacity;

        return factionProductionView;
    }

    private long computeIncomingCapacity() {
        long capacity = 0;
        if(nextFactoryCapacityOrder != null) {
            capacity += nextFactoryCapacityOrder.count;
        }
        
        for(FactoryCapacityOrder order: factoryCapacityOrderList) {
            capacity += order.count;
        }
        
        return capacity;
    }
   
    
}
