package com.irr310.server.world.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec3;
import com.irr310.server.world.product.ComponentProduct.ComponentLinearEngineCapacityProduct;

public class ComponentProduct extends Product {

    private Map<String, ComponentSlotProduct> mSlots = new HashMap<String, ComponentSlotProduct>();
    private List<ComponentPartProduct> mParts = new ArrayList<ComponentPartProduct>();
    private List<ComponentCapacityProduct> mCapacities = new ArrayList<ComponentCapacityProduct>();
    private long mOreCost = -1;
    private long mFactoryCost = -1;
    
    public ComponentSlotProduct getSlot(String key) {
        return mSlots.get(key);
    }

    
    @Override
    public boolean performLinks(Map<String, Product> productIds) {
        if(!super.performLinks(productIds)) {
            return false;
        }
        
        if(mOreCost == -1) {
            Log.warn("Product '"+getId()+"' has no ore cost.");
            return false;
        }
        
        if(mFactoryCost == -1) {
            Log.warn("Product '"+getId()+"' has no production capacity cost.");
            return false;
        }
        
        return true;
    }
    
    public void notifyNewSlot(ComponentSlotProduct slotProduct) {
        if(mSlots.containsKey(slotProduct.getKey())) {
            throw new RessourceLoadingException("The component '"+getId()+"' has already a slot with '"+slotProduct.getKey()+"' as key");
        }
        
        mSlots.put(slotProduct.getKey(), slotProduct);
    }
    
    @Override
    public boolean isShip() {
        return false;
    }
    
    public static class ComponentSlotProduct {

        private final Vec3 location;
        private final String key;
        private ComponentPartProduct part;

        public ComponentSlotProduct(ComponentPartProduct part, String key, Vec3 location) {
            this.part = part;
            this.key = key;
            this.location = location;
        }
        
        public Vec3 getLocation() {
            return location;
        }
        
        public String getKey() {
            return key;
        }
        
        public ComponentPartProduct getPart() {
            return part;
        }
    }

    public static class ComponentPartProduct {
        private double mass;
        private PartShapeProduct shape;
        private final ComponentProduct component;
        private List<ComponentSlotProduct> slots = new ArrayList<ComponentSlotProduct>();
        
        public ComponentPartProduct(ComponentProduct component) {
            this.component = component;
        }
        
        public void setMass(double mass) {
            this.mass = mass;
        }

        public void setShape(PartShapeProduct shape) {
            this.shape = shape;
        }

        public ComponentProduct getComponent() {
            return component;
        }

        public PartShapeProduct getShape() {
            return shape;
        }

        public double getMass() {
            return mass;
        }
        
        public void addSlot(String key ,Vec3 slotLocation) {
            ComponentSlotProduct slotProduct = new ComponentSlotProduct(this,key, slotLocation);
            slots.add(slotProduct);
            component.notifyNewSlot(slotProduct);
        }
        
        public List<ComponentSlotProduct> getSlots() {
            return slots;
        }

    }
    
    public static class PartShapeProduct {
        private final Vec3 size;
        private final ShapeType type;
        
        public enum ShapeType {
            BOX,
        }
        
        PartShapeProduct(ShapeType type, Vec3 size) {
            this.type = type;
            this.size = size;
        }
        
        public ShapeType getType() {
            return type;
        }
        
        public Vec3 getSize() {
            return size;
        }
    }
    
    @Override
    public List<SubProduct> getSubProducts() {
        return new ArrayList<SubProduct>();
    }
    
    
    public long getOreCost() {
        return mOreCost;
    }
    
    public long getFactoryCost() {
        return mFactoryCost;
    }
    
    public void setOreCost(long oreCost) {
        this.mOreCost = oreCost;
    }
    
    public void setFactoryCost(long factoryCost) {
        this.mFactoryCost = factoryCost;
    }

    public void addPart(ComponentPartProduct part) {
        mParts.add(part);
    }
    
    public void addCapacity(ComponentCapacityProduct capacity) {
        mCapacities.add(capacity);
    }
    
    public List<ComponentPartProduct> getParts() {
        return mParts;
    }
    
    public List<ComponentCapacityProduct> getCapacities() {
        return mCapacities;
    }
    
    public interface ComponentCapacityProduct {
        
    }
    
    public static class ComponentLinearEngineCapacityProduct implements ComponentCapacityProduct {
        double mAirFriction = 0;
        double mTheoricalMaxThrust=0;
        double mTheoricalMinThrust=0;
        double mTheoricalVariationSpeed=0; 
        private final ComponentProduct component;

        
        public ComponentLinearEngineCapacityProduct(ComponentProduct component) {
            this.component = component;
        }
        
        public ComponentProduct getComponent() {
            return component;
        }
        
        public double getAirFriction() {
            return mAirFriction;
        }
        public void setAirFriction(double airFriction) {
            mAirFriction = airFriction;
        }
        public double getTheoricalMaxThrust() {
            return mTheoricalMaxThrust;
        }
        public void setTheoricalMaxThrust(double theoricalMaxThrust) {
            mTheoricalMaxThrust = theoricalMaxThrust;
        }
        public double getTheoricalMinThrust() {
            return mTheoricalMinThrust;
        }
        public void setTheoricalMinThrust(double theoricalMinThrust) {
            mTheoricalMinThrust = theoricalMinThrust;
        }
        public double getTheoricalVariationSpeed() {
            return mTheoricalVariationSpeed;
        }
        public void setTheoricalVariationSpeed(double theoricalVariationSpeed) {
            mTheoricalVariationSpeed = theoricalVariationSpeed;
        }
    }
    
    public static class ComponentElectricStorageCapacityProduct implements ComponentCapacityProduct {
        double mCapacity = 0;
        double mYield=0;
        private final ComponentProduct component;

        
        public ComponentElectricStorageCapacityProduct(ComponentProduct component) {
            this.component = component;
        }
        
        public ComponentProduct getComponent() {
            return component;
        }

        public double getCapacity() {
            return mCapacity;
        }

        public void setCapacity(double capacity) {
            mCapacity = capacity;
        }

        public double getYield() {
            return mYield;
        }

        public void setYield(double yield) {
            mYield = yield;
        }
    }
    
    public static class ComponentKernelCapacityProduct implements ComponentCapacityProduct {
        double mElectricConsumption=0;
        private final ComponentProduct component;

        
        public ComponentKernelCapacityProduct(ComponentProduct component) {
            this.component = component;
        }
        
        public ComponentProduct getComponent() {
            return component;
        }

        public double getElectricConsumption() {
            return mElectricConsumption;
        }

        public void setElectricConsumption(double electricConsumption) {
            mElectricConsumption = electricConsumption;
        }
    }
}
