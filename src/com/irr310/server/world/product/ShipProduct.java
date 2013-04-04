package com.irr310.server.world.product;

import java.util.HashMap;
import java.util.Map;

import com.irr310.common.tools.Log;

public class ShipProduct extends Product {

    private String kernelKey;
    private ShipComponentProduct kernel;
    private Map<String, ShipComponentProduct> components = new HashMap<String, ShipProduct.ShipComponentProduct>();

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
            if(product == null || !product.performLinks(productIds)) {
                Log.warn("Ship '"+getId()+"' contains component '"+component.getKey()+"' -> '"+component.getRef()+"' but no component has this id.");
                return false;
            }
            
            if(!product.getClass().isAssignableFrom(ComponentProduct.class)) {
                Log.warn("Ship '"+getId()+"' contains component '"+component.getKey()+"' -> '"+component.getRef()+"' but it is not the id of a component.");
                return false;
            }
            component.setComponent((ComponentProduct) product);
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
    
    private class ShipComponentProduct {

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


    
    
}
