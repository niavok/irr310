package com.irr310.common.world;

import java.util.List;

import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ProductManager;

public class FactionAvailableProductList {

    private final Faction mFaction;
    private ProductManager mProductManager;
    
    public FactionAvailableProductList(Faction faction, ProductManager productManager) {
        this.mFaction = faction;
        this.mProductManager = productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.mProductManager = productManager;
    }
    
    public Faction getFaction() {
        return mFaction;
    }
    
    public List<Product> getProducts() {
        return mProductManager.getProducts();
    }
}
