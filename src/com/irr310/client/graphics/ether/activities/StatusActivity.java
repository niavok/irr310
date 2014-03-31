package com.irr310.client.graphics.ether.activities;

import com.irr310.client.graphics.SettingsPopupActivity;
import com.irr310.client.graphics.ether.activities.worldmap.SystemView;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.I3dVec2;
import com.irr310.i3d.Intent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.ImageButton;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class StatusActivity extends Activity {


    private Button settingsButton;
    private StatusActivityListener mListener;

    @Override
    public void onCreate(final Bundle bundle) {
        setContentView("main@layout/status");
        setStackable(false);
        
        mListener = (StatusActivityListener) bundle.getObject();
        
        settingsButton = (Button) findViewById("settingsButton@layout/status");
        
        settingsButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                getContext().setPopUpActivity(new Intent(SettingsPopupActivity.class, bundle), new I3dVec2(mouseEvent.getRootEvent().getX(), 25));
            }
        });
    }
    
    public interface StatusActivityListener {
        void onQuit();
    }

    @Override
    protected void onResume() {
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onDestroy() {
    }

}
