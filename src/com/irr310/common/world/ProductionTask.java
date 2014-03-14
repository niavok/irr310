package com.irr310.common.world;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.Item.State;
import com.irr310.common.world.system.WorldEntity;
import com.irr310.server.ProductionManager.ProductionStatus;
import com.irr310.server.ProductionManager.ProductionStatus.ProductionEndCause;
import com.irr310.server.world.product.ComponentProduct;
import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ShipProduct;
import com.irr310.server.world.product.SubProduct;

public class ProductionTask extends WorldEntity {

    // List<Item> reservedItems = new ArrayList<Item>();
    private BatchWorkUnit mRootWorkUnit;
    private final FactionProduction factionProduction;

    public ProductionTask(FactionProduction factionProduction, long id, Product product, long quantity) {
        super(factionProduction.getFaction().getWorld(), id);
        this.factionProduction = factionProduction;
        mRootWorkUnit = new BatchWorkUnit(product, quantity);

    }
    
    public Product getProduct() {
        return mRootWorkUnit.getProduct();
    }
    
    public long getDoneQuantity() {
        return mRootWorkUnit.getDoneQuantity();
    }
    
    public long getRequestedQuantity() {
        return mRootWorkUnit.getRequestedQuantity();
    }

    public void produce(ProductionStatus productionStatus) {

        if (mRootWorkUnit.isFinished()) {
            productionStatus.setProductionEndCause(ProductionEndCause.NOTHING_TO_PRODUCE);
        } else {
            mRootWorkUnit.produce(productionStatus);
        }
        if (mRootWorkUnit.isFinished()) {
            factionProduction.notifyTaskFinished(this);
            productionStatus.setProductionEndCause(ProductionEndCause.NOTHING_TO_PRODUCE);
        }

        //
        // // Depth priority TODO wide priority
        //
        // long usedProductionCapacity = 0;
        //
        // if(isFinished()) {
        // return availableProductionCapacity;
        // }
        //
        // // Free reserved resource. You must do that, because during
        // // the turn, the faction may have acquired a new ship so a
        // // reserved resources can have became useless.
        //
        // for(Item item: reservedItems) {
        // item.setReserved(false);
        // }
        // reservedItems.clear();
        //
        // // Reserved existing and useful Items
        //
        // List<Product> subProducts = product.getSubProducts();
        //
        // Product fistMissingSubProduct = null;
        //
        // for(Product product: subProducts) {
        // fistMissingSubProduct = reserveProduct(product);
        // }
        //
        // if(fistMissingSubProduct != null) {
        // // There is at least one missing sub product to make this product.
        // // Build it
        // usedProductionCapacity =
        // fistMissingSubProduct.produce(availableProductionCapacity);
        // } else {
        // usedProductionCapacity =
        // product.produce(availableProductionCapacity);
        // }
    }

    // private Product reserveProduct(Product productToReserver) {
    // Product fistMissingSubProduct = null;
    // return null;
    // }

    public void activate() {
        mRootWorkUnit.pause();
    }

    public boolean isPaused() {
        return false;
    }

    public void desactivate() {
        mRootWorkUnit.cancel();
    }

    public boolean isFinished() {
        return mRootWorkUnit.isFinished();
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
        private long doneQuantity;
        private WorkUnit currentWorkUnit;

        public BatchWorkUnit(Product product, long quantity) {
            this.product = product;
            this.requestedQuantity = quantity;
            this.doneQuantity = 0;
        }

        public void produce(ProductionStatus productionStatus) {

            while (productionStatus.isProducing() && !isFinished()) {
                if (currentWorkUnit == null) {
                    createNewWorkUnit();
                } else if (currentWorkUnit.isFinished()) {
                    doneQuantity++;
                    createNewWorkUnit();
                } else {
                    currentWorkUnit.produce(productionStatus);
                }
            }
        }

        private void createNewWorkUnit() {
            if (product instanceof ShipProduct || product instanceof ComponentProduct) {
                currentWorkUnit = new BuildWorkUnit(product);
            } else {
                Log.error("Unknown product type: " + product.getClass().getSimpleName());
            }

        }

        public boolean isFinished() {
            return doneQuantity == requestedQuantity;
        }

        public long getDoneQuantity() {
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
            if (currentWorkUnit != null) {
                currentWorkUnit.pause();
            }
        }

        @Override
        public void cancel() {
            if (currentWorkUnit != null) {
                currentWorkUnit.cancel();
            }
        }
    }

    private class BuildWorkUnit extends WorkUnit {

        private final Product product;
        private WorkState workState;
        private long pendingOres;
        private long accumulatedProductionCapacity;
        private Map<String, Item> reservedItems;
        private BuildWorkUnit subItemWorkUnit;

        public BuildWorkUnit(Product product) {
            this.product = product;
            pendingOres = 0;
            workState = WorkState.WAITING_FOR_ITEMS;
            //product.getSubProducts().size()
            reservedItems = new HashMap<String, Item>();
        }

        @Override
        public boolean isFinished() {
            return (workState == WorkState.FINISHED);
        }

        @Override
        public void produce(ProductionStatus productionStatus) {

            switch (workState) {
                case WAITING_FOR_ITEMS:
                    boolean allItemsReady = true;
                    
                    for(SubProduct subProduct: product.getSubProducts()) {
                        if(!reservedItems.containsKey(subProduct.getKey())) {
                            allItemsReady = false;
                            Item item = factionProduction.getFaction().getStocks().getAvailableItem(subProduct.getProduct());
                            if (item == null) {
                                subItemWorkUnit = new BuildWorkUnit(subProduct.getProduct());
                                workState = WorkState.BUILDING_ITEM;
                                break;
                            } else {
                                item.setState(State.RESERVED);
                                reservedItems.put(subProduct.getKey(), item);
                            }
                        }
                    }

                    if (allItemsReady) {
                        workState = WorkState.WAITING_FOR_RESSOURCES;
                    }
                    break;
                case BUILDING_ITEM:
                    //Check if the product is now available
                    
                    
                    subItemWorkUnit.produce(productionStatus);
                    if(subItemWorkUnit.isFinished()) {
                        workState = WorkState.WAITING_FOR_ITEMS;
                    }
                break;
                case WAITING_FOR_RESSOURCES:
                    if (factionProduction.getFaction().getOresAmount() >= product.getOreCost()) {
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
                    Log.trace("" + product.getId() + ": " + accumulatedProductionCapacity + "/" + product.getFactoryCost());
                    if (missingProductionCapacity == 0) {
                        // Create item
                        getWorld().getItemFactory().createItem(product, factionProduction.getFaction(), reservedItems);
                        workState = WorkState.FINISHED;
                        pendingOres = 0;
                        accumulatedProductionCapacity = 0;
                    } else if (productionStatus.getAvailableProductionCapacity() > 0) {
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
            
            for(Item item : reservedItems.values()) {
                item.setState(State.STOCKED);
            }
            reservedItems.clear();
                        
            if(workState == WorkState.BUILDING_ITEM) {
                subItemWorkUnit.pause();
            } else {
                workState = WorkState.WAITING_FOR_ITEMS;    
            }
            
            
        }

        @Override
        public void cancel() {
            pause();
            factionProduction.getFaction().giveOres(pendingOres);
            pendingOres = 0;
            accumulatedProductionCapacity = 0;
            workState = WorkState.FINISHED;
        }

    }

    private enum WorkState {
        WAITING_FOR_RESSOURCES, BUILDING, WAITING_FOR_ITEMS, BUILDING_ITEM, FINISHED,
    }

}
