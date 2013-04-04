package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class AvailableProductView extends ProxyView {

    private final ProductState product;
    private TextView productNameTextView;

    public AvailableProductView(ProductState product) {
        super(I3dRessourceManager.loadView("main@layout/production/available_product"));
        this.product = product;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/available_product");
        
        productNameTextView.setText(product.name);
    }

}