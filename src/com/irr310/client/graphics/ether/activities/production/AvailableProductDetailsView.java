package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.world.state.ProductState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class AvailableProductDetailsView extends ProxyView {

    private final ProductState product;
    private TextView productNameTextView;
    private Button buyProductButton;
    private final FactoryActivity productionActivity;

    public AvailableProductDetailsView(FactoryActivity productionActivity, ProductState product) {
        super(I3dRessourceManager.loadView("main@layout/production/available_product_details"));
        this.productionActivity = productionActivity;
        this.product = product;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/available_product_details");
        
        buyProductButton = (Button) findViewById("buyProductButton@layout/production/available_product_details");
        
        productNameTextView.setText(product.name +" "+ product.code);
        
        
        buyProductButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                AvailableProductDetailsView.this.productionActivity.buyProduct(AvailableProductDetailsView.this.product, 1);
            }
        });
    }

}
