package com.irr310.client.graphics.ether.activities;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Measure;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Layout;
import com.irr310.i3d.view.Triangle;
import com.irr310.server.Duration;
import com.irr310.server.Time;

public class MainActivity extends Activity {

    private Triangle mobileLogoPart;
	private Measure animationMesure;
	private Time lastTime;

	@Override
    public void onCreate(Object objectBundle) {
        setContentView("main@layout/main");
        
        mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/main");
        animationMesure = new Measure(0, true);
        lastTime = Time.now(false);
    }

	@Override
    public void onStart() {
        
    }

    @Override
    public void onPause() {
        
    }

    public void onResume() {
        
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
        Duration duration = lastTime.durationTo(absTime);

        //Log.trace("Update animatation after "+duration.getMilliseconds()+" ms ("+(1f/duration.getSeconds())+"fps)");
        lastTime = absTime;

        Layout layout = mobileLogoPart.getLayout();
        animationMesure.setValue((float) (25f/4.5f * (1+ Math.sin(absTime.getSeconds()*3))));

        float offset = layout.computeMesure(animationMesure);

        //Log.trace("Offset: "+offset);

        layout.setOffsetX(offset);
        layout.setOffsetY(offset);
    }

}
