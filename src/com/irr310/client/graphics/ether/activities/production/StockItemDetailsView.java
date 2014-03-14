package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.ShipItem;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class StockItemDetailsView extends ProxyView {

    private final Item item;
    private TextView itemNameTextView;
    private Button deployShipButton;
    private final StocksActivity stocksActivity;

    public StockItemDetailsView(StocksActivity stocksActivity, Item item) {
        super(I3dRessourceManager.loadView("main@layout/production/stock_item_details"));
        this.stocksActivity = stocksActivity;
        this.item = item;
        itemNameTextView = (TextView) findViewById("itemNameTextView@layout/production/stock_item_details");
        
        deployShipButton = (Button) findViewById("deployShipButton@layout/production/stock_item_details");
        
        if(!item.getProduct().isShip()) {
            deployShipButton.setVisible(false);
        } else {
            deployShipButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(V3DMouseEvent mouseEvent, View view) {
                    StockItemDetailsView.this.stocksActivity.deployShip((ShipItem) StockItemDetailsView.this.item);
                }
            });
        }
        
        itemNameTextView.setText(item.getProduct().getName() +" "+ item.getProduct().getCode());
        
        
    }

}
