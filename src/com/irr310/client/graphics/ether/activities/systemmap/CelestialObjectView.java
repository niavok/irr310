package com.irr310.client.graphics.ether.activities.systemmap;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Asteroid;
import com.irr310.common.world.system.CelestialObject;
import com.irr310.common.world.system.Nexus;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.View;

public class CelestialObjectView extends View {

    private CelestialObject mCelestialObject;
    private SystemDetailCircleView parentView;
    private float radius;

    public CelestialObjectView(CelestialObject celestialObject, SystemDetailCircleView parentView) {
        this.mCelestialObject = celestialObject;
        this.parentView = parentView;
        
    }

    
    private static final Color portalOuterColor = new Color(102, 10, 190);
    
    @Override
    public void onDraw(Graphics g) {
        
        
        g.setColor(Color.green);
        g.drawRing(radius,radius,radius,0,Color.mauve, portalOuterColor, 16);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        
        radius = 2;

        Vec3 location = mCelestialObject.getFirstPart().getTransform().getTranslation();

        getLayoutParams().mLeft = (float) (location.x * parentView.getScale() + parentView.getOffset() - radius);
        getLayoutParams().mTop = (float) (location.y * parentView.getScale() + parentView.getOffset()- radius);
        
    }

    @Override
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
        mLayoutParams.mMeasuredContentWidth = 20;
        mLayoutParams.mMeasuredContentHeight = 20;
    }

    @Override
    public View duplicate() {
        return null;
    }
    
    

}
