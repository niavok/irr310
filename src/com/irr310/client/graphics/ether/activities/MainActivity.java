package com.irr310.client.graphics.ether.activities;

import com.irr310.client.graphics.ether.activities.StatusActivity.StatusActivityListener;
import com.irr310.common.tools.Log;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.LayoutParams;
import com.irr310.i3d.view.Triangle;
import com.irr310.server.Duration;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

public class MainActivity extends Activity {

    private Triangle mobileLogoPart;
    private Measure animationMesure;
    private Time startTime;
    private boolean axisX;
    private float lastOffset;
    private float lastLastOffset;
    private MainActivityListener mListener;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        setStackable(false);
        //Log.trace("onCreate");
        mListener = (MainActivityListener) bundle.getObject();
        
        mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/logo");
        animationMesure = new Measure(0, true, Axis.VERTICAL);
        I3dContext.getInstance().preload();
    }

    @Override
    public void onResume() {
        startTime = Time.now(false);
        //Log.trace("onResume "+startTime.getSeconds());
        axisX = true;
        lastOffset = 0;
        lastLastOffset = 0;
    }

    @Override
    public void onPause() {
        //Log.trace("onPause");
        
    }
    
    @Override
    public void onDestroy() {
        Log.trace("onDestroy");
    }

    @Override
    protected void onUpdate(Timestamp time) {
        //Log.trace("onUpdate "+absTime.getSeconds()+ " "+ Time.now(false).getSeconds()+ " "+gameTime.getSeconds());
        Duration duration = startTime.durationTo(time.getTime());
        //Log.trace("onUpdate duration "  +duration.getSeconds());
        // Log.trace("Update animatation after "+duration.getMilliseconds()+" ms ("+(1f/duration.getSeconds())+"fps)");
        //startTime = absTime;

        LayoutParams layout = mobileLogoPart.getLayoutParams();
        animationMesure.setValue((float) (25f / 2.2f * (1 + Math.sin(time.getTime().getSeconds() * 4))));

        float offset = layout.computeMesure(animationMesure);

        //Log.trace("Offset: "+offset);
        if (axisX) {
            //layout.setOffsetX(offset);
        } else {
            //layout.setOffsetY(offset);
        }

        if ((lastOffset - lastLastOffset) < 0 && (offset - lastOffset > 0)) {
            // Cross a minimal
            axisX = !axisX;
        }

        lastLastOffset = lastOffset;
        lastOffset = offset;

        // layout.setOffsetY(offset);
        if(duration.getSeconds() > 1) {
            //startActivity(new Intent(MainMenuActivity.class));
        }
        if(getContext().isPreloaded())  {
            startActivity(new Intent(MainMenuActivity.class));
            mListener.onDone();
        }
    }
    
    public interface MainActivityListener {
        void onDone();
    }

}
