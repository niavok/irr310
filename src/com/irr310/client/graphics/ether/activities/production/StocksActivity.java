package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.event.world.ActionBuyProductEvent;
import com.irr310.common.event.world.ActionDeployShipEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionStocksStateEvent;
import com.irr310.common.event.world.QueryFactionStocksStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.world.item.Item.State;
import com.irr310.common.world.state.FactionStocksState;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.NexusState;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.i3d.view.View.ViewState;

public class StocksActivity extends Activity {

    protected static StockItemDetailsView stockItemView;
    private WorldEventDispatcher worldEngine;
    private Button productionCategoryFactoryButton;
    private Button productionCategoryStocksButton;
    private Button productionCategoryDesignButton;
    private DefaultWorldEventVisitor visitor;
    private LinearLayout stocksListLinearLayout;
    private FactionStocksState stocks;
    private SelectionManager<ItemState> stockItemSelectionManager;
    private LinearLayout stockDetailsLinearLayout;
    
    private static final int UPDATE_STOCKS_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production/stocks");
        worldEngine = (WorldEventDispatcher) bundle.getObject();
        
        productionCategoryFactoryButton = (Button) findViewById("productionCategoryFactoryButton@layout/production/production_categories"); 
        productionCategoryStocksButton = (Button) findViewById("productionCategoryStocksButton@layout/production/production_categories");
        productionCategoryDesignButton = (Button) findViewById("productionCategoryDesignButton@layout/production/production_categories");
        
        
        stocksListLinearLayout = (LinearLayout) findViewById("stocksListLinearLayout@layout/production/stocks");
        stockDetailsLinearLayout = (LinearLayout) findViewById("stockDetailsLinearLayout@layout/production/stocks");
        
        
        productionCategoryStocksButton.setState(ViewState.SELECTED);
        
        productionCategoryFactoryButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(FactoryActivity.class, bundle));
            }
        });
       
        visitor = new DefaultWorldEventVisitor() {
            @Override
            public void visit(FactionStocksStateEvent event) {
                if (LoginManager.getLocalPlayer().faction.id == event.getFactionStocks().factionId) {
                    getHandler().obtainMessage(UPDATE_STOCKS_WHAT, event.getFactionStocks()).send();
                }
            }
        };
        
        stockItemSelectionManager = new SelectionManager<ItemState>();
       
        stockItemSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ItemState>() {

            @Override
            public void onSelectionChange(List<ItemState> selection) {
                if(selection.size() == 1) {
                   
                    stockDetailsLinearLayout.removeView(stockItemView);
                    stockItemView = new StockItemDetailsView(StocksActivity.this,selection.get(0));
                    stockDetailsLinearLayout.addViewInLayout(stockItemView);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return false;
            }});
        
    };

    @Override
    public void onResume() {
        worldEngine.registerEventVisitor(visitor);
        worldEngine.sendToAll(new QueryFactionStocksStateEvent(LoginManager.getLocalPlayer().faction));
    }

    @Override
    public void onPause() {
        worldEngine.unregisterEventVisitor(visitor);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    protected void onMessage(Message message) {
        switch (message.what) {
            case UPDATE_STOCKS_WHAT:
                stocks = (FactionStocksState) message.obj;
                updateFields();
                break;
        }
    }

    private void updateFields() {
        // Update available product list
        stocksListLinearLayout.removeAllView();
        stockItemSelectionManager.clear(StockItemView.class);
        
        for(ItemState item: stocks.stocks) {
            if(item.state == ItemState.STOCKED) {
                stocksListLinearLayout.addViewInLayout(new StockItemView(item, stockItemSelectionManager));
            }
        }        
    }

    public void deployShip(ItemState item) {
        NexusState nexus = LoginManager.getLocalPlayer().faction.rootNexus;
        worldEngine.sendToAll(new ActionDeployShipEvent(item, nexus));
    }

  

}
