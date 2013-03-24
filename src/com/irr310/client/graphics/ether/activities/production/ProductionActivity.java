package com.irr310.client.graphics.ether.activities.production;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.event.world.ActionBuyFactionFactoryCapacityEvent;
import com.irr310.common.event.world.ActionSellFactionFactoryCapacityEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionProductionStateEvent;
import com.irr310.common.event.world.FactionStateEvent;
import com.irr310.common.event.world.QueryFactionProductionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.world.World;
import com.irr310.common.world.view.FactionProductionView;
import com.irr310.common.world.view.FactionView;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
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
    private FactionView faction;
    private FactionProductionView production;
    private TextView factoryOresTextView;
    private TextView factoryOresNeedsTextView;
    private TextView factoryCapacityNeedsTextView;
    private TextView factoryTimeEstimationTextView;
    private static final int UPDATE_FACTION_WHAT = 1;
    private static final int UPDATE_PRODUCTION_WHAT = 2;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production");
        worldEngine = (WorldEventDispatcher) bundle.getObject();
        factoryStatersAmountTextView = (TextView) findViewById("factoryStatersAmountTextView@layout/production");
        factoryMaintenanceAmountTextView = (TextView) findViewById("factoryMaintenanceAmountTextView@layout/production");
        factoryCapacityAmountTextView = (TextView) findViewById("factoryCapacityAmountTextView@layout/production");
        factoryRentCapacityAmountTextView = (TextView) findViewById("factoryRentCapacityAmountTextView@layout/production");
        factoryTotalCapacityAmountTextView = (TextView) findViewById("factoryTotalCapacityAmountTextView@layout/production");

        factoryBuyFactoryButton = (Button) findViewById("factoryBuyFactoryButton@layout/production");
        factorySellFactoryButton = (Button) findViewById("factorySellFactoryButton@layout/production");
        factoryIncomingCapacityTextView = (TextView) findViewById("factoryIncomingCapacityTextView@layout/production");
        factoryIncomingCapacityDelayTextView = (TextView) findViewById("factoryIncomingCapacityDelayTextView@layout/production");

        factoryOresTextView = (TextView) findViewById("factoryOresTextView@layout/production");
        factoryOresNeedsTextView = (TextView) findViewById("factoryOresNeedsTextView@layout/production");
        factoryCapacityNeedsTextView = (TextView) findViewById("factoryCapacityNeedsTextView@layout/production");
        factoryTimeEstimationTextView = (TextView) findViewById("factoryTimeEstimationTextView@layout/production");

        factoryOresNeedsTextView.setText("8420 [ores@icons]");
        factoryCapacityNeedsTextView.setText("3580 [factory@icons]");
        factoryTimeEstimationTextView.setText("(12 min 35s)");
        
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
    }

    @Override
    public void onResume() {
        worldEngine.registerEventVisitor(visitor);
        worldEngine.sendToAll(new QueryFactionStateEvent(LoginManager.getLocalPlayer().faction));
        worldEngine.sendToAll(new QueryFactionProductionStateEvent(LoginManager.getLocalPlayer().faction));
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
                    faction = (FactionView) message.obj;
                    updateFields();
                    break;
                case UPDATE_PRODUCTION_WHAT:
                    production = (FactionProductionView) message.obj;
                    updateFields();
                    break;
            }

        }
    }

    protected void updateFields() {
        if (faction == null || production == null) {
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
        
    }

}
