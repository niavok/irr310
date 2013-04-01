package com.irr310.common.world;

import java.util.ArrayList;

import com.irr310.common.world.view.FactionAvailableProductListView;
import com.irr310.common.world.view.ProductView;

public class FactionAvailableProductList {

    private final Faction faction;
    
    public FactionAvailableProductList(Faction faction) {
        this.faction = faction;
    }
    
    public Faction getFaction() {
        return faction;
    }

    public FactionAvailableProductListView toView() {
        FactionAvailableProductListView factionAvailableProductListView = new FactionAvailableProductListView();
        factionAvailableProductListView.factionId = faction.getId();

        factionAvailableProductListView.products = new ArrayList<ProductView>();
        
        ProductView productView1 = new ProductView(); 
        ProductView productView2 = new ProductView();
        ProductView productView3 = new ProductView();
        ProductView productView4 = new ProductView(); 
        ProductView productView5 = new ProductView();
        ProductView productView6 = new ProductView();
        
        productView1.name = "Fighter X34";
        productView2.name = "Light Machine gun";
        productView3.name = "Structural bar 1m";
        productView4.name = "Fighter X35";
        productView5.name = "Heavy Machine gun";
        productView6.name = "Structural bar 2m";
        
        factionAvailableProductListView.products.add(productView1);
        factionAvailableProductListView.products.add(productView2);
        factionAvailableProductListView.products.add(productView3);
        factionAvailableProductListView.products.add(productView4);
        factionAvailableProductListView.products.add(productView5);
        factionAvailableProductListView.products.add(productView6);
        
        return factionAvailableProductListView;
    }
    
}
