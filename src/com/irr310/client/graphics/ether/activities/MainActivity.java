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
    private boolean axisX;
    private float lastOffset;
    private float lastLastOffset;

    @Override
    public void onCreate(Object objectBundle) {
        setContentView("main@layout/main");

        mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/main");
        animationMesure = new Measure(0, true);

    }

    @Override
    public void onStart() {
        lastTime = Time.now(false);
        axisX = true;
        lastOffset = 0;
        lastLastOffset = 0;
    }

    @Override
    public void onPause() {

    }

    public void onResume() {

    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
        Duration duration = lastTime.durationTo(absTime);

        // Log.trace("Update animatation after "+duration.getMilliseconds()+" ms ("+(1f/duration.getSeconds())+"fps)");
        lastTime = absTime;

        Layout layout = mobileLogoPart.getLayout();
        animationMesure.setValue((float) (25f / 2.2f * (1 + Math.sin(absTime.getSeconds() * 4))));

        float offset = layout.computeMesure(animationMesure);

        // Log.trace("Offset: "+offset);
        if (axisX) {
            layout.setOffsetX(offset);
        } else {
            layout.setOffsetY(offset);
        }

        if ((lastOffset - lastLastOffset) < 0 && (offset - lastOffset > 0)) {
            // Cross a minimal
            axisX = !axisX;
        }

        lastLastOffset = lastOffset;
        lastOffset = offset;

        // layout.setOffsetY(offset);
    }

}
