package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.ProductState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class StockItemDetailsView extends ProxyView {

    private final ItemState item;
    private TextView itemNameTextView;
    private Button deployShipButton;
    private final StocksActivity stocksActivity;

    public StockItemDetailsView(StocksActivity stocksActivity, ItemState item) {
        super(I3dRessourceManager.loadView("main@layout/production/stock_item_details"));
        this.stocksActivity = stocksActivity;
        this.item = item;
        itemNameTextView = (TextView) findViewById("itemNameTextView@layout/production/stock_item_details");
        
        deployShipButton = (Button) findViewById("deployShipButton@layout/production/stock_item_details");
        
        if(item.product.type != ProductState.TYPE_SHIP) {
            deployShipButton.setVisible(false);
        } else {
            deployShipButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(V3DMouseEvent mouseEvent, View view) {
                    StockItemDetailsView.this.stocksActivity.deployShip(StockItemDetailsView.this.item);
                }
            });
        }
        
        itemNameTextView.setText(item.product.name +" "+ item.product.code);
        
        
    }

}
