package com.irr310.common.world;

import com.irr310.common.tools.Log;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.ProductionManager.ProductionStatus;
import com.irr310.server.ProductionManager.ProductionStatus.ProductionEndCause;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ShipProduct;

public class ProductionTask extends WorldEntity {

    
//    List<Item> reservedItems = new ArrayList<Item>();
    private BatchWorkUnit rootWorkUnit;
    private final FactionProduction factionProduction;
    
    public ProductionTask(FactionProduction factionProduction, long id, Product product, long quantity) {
        super(factionProduction.getFaction().getWorld(), id);
        this.factionProduction = factionProduction;
        rootWorkUnit = new BatchWorkUnit(product, quantity);
        
    }
    
    public ProductionTaskState toState() {
        ProductionTaskState state = new ProductionTaskState();
        
        state.id = getId();
        state.requestedQuantity = rootWorkUnit.getRequestedQuantity();
        state.doneQuantity = rootWorkUnit.getDoneQuantity();
        state.product = rootWorkUnit.getProduct().toState();
        
//        state.reservedItemIds = new ArrayList<Long>();
//        
//        for(Item item : reservedItems) {
//            state.reservedItemIds.add(item.getId());
//        }
        
        return state;
    }

    public void produce(ProductionStatus productionStatus) {

        if(rootWorkUnit.isFinished()) {
            productionStatus.setProductionEndCause(ProductionEndCause.NOTHING_TO_PRODUCE);
        } else {
            rootWorkUnit.produce(productionStatus);
        }
        if(rootWorkUnit.isFinished()) {
            factionProduction.notifyTaskFinished(this);
            productionStatus.setProductionEndCause(ProductionEndCause.NOTHING_TO_PRODUCE);
        }
        
//        
//        // Depth priority TODO wide priority
//        
//        long usedProductionCapacity = 0;
//        
//        if(isFinished()) {
//            return availableProductionCapacity;
//        }
//        
//        // Free reserved resource. You must do that, because during
//        // the turn, the faction may have acquired a new ship so a 
//        // reserved resources can have became useless.
//        
//        for(Item item: reservedItems) {
//            item.setReserved(false);
//        }
//        reservedItems.clear();
//        
//        // Reserved existing and useful Items
//        
//        List<Product> subProducts = product.getSubProducts();
//        
//        Product fistMissingSubProduct = null;
//        
//        for(Product product: subProducts) {
//            fistMissingSubProduct = reserveProduct(product);
//        }
//        
//        if(fistMissingSubProduct != null) {
//            // There is at least one missing sub product to make this product.
//            // Build it
//            usedProductionCapacity = fistMissingSubProduct.produce(availableProductionCapacity);
//        } else {
//            usedProductionCapacity = product.produce(availableProductionCapacity);
//        }
    }

//    private Product reserveProduct(Product productToReserver) {
//        Product fistMissingSubProduct = null;
//        return null;
//    }

    public void activate() {
        rootWorkUnit.pause();
    }

    public boolean isPaused() {
        return false;
    }

    public void desactivate() {
        rootWorkUnit.cancel();
    }

    public boolean isFinished() {
        return rootWorkUnit.isFinished();
    }

    
    private abstract class WorkUnit {

        public abstract boolean isFinished();
        
        public abstract void produce(ProductionStatus productionStatus);
        
        public abstract void pause();
        
        public abstract void cancel();
        
    }
    
    private class BatchWorkUnit extends WorkUnit {
        private final Product product;
        private final long requestedQuantity;
        private int doneQuantity;
        private WorkUnit currentWorkUnit;
        
        public BatchWorkUnit(Product product, long quantity) {
            this.product = product;
            this.requestedQuantity = quantity;
            this.doneQuantity = 0;
        }

        public void produce(ProductionStatus productionStatus) {
            long usedProductionCapacity = 0;
            
            while(productionStatus.isProducing() && !isFinished()) {
                if(currentWorkUnit == null) {
                    createNewWorkUnit();
                } else if(currentWorkUnit.isFinished()) {
                    doneQuantity++;
                    createNewWorkUnit();
                } else {
                    currentWorkUnit.produce(productionStatus);
                }
            }
        }

