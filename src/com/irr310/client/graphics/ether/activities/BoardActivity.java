package com.irr310.client.graphics.ether.activities;

import com.irr310.client.graphics.ether.activities.production.ProductionActivity;
import com.irr310.client.graphics.ether.activities.worldmap.WorldMapActivity;
import com.irr310.common.binder.BindVariable;
import com.irr310.common.binder.BinderClient;
import com.irr310.common.binder.BinderListener;
import com.irr310.common.world.World;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.Time;

public class BoardActivity extends Activity {

    
    private World world;
    private Button productionButton;
    private Button worldMapButton;
    private TextView boardStatersAmountTextView;
    private TextView boardOresAmountTextView;
    private TextView boardKoliumAmountTextView;
    private TextView boardNeuridiumAmountTextView;
    private BinderClient binder;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        world = ((BoardActivityBundle) bundle).getWorld();
        
        
        worldMapButton = (Button) findViewById("seeWorldMapButton@layout/board");
        productionButton = (Button) findViewById("manageProductionButton@layout/board");
        boardStatersAmountTextView = (TextView) findViewById("boardStatersAmountTextView@layout/board");
        boardOresAmountTextView = (TextView) findViewById("boardOresAmountTextView@layout/board");
        boardKoliumAmountTextView = (TextView) findViewById("boardKoliumAmountTextView@layout/board");
        boardNeuridiumAmountTextView = (TextView) findViewById("boardNeuridiumAmountTextView@layout/board");
        
        
        worldMapButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle(world);
                startActivity(new Intent(WorldMapActivity.class, bundle));
            }
        });
        
        productionButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle(world);
                startActivity(new Intent(ProductionActivity.class, bundle));
            }
        });
    }

    private void initBinders() {
        binder = new BinderClient();
        binder.bind(world.getLocalPlayer().getFaction().getStatersAmount(), new BinderListener<Integer>() {

            @Override
            public void onChange(BindVariable<Integer> variable) {
                boardStatersAmountTextView.setText(variable.get()+" [staters@icons]");
            }
            
        });
        
        boardOresAmountTextView.setText("4658 [ores@icons]");
        boardKoliumAmountTextView.setText("2468 [kolium@icons]");
        boardNeuridiumAmountTextView.setText("569 [neuridium@icons]");
        
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
    
    public static class BoardActivityBundle extends Bundle {

        public BoardActivityBundle(World world) {
            super(world);
        }
        
        public World getWorld() {
            return (World) getObject();
        }
    }

}
