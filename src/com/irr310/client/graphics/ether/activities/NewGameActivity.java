package com.irr310.client.graphics.ether.activities;

import com.irr310.client.GameClient;
import com.irr310.common.tools.Log;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LayoutParams;
import com.irr310.i3d.view.Triangle;
import com.irr310.i3d.view.View;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class NewGameActivity extends Activity {


    private Handler handler;

    private static final int NEW_GAME_CREATED_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        handler = new Handler();
        new Thread() {
            public void run() {
                GameClient.getInstance().playSoloGame();
                handler.obtainMessage(NEW_GAME_CREATED_WHAT).send();
            };
        }.start();
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
        
        while(handler.hasMessages()) {
            Message message = handler.getMessage();
            
            switch(message.what) {
                case NEW_GAME_CREATED_WHAT:
                    startActivity(new Intent(BoardActivity.class));
                    break;
                    
            }
            
        }
    }

}
