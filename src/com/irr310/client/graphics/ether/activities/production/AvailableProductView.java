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

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class AvailableProductView extends ProxyView {

    private final ProductState product;
    private TextView productNameTextView;

    public AvailableProductView(ProductState product, final SelectionManager<ProductState> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/production/available_product"));
        this.product = product;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/available_product");
        
        productNameTextView.setText(product.name +" "+ product.code);
        
        this.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                selectionManager.select(AvailableProductView.this.product);
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ProductState>() {
          
            public void onSelectionChange(List<ProductState> selection) {
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
