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
import com.irr310.common.event.world.QueryFactionAvailableProductListEvent;
import com.irr310.common.event.world.QueryFactionProductionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.world.World;
import com.irr310.common.world.state.FactionAvailableProductListState;
import com.irr310.common.world.state.FactionProductionState;
import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
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
import com.irr310.server.Time;

public class ProductionActivity extends Activity {

    private WorldEventDispatcher worldEngine;
    private TextView factoryStatersAmountTextView;
    // private BinderClient binder;
    private TextView factoryTotalCapacityAmountTextView;
    private TextView factoryRentCapacityAmountTextView;
    private TextView factoryCapacityAmountTextView;
    private TextView factoryMaintenanceAmountTextView;
    private Handler handler = new Handler();
    private WorldEventVisitor visitor;
    private Button factoryBuyFactoryButton;
    private Button factorySellFactoryButton;
    private TextView factoryIncomingCapacityTextView;
    private TextView factoryIncomingCapacityDelayTextView;
    private FactionState faction;
    private FactionProductionState production;
    private TextView factoryOresTextView;
    private TextView factoryOresNeedsTextView;
    private TextView factoryCapacityNeedsTextView;
    private TextView factoryTimeEstimationTextView;
    private FactionAvailableProductListState availableProductList;
    private LinearLayout availableProductListLinearLayout;
    private SelectionManager<ProductState> productSelectionManager;
    private SelectionManager<ProductionTaskState> productionTaskSelectionManager;
    private LinearLayout productionDetailsLinearLayout;
    private LinearLayout productionTaskQueueLinearLayout;
    private static final int UPDATE_FACTION_WHAT = 1;
    private static final int UPDATE_PRODUCTION_WHAT = 2;
    private static final int UPDATE_AVAILABLE_PRODUCT_LIST_WHAT = 3;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production/production");
        worldEngine = (WorldEventDispatcher) bundle.getObject();
        factoryStatersAmountTextView = (TextView) findViewById("factoryStatersAmountTextView@layout/production/production");
        factoryMaintenanceAmountTextView = (TextView) findViewById("factoryMaintenanceAmountTextView@layout/production/production");
        factoryCapacityAmountTextView = (TextView) findViewById("factoryCapacityAmountTextView@layout/production/production");
        factoryRentCapacityAmountTextView = (TextView) findViewById("factoryRentCapacityAmountTextView@layout/production/production");
        factoryTotalCapacityAmountTextView = (TextView) findViewById("factoryTotalCapacityAmountTextView@layout/production/production");

        factoryBuyFactoryButton = (Button) findViewById("factoryBuyFactoryButton@layout/production/production");
        factorySellFactoryButton = (Button) findViewById("factorySellFactoryButton@layout/production/production");
        factoryIncomingCapacityTextView = (TextView) findViewById("factoryIncomingCapacityTextView@layout/production/production");
        factoryIncomingCapacityDelayTextView = (TextView) findViewById("factoryIncomingCapacityDelayTextView@layout/production/production");

        factoryOresTextView = (TextView) findViewById("factoryOresTextView@layout/production/production");
        factoryOresNeedsTextView = (TextView) findViewById("factoryOresNeedsTextView@layout/production/production");
        factoryCapacityNeedsTextView = (TextView) findViewById("factoryCapacityNeedsTextView@layout/production/production");
        factoryTimeEstimationTextView = (TextView) findViewById("factoryTimeEstimationTextView@layout/production/production");

        
        productionTaskQueueLinearLayout = (LinearLayout) findViewById("productionTaskQueueLinearLayout@layout/production/production");
        
        availableProductListLinearLayout = (LinearLayout) findViewById("availableProductListLinearLayout@layout/production/production");
        
        productionDetailsLinearLayout = (LinearLayout) findViewById("productionDetailsLinearLayout@layout/production/production");
        
        
        factoryOresNeedsTextView.setText("8420 [ores@icons]");
        factoryCapacityNeedsTextView.setText("3580 [factory@icons]");
        factoryTimeEstimationTextView.setText("(12 min 35s)");
        
        productSelectionManager = new SelectionManager<ProductState>();
        productionTaskSelectionManager = new SelectionManager<ProductionTaskState>();
        
