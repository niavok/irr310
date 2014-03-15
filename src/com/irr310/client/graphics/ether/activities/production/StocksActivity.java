package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.ShipItem;
import com.irr310.common.world.system.Nexus;
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
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.engine.world.WorldEngineObserver;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class StocksActivity extends Activity {

    protected static StockItemDetailsView stockItemView;
    private WorldEngine worldEngine;
    private Button productionCategoryFactoryButton;
    private Button productionCategoryStocksButton;
    private Button productionCategoryDesignButton;
    private LinearLayout stocksListLinearLayout;
    private FactionStocks mStocks;
    private SelectionManager<Item> stockItemSelectionManager;
    private LinearLayout stockDetailsLinearLayout;
    private WorldEngineObserver mWorldEngineObserver;
    
    private static final int UPDATE_STOCKS_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production/stocks");
        worldEngine = (WorldEngine) bundle.getObject();
        mStocks =  LoginManager.getLocalPlayer().getFaction().getStocks(); 
        
        productionCategoryFactoryButton = (Button) findViewById("productionCategoryFactoryButton@layout/production/production_categories"); 
        productionCategoryStocksButton = (Button) findViewById("productionCategoryStocksButton@layout/production/production_categories");
        productionCategoryDesignButton = (Button) findViewById("productionCategoryDesignButton@layout/production/production_categories");
        
        
        stocksListLinearLayout = (LinearLayout) findViewById("stocksListLinearLayout@layout/production/stocks");
        stockDetailsLinearLayout = (LinearLayout) findViewById("stockDetailsLinearLayout@layout/production/stocks");
        
        
        productionCategoryStocksButton.setState(ViewState.SELECTED);
        
        productionCategoryFactoryButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(FactoryActivity.class, bundle));
            }
        });
       
        
        mWorldEngineObserver = new WorldEngineObserver() {
            @Override
            public void onStocksChanged(FactionStocks stocks) {
                if (mStocks.equals(stocks)) {
                    getHandler().removeMessages(UPDATE_STOCKS_WHAT);
                    getHandler().obtainMessage(UPDATE_STOCKS_WHAT).send();
                }
            }

            @Override
            public void onFactionChanged(Faction faction) {
            }
            
            @Override
            public void onPlayerConnected(Player player) {
            }
            
            @Override
            public void onProductionChanged(FactionProduction production) {
            }
        };
        
        stockItemSelectionManager = new SelectionManager<Item>();
       
        stockItemSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Item>() {

            @Override
            public void onSelectionChange(List<Item> selection) {
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
        worldEngine.getWorldEnginObservable().register(this, mWorldEngineObserver);
        updateFields();
    }

    @Override
    public void onPause() {
        worldEngine.getWorldEnginObservable().unregister(this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    protected void onMessage(Message message) {
        switch (message.what) {
            case UPDATE_STOCKS_WHAT:
                updateFields();
                break;
        }
    }

    private void updateFields() {
        // Update available product list
        stocksListLinearLayout.removeAllView();
        stockItemSelectionManager.clear(StockItemView.class);
        
        for(Item item: mStocks.getStocks()) {
            if(item.getState() == Item.State.STOCKED) {
                stocksListLinearLayout.addViewInLayout(new StockItemView(item, stockItemSelectionManager));
            }
        }        
    }

    public void deployShip(ShipItem ship) {
        Nexus nexus = LoginManager.getLocalPlayer().getFaction().getRootNexus();
        worldEngine.deployShipAction(ship, nexus);
    }

  

}
