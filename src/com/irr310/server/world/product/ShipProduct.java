package com.irr310.server.world.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec3;
import com.irr310.server.world.product.ComponentProduct.ComponentSlotProduct;

public class ShipProduct extends Product {

    private static final long ORES_COST_PER_LINK = 10;
    private static final long FACTORY_COST_PER_LINK = 50;
    private String kernelKey;
    private ShipComponentProduct kernel;
    private Map<String, ShipComponentProduct> components = new HashMap<String, ShipProduct.ShipComponentProduct>();
    private List<ShipLinkProduct> links = new ArrayList<ShipLinkProduct>();
    private ArrayList<SubProduct> subProducts;

    public void setKernelKey(String kernelKey) {
        this.kernelKey = kernelKey;
    }
    
    public String getKernelCode() {
        return kernelKey;
    }
    
    public String getKernelKey() {
        return kernelKey;
    }
    
    public ShipComponentProduct getKernel() {
        return kernel;
    }

    public boolean performLinks(Map<String, Product> productIds) {
        if(!super.performLinks(productIds)) {
            return false;
        }
        
        if(kernelKey == null || kernelKey.isEmpty()) {
            Log.warn("Ship '"+getId()+"' has no kernel key.");
            return false;
        }
        
        if(!components.containsKey(kernelKey)) {
            Log.warn("Ship '"+getId()+"' has kernel key '"+kernelKey+"' but no component has this name'.");
            return false;
        } else {
            kernel = components.get(kernelKey);
        }
        
        //Link components
        for(ShipComponentProduct component: components.values()) {
            Product product = productIds.get(component.getRef());
            if(product == null) {
                Log.warn("Ship '"+getId()+"' contains component '"+component.getKey()+"' -> '"+component.getRef()+"' but no component has this id.");
                return false;
            }
            
            if(!product.performLinks(productIds)) {
                Log.warn("Ship '"+getId()+"' contains component '"+component.getKey()+"' -> '"+component.getRef()+"' but this component is invalid.");
                    return false;
            }
            
            if(!product.getClass().isAssignableFrom(ComponentProduct.class)) {
                Log.warn("Ship '"+getId()+"' contains component '"+component.getKey()+"' -> '"+component.getRef()+"' but it is not the id of a component.");
                return false;
            }
            component.setComponent((ComponentProduct) product);
        }
        
        //Link links
        for(ShipLinkProduct link: links) {
            
            //Slot A
            String[] splitA = link.getRefA().split("@");
            if(splitA.length != 2) {
                Log.warn("Ship '"+getId()+"' has Link slot A with a bad format: '"+link.getRefA()+"'. It must be formated like this: 'slotKey@componentKey'");
                return false;
            }
            
            if(!components.containsKey(splitA[1])) {
                Log.warn("Ship '"+getId()+"' has Link slot A '"+link.getRefA()+"' has component key '"+splitA[1]+"' but no component has this name'.");
                return false;
            }
            
            ShipComponentProduct componentA = components.get(splitA[1]);
            ComponentSlotProduct componentASlot =  componentA.getComponent().getSlot(splitA[0]);
            
            if(componentASlot == null) {
                Log.warn("Ship '"+getId()+"' has Link slot A '"+link.getRefA()+"' but the target component has no slot with this name.");
                return false;
            }
            
            //Slot B
            String[] splitB = link.getRefB().split("@");
            if(splitB.length != 2) {
                Log.warn("Ship '"+getId()+"' has Link slot B with a bad format: '"+link.getRefB()+"'. It must be formated like this: 'slotKey@componentKey'");
                return false;
            }
            
            if(!components.containsKey(splitB[1])) {
                Log.warn("Ship '"+getId()+"' has Link slot B '"+link.getRefB()+"' has component key '"+splitB[1]+"' but no component has this name'.");
                return false;
            }
            
            ShipComponentProduct componentB = components.get(splitB[1]);
            ComponentSlotProduct componentBSlot =  componentB.getComponent().getSlot(splitB[0]);
            
            if(componentBSlot == null) {
                Log.warn("Ship '"+getId()+"' has Link slot B '"+link.getRefB()+"' but the target component has no slot with this name.");
                return false;
            }
            
            // Both slot ok, link the slots
            link.setSlotA(componentASlot, componentA);
            link.setSlotB(componentBSlot, componentB);
            
            
        }
        return true;
    }
    
