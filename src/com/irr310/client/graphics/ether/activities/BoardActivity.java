package com.irr310.client.graphics.ether.activities;

import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.Time;

public class BoardActivity extends Activity {

    
    private Button worldMapButton;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        
        worldMapButton = (Button) findViewById("worldMapButton@layout/board");
        
        worldMapButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorldMapActivity.class));
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

}
