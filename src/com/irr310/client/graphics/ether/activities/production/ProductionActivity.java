package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.world.World;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.server.Time;

public class ProductionActivity extends Activity {

    private World world;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/production");
        world = (World) bundle.getObject();
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

}
