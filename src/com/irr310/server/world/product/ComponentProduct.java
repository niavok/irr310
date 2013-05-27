package com.irr310.server.world.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.state.ProductState;
import com.irr310.server.world.product.ComponentProduct.ComponentPartProduct;

public class ComponentProduct extends Product {

    private Map<String, ComponentSlotProduct> slots = new HashMap<String, ComponentSlotProduct>();
    private List<ComponentPartProduct> parts = new ArrayList<ComponentPartProduct>();
    private long oreCost = -1;
    private long factoryCost = -1;
    
    public ComponentSlotProduct getSlot(String key) {
        return slots.get(key);
    }

    
    @Override
    public boolean performLinks(Map<String, Product> productIds) {
        if(!super.performLinks(productIds)) {
            return false;
        }
        
        if(oreCost == -1) {
            Log.warn("Product '"+getId()+"' has no ore cost.");
            return false;
        }
        
        if(factoryCost == -1) {
            Log.warn("Product '"+getId()+"' has no production capacity cost.");
            return false;
        }
        
        return true;
    }
    
    public void notifyNewSlot(ComponentSlotProduct slotProduct) {
        if(slots.containsKey(slotProduct.getKey())) {
            throw new RessourceLoadingException("The component '"+getId()+"' has already a slot with '"+slotProduct.getKey()+"' as key");
        }
        
        slots.put(slotProduct.getKey(), slotProduct);
    }
    
    @Override
    public ProductState toState() {
        ProductState productState = new ProductState();
        productState.id = getId();
        productState.name = getName();
        productState.code = getCode();
        productState.description = getDescription();
        productState.type = ProductState.TYPE_COMPONENT;
        
        return productState;
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
        return oreCost;
    }
    
    public long getFactoryCost() {
        return factoryCost;
    }
    
    public void setOreCost(long oreCost) {
        this.oreCost = oreCost;
    }
    
    public void setFactoryCost(long factoryCost) {
        this.factoryCost = factoryCost;
    }

    public void addPart(ComponentPartProduct part) {
        parts.add(part);
    }
    
    public List<ComponentPartProduct> getParts() {
        return parts;
    }
}
