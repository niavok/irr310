package com.irr310.common.world;

import java.util.ArrayList;

import com.irr310.common.world.state.FactionAvailableProductListState;
import com.irr310.common.world.state.ProductState;

public class FactionAvailableProductList {

    private final Faction faction;
    
    public FactionAvailableProductList(Faction faction) {
        this.faction = faction;
    }
    
    public Faction getFaction() {
        return faction;
    }

    public FactionAvailableProductListState toState() {
        FactionAvailableProductListState factionAvailableProductListState = new FactionAvailableProductListState();
        factionAvailableProductListState.factionId = faction.getId();

        factionAvailableProductListState.products = new ArrayList<ProductState>();
        
        ProductState productView1 = new ProductState(); 
        ProductState productView2 = new ProductState();
        ProductState productView3 = new ProductState();
        ProductState productView4 = new ProductState(); 
        ProductState productView5 = new ProductState();
        ProductState productView6 = new ProductState();
        
        productView1.name = "Fighter X34";
        productView2.name = "Light Machine gun";
        productView3.name = "Structural bar 1m";
        productView4.name = "Fighter X35";
        productView5.name = "Heavy Machine gun";
        productView6.name = "Structural bar 2m";
        
        factionAvailableProductListState.products.add(productView1);
        factionAvailableProductListState.products.add(productView2);
        factionAvailableProductListState.products.add(productView3);
        factionAvailableProductListState.products.add(productView4);
        factionAvailableProductListState.products.add(productView5);
        factionAvailableProductListState.products.add(productView6);
        
        return factionAvailableProductListState;
    }
    
}
