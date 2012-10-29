package com.irr310.client.graphics.ether.activities;

import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.server.Time;

public class BoardActivity extends Activity {

    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/mainmenu");
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
