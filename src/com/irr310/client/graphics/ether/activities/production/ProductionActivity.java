package com.irr310.client.graphics.ether.activities.production;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.world.World;
import com.irr310.common.world.view.FactionView;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.TextView;
import com.irr310.server.Time;

public class ProductionActivity extends Activity {

    private WorldEventDispatcher worldEngine;
    private TextView factoryStatersAmountTextView;
//    private BinderClient binder;
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
    private static final int UPDATE_FACTION_WHAT = 1;
    
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
        
        
        
        
        factoryMaintenanceAmountTextView .setText("20 [staters@icons]");
        factoryCapacityAmountTextView .setText("35 [factory@icons]");
        factoryRentCapacityAmountTextView .setText("5 [factory@icons]");
        factoryTotalCapacityAmountTextView .setText("40 [factory@icons]");
        
        factoryIncomingCapacityTextView .setText("+3");
        factoryIncomingCapacityDelayTextView .setText("35 s");
        
        visitor = new DefaultWorldEventVisitor() {
            
            @Override
            public void visit(FactionStateEvent event) {
                handler.obtainMessage(UPDATE_FACTION_WHAT, event.getFaction()).send();
            }
        };
    }
    
    @Override
    public void onResume() {
        worldEngine.registerEventVisitor(visitor);
        worldEngine.sendToAll(new QueryFactionStateEvent(LoginManager.getLocalPlayer().faction));
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
        while(handler.hasMessages()) {
            Message message = handler.getMessage();
            
            switch(message.what) {
                case UPDATE_FACTION_WHAT:
                    updateFields((FactionView) message.obj);
                    break;
            }
            
        }
    }
    
    protected void updateFields(FactionView faction) {
        factoryStatersAmountTextView.setText(faction.statersAmount+" [staters@icons]");
    }


}
