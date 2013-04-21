package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class StockItemView extends ProxyView {

    private final ItemState item;
    private TextView itemNameTextView;

    public StockItemView(ItemState item, final SelectionManager<ItemState> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/production/stock_item"));
        this.item = item;
        itemNameTextView = (TextView) findViewById("itemNameTextView@layout/production/stock_item");
        
        itemNameTextView.setText(item.product.name +" "+ item.product.code);
        
        this.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                selectionManager.select(StockItemView.this.item);
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ItemState>() {
          
            public void onSelectionChange(List<ItemState> selection) {
                if(selection.contains(StockItemView.this.item)) {
                    setState(ViewState.SELECTED);
                } else {
                    setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(StockItemView.class));
            }
        }
        );
        
        if(selectionManager.getSelection().contains(item)) {
            setState(ViewState.SELECTED);
        }
    }

}
