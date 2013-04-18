package com.irr310.common.world;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.common.world.state.FactionProductionState;
import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.server.world.product.Product;

public class FactionProduction {

    public static final long FACTORY_CAPACITY_INCREASE_PRICE = 300;
    public static final long FACTORY_CAPACITY_SELL_PRICE = 200;
    public static final long FACTORY_CAPACITY_INCREASE_DELAY = 1; //TickCount
    public static final long FACTORY_MAINTENANCE_PRICE = 10;
    
    private long factoryCapacity;
    private long nextFactoryCapacityIncreaseTicks;
    private FactoryCapacityOrder nextFactoryCapacityOrder;
    
    private Queue<FactoryCapacityOrder> factoryCapacityActiveList = new LinkedBlockingQueue<FactoryCapacityOrder>();
    
    private Queue<FactoryCapacityOrder> factoryCapacityOrderList = new LinkedBlockingQueue<FactoryCapacityOrder>();
    private final Faction faction;
    
    private Queue<ProductionTask> productionTaskQueue = new LinkedBlockingQueue<ProductionTask>();
    private ProductionTask activeTask;
    
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

    public FactionProductionState toState() {
        FactionProductionState factionProductionState = new FactionProductionState();
        factionProductionState.factionId = faction.getId();
        factionProductionState.factoryCapacity = factoryCapacity;
        factionProductionState.incomingCapacity = computeIncomingCapacity();
        factionProductionState.nextFactoryCapacityIncreaseTicks = nextFactoryCapacityIncreaseTicks;
        factionProductionState.maintenanceAmount = factoryCapacity * FACTORY_MAINTENANCE_PRICE;
        factionProductionState.factoryRentCapacity = 0; 
        factionProductionState.factoryTotalCapacity = factoryCapacity;

        factionProductionState.productionTaskQueue = new ArrayList<ProductionTaskState>();
        
        for(ProductionTask task : productionTaskQueue) {
            factionProductionState.productionTaskQueue.add(task.toState());
        }
        
        
        return factionProductionState;
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

    public void addTask(long id, Product product, long count) {
        if(product.isAvailable(faction)) {
            productionTaskQueue.add(new ProductionTask(this, id, product, count));
        }
        
        findActiveTask();
    }
    
    public Queue<ProductionTask> getProductionTaskQueue() {
        return productionTaskQueue;
    }

    private void findActiveTask() {
        
        if(activeTask != null) {
            activeTask.desactivate();
        }
        
        for(ProductionTask task : productionTaskQueue) {
            if(!task.isPaused()) {
                activeTask = task;
                activeTask.activate();
                break;
            }
        }
    }
    
    public ProductionTask getActiveProductionTask() {
        return activeTask;
    }

    public void notifyTaskFinished(ProductionTask task) {
        productionTaskQueue.remove(task);
        if(activeTask.equals(task)) {
            activeTask.desactivate();
        }
        activeTask = null;
        findActiveTask();
    }
   
    
}
