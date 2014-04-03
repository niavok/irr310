package com.irr310.client.graphics.ether.activities.production;

import java.util.ArrayList;
import java.util.List;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionAvailableProductList;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.common.world.ProductionTask;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.i3d.view.View.ViewState;
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.engine.world.WorldEngineObserver;
import com.irr310.server.world.product.Product;

public class FactoryActivity extends Activity {

    private WorldEngine worldEngine;
    private TextView factoryStatersAmountTextView;
    private TextView factoryTotalCapacityAmountTextView;
    private TextView factoryRentCapacityAmountTextView;
    private TextView factoryCapacityAmountTextView;
    private TextView factoryMaintenanceAmountTextView;
    private Button factoryBuyFactoryButton;
    private Button factorySellFactoryButton;
    private TextView factoryIncomingCapacityTextView;
    private TextView factoryIncomingCapacityDelayTextView;
    private TextView factoryOresTextView;
    private TextView factoryOresNeedsTextView;
    private TextView factoryCapacityNeedsTextView;
    private TextView factoryTimeEstimationTextView;
    
    private LinearLayout availableProductListLinearLayout;
    private SelectionManager<Product> productSelectionManager;
    private SelectionManager<ProductionTask> productionTaskSelectionManager;
    private LinearLayout productionDetailsLinearLayout;
    private LinearLayout productionTaskQueueLinearLayout;
    private Button productionCategoryFactoryButton;
    private Button productionCategoryStocksButton;
    private Button productionCategoryDesignButton;
    private Faction mFaction;
    private FactionAvailableProductList mAvailableProductList;
    private WorldEngineObserver mWorldEngineObserver;
    private static final int UPDATE_WHAT = 1;
    private FactionProduction mProduction;
    private FactionStocks mStocks;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production/factory");
        worldEngine = (WorldEngine) bundle.getObject();
        
        mFaction = LoginManager.getLocalPlayer().getFaction();
        mProduction = mFaction.getProduction();
        mStocks = mFaction.getStocks();
        mAvailableProductList = mFaction.getAvailableProductList();
        
        factoryStatersAmountTextView = (TextView) findViewById("factoryStatersAmountTextView@layout/production/factory");
        factoryMaintenanceAmountTextView = (TextView) findViewById("factoryMaintenanceAmountTextView@layout/production/factory");
        factoryCapacityAmountTextView = (TextView) findViewById("factoryCapacityAmountTextView@layout/production/factory");
        factoryRentCapacityAmountTextView = (TextView) findViewById("factoryRentCapacityAmountTextView@layout/production/factory");
        factoryTotalCapacityAmountTextView = (TextView) findViewById("factoryTotalCapacityAmountTextView@layout/production/factory");

        factoryBuyFactoryButton = (Button) findViewById("factoryBuyFactoryButton@layout/production/factory");
        factorySellFactoryButton = (Button) findViewById("factorySellFactoryButton@layout/production/factory");
        factoryIncomingCapacityTextView = (TextView) findViewById("factoryIncomingCapacityTextView@layout/production/factory");
        factoryIncomingCapacityDelayTextView = (TextView) findViewById("factoryIncomingCapacityDelayTextView@layout/production/factory");

        factoryOresTextView = (TextView) findViewById("factoryOresTextView@layout/production/factory");
        factoryOresNeedsTextView = (TextView) findViewById("factoryOresNeedsTextView@layout/production/factory");
        factoryCapacityNeedsTextView = (TextView) findViewById("factoryCapacityNeedsTextView@layout/production/factory");
        factoryTimeEstimationTextView = (TextView) findViewById("factoryTimeEstimationTextView@layout/production/factory");

        
        productionTaskQueueLinearLayout = (LinearLayout) findViewById("productionTaskQueueLinearLayout@layout/production/factory");
        
        availableProductListLinearLayout = (LinearLayout) findViewById("availableProductListLinearLayout@layout/production/factory");
        
        productionDetailsLinearLayout = (LinearLayout) findViewById("productionDetailsLinearLayout@layout/production/factory");
        
        productionCategoryFactoryButton = (Button) findViewById("productionCategoryFactoryButton@layout/production/production_categories"); 
        productionCategoryStocksButton = (Button) findViewById("productionCategoryStocksButton@layout/production/production_categories");
        productionCategoryDesignButton = (Button) findViewById("productionCategoryDesignButton@layout/production/production_categories");
        
        productionCategoryFactoryButton.setState(ViewState.SELECTED);
        
        productionCategoryStocksButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(StocksActivity.class, bundle));
            }
        });
        
        
        factoryOresNeedsTextView.setText("8420 [ores@icons]");
        factoryCapacityNeedsTextView.setText("3580 [factory@icons]");
        factoryTimeEstimationTextView.setText("(12 min 35s)");
        
        productSelectionManager = new SelectionManager<Product>();
        productionTaskSelectionManager = new SelectionManager<ProductionTask>();
        
        mWorldEngineObserver = new WorldEngineObserver() {
            
            @Override
            public void onStocksChanged(FactionStocks stocks) {
                if (mStocks.equals(stocks)) {
                    getHandler().removeMessages(UPDATE_WHAT);
                    getHandler().obtainMessage(UPDATE_WHAT).send();
                }
            }
            
            @Override
            public void onProductionChanged(FactionProduction production) {
                if (mProduction.equals(production)) {
                    getHandler().removeMessages(UPDATE_WHAT);
                    getHandler().obtainMessage(UPDATE_WHAT).send();
                }
            }
            
            @Override
            public void onPlayerConnected(Player player) {
            }
            
            @Override
            public void onFactionChanged(Faction faction) {
                if (mFaction.equals(faction)) {
                    getHandler().removeMessages(UPDATE_WHAT);
                    getHandler().obtainMessage(UPDATE_WHAT, faction).send();
                }
            }
        };

        factoryBuyFactoryButton.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                worldEngine.buyFactoryCapacityAction(LoginManager.getLocalPlayer().getFaction(), 1);
            }
        });
        
        factorySellFactoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
            	worldEngine.sellFactoryCapacityAction(LoginManager.getLocalPlayer().getFaction(), 1);
            }
        });
        
        productSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<Product>() {
            
            private List<View> addedViews = new ArrayList<View>();
            @Override
            public void onSelectionChange(List<Product> selection) {
                for(View view: addedViews) {
                    productionDetailsLinearLayout.removeView(view);
                }
                addedViews.clear();
                
                for(Product product: selection) {
                    View view = new AvailableProductDetailsView(FactoryActivity.this, product);
                    addedViews.add(view);
                    productionDetailsLinearLayout.addViewInLayout(view);
                }
                if(selection.size() > 0) {
                    productionTaskSelectionManager.clearSelection();
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return false;
            }
        });
        
        productionTaskSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ProductionTask>() {
            
            private List<View> addedViews = new ArrayList<View>();
            
            @Override
            public void onSelectionChange(List<ProductionTask> selection) {
                for(View view: addedViews) {
                    productionDetailsLinearLayout.removeView(view);
                }
                addedViews.clear();
                
                for(ProductionTask productionTask: selection) {
                    View view = new ProductionTaskDetailsView(FactoryActivity.this, productionTask);
                    addedViews.add(view);
                    productionDetailsLinearLayout.addViewInLayout(view);
                }
                
                if(selection.size() > 0) {
                    productSelectionManager.clearSelection();
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return false;
            }
        });
    }

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
            case UPDATE_WHAT:
                updateFields();
                break;
        }
    }

    protected void updateFields() {
        factoryStatersAmountTextView.setText(mFaction.getStatersAmount() + " [staters@icons]");
        factoryMaintenanceAmountTextView.setText(mProduction.getMaintenanceAmount() + " [staters@icons]");
        factoryCapacityAmountTextView.setText(mProduction.getFactoryCapacity() + " [factory@icons]");
        factoryRentCapacityAmountTextView.setText(mProduction.getFactoryRentCapacity() + " [factory@icons]");
        factoryTotalCapacityAmountTextView.setText(mProduction.getFactoryTotalCapacity() + " [factory@icons]");

        if (mProduction.getIncomingCapacity() > 0) {
            factoryIncomingCapacityTextView.setText("+" + mProduction.getIncomingCapacity());
            factoryIncomingCapacityDelayTextView.setText(mProduction.getNextFactoryCapacityIncreaseRounds() + " s");
        } else {
            factoryIncomingCapacityTextView.setText("0");
            factoryIncomingCapacityDelayTextView.setText("--");
        }

        factoryOresTextView.setText(mFaction.getOresAmount() + " [ores@icons]");
        
        
        
        // Update production task queue
        productionTaskQueueLinearLayout.removeAllView();
        productionTaskSelectionManager.clear(ProductionTaskView.class);
        
        for(ProductionTask productionTask: mProduction.getProductionTaskQueue()) {            
            productionTaskQueueLinearLayout.addViewInLayout(new ProductionTaskView(productionTask, productionTaskSelectionManager));
        }
        
        // Update available product list
        availableProductListLinearLayout.removeAllView();
        productSelectionManager.clear(AvailableProductView.class);
        
        for(Product product: mAvailableProductList.getProducts()) {            
            availableProductListLinearLayout.addViewInLayout(new AvailableProductView(product, productSelectionManager));
        }
        
    }
    
    void buyProduct(Product product, long quantity) {
        worldEngine.buyProductAction(LoginManager.getLocalPlayer().getFaction(), product, quantity);  
    }

}