        private void createNewWorkUnit() {
            if(product instanceof ShipProduct) {
                currentWorkUnit = new AssemblyWorkUnit((ShipProduct) product);
            } else if(product instanceof ComponentProduct) {
                currentWorkUnit = new BuildWorkUnit((ComponentProduct) product);
            } else {
                Log.error("Unknown product type: "+product.getClass().getSimpleName());
            }
            
        }

        public boolean isFinished() {
            return doneQuantity == requestedQuantity;
        }
        
        public int getDoneQuantity() {
            return doneQuantity;
        }
        
        public long getRequestedQuantity() {
            return requestedQuantity;
        }
        
        public Product getProduct() {
            return product;
        }

        @Override
        public void pause() {
            if(currentWorkUnit != null) {
                currentWorkUnit.pause();
            }
        }

        @Override
        public void cancel() {
            if(currentWorkUnit != null) {
                currentWorkUnit.cancel();
            }
        }
    }
    
    private class AssemblyWorkUnit extends WorkUnit {

        public AssemblyWorkUnit(ShipProduct product) {
            // TODO Auto-generated constructor stub
        }

        @Override
        public boolean isFinished() {
            // TODO Auto-generated method stub
            Log.error("TODO");
            return false;
        }

        @Override
        public void produce(ProductionStatus productionStatus) {
            // TODO Auto-generated method stub
            Log.error("TODO");
        }

        @Override
        public void pause() {
            // TODO Auto-generated method stub
            Log.error("TODO");
            
        }

        @Override
        public void cancel() {
            // TODO Auto-generated method stub
            Log.error("TODO");
        }
        
    }
    
    private class BuildWorkUnit extends WorkUnit {

        private final ComponentProduct product;
        private WorkState workState;
        private long pendingOres;
        private long accumulatedProductionCapacity;
        
        public BuildWorkUnit(ComponentProduct product) {
            this.product = product;
            pendingOres = 0;
            workState = WorkState.WAITING_FOR_RESSOURCES;
        }

        @Override
        public boolean isFinished() {
            return (workState == WorkState.FINISHED);
        }

        @Override
        public void produce(ProductionStatus productionStatus) {
            
            switch(workState) {
                case WAITING_FOR_RESSOURCES:
                    if(factionProduction.getFaction().getOresAmount() >= product.getOreCost()) {
                        // Enougth ressource, grab them and start building
                        pendingOres = product.getOreCost();
                        factionProduction.getFaction().takeOres(pendingOres);
                        workState = WorkState.BUILDING;
                        produce(productionStatus);
                    } else {
                        productionStatus.setProductionEndCause(ProductionEndCause.WAITING_FOR_RESSOURCES);
                    }
                break;
                case BUILDING:
                    long missingProductionCapacity = product.getFactoryCost() - accumulatedProductionCapacity;
                    Log.trace(""+product.getId()+": "+accumulatedProductionCapacity+"/"+product.getFactoryCost());
                    if(missingProductionCapacity == 0) {
                        // Create item
                        getWorld().getItemFactory().createItem(product, factionProduction.getFaction());
                        workState = WorkState.FINISHED;
                        pendingOres = 0;
                        accumulatedProductionCapacity = 0;
                    } else if(productionStatus.getAvailableProductionCapacity()>0){
                        long usedCapacity = Math.min(missingProductionCapacity, productionStatus.getAvailableProductionCapacity());
                        productionStatus.consumeProductionCapacity(usedCapacity);
                        accumulatedProductionCapacity += usedCapacity;
                        produce(productionStatus);
                    } else {
                        // No more factory time
                    }
                break;
            }
        }

        @Override
        public void pause() {
        }

        @Override
        public void cancel() {
            pause();
            factionProduction.getFaction().giveOres(pendingOres);
            pendingOres = 0;
            accumulatedProductionCapacity = 0;
        }
        
    }
    
    private enum WorkState {
        WAITING_FOR_RESSOURCES,
        BUILDING,
        FINISHED
    }
    
    
}