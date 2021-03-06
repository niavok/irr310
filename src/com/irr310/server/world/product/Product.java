package com.irr310.server.world.product;

import java.util.List;
import java.util.Map;

import com.irr310.common.tools.Log;
import com.irr310.common.world.Faction;

public abstract class Product {

    private String id = null;
    
    /**
     * The code is a short display name
     */
    private String code = null;
    private String name = null;
    private String description = "";
    
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
  
    
    public void setDescription(String description) {
        this.description  = description;
    }
    
    public String getDescription() {
        return description;
    }

    public boolean performLinks(Map<String, Product> productIds) {
        if(id == null || id.isEmpty()) {
            Log.warn("Product with no id. Class:"+this.getClass().getSimpleName());
            return false;
        }
        
        if(name == null || name.isEmpty()) {
            Log.warn("Product '"+getId()+"' has no name.");
            return false;
        }
        
        if(code == null || code.isEmpty()) {
            Log.warn("Product '"+getId()+"' has no code.");
            return false;
        }
        
        if(description == null || description.isEmpty()) {
            Log.warn("Product '"+getId()+"' has no description.");
            // No fatal
        }
        
        return true;
    }

    public boolean isAvailable(Faction faction) {
        // TODO: check techno and law.
        return true;
    }

    /**
     * Return sorted product list
     */
    public abstract List<SubProduct> getSubProducts();

    public abstract long getOreCost();

    public abstract long getFactoryCost();

    public abstract boolean isShip();
}
