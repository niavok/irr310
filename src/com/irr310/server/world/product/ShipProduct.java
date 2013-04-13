package com.irr310.server.world.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.world.state.ProductState;
import com.irr310.server.world.product.ComponentProduct.ComponentSlotProduct;

public class ShipProduct extends Product {

    private String kernelKey;
    private ShipComponentProduct kernel;
    private Map<String, ShipComponentProduct> components = new HashMap<String, ShipProduct.ShipComponentProduct>();
    private List<ShipLinkProduct> links = new ArrayList<ShipLinkProduct>();

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
            link.setSlotA(componentASlot);
            link.setSlotB(componentBSlot);
            
            
        }
        return true;
    }
    
    public void addComponent(String key, String ref) {
        if(key == null | key.isEmpty()) {
            Log.warn("A component of ship '"+getId()+"' has no key. Skip");
        } else if(ref == null | ref.isEmpty()) {
            Log.warn("A component of ship '"+getId()+"' has no ref. Skip");
        } else {
            // All is ok, let's add the component to ship
            components.put(key,new ShipComponentProduct(key, ref));
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
    public ProductState toState() {
        ProductState productState = new ProductState();
        productState.id = getId();
        productState.name = getName();
        productState.description = getDescription();
        productState.code = getCode();
        productState.type = ProductState.TYPE_SHIP;
        return productState;
    }
    
    public static class ShipComponentProduct {

        private final String key;
        private final String ref;
        private ComponentProduct component;

        public ShipComponentProduct(String key, String ref) {
            this.key = key;
            this.ref = ref;
        }
        
        public void setComponent(ComponentProduct component) {
            this.component = component;
        }

        public String getRef() {
            return ref;
        }
        
        public String getKey() {
            return key;
        }

        public ComponentProduct getComponent() {
            return component;
        }
        
    }
    
    public static class ShipLinkProduct {
        
        private String refA;
        private String refB;
        private ComponentSlotProduct slotA;
        private ComponentSlotProduct slotB;
        
        public ShipLinkProduct(String refA, String refB) {
            this.refA = refA;
            this.refB = refB;
        }
        
        public String getRefA() {
            return refA;
        }
        
        public String getRefB() {
            return refB;
        }
        
        public void setSlotA(ComponentSlotProduct slotA) {
            this.slotA = slotA;
        }
        
        public void setSlotB(ComponentSlotProduct slotB) {
            this.slotB = slotB;
        }
        
        public ComponentSlotProduct getSlotA() {
            return slotA;
        }
        
        public ComponentSlotProduct getSlotB() {
            return slotB;
        }
    }

    @Override
    public List<Product> getSubProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        for(ShipComponentProduct shipComponentProduct: components.values()) {
            products.add(shipComponentProduct.getComponent());
        }
        return products;
    }

    
    
}
