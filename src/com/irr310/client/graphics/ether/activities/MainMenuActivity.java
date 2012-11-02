package com.irr310.client.graphics.ether.activities;

import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class MainMenuActivity extends Activity {

    //private Triangle mobileLogoPart;
    private Measure animationMesure;
    private Time startTime;
    private Button newGameMenu;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/mainmenu");

      //  mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/logo");
        animationMesure = new Measure(0, true);

        newGameMenu = (Button) findViewById("newGameButton@layout/mainmenu");
        newGameMenu.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewGameActivity.class));
            }
        });
        
    }

    @Override
    public void onResume() {
        startTime = Time.now(false);
    }

    @Override
    public void onPause() {

    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
        Duration duration = startTime.durationTo(absTime);
        // Log.trace("Update animatation after "+duration.getMilliseconds()+" ms ("+(1f/duration.getSeconds())+"fps)");
        //startTime = absTime;

        /*Layout layout = mobileLogoPart.getLayout();
        animationMesure.setValue((float) (25f / 4.2f * (1 + Math.sin(absTime.getSeconds() * 4))));

        float offset = layout.computeMesure(animationMesure);

        // Log.trace("Offset: "+offset);
            layout.setOffsetX(offset);
            layout.setOffsetY(offset);

        
        // layout.setOffsetY(offset);
         */   
        /*if(duration.getSeconds() > 2) {
            startActivity(new Intent(MainActivity.class));
        }*/
    }

}
