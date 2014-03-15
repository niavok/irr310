package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.common.world.item.Item;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class StockItemView extends ProxyView {

    private final Item item;
    private TextView itemNameTextView;

    public StockItemView(Item item, final SelectionManager<Item> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/production/stock_item"));
        this.item = item;
        itemNameTextView = (TextView) findViewById("itemNameTextView@layout/production/stock_item");
        
        itemNameTextView.setText(item.getProduct().getName() +" "+ item.getProduct().getCode());
        
        this.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                selectionManager.select(StockItemView.this.item);
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Item>() {
          
            public void onSelectionChange(List<Item> selection) {
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
