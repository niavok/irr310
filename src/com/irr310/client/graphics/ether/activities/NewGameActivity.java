package com.irr310.client.graphics.ether.activities;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LayoutParams;
import com.irr310.i3d.view.Triangle;
import com.irr310.i3d.view.View;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class NewGameActivity extends Activity {


    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        
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
