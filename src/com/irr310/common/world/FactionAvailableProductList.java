package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.state.FactionAvailableProductListState;
import com.irr310.common.world.state.ProductState;
import com.irr310.server.world.product.Product;
import com.irr310.server.world.product.ProductManager;

public class FactionAvailableProductList {

    private final Faction faction;
    private ProductManager productManager;
    
    public FactionAvailableProductList(Faction faction, ProductManager productManager) {
        this.faction = faction;
        this.productManager = productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
    
    public Faction getFaction() {
        return faction;
    }

    public FactionAvailableProductListState toState() {
        FactionAvailableProductListState factionAvailableProductListState = new FactionAvailableProductListState();
        factionAvailableProductListState.factionId = faction.getId();

        factionAvailableProductListState.products = new ArrayList<ProductState>();
        
        for(Product product: productManager.getProducts()) {
            
            factionAvailableProductListState.products.add(product.toState());
        }
        
        return factionAvailableProductListState;
    }

    public Product getProduct(ProductState product) {
        return productManager.getProductById(product.id);
    }
    
}
