package com.irr310.common.world;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.irr310.server.world.product.Product;

public class FactionProduction {

    public static final long FACTORY_CAPACITY_INCREASE_PRICE = 300;
    public static final long FACTORY_CAPACITY_SELL_PRICE = 200;
    public static final long FACTORY_CAPACITY_INCREASE_DELAY = 1; //TickCount
    public static final long FACTORY_MAINTENANCE_PRICE = 10;
    
    private long mFactoryCapacity;
    private long mNextFactoryCapacityIncreaseRounds;
    private FactoryCapacityOrder mNextFactoryCapacityOrder;
    
    private Queue<FactoryCapacityOrder> factoryCapacityActiveList = new LinkedBlockingQueue<FactoryCapacityOrder>();
    
    private Queue<FactoryCapacityOrder> mFactoryCapacityOrderList = new LinkedBlockingQueue<FactoryCapacityOrder>();
    private final Faction mFaction;
    
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
        this.mFaction = faction;
    }
    
    public long getFactoryCapacity() {
        return mFactoryCapacity;
    }
    
    public Faction getFaction() {
        return mFaction;
    }

    public long getNextFactoryCapacityIncreaseRounds() {
        return mNextFactoryCapacityIncreaseRounds;
    }

    public void setNextFactoryCapacityIncreaseRounds(long rounds) {
        this.mNextFactoryCapacityIncreaseRounds = rounds;
    }

    public FactoryCapacityOrder getNextFactoryCapacityOrder() {
        return mNextFactoryCapacityOrder;
    }

    public void setNextFactoryCapacityOrder(FactoryCapacityOrder nextFactoryCapacityOrder) {
        this.mNextFactoryCapacityOrder = nextFactoryCapacityOrder;
    }

    public void setFactoryCapacity(long factoryCapacity) {
        this.mFactoryCapacity = factoryCapacity;
    }

    public Queue<FactoryCapacityOrder> getFactoryCapacityOrderList() {
        return mFactoryCapacityOrderList;
    }

    public void setFactoryCapacityOrderList(Queue<FactoryCapacityOrder> factoryCapacityOrderList) {
        this.mFactoryCapacityOrderList = factoryCapacityOrderList;
    }

    public Queue<FactoryCapacityOrder> getFactoryCapacityActiveList() {
        return factoryCapacityActiveList;
    }

    public void setFactoryCapacityActiveList(Queue<FactoryCapacityOrder> factoryCapacityActiveList) {
        this.factoryCapacityActiveList = factoryCapacityActiveList;
    }

    private long computeIncomingCapacity() {
        long capacity = 0;
        if(mNextFactoryCapacityOrder != null) {
            capacity += mNextFactoryCapacityOrder.count;
        }
        
        for(FactoryCapacityOrder order: mFactoryCapacityOrderList) {
            capacity += order.count;
        }
        
        return capacity;
    }

    public void addTask(long id, Product product, long count) {
        if(product.isAvailable(mFaction)) {
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

    public long getMaintenanceAmount() {
        return mFactoryCapacity * FACTORY_MAINTENANCE_PRICE;
    }

    public long getIncomingCapacity() {
        return computeIncomingCapacity();
    }

    public long getFactoryRentCapacity() {
        return 0;
    }

    public long getFactoryTotalCapacity() {
        return mFactoryCapacity;
    }
   
    
}
