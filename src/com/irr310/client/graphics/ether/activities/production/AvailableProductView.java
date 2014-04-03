package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.server.world.product.Product;

public class AvailableProductView extends ProxyView {

    private final Product product;
    private TextView productNameTextView;

    public AvailableProductView(Product product, final SelectionManager<Product> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/production/available_product"));
        this.product = product;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/available_product");
        
        productNameTextView.setText(product.getName() +" "+ product.getCode());
        
        this.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                selectionManager.select(AvailableProductView.this.product);
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Product>() {
          
            public void onSelectionChange(List<Product> selection) {
                if(selection.contains(AvailableProductView.this.product)) {
                    setState(ViewState.SELECTED);
                } else {
                    setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(AvailableProductView.class));
            }
        }
        );
        
        if(selectionManager.getSelection().contains(product)) {
            setState(ViewState.SELECTED);
        }
    }

}
