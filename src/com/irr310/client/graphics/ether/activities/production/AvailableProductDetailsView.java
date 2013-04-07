package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class AvailableProductDetailsView extends ProxyView {

    private final ProductState product;
    private TextView productNameTextView;

    public AvailableProductDetailsView(ProductState product) {
        super(I3dRessourceManager.loadView("main@layout/production/available_product_details"));
        this.product = product;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/available_product_details");
        
        productNameTextView.setText(product.name +" "+ product.code);
    }

}
