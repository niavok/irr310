package com.irr310.server;

import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionProduction.FactoryCapacityOrder;

public class ProductionManager {

    public void doTick(FactionProduction production) {
        checkCapacityIncrease(production);
    }
    
    public  void doTurn(FactionProduction production) {
        payMaintenance(production);
    }
    
    
    void payMaintenance(FactionProduction production) {
        production.getFaction().takeStaters(FactionProduction.FACTORY_MAINTENANCE_PRICE * production.getFactoryCapacity());
    }
    
    void checkCapacityIncrease(FactionProduction production) {
        
        production.setNextFactoryCapacityIncreaseTicks(production.getNextFactoryCapacityIncreaseTicks() - 1);
        
        if(production.getNextFactoryCapacityOrder() != null) {

            // Check delay expire
            if(production.getNextFactoryCapacityIncreaseTicks() < 0) {
                // Increase capacity
                production.setFactoryCapacity(production.getFactoryCapacity() + 1);
                //TODO Aggregate same prices
                production.getFactoryCapacityActiveList().add(new FactoryCapacityOrder(production.getNextFactoryCapacityOrder().sellPricePerCount, 1));
                
                if(production.getNextFactoryCapacityOrder().count > 1) {
                    // Decrease count for current capacity order
                    FactoryCapacityOrder factoryCapacityOrder = new FactoryCapacityOrder(FactionProduction.FACTORY_CAPACITY_INCREASE_PRICE, production.getNextFactoryCapacityOrder().count - 1);
                    production.setNextFactoryCapacityOrder(factoryCapacityOrder);
                } else if(!production.getFactoryCapacityOrderList().isEmpty()) {

                    production.setNextFactoryCapacityOrder(production.getFactoryCapacityOrderList().poll());
                } else {
                    production.setNextFactoryCapacityOrder(null);
                }
                production.setNextFactoryCapacityIncreaseTicks(FactionProduction.FACTORY_CAPACITY_INCREASE_DELAY);
            }
        }
    }
    
    void buyFactoryCapacity(FactionProduction production, long count) {
        FactoryCapacityOrder factoryCapacityOrder = new FactoryCapacityOrder(FactionProduction.FACTORY_CAPACITY_SELL_PRICE, count);
        if(production.getFaction().takeStaters(FactionProduction.FACTORY_CAPACITY_INCREASE_PRICE)) {
            if(production.getNextFactoryCapacityOrder() == null) {
                production.setNextFactoryCapacityOrder(factoryCapacityOrder);
                production.setNextFactoryCapacityIncreaseTicks(FactionProduction.FACTORY_CAPACITY_INCREASE_DELAY);
            } else {
                production.getFactoryCapacityOrderList().add(factoryCapacityOrder);
            }
        }
    }
    
    void sellFactoryCapacity(FactionProduction production, long count) {
        
        boolean empty = false;
        
        while((count > 0) && !empty) {
        
            if(!production.getFactoryCapacityOrderList().isEmpty()) {
                FactoryCapacityOrder factoryCapacityOrder = production.getFactoryCapacityOrderList().poll();
                
                long countToSell = Math.min(count, factoryCapacityOrder.count);
                
                production.getFaction().giveStaters(factoryCapacityOrder.sellPricePerCount * countToSell);
                
                if(countToSell < factoryCapacityOrder.count) {
                    production.getFactoryCapacityOrderList().add(new FactoryCapacityOrder(FactionProduction.FACTORY_CAPACITY_INCREASE_PRICE, factoryCapacityOrder.count - countToSell));
                }
                count -= countToSell;
            } else if(production.getNextFactoryCapacityOrder() != null) {
                long countToSell = Math.min(count, production.getNextFactoryCapacityOrder().count);
                production.getFaction().giveStaters(production.getNextFactoryCapacityOrder().sellPricePerCount * countToSell);
                if(countToSell < production.getNextFactoryCapacityOrder().count) {
                    production.setNextFactoryCapacityOrder(new FactoryCapacityOrder(FactionProduction.FACTORY_CAPACITY_INCREASE_PRICE, production.getNextFactoryCapacityOrder().count - countToSell));
                } else {
                    production.setNextFactoryCapacityOrder(null);
                }
                count -= countToSell;
            } else if(!production.getFactoryCapacityActiveList().isEmpty()) {
                FactoryCapacityOrder factoryCapacityOrder = production.getFactoryCapacityActiveList().poll();
                
                long countToSell = Math.min(count, factoryCapacityOrder.count);
                
                production.getFaction().giveStaters(factoryCapacityOrder.sellPricePerCount * countToSell);
                
                if(countToSell < factoryCapacityOrder.count) {
                    production.getFactoryCapacityActiveList().add(new FactoryCapacityOrder(FactionProduction.FACTORY_CAPACITY_INCREASE_PRICE, factoryCapacityOrder.count - countToSell));
                }
                production.setFactoryCapacity(production.getFactoryCapacity() - countToSell);
                count -= countToSell;
            } else {
                empty = true;
            }
        
        }
    }
    
}
