package com.irr310.client.graphics;

import com.irr310.client.graphics.ether.activities.StatusActivity.StatusActivityListener;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;

public class SettingsPopupActivity extends Activity {


    private Button settingsButton;
    private StatusActivityListener mListener;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/status_settings_popup");
        setStackable(false);
        
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        
    }

}