    public void addComponent(String key, String ref, Vec3 location, Vec3 rotation) {
        if(key == null | key.isEmpty()) {
            Log.warn("A component of ship '"+getId()+"' has no key. Skip");
        } else if(ref == null | ref.isEmpty()) {
            Log.warn("A component of ship '"+getId()+"' has no ref. Skip");
        } else {
            // All is ok, let's add the component to ship
            components.put(key,new ShipComponentProduct(key, ref, location, rotation));
        }
        
        
    }
    
    public void addLink(String refA, String refB) {
        if(refA == null | refA.isEmpty()) {
            Log.warn("A component of ship '"+getId()+"' has no ref A. Skip");
        } else if(refB == null | refB.isEmpty()) {
            Log.warn("A component of ship '"+getId()+"' has no ref B. Skip");
        } else {
            // All is ok, let's add the link to ship
            links.add(new ShipLinkProduct(refA, refB));
        }
    }
  
    @Override
    public boolean isShip() {
        return true;
    }
    
    public static class ShipComponentProduct {

        private final String mKey;
        private final String mRef;
        private ComponentProduct mComponent;
        private Vec3 mLocation;
        private Vec3 mRotation;

        public ShipComponentProduct(String key, String ref, Vec3 location, Vec3 rotation) {
            this.mKey = key;
            this.mRef = ref;
            this.mLocation = location;
            mRotation = rotation;
        }
        
        public void setComponent(ComponentProduct component) {
            this.mComponent = component;
        }

        public String getRef() {
            return mRef;
        }
        
        public String getKey() {
            return mKey;
        }

        public ComponentProduct getComponent() {
            return mComponent;
        }
        
        public Vec3 getLocation() {
            return mLocation;
        }
        
        public Vec3 getRotation() {
            return mRotation;
        }
        
    }
    
    public static class ShipLinkProduct {
        
        private String mRefA;
        private String mRefB;
        private ComponentSlotProduct mSlotA;
        private ComponentSlotProduct mSlotB;
        private ShipComponentProduct mComponentA;
        private ShipComponentProduct mComponentB;
        
        public ShipLinkProduct(String refA, String refB) {
            this.mRefA = refA;
            this.mRefB = refB;
        }
        
        public String getRefA() {
            return mRefA;
        }
        
        public String getRefB() {
            return mRefB;
        }
        
        public void setSlotA(ComponentSlotProduct slotA, ShipComponentProduct componentA) {
            this.mSlotA = slotA;
            mComponentA = componentA;
        }
        
        public void setSlotB(ComponentSlotProduct slotB, ShipComponentProduct componentB) {
            this.mSlotB = slotB;
            mComponentB = componentB;
        }
        
        public ComponentSlotProduct getSlotA() {
            return mSlotA;
        }
        
        public ComponentSlotProduct getSlotB() {
            return mSlotB;
        }
        
        public ShipComponentProduct getComponentA() {
            return mComponentA;
        }
        
        public ShipComponentProduct getComponentB() {
            return mComponentB;
        }
    }

    @Override
    public List<SubProduct> getSubProducts() {
        
        if(subProducts == null) {
            subProducts = new ArrayList<SubProduct>();
            for(ShipComponentProduct shipComponentProduct: components.values()) {
                subProducts.add(new SubProduct(shipComponentProduct.getKey(), shipComponentProduct.getComponent()));
            }
            Collections.sort(subProducts, new Comparator<SubProduct>() {
                @Override
                public int compare(SubProduct o1, SubProduct o2) {
                    return o1.getProduct().getId().compareTo(o2.getProduct().getId());
                }});
        }
        
        return subProducts;
    }

    @Override
    public long getOreCost() {
        return links.size()* ORES_COST_PER_LINK / ProductManager.DEBUG_COEF;
    }

    @Override
    public long getFactoryCost() {
        return links.size()* FACTORY_COST_PER_LINK / ProductManager.DEBUG_COEF;
    }
    
    public Map<String, ShipComponentProduct> getComponents() {
        return components;
    }
    
    public List<ShipLinkProduct> getLinks() {
        return links;
    }
    
}
