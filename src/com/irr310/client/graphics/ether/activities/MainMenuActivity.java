package com.irr310.client.graphics.ether.activities;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Layout;
import com.irr310.i3d.view.Triangle;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class MainMenuActivity extends Activity {

    //private Triangle mobileLogoPart;
    private Measure animationMesure;
    private Time startTime;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/mainmenu");

      //  mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/logo");
        animationMesure = new Measure(0, true);

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