        visitor = new DefaultWorldEventVisitor() {

            @Override
            public void visit(FactionStateEvent event) {
                if (LoginManager.getLocalPlayer().faction.id == event.getFaction().id) {
                    handler.obtainMessage(UPDATE_FACTION_WHAT, event.getFaction()).send();
                }
            }

            @Override
            public void visit(FactionProductionStateEvent event) {
                if (LoginManager.getLocalPlayer().faction.id == event.getFactionProduction().factionId) {
                    handler.obtainMessage(UPDATE_PRODUCTION_WHAT, event.getFactionProduction()).send();
                }
            }
            
            @Override
            public void visit(FactionAvailableProductListEvent event) {
                if (LoginManager.getLocalPlayer().faction.id == event.getFactionAvailableProductList().factionId) {
                    handler.obtainMessage(UPDATE_AVAILABLE_PRODUCT_LIST_WHAT, event.getFactionAvailableProductList()).send();
                }
            }
        };

        factoryBuyFactoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                worldEngine.sendToAll(new ActionBuyFactionFactoryCapacityEvent(LoginManager.getLocalPlayer().faction, 1));
            }
        });
        
        factorySellFactoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                worldEngine.sendToAll(new ActionSellFactionFactoryCapacityEvent(LoginManager.getLocalPlayer().faction, 1));
            }
        });
        
        
        productSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ProductState>() {
            
            private List<View> addedViews = new ArrayList<View>();
            @Override
            public void onSelectionChange(List<ProductState> selection) {
                for(View view: addedViews) {
                    productionDetailsLinearLayout.removeView(view);
                }
                addedViews.clear();
                
                for(ProductState product: selection) {
                    View view = new AvailableProductDetailsView(ProductionActivity.this, product);
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
        
        productionTaskSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ProductionTaskState>() {
            
            private List<View> addedViews = new ArrayList<View>();
            
            @Override
            public void onSelectionChange(List<ProductionTaskState> selection) {
                for(View view: addedViews) {
                    productionDetailsLinearLayout.removeView(view);
                }
                addedViews.clear();
                
                for(ProductionTaskState productionTask: selection) {
                    View view = new ProductionTaskDetailsView(ProductionActivity.this, productionTask);
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
        worldEngine.registerEventVisitor(visitor);
        worldEngine.sendToAll(new QueryFactionStateEvent(LoginManager.getLocalPlayer().faction));
        worldEngine.sendToAll(new QueryFactionProductionStateEvent(LoginManager.getLocalPlayer().faction));
        worldEngine.sendToAll(new QueryFactionAvailableProductListEvent(LoginManager.getLocalPlayer().faction));
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
                case UPDATE_FACTION_WHAT:
                    faction = (FactionState) message.obj;
                    updateFields();
                    break;
                case UPDATE_PRODUCTION_WHAT:
                    production = (FactionProductionState) message.obj;
                    updateFields();
                    break;
                case UPDATE_AVAILABLE_PRODUCT_LIST_WHAT:
                    availableProductList = (FactionAvailableProductListState) message.obj;
                    updateFields();
                    break;
            }

        }
    }

    protected void updateFields() {
        if (faction == null || production == null || availableProductList == null) {
            return;
        }
        factoryStatersAmountTextView.setText(faction.statersAmount + " [staters@icons]");
        factoryMaintenanceAmountTextView.setText(production.maintenanceAmount + " [staters@icons]");
        factoryCapacityAmountTextView.setText(production.factoryCapacity + " [factory@icons]");
        factoryRentCapacityAmountTextView.setText(production.factoryRentCapacity + " [factory@icons]");
        factoryTotalCapacityAmountTextView.setText(production.factoryTotalCapacity + " [factory@icons]");

        if (production.incomingCapacity > 0) {
            factoryIncomingCapacityTextView.setText("+" + production.incomingCapacity);
            factoryIncomingCapacityDelayTextView.setText(production.nextFactoryCapacityIncreaseTicks + " s");
        } else {
            factoryIncomingCapacityTextView.setText("0");
            factoryIncomingCapacityDelayTextView.setText("--");
        }

        factoryOresTextView.setText(faction.oresAmount + " [ores@icons]");
        
        
        
        // Update production task queue
        productionTaskQueueLinearLayout.removeAllView();
        productionTaskSelectionManager.clear(ProductionTaskView.class);
        
        for(ProductionTaskState productionTask: production.productionTaskQueue) {            
            productionTaskQueueLinearLayout.addViewInLayout(new ProductionTaskView(productionTask, productionTaskSelectionManager));
        }
        
        // Update available product list
        availableProductListLinearLayout.removeAllView();
        productSelectionManager.clear(AvailableProductView.class);
        
        for(ProductState product: availableProductList.products) {            
            availableProductListLinearLayout.addViewInLayout(new AvailableProductView(product, productSelectionManager));
        }
        
    }
    
    void buyProduct(ProductState product, int quantity) {
        worldEngine.sendToAll(new ActionBuyProductEvent(LoginManager.getLocalPlayer().faction, product, quantity));
    }

}
