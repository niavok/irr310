package com.irr310.client.graphics.ether.activities.production;

import java.util.ArrayList;
import java.util.List;

import sun.security.util.Length;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.event.world.ActionBuyFactionFactoryCapacityEvent;
import com.irr310.common.event.world.ActionBuyProductEvent;
import com.irr310.common.event.world.ActionSellFactionFactoryCapacityEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionAvailableProductListEvent;
import com.irr310.common.event.world.FactionProductionStateEvent;
import com.irr310.common.event.world.FactionStateEvent;
import com.irr310.common.event.world.FactionStocksStateEvent;
import com.irr310.common.event.world.QueryFactionAvailableProductListEvent;
import com.irr310.common.event.world.QueryFactionProductionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.QueryFactionStocksStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.world.World;
import com.irr310.common.world.state.FactionAvailableProductListState;
import com.irr310.common.world.state.FactionProductionState;
import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.FactionStocksState;
import com.irr310.common.world.state.ItemState;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.i3d.view.View.ViewState;
import com.irr310.server.Time;

public class StocksActivity extends Activity {

    private WorldEventDispatcher worldEngine;
    private Button productionCategoryFactoryButton;
    private Button productionCategoryStocksButton;
    private Button productionCategoryDesignButton;
    private DefaultWorldEventVisitor visitor;
    private LinearLayout stocksListLinearLayout;
    private Handler handler = new Handler();
    private FactionStocksState stocks;
    private SelectionManager<ItemState> stockItemSelectionManager;
    
    private static final int UPDATE_STOCKS_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production/stocks");
        worldEngine = (WorldEventDispatcher) bundle.getObject();
        
        productionCategoryFactoryButton = (Button) findViewById("productionCategoryFactoryButton@layout/production/production_categories"); 
        productionCategoryStocksButton = (Button) findViewById("productionCategoryStocksButton@layout/production/production_categories");
        productionCategoryDesignButton = (Button) findViewById("productionCategoryDesignButton@layout/production/production_categories");
        
        
        stocksListLinearLayout = (LinearLayout) findViewById("stocksListLinearLayout@layout/production/stocks");
        
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
                    handler.obtainMessage(UPDATE_STOCKS_WHAT, event.getFactionStocks()).send();
                }
            }
        };
        
        stockItemSelectionManager = new SelectionManager<ItemState>();
       
    }

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
    protected void onUpdate(Time absTime, Time gameTime) {
        while (handler.hasMessages()) {
            Message message = handler.getMessage();

            switch (message.what) {
                case UPDATE_STOCKS_WHAT:
                    stocks = (FactionStocksState) message.obj;
                    updateFields();
                    break;
            }

        }
    }

    private void updateFields() {
        // Update available product list
        stocksListLinearLayout.removeAllView();
        stockItemSelectionManager.clear(StockItemView.class);
        
        for(ItemState item: stocks.stocks) {            
            stocksListLinearLayout.addViewInLayout(new StockItemView(item, stockItemSelectionManager));
        }        
    }

  

}
