package com.irr310.server.world.product;

public class SubProduct {

    private String key;
    private Product product;

    public SubProduct(String key, Product product) {
        this.key = key;
        this.product = product;
    }

    public String getKey() {
        return key;
    }
    
    public Product getProduct() {
        return product;
    }
    

    
}
