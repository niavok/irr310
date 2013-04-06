package com.irr310.server.world.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec3;

public class ComponentProduct extends Product {

    private Map<String, ComponentSlotProduct> slots = new HashMap<String, ComponentSlotProduct>();

    public ComponentSlotProduct getSlot(String key) {
        return slots.get(key);
    }

    
    public void notifyNewSlot(ComponentSlotProduct slotProduct) {
        if(slots.containsKey(slotProduct.getKey())) {
            throw new RessourceLoadingException("The component '"+getId()+"' has already a slot with '"+slotProduct.getKey()+"' as key");
        }
        
        slots.put(slotProduct.getKey(), slotProduct);
    }
    
    public static class ComponentSlotProduct {

        private final Vec3 position;
        private final String key;

        public ComponentSlotProduct(String key, Vec3 position) {
            this.key = key;
            this.position = position;
        }
        
        public Vec3 getPosition() {
            return position;
        }
        
        public String getKey() {
            return key;
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

        public void addSlot(String key ,Vec3 slotPosition) {
            ComponentSlotProduct slotProduct = new ComponentSlotProduct(key, slotPosition);
            slots.add(slotProduct);
            component.notifyNewSlot(slotProduct);
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

    
    
}
