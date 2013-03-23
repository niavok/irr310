package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.binder.BindVariable;
import com.irr310.common.binder.BinderClient;
import com.irr310.common.binder.BinderListener;
import com.irr310.common.tools.Log;
import com.irr310.common.world.World;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.TextView;
import com.irr310.server.Time;

public class ProductionActivity extends Activity {

    private World world;
    private TextView factoryStatersAmountTextView;
    private BinderClient binder;
    private TextView factoryTotalCapacityAmountTextView;
    private TextView factoryRentCapacityAmountTextView;
    private TextView factoryCapacityAmountTextView;
    private TextView factoryMaintenanceAmountTextView;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production");
        world = (World) bundle.getObject();
        factoryStatersAmountTextView = (TextView) findViewById("factoryStatersAmountTextView@layout/production");
        factoryMaintenanceAmountTextView = (TextView) findViewById("factoryMaintenanceAmountTextView@layout/production");
        factoryCapacityAmountTextView = (TextView) findViewById("factoryCapacityAmountTextView@layout/production");
        factoryRentCapacityAmountTextView = (TextView) findViewById("factoryRentCapacityAmountTextView@layout/production");
        factoryTotalCapacityAmountTextView = (TextView) findViewById("factoryTotalCapacityAmountTextView@layout/production");
        
        factoryMaintenanceAmountTextView .setText("20 [staters@icons]");
        factoryCapacityAmountTextView .setText("35 [production@icons]");
        factoryRentCapacityAmountTextView .setText("5 [production@icons]");
        factoryTotalCapacityAmountTextView .setText("40 [production@icons]");
    }

    private void initBinders() {
        binder = new BinderClient();
        binder.bind(world.getLocalPlayer().getFaction().getStatersAmount(), new BinderListener<Integer>() {
            @Override
            public void onChange(BindVariable<Integer> variable) {
                factoryStatersAmountTextView.setText(variable.get()+" [staters@icons]");
            }
        });
        
        binder.forceProcess();
    }
    
    @Override
    public void onResume() {
        initBinders();
    }

    @Override
    public void onPause() {
        binder.clear();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
        binder.process();
    }

}
