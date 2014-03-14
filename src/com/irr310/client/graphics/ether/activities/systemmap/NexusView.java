package com.irr310.client.graphics.ether.activities.systemmap;

import com.irr310.common.world.system.Nexus;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.View;

public class NexusView extends View {

    private Nexus nexus;
    private SystemDetailCircleView parentView;
    private float radius;

    public NexusView(Nexus nexus, SystemDetailCircleView parentView) {
        this.nexus = nexus;
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
        
        radius = (float) (nexus.getRadius() * parentView.getScale()) + 20;
        
        getLayoutParams().mLeft = (float) (nexus.getLocation().x * parentView.getScale() + parentView.getOffset() - radius);
        getLayoutParams().mTop = (float) (nexus.getLocation().y * parentView.getScale() + parentView.getOffset()- radius);
        
    }

    @Override
    public void onMeasure() {
        layoutParams.mContentWidth = 20;
        layoutParams.mContentHeight = 20;
    }

    @Override
    public View duplicate() {
        return null;
    }
    
    

}
