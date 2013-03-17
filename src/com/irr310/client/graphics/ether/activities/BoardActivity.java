package com.irr310.client.graphics.ether.activities;

import com.irr310.client.graphics.ether.activities.production.ProductionActivity;
import com.irr310.client.graphics.ether.activities.worldmap.WorldMapActivity;
import com.irr310.common.world.World;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.Time;

public class BoardActivity extends Activity {

    
    private World world;
    private Button productionButton;
    private Button worldMapButton;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        world = ((BoardActivityBundle) bundle).getWorld();
        
        
        worldMapButton = (Button) findViewById("seeWorldMapButton@layout/board");
        productionButton = (Button) findViewById("manageProductionButton@layout/board");
        
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

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
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
